package com.appmetr.monblank.s2s.dao;

import com.appmetr.monblank.Counter;
import com.appmetr.monblank.MonblankConst;
import com.appmetr.monblank.MonitorKey;
import com.appmetr.monblank.Monitoring;
import com.appmetr.s2s.AppMetr;
import com.appmetr.s2s.events.Action;
import com.appmetr.s2s.events.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class MonitoringDataAccess {
    private static final Logger log = LoggerFactory.getLogger(MonitoringDataAccess.class);

    private final Monitoring monitoring;
    private final AppMetr appMetr;
    private final Clock clock;
    private final ScheduledExecutorService executorService;
    private final boolean needShutdownExecutor;
    private final boolean needShutdownAppMetr;
    private final Future<?> jobFuture;

    public MonitoringDataAccess(Monitoring monitoring, AppMetr appMetr) {
        this(monitoring, appMetr, Clock.systemUTC(),
                Executors.newSingleThreadScheduledExecutor(), true, true);
    }

    public MonitoringDataAccess(Monitoring monitoring, AppMetr appMetr, Clock clock,
                                ScheduledExecutorService executorService,
                                boolean needShutdownExecutor, boolean needShutdownAppMetr) {
        this.monitoring = monitoring;
        this.appMetr = appMetr;
        this.clock = clock;
        this.executorService = executorService;
        this.needShutdownExecutor = needShutdownExecutor;

        jobFuture = executorService.scheduleWithFixedDelay(this::execute, MonblankConst.MONITOR_FLUSH_INTERVAL_MINUTES,
                MonblankConst.MONITOR_FLUSH_INTERVAL_MINUTES, TimeUnit.MINUTES);
        this.needShutdownAppMetr = needShutdownAppMetr;
    }

    public void execute() {
        try {
            //shift timestamp backward, 'cause we need to store events "in past"
            final Instant timestamp = Instant.now(clock).minus(MonblankConst.MONITOR_FLUSH_INTERVAL_MINUTES, ChronoUnit.MINUTES);

            saveAndReset(timestamp);

        } catch (Throwable throwable) {
            log.error("Exception during saving metrics", throwable);
        }
    }

    protected void saveAndReset(Instant timestamp) {
        final List<Counter> monitors = monitoring.reset();
        persistMonitors(monitors, timestamp);
    }

    public void stop() {
        try {
            if (!jobFuture.cancel(false)) {
                jobFuture.get();
            }
        } catch (InterruptedException e) {
            log.warn("Monitor stopping was interrupted", e);
            Thread.currentThread().interrupt();
            return;
        } catch (ExecutionException | CancellationException e) {
            log.warn("Exception while execution", e);
        }

        if (needShutdownExecutor) {
            executorService.shutdown();
        }

        if (needShutdownAppMetr) {
            appMetr.stop();
        }
    }

    protected void persistMonitors(List<Counter> activeCounters, Instant timestamp) {
        for (Counter counter : activeCounters) {
            try {
                appMetr.track(convertMonitorToEvent(counter, timestamp));
            } catch (Exception e) {
                log.error("Unexpected exception while persisting monitors.", e);
            }
        }

        log.debug("Persisted monitors: {}", activeCounters.size());
    }

    protected Action convertMonitorToEvent(Counter counter, Instant timestamp) {
        final MonitorKey monitorKey = counter.getKey();
        final Map<String, Object> map = new HashMap<>();

        counter.getKey().getProperties().forEach(map::put);

        map.put(MonblankConst.HITS_FEATURE, counter.getHits());
        map.put(MonblankConst.TOTAL_FEATURE, counter.getTotal());
        map.put(MonblankConst.SQUARES_SUM_FEATURES, counter.getSumOfSquares());
        map.put(MonblankConst.MIN_FEATURE, counter.getMin());
        map.put(MonblankConst.MAX_FEATURE, counter.getMax());

        return new Event(monitorKey.getName()).setTimestamp(timestamp.toEpochMilli()).setProperties(map);
    }
}
