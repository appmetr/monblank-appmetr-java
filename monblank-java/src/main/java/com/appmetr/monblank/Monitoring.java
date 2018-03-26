package com.appmetr.monblank;

/*
    NOTE: do not remove service+implementation pair. Wicket's guice injection cant' instantiate services without interface!
 */

import java.util.List;
import java.util.Map;

public interface Monitoring {

    /**
     *  Creates and starts monitor and returns it instance
     *
     * @param group       - group name
     * @param monitorName - name of monitor.<br><b>Do not use character '@' in group or monitor name!</b>
     * @param properties  - event attributes
     * @return - monitor instance to stop created monitor
     */
    StopWatch start(String group, String monitorName, Map<String, String> properties);

    StopWatch start(String group, String monitorName);

    StopWatch start(MonitorKey key);

    /**
     * Add value to monitor.
     *
     * @param group       - group name
     * @param monitorName - name.<br> <b>Do not use character '@' in group or monitor name!</b>
     * @param units       - measuring units (should be one of BYTES\COUNT\MS constant from MonitoringService)
     * @param value       - value to add
     * @param properties  - event attributes
     */
    void add(String group, String monitorName, String units, double value, Map<String, String> properties);

    void add(String group, String monitorName, String units, double value);

    void add(MonitorKey key, double value);

    /**
     * Set value to monitor.
     *
     * @param group       - group name
     * @param monitorName - name.<br> <b>Do not use character '@' in group or monitor name!</b>
     * @param units       - measuring units (should be one of BYTES\COUNT\MS constant from MonitoringService)
     * @param value       - value to add
     * @param properties  - event attributes
     */
    void set(String group, String monitorName, String units, double value, Map<String, String> properties);

    void set(String group, String monitorName, String units, double value);

    void set(MonitorKey key, double value);

    /**
     * Increment monitor's value by 1
     *
     * @param group       - group name
     * @param monitorName - name.<br> <b>Do not use character '@' in group or monitor name!</b>
     * @param properties  - event attributes
     */
    void inc(String group, String monitorName, Map<String, String> properties);

    void inc(String group, String monitorName);

    void inc(MonitorKey key);

    /**
     * Removes all currently active monitors and resets all counters.
     * @return List of all counters existed before reset
     */
    List<Counter> reset();
}
