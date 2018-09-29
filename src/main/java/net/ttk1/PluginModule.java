package net.ttk1;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import net.ttk1.command.HideMeCommandExecutor;

import net.ttk1.command.HideMeTabCompleter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

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
        bind(CommandExecutor.class).to(HideMeCommandExecutor.class);
        bind(TabCompleter.class).to(HideMeTabCompleter.class);
    }
}