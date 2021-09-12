package net.ttk1.hideme;

import com.comphenix.protocol.ProtocolLibrary;
import net.ttk1.hideme.command.HideMeCommandExecutor;
import net.ttk1.hideme.command.HideMeCommandManager;
import net.ttk1.hideme.command.HideMeTabCompleter;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * プレーヤーの可視性をコントロールするJavaPluginクラス
 */
public class HideMe extends JavaPlugin {
    private Logger logger;
    private HideMeManager manager;

    @Override
    public void onEnable() {
        // logger
        logger = getLogger();
        logger.info("Hello!");

        try {
            // manager
            manager = new HideMeManager(this);

            // event listener
            getServer().getPluginManager().registerEvents(new SessionListener(this), this);
            ProtocolLibrary.getProtocolManager().addPacketListener(new ServerPingPacketAdapter(this));

            // command
            PluginCommand command = getCommand("hideme");
            HideMeCommandManager commandManager = new HideMeCommandManager(this);
            command.setExecutor(new HideMeCommandExecutor(commandManager));
            command.setTabCompleter(new HideMeTabCompleter(commandManager));
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("初期化に失敗しました。");
            return;
        }
        logger.info("初期化が完了しました。");
    }

    @Override
    public void onDisable() {
        manager.save();
        logger.info("Bye!");
    }

    public HideMeManager getManager() {
        return manager;
    }
}