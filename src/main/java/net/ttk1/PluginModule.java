package net.ttk1;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class PluginModule extends AbstractModule {

    private final HideMe plugin;

    public PluginModule(HideMe plugin) {
        this.plugin = plugin;
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure() {
        bind(HideMe.class).toInstance(plugin);
    }
}