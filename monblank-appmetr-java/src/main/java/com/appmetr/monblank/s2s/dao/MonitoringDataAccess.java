package com.appmetr.monblank.s2s.dao;

import com.appmetr.monblank.Counter;
import com.appmetr.monblank.MonblankConst;
import com.appmetr.monblank.Monitoring;
import com.appmetr.monblank.StopWatch;
import com.appmetr.s2s.AppMetr;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonitoringDataAccess {
    private static Logger logger = LoggerFactory.getLogger(MonitoringDataAccess.class);

    private AppMetr appMetr;
    private AppMetrTimer flushJob;
    private Monitoring monitoring;
    private MonitoringCounterService monitoringCounterService;

    private Lock flushJobLock = new ReentrantLock();

    private static final int MILLIS_PER_MINUTE = 1000 * 60;

    public MonitoringDataAccess(Monitoring monitoring, AppMetr appMetr) {
        this.monitoring = monitoring;
        this.appMetr = appMetr;
        monitoringCounterService = new MonitoringCounterService(appMetr);

        flushJob = new AppMetrTimer(MonblankConst.MONITOR_FLUSH_INTERVAL_MINUTES * MILLIS_PER_MINUTE, new Runnable() {
            @Override public void run() {
                execute();
            }
        }, "MonitorFlushJob");
        new Thread(flushJob).start();
    }

    public void execute() {
        flushJobLock.lock();

        final long startMillis = System.currentTimeMillis();
        try {
            MutableDateTime runTime = new MutableDateTime(System.currentTimeMillis());

            //shift timestamp backward, 'cause we need to store events "in past"
            runTime.addMinutes(-1 * MonblankConst.MONITOR_FLUSH_INTERVAL_MINUTES);
            saveAndReset(runTime);

        } catch (Exception e) {
            logger.error("Exception while persisting monitors", e);
        } finally {
            final long persistEnd = System.currentTimeMillis();
            logger.info("Monitor scheduler persist millis took: " + (persistEnd - startMillis));

            flushJobLock.unlock();
        }
    }

    public void saveAndReset(ReadableDateTime time) {
        StopWatch swMethod = new StopWatch().start();

        final List<Counter> monitors = monitoring.reset();
        monitoringCounterService.persistMonitors(monitors, time);

        swMethod.stop();
        logger.info(String.format("Getting active monitors. Method execution time %s", swMethod.toString()));
    }

    public void stop() {
        flushJobLock.lock();

        try {
            flushJob.stop();
            appMetr.stop();
        } finally {
            flushJobLock.unlock();
        }
    }
}
