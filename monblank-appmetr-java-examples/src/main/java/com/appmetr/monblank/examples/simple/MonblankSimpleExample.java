package com.appmetr.monblank.examples.simple;

import com.appmetr.monblank.MonitorKey;
import com.appmetr.monblank.Monitoring;
import com.appmetr.monblank.s2s.MonitoringS2SImpl;
import com.appmetr.monblank.s2s.dao.MonitoringDataAccess;
import com.appmetr.s2s.AppMetr;
import com.appmetr.s2s.persister.HeapStorage;

import java.util.HashMap;

public class MonblankSimpleExample {

    private static final String TOKEN = "";
    private static final String URL = "";

    public static void main(String[] args) throws InterruptedException {
        AppMetr appMetr = new AppMetr(TOKEN, URL);
        appMetr.setBatchStorage(new HeapStorage());

        Monitoring monitoring = new MonitoringS2SImpl();
        MonitoringDataAccess monitoringDataAccess = new MonitoringDataAccess(monitoring, appMetr);
        HashMap<String, String> properties = new HashMap<>();
        properties.put("first", "1");
        properties.put("second", "2");
        properties.put("third", "3");
        monitoring.add(new MonitorKey("someKey", properties), 25);

        Thread.sleep(90 * 1000);

        monitoringDataAccess.stop();

        Thread.sleep(5 * 1000);
    }
}
