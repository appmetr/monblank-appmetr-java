package com.appmetr.monblank.s2s.bridge;

import com.appmetr.monblank.MonitorKey;
import com.appmetr.monblank.Monitoring;
import com.appmetr.monblank.StopWatch;

import java.io.Serializable;

public class PersistenceStopWatch extends StopWatch implements Serializable {
    private Monitoring monitoring;
    private MonitorKey key;


    public PersistenceStopWatch(Monitoring monitoring, MonitorKey key) {
        this.monitoring = monitoring;
        this.key = key;
    }

    public long stop() {
        final long elapsedTime = super.stop();
        monitoring.add(key, elapsedTime);
        return elapsedTime;
    }
}
