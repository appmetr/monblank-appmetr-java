package com.appmetr.s2s;

import com.appmetr.monblank.MonitorKey;
import com.appmetr.monblank.Monitoring;
import com.appmetr.monblank.s2s.MonitoringS2SImpl;
import com.appmetr.monblank.s2s.dao.MonitoringDataAccess;
import com.appmetr.s2s.persister.FileBatchPersister;

import java.util.HashMap;

public class FullTest {

    private static String token = "";
    private static String url = "";
    private static String filePersisterPath = "";

    public void testBridge(){
        AppMetr appMetr = new AppMetr(token, url, new FileBatchPersister(filePersisterPath));

        Monitoring monitoring = new MonitoringS2SImpl();
        MonitoringDataAccess monitoringDataAccess = new MonitoringDataAccess(monitoring, appMetr);
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("first", "1");
        properties.put("second", "2");
        properties.put("third", "3");
        monitoring.add(new MonitorKey("someKey", properties), 25);
        try{
            Thread.sleep(60 * 1000);
        } catch (Exception e){}

        monitoringDataAccess.stop();
    }

}
