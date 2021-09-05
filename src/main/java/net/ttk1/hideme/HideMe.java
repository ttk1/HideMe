package net.ttk1.hideme;

import com.comphenix.protocol.ProtocolLibrary;
import net.ttk1.hideme.adapter.ServerPingPacketAdapter;
import net.ttk1.hideme.api.HideMeManager;
import net.ttk1.hideme.listener.SessionListener;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * プレーヤーの可視性をコントロールするJavaPluginクラス
 */
public class HideMe extends JavaPlugin {
    private ServerPingPacketAdapter serverPingPacketAdapter;
    private SessionListener sessionListener;
    private HideMeManager hideMeManager;

    private CommandExecutor commandExecutor;
    private TabCompleter tabCompleter;

    @Override
    public void onEnable() {
        // event listener
        getServer().getPluginManager().registerEvents(sessionListener, this);
        ProtocolLibrary.getProtocolManager().addPacketListener(serverPingPacketAdapter);

        // command
        PluginCommand command = getCommand("hideme");
        command.setExecutor(commandExecutor);
        command.setTabCompleter(tabCompleter);

        getLogger().info("HideMe enabled");
    }

    @Override
    public void onDisable() {
        hideMeManager.save();
        getLogger().info("HideMe disabled");
    }

    /**
     * @return HiddenPlayerManager
     */
    public HideMeManager getManager() {
        return hideMeManager;
    }
}