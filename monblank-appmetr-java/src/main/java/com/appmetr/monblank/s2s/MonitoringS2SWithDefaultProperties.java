package com.appmetr.monblank.s2s;

import com.appmetr.monblank.MonitorKey;

import java.util.HashMap;
import java.util.Map;

public class MonitoringS2SWithDefaultProperties extends MonitoringS2SImpl {
    private final Map<String, String> defaultProperties;

    public MonitoringS2SWithDefaultProperties(Map<String, String> defaultProperties) {
        this.defaultProperties = defaultProperties;
    }

    @Override protected MonitorKey createMonitorKey(String name, Map<String, String> properties) {
        if (properties == null || properties.isEmpty()) {
            return super.createMonitorKey(name, defaultProperties);
        }

        try {
            properties.putAll(defaultProperties);
            return super.createMonitorKey(name, properties);
        } catch (UnsupportedOperationException e) { //if properties is singleton or unmodifiable map
            final HashMap<String, String> newProperties = new HashMap<>(properties);
            newProperties.putAll(defaultProperties);
            return super.createMonitorKey(name, new HashMap<>(properties));
        }
    }
}
