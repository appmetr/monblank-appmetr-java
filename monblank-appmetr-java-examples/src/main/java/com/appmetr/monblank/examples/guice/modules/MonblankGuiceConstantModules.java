package com.appmetr.monblank.examples.guice.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class MonblankGuiceConstantModules extends AbstractModule {
    @Override protected void configure() {
        bind(String.class)
                .annotatedWith(Names.named("appmetr.token"))
                .toInstance("<token>");
        bind(String.class)
                .annotatedWith(Names.named("appmetr.url"))
                .toInstance("<url>");
        bind(String.class)
                .annotatedWith(Names.named("appmetr.filepath"))
                .toInstance("<path_to_temp_dir>");
    }
}
