package net.ttk1;

import java.util.List;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class HideMe extends JavaPlugin {
    private List<String> hidden;
    private File path;
    private File dataFile;
    private FileConfiguration data;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new SessionListener(this), this);

        // データのロード
        path = getDataFolder();
        dataFile = new File(path, "hidden.yml");

        if (!path.exists()) {
            try {
                path.mkdir();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        data = new YamlConfiguration();
        try {
            data.load(dataFile);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        hidden = data.getStringList("players");

        getLogger().info("HideMe enabled");
    }

    @Override
    public void onDisable() {
        data.set("players", hidden);
        try {
            data.save(dataFile);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        getLogger().info("HideMe disabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String uuid = player.getUniqueId().toString();
            if (player.hasPermission("hideme.hideme")) {
                if(!hidden.contains(uuid)) {
                    hidden.add(uuid);
                    hideMe(player);
                    player.sendMessage("You are now hidden.");
                } else {
                    hidden.remove(uuid);
                    showMe(player);
                    player.sendMessage("You are now visible.");
                }
            } else {
                player.sendMessage("You don't have permission.");
            }
        }

        return true;
    }

    private void hideMe(Player player) {
        for (Player p: getServer().getOnlinePlayers()) {
            if(!player.equals(p) && !p.hasPermission("hideme.bypass")) {
                p.hidePlayer(this, player);
            }
        }
    }
    private void showMe(Player player) {
        for (Player p: getServer().getOnlinePlayers()) {
            if(!player.equals(p)) {
                p.showPlayer(this, player);
            }
        }
    }

    private void hideAll(Player player) {
        if (player.hasPermission("hideme.bypass")) {
            return;
        }

        for (Player p: getServer().getOnlinePlayers()) {
            if(!player.equals(p) && hidden.contains(p.getUniqueId().toString())) {
                player.hidePlayer(this, p);
            }
        }
    }

    // ログイン・ログアウトメッセージの抑止をバイパスする
    private void sendMsg(String msg) {
        for (Player player: getServer().getOnlinePlayers()) {
            if(player.hasPermission("hideme.bypass")) {
                player.sendMessage(msg);
            }
        }
    }

    class SessionListener implements Listener {
        HideMe plg;
        SessionListener(HideMe plg) {
            this.plg = plg;
        }

        @EventHandler
        public void onPlayerJoinEvent(PlayerJoinEvent event) {
            Player player = event.getPlayer();
            String uuid = player.getUniqueId().toString();
            if (hidden.contains(uuid)) {
                sendMsg(event.getJoinMessage());
                event.setJoinMessage(null);
                plg.hideMe(player);
            } else {
                plg.showMe(event.getPlayer());
            }
            plg.hideAll(player);
        }

        @EventHandler
        public void onPlayerQuitEvent(PlayerQuitEvent event) {
            Player player = event.getPlayer();
            String uuid = player.getUniqueId().toString();
            if (hidden.contains(uuid)) {
                sendMsg(event.getQuitMessage());
                event.setQuitMessage(null);
            }
        }
    }
}
