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
     * @param monitorName - name of monitor.<br/><b>Do not use character '@' in group or monitor name!</b>
     * @return - monitor instance to stop created monitor
     */
    public StopWatch start(String group, String monitorName, Map<String, String> properties);

    public StopWatch start(String group, String monitorName);

    public StopWatch start(MonitorKey key);

    /**
     * Add value to monitor.
     *
     * @param group       - group name
     * @param monitorName - name.<br/> <b>Do not use character '@' in group or monitor name!</b>
     * @param units       - measuring units (should be one of BYTES\COUNT\MS constant from MonitoringService)
     * @param value       - value to add
     */
    public void add(String group, String monitorName, String units, double value, Map<String, String> properties);

    public void add(String group, String monitorName, String units, double value);

    public void add(MonitorKey key, double value);

    /**
     * Set value to monitor.
     *
     * @param group       - group name
     * @param monitorName - name.<br/> <b>Do not use character '@' in group or monitor name!</b>
     * @param units       - measuring units (should be one of BYTES\COUNT\MS constant from MonitoringService)
     * @param value       - value to add
     */
    public void set(String group, String monitorName, String units, double value, Map<String, String> properties);

    public void set(String group, String monitorName, String units, double value);

    public void set(MonitorKey key, double value);

    /**
     * Increment monitor's value by 1
     *
     * @param group       - group name
     * @param monitorName - name.<br/> <b>Do not use character '@' in group or monitor name!</b>
     */
    public void inc(String group, String monitorName, Map<String, String> properties);

    public void inc(String group, String monitorName);

    public void inc(MonitorKey key);

    /**
     * Removes all currently active monitors and resets all counters.
     * @return List of all counters existed before reset
     */
    public List<Counter> reset();
}
