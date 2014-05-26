package com.appmetr.monblank.examples.guice.modules;

import com.appmetr.monblank.Monitoring;
import com.appmetr.monblank.s2s.dao.MonitoringDataAccess;
import com.appmetr.s2s.AppMetr;
import com.appmetr.s2s.persister.FileBatchPersister;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

public class MonblankDataAccessProvider implements Provider<MonitoringDataAccess> {
    @Inject @Named("appmetr.token") private String token;
    @Inject @Named("appmetr.url") private String url;
    @Inject @Named("appmetr.filepath") private String path;

    @Inject private Monitoring monitoring;

    @Override public MonitoringDataAccess get() {
        AppMetr appMetr = new AppMetr(token, url, new FileBatchPersister(path));
        return new MonitoringDataAccess(monitoring, appMetr);
    }
}
