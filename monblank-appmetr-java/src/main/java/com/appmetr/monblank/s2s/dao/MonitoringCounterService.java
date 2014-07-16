package com.appmetr.monblank.s2s.dao;

import com.appmetr.monblank.Counter;
import com.appmetr.monblank.MonblankConst;
import com.appmetr.monblank.MonitorKey;
import com.appmetr.s2s.AppMetr;
import com.appmetr.s2s.events.Action;
import com.appmetr.s2s.events.Event;
import org.joda.time.DateTime;
import org.joda.time.ReadableDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonitoringCounterService {
    private static Logger logger = LoggerFactory.getLogger(MonitoringCounterService.class);

    private AppMetr appMetr;

    public MonitoringCounterService(AppMetr appMetr){
        this.appMetr = appMetr;
    }

    public void persistMonitors(List<Counter> activeMonitors, ReadableDateTime readableDateTime) {
        for (Counter monitor : activeMonitors) {
            try {
                synchronized (monitor) {
                    appMetr.track(convertMonitorToEvent(monitor, readableDateTime.toDateTime()));
                }
            } catch (Exception e) {
                logger.error("Unexpected exception while persisting monitors.", e);
            }
        }

        logger.info("Persisted monitors: {}", activeMonitors.size());
    }

    private Action convertMonitorToEvent(Counter counter, DateTime timestamp) {
        final MonitorKey monitorKey = counter.getKey();
        Map<String, Object> map = new HashMap<String, Object>();
        for (Map.Entry<String, String> entry : counter.getKey().getProperties().entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }

        map.put(MonblankConst.HITS_FEATURE, counter.getHits());
        map.put(MonblankConst.TOTAL_FEATURE, counter.getTotal());
        map.put(MonblankConst.SQUARES_SUM_FEATURES, counter.getSumOfSquares());
        map.put(MonblankConst.MIN_FEATURE, counter.getMin());
        map.put(MonblankConst.MAX_FEATURE, counter.getMax());

        return new Event(monitorKey.getName()).setTimestamp(timestamp.getMillis()).setProperties(map);
    }
}
