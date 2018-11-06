package net.ttk1.hideme;

import com.google.inject.Inject;

import com.google.inject.Injector;

import net.ttk1.hideme.api.HideMeManager;
import net.ttk1.hideme.adapter.ServerPingPacketAdapter;
import net.ttk1.hideme.listener.SessionListener;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;

/**
 * プレーヤーの可視性をコントロールするJavaPluginクラス
 * @author ttk1
 */
public class HideMe extends JavaPlugin {
    private ServerPingPacketAdapter serverPingPacketAdapter;
    private SessionListener sessionListener;
    private HideMeManager hideMeManager;

    private CommandExecutor commandExecutor;
    private TabCompleter tabCompleter;

    @Inject
    private void setServerPingPacketAdapter(ServerPingPacketAdapter serverPingPacketAdapter) {
        this.serverPingPacketAdapter = serverPingPacketAdapter;
    }

    @Inject
    private void setSessionListener(SessionListener sessionListener) {
        this.sessionListener = sessionListener;
    }

    @Inject
    private void setHideMeManager(HideMeManager hideMeManager) {
        this.hideMeManager = hideMeManager;
    }

    @Inject
    private void setCommandExecutor(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    @Inject
    private void setTabCompleter(TabCompleter tabCompleter) {
        this.tabCompleter = tabCompleter;
    }

    @Override
    public void onEnable() {
        // クラスローダーを書き換える
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(getClassLoader());

        PluginModule module = new PluginModule(this);
        Injector injector = module.createInjector();
        injector.injectMembers(this);

        // event listener
        getServer().getPluginManager().registerEvents(sessionListener, this);
        ProtocolLibrary.getProtocolManager().addPacketListener(serverPingPacketAdapter);

        // command
        PluginCommand command = getCommand("hideme");
        command.setExecutor(commandExecutor);
        command.setTabCompleter(tabCompleter);

        // クラスローダーを元に戻す
        Thread.currentThread().setContextClassLoader(currentClassLoader);

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