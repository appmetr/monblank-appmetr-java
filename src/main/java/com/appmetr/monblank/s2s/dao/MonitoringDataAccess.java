package com.appmetr.monblank.s2s.dao;

import com.appmetr.monblank.Counter;
import com.appmetr.monblank.MonblankConst;
import com.appmetr.monblank.Monitoring;
import com.appmetr.monblank.StopWatch;
import com.appmetr.s2s.AppMetr;
import com.appmetr.s2s.AppMetrTimer;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MonitoringDataAccess {
    private static Logger logger = LoggerFactory.getLogger(MonitoringDataAccess.class);

    private AppMetr appMetr;
    private AppMetrTimer flushJob;
    private Monitoring monitoring;
    private MonitoringCounterService monitoringCounterService;

    private Object flushJobLock = new Object();

    private static final int MILLIS_PER_MINUTE = 1000 * 60;

    public MonitoringDataAccess(Monitoring monitoring, AppMetr appMetr){
        this.monitoring = monitoring;
        this.appMetr = appMetr;
        monitoringCounterService = new MonitoringCounterService(appMetr);

        flushJob = new AppMetrTimer(MonblankConst.MONITOR_FLUSH_INTERVAL_MINUTES * MILLIS_PER_MINUTE, new Runnable() {
            @Override public void run() {
                execute();
            }
        });
        new Thread(flushJob).start();
    }

    public void execute() {
        synchronized (flushJobLock){
            final long startMillis = System.currentTimeMillis();
            MutableDateTime runTime = new MutableDateTime(System.currentTimeMillis());

            try {
                //shift timestamp backward, 'cause we need to store events "in past"
                runTime.addMinutes(-1 * MonblankConst.MONITOR_FLUSH_INTERVAL_MINUTES);
                saveAndReset(runTime);

            } catch (Exception e) {
                logger.error("Exception while persisting monitors", e);
            } finally {
                final long persistEnd = System.currentTimeMillis();
                logger.info("Monitor scheduler persist millis took: " + (persistEnd - startMillis));
            }
        }
    }

    public void saveAndReset(ReadableDateTime time) {
        StopWatch swMethod = new StopWatch().start();

        final List<Counter> monitors = monitoring.reset();
        monitoringCounterService.persistMonitors(monitors, time);

        swMethod.stop();
        logger.info(String.format("Getting active monitors. Method execution time %s", swMethod.toString()));
    }

    public void stop(){
        synchronized (flushJobLock){
            flushJob.stop();
        }
        appMetr.stop();
    }
}
