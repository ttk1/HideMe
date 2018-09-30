package net.ttk1;

import com.google.inject.Inject;

import com.google.inject.Injector;
import net.ttk1.api.PlayerManager;
import net.ttk1.listener.SessionListener;
import net.ttk1.adapter.ServerPingPacketAdapter;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;

/**
 * プレーヤーの可視性をコントロールするJavaPluginクラス
 * @author ttk1
 */
public class HideMe extends JavaPlugin {
    private ServerPingPacketAdapter serverPingPacketAdapter;
    private SessionListener sessionListener;
    private PlayerManager playerManager;

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
    private void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
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

        // event listenerの登録
        {
            getServer().getPluginManager().registerEvents(sessionListener, this);
            ProtocolLibrary.getProtocolManager().addPacketListener(serverPingPacketAdapter);
        }

        // command
        {
            PluginCommand command = getCommand("hideme");
            command.setExecutor(commandExecutor);
            command.setTabCompleter(tabCompleter);
        }

        // クラスローダーを元に戻す
        Thread.currentThread().setContextClassLoader(currentClassLoader);

        getLogger().info("HideMe enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("HideMe disabled");
    }

    /**
     * @return HiddenPlayerManager
     */
    public PlayerManager getManager() {
        return playerManager;
    }

    /**
     * コマンドを処理
     */
    /*
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("hideme.hideme")) {
                if(!playerManager.isHidden(player)) {
                    playerManager.addHiddenPlayer(player);
                    hideMe(player);
                    player.sendMessage("You are now hidden.");
                } else {
                    playerManager.removeHiddenPlayer(player);
                    showMe(player);
                    player.sendMessage("You are now visible.");
                }
            } else {
                player.sendMessage("You don't have permission.");
            }
        }

        return true;
    }
    */

    /**
     * 指定したプレーヤーを他のプレーヤーから隠す
     * @param player
     */
    public void hideMe(Player player) {
        for (Player p: getServer().getOnlinePlayers()) {
            if(!player.equals(p) && !p.hasPermission("hideme.bypass")) {
                p.hidePlayer(this, player);
            }
        }
    }

    /**
     * 指定したプレーヤーを他のプレーヤーから見えるようにする
     * @param player
     */
    public void showMe(Player player) {
        for (Player p: getServer().getOnlinePlayers()) {
            if(!player.equals(p)) {
                p.showPlayer(this, player);
            }
        }
    }

    /**
     * 指定したプレーヤーが隠れたプレーヤーを見えないようにする
     * @param player
     */
    public void hideOthers(Player player) {
        if (player.hasPermission("hideme.bypass")) {
            return;
        }
        for (Player p: getServer().getOnlinePlayers()) {
            if(!player.equals(p) && playerManager.isHidden(p)) {
                player.hidePlayer(this, p);
            }
        }
    }

    /**
     * ログイン・ログアウトメッセージの抑止をバイパスして送信する
     * @param msg
     */
    public void sendMsg(String msg) {
        for (Player player: getServer().getOnlinePlayers()) {
            if(player.hasPermission("hideme.bypass")) {
                player.sendMessage(msg);
            }
        }
    }
}