package net.ttk1.hideme;

import java.io.File;

import java.util.*;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import net.ttk1.hideme.api.HideMeManager;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 * 隠れたプレーヤーを管理するクラス
 */

@Singleton
public class HideMeManagerImpl implements HideMeManager {
    private static final String HIDDEN_PLAYERS = "hidden_players.yml";
    private static final String HIDDEN_PLAYERS_KEY = "hidden_players";
    private static final String BYPASS_PERMISSION = "hideme.bypass";

    private HideMe plugin;
    private File dataFile;

    private Set<String> hiddenPlayerUUIDs;

    //@Inject
    private void setPlugin(HideMe plugin) {
        this.plugin = plugin;
    }

    @Inject
    public HideMeManagerImpl(HideMe plugin){
        setPlugin(plugin);

        File path = plugin.getDataFolder();
        dataFile = new File(path, HIDDEN_PLAYERS);

        // pluginディレクトリが無ければ作成する
        if (!path.exists()) {
            try {
                path.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // データファイルが無ければ作成する
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (dataFile.exists()) {
            FileConfiguration conf = new YamlConfiguration();
            try {
                conf.load(dataFile);
                hiddenPlayerUUIDs = new HashSet<>(conf.getStringList(HIDDEN_PLAYERS_KEY));
            } catch (Exception e) {
                e.printStackTrace();
                hiddenPlayerUUIDs = new HashSet<>();
            }

        } else {
            hiddenPlayerUUIDs = new HashSet<>();
        }
    }

    @Override
    public void save() {
        if (dataFile.exists()) {
            FileConfiguration conf = new YamlConfiguration();
            try {
                conf.set(HIDDEN_PLAYERS_KEY, new ArrayList<String>(hiddenPlayerUUIDs));
                conf.save(dataFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void hidePlayer(Player player) {
        if (player != null) {
            addHiddenPlayer(player.getUniqueId().toString());
            for (Player p: plugin.getServer().getOnlinePlayers()) {
                if (!player.equals(p)) {
                    if (p.hasPermission(BYPASS_PERMISSION)) {
                        p.showPlayer(plugin, player);
                    } else {
                        p.hidePlayer(plugin, player);
                    }
                }
            }
        }
    }

    @Override
    public void hidePlayer(OfflinePlayer offlinePlayer) {
        if (offlinePlayer != null) {
            if (offlinePlayer.isOnline()) {
                hidePlayer(offlinePlayer.getPlayer());
            } else {
                addHiddenPlayer(offlinePlayer.getUniqueId().toString());
            }
        }
    }

    @Override
    public void showPlayer(Player player) {
        if (player != null) {
            removeHiddenPlayer(player.getUniqueId().toString());
            for (Player p: plugin.getServer().getOnlinePlayers()) {
                if (!player.equals(p)) {
                    p.showPlayer(plugin, player);
                }
            }
        }
    }

    @Override
    public void showPlayer(OfflinePlayer offlinePlayer) {
        if (offlinePlayer != null) {
            if (offlinePlayer.isOnline()) {
                showPlayer(offlinePlayer.getPlayer());
            } else {
                removeHiddenPlayer(offlinePlayer.getUniqueId().toString());
            }
        }
    }

    @Override
    public void refresh(Player player) {
        if (player != null) {
            for (Player p: plugin.getServer().getOnlinePlayers()) {
                if (!player.equals(p)) {
                    if (isHidden(p) && !player.hasPermission(BYPASS_PERMISSION)) {
                        player.hidePlayer(plugin, p);
                    } else {
                        player.showPlayer(plugin, p);
                    }
                }
            }
        }
    }

    @Override
    public boolean isHidden(Player player) {
        return hiddenPlayerUUIDs.contains(player.getUniqueId().toString());
    }

    @Override
    public boolean isHidden(OfflinePlayer offlinePlayer) {
        return hiddenPlayerUUIDs.contains(offlinePlayer.getUniqueId().toString());
    }

    @Override
    public void bypassMessage(String message) {
        for (Player player: plugin.getServer().getOnlinePlayers()) {
            if(player.hasPermission(BYPASS_PERMISSION)) {
                player.sendMessage(message);
            }
        }
    }

    @Override
    public Set<OfflinePlayer> getHiddenPlayers() {
        return Arrays.stream(plugin.getServer().getOfflinePlayers()).filter(this::isHidden).collect(Collectors.toSet());
    }

    private void addHiddenPlayer(String playerUUID){
        hiddenPlayerUUIDs.add(playerUUID);
    }

    private void removeHiddenPlayer(String playerUUID){
        hiddenPlayerUUIDs.remove(playerUUID);
    }
}