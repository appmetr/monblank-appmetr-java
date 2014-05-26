package com.appmetr.monblank.examples.guice;

import com.appmetr.monblank.Monitoring;
import com.appmetr.monblank.examples.guice.modules.MonblankGuiceConstantModules;
import com.appmetr.monblank.examples.guice.modules.MonblankGuiceModule;
import com.appmetr.monblank.s2s.dao.MonitoringDataAccess;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class MonblankGuiceExample {

    public static void main(String[] args) throws InterruptedException {
        Injector injector = Guice.createInjector(
                new MonblankGuiceConstantModules(),
                new MonblankGuiceModule()
        );

        try {
            Monitoring monitoring = injector.getInstance(Monitoring.class);

            monitoring.inc("test", "test value");

            //Need time to flush and upload
            Thread.sleep(1000 * 60 * 2);

        } finally {
            //You should call stop method on exit from your application
            injector.getInstance(MonitoringDataAccess.class).stop();
        }
    }
}
