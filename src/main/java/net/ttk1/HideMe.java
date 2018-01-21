package net.ttk1;

import net.ttk1.listener.SessionListener;
import net.ttk1.adapter.ServerPingPacketAdapter;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;

/**
 * プレーヤーの可視性をコントロールするJavaPluginクラス
 * @author ttk1
 */
public class HideMe extends JavaPlugin {
    private File path;
    private File dataFile;
    private FileConfiguration data;
    private PlayerManager manager;
    private HideMeLogger logger;

    @Override
    public void onEnable() {
        // データのロード
        path = getDataFolder();
        dataFile = new File(path, "hidden.yml");

        if (!path.exists()) {
            try {
                path.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<String> hidden;
        if (dataFile.exists()) {
            data = new YamlConfiguration();
            try {
                data.load(dataFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            hidden = data.getStringList("players");
        } else {
            hidden = new ArrayList<>();
        }

        logger = new HideMeLogger(this);
        manager = new PlayerManager(new HashSet<String>(hidden));
        getServer().getPluginManager().registerEvents(new SessionListener(this), this);
        ProtocolLibrary.getProtocolManager().addPacketListener(new ServerPingPacketAdapter(this));
        getLogger().info("HideMe enabled");
    }

    @Override
    public void onDisable() {
        if (data != null && dataFile.exists()) {
            data.set("players", new ArrayList<String>(manager.getHiddenSet()));
            try {
                data.save(dataFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        getLogger().info("HideMe disabled");
    }

    /**
     *
     * @return HideMe用のlogger
     */
    public HideMeLogger getHideMeLogger() {
        return logger;
    }

    /**
     * @return HiddenPlayerManager
     */
    public PlayerManager getManager() {
        return manager;
    }

    /**
     * コマンドを処理
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("hideme.hideme")) {
                if(!manager.isHidden(player)) {
                    manager.add(player);
                    hideMe(player);
                    player.sendMessage("You are now hidden.");
                } else {
                    manager.remove(player);
                    showMe(player);
                    player.sendMessage("You are now visible.");
                }
            } else {
                player.sendMessage("You don't have permission.");
            }
        }

        return true;
    }

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
    public void hideAll(Player player) {
        if (player.hasPermission("hideme.bypass")) {
            return;
        }
        for (Player p: getServer().getOnlinePlayers()) {
            if(!player.equals(p) && manager.isHidden(p)) {
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
