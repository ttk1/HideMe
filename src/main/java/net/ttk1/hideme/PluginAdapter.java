package net.ttk1.hideme;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * JavaPlugin の各メソッドが final で Mock 化出来ないのでこれを挟むことにする
 */
public class PluginAdapter {
    private final JavaPlugin plugin;

    public PluginAdapter(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public File getDataFolder() {
        return plugin.getDataFolder();
    }

    public Logger getLogger() {
        return plugin.getLogger();
    }

    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    public Collection<? extends Player> getOnlinePlayers() {
        return plugin.getServer().getOnlinePlayers();
    }

    public OfflinePlayer[] getOfflinePlayers() {
        return plugin.getServer().getOfflinePlayers();
    }
}
