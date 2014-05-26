package com.appmetr.monblank.examples.guice.modules;

import com.appmetr.monblank.Monitoring;
import com.appmetr.monblank.s2s.MonitoringS2SImpl;
import com.appmetr.monblank.s2s.dao.MonitoringDataAccess;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class MonblankGuiceModule extends AbstractModule {
    @Override protected void configure() {
        bind(Monitoring.class).to(MonitoringS2SImpl.class).in(Scopes.SINGLETON);
        bind(MonitoringDataAccess.class).toProvider(MonblankDataAccessProvider.class).asEagerSingleton();
    }
}
