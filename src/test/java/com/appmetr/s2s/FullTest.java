package com.appmetr.s2s;

import com.appmetr.monblank.MonitorKey;
import com.appmetr.monblank.Monitoring;
import com.appmetr.monblank.s2s.MonitoringS2SImpl;
import com.appmetr.monblank.s2s.dao.MonitoringDataAccess;
import com.appmetr.s2s.persister.FileBatchPersister;
import org.junit.Test;

import java.util.HashMap;

public class FullTest {

    @Test
    public void testBridge(){
        AppMetr appMetr = new AppMetr("bf099ce8-605a-40c6-b98c-b67c63eb1848", "http://localhost/api", new FileBatchPersister("/Users/pronvis/!batches"));

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
