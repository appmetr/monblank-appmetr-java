package com.appmetr.monblank;

import java.util.HashMap;
import java.util.Map;

public class MonitorProperties {
    private Map<String, String> properties = new HashMap<>();

    public static MonitorProperties create() {
        return new MonitorProperties();
    }

    public static MonitorProperties create(String name, Object value) {
        return create().add(name, value);
    }

    public static MonitorProperties create(Map<String, String> properties) {
        MonitorProperties monitorProperties = new MonitorProperties();
        monitorProperties.properties.putAll(properties);
        return monitorProperties;
    }

    public MonitorProperties add(String name, Object value) {
        if (value != null) {
            properties.put(name, String.valueOf(value));
        }
        return this;
    }

    public MonitorProperties addAll(Map<String, Object> properties) {
        properties.forEach(this::add);
        return this;
    }

    public Map<String, String> asMap() {
        return properties;
    }
}
