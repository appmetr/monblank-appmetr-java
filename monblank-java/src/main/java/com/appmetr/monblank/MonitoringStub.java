package com.appmetr.monblank;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MonitoringStub implements Monitoring {

    @Override public StopWatch start(String group, String monitorName) {
        return new StopWatchStub();
    }

    @Override public StopWatch start(String group, String monitorName, Map<String, String> properties) {
        return new StopWatchStub();
    }

    @Override public StopWatch start(MonitorKey key) {
        return new StopWatchStub();
    }

    @Override public void add(String group, String monitorName, String units, double value, Map<String, String> properties) {
        //NOP
    }

    @Override public void add(String group, String monitorName, String units, double value) {
        //NOP
    }

    @Override public void add(MonitorKey key, double value) {
        //NOP
    }

    @Override public void set(String group, String monitorName, String units, double value, Map<String, String> properties) {
        //NOP
    }

    @Override public void set(String group, String monitorName, String units, double value) {
        //NOP
    }

    @Override public void set(MonitorKey key, double value) {
        //NOP
    }

    @Override public void inc(String group, String monitorName, Map<String, String> properties) {
        //NOP
    }

    @Override public void inc(String group, String monitorName) {
        //NOP
    }

    @Override public void inc(MonitorKey key) {
        //NOP
    }

    @Override public List<Counter> reset() {
        return Collections.emptyList();
    }

    private static class StopWatchStub extends StopWatch {
        @Override public StopWatch start() {
            return this;
        }

        @Override public StopWatch reset() {
            return this;
        }

        @Override public void resume() {
        }

        @Override public long pause() {
            return 0L;
        }

        @Override public long lap() {
            return 0L;
        }

        @Override public long stop() {
            return 0L;
        }
    }
}
