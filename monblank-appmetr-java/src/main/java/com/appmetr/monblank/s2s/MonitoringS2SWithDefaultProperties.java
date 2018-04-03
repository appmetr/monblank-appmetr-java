package com.appmetr.monblank.s2s;

import com.appmetr.monblank.MonitorKey;

import java.util.HashMap;
import java.util.Map;

public class MonitoringS2SWithDefaultProperties extends MonitoringS2SImpl {
    protected final Map<String, String> defaultProperties;

    public MonitoringS2SWithDefaultProperties(Map<String, String> defaultProperties) {
        this.defaultProperties = defaultProperties;
    }

    @Override protected MonitorKey createMonitorKey(String name, Map<String, String> properties) {
        if (defaultProperties == null || defaultProperties.isEmpty()) {
            return super.createMonitorKey(name, properties);
        }

        if (properties == null || properties.isEmpty()) {
            return super.createMonitorKey(name, defaultProperties);
        }

        try {
            defaultProperties.forEach(properties::putIfAbsent);
            return super.createMonitorKey(name, properties);
        } catch (UnsupportedOperationException e) { //if properties is singleton or unmodifiable map
            final HashMap<String, String> newProperties = new HashMap<>(defaultProperties);
            newProperties.putAll(properties);
            return super.createMonitorKey(name, newProperties);
        }
    }
}
