package com.appmetr.monblank.s2s;

import com.appmetr.monblank.*;
import com.appmetr.monblank.s2s.bridge.PersistenceStopWatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonitoringS2SImpl implements Monitoring {
    private final Map<MonitorKey, Counter> monitors = new HashMap<MonitorKey, Counter>();

    /**
     * Creates monitor and returns it instance
     *
     * @param key - key for monitor.<br/><b>Do not use character '@' in group or monitor name!</b>
     * @return - monitor instance to stop created monitor
     */
    private StopWatch create(MonitorKey key) {
        return new PersistenceStopWatch(this, key);
    }

    @Override public synchronized List<Counter> reset() {
        final List<Counter> counters = new ArrayList<Counter>(monitors.values());
        monitors.clear();
        return counters;
    }

    private void updateCounter(MonitorKey key, double value) {
        final Counter counter = getOrCreateCounter(key);
        counter.update(value);
    }

    private Counter getOrCreateCounter(MonitorKey key) {
        final Counter counter = monitors.get(key);
        return counter == null ? safeCreateCounter(key) : counter;
    }

    private synchronized Counter safeCreateCounter(MonitorKey key) {
        Counter counter = monitors.get(key);

        if (counter != null) return counter;

        counter = new Counter(key);
        monitors.put(key, counter);
        return counter;
    }

    @Override public StopWatch start(String name) {
        return create(new MonitorKey(name + ms()));
    }

    @Override public StopWatch start(String group, String name) {
        return start(group + MonblankConst.EVENT_DELIMITER + name).start();
    }

    @Override public StopWatch start(String name, String propertyName, String propertyValue) {
        return create(new MonitorKey(name + ms(), propertyName, propertyValue));
    }

    @Override public StopWatch start(String name, String... properties) {
        return create(new MonitorKey(name + ms(), properties));
    }

    @Override public StopWatch start(MonitorKey key) {
        return create(key);
    }

    @Override public void add(String group, String monitorName, String units, double value) {
        String name = group + MonblankConst.EVENT_DELIMITER + monitorName + wrapIfSet(units);
        updateCounter(new MonitorKey(name), value);
    }

    @Override public void add(String name, String units, double value) {
        String monitorName = name + wrapIfSet(units);
        updateCounter(new MonitorKey(monitorName), value);
    }

    @Override public void add(String name, String units, double value, String propertyName, String propertyValue) {
        String monitorName = name + wrapIfSet(units);
        updateCounter(new MonitorKey(monitorName, propertyName, propertyValue), value);
    }

    @Override public void add(String name, String units, double value, String... properties) {
        String monitorName = name + wrapIfSet(units);
        updateCounter(new MonitorKey(monitorName, properties), value);
    }

    @Override public void add(MonitorKey key, double value) {
        updateCounter(key, value);
    }

    @Override public void inc(String group, String name) {
        String monitorName = group + MonblankConst.EVENT_DELIMITER + name + inc();
        updateCounter(new MonitorKey(monitorName), 1.0);
    }

    @Override public void inc(String name) {
        String monitorName = name + inc();
        updateCounter(new MonitorKey(monitorName), 1.0);
    }

    @Override public void inc(String name, String propertyName, String propertyValue) {
        String monitorName = name + inc();
        updateCounter(new MonitorKey(monitorName, propertyName, propertyValue), 1.0);
    }

    @Override public void inc(String name, String... properties) {
        String monitorName = name + inc();
        updateCounter(new MonitorKey(monitorName, properties), 1.0);
    }

    @Override public void inc(MonitorKey key) {
        updateCounter(key, 1.0);
    }

    private static String wrapIfSet(String units) {
        if(units == null || units.isEmpty()){
            return "";
        }
        return MonblankConst.UNIT_DELIMITER + "(" + units + ")";
    }

    private static String ms() {
        return wrapIfSet(MonblankConst.MS);
    }

    private static String inc() {
        return wrapIfSet(MonblankConst.COUNT);
    }
}
