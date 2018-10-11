package net.ttk1.hideme;

import java.io.File;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.base.Predicates;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import net.ttk1.hideme.api.PlayerManager;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 * 隠れたプレーヤーを管理するクラス
 */

@Singleton
public class PlayerManagerImpl implements PlayerManager {
    private static final String HIDDEN_PLAYERS = "hidden_players.yml";
    private static final String HIDDEN_PLAYERS_KEY = "hidden_players";

    private HideMe plugin;
    private File dataFile;

    private Set<String> hiddenPlayerUUIDs;

    //@Inject
    private void setPlugin(HideMe plugin) {
        this.plugin = plugin;
    }

    @Inject
    public PlayerManagerImpl(HideMe plugin){
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
    public void addHiddenPlayer(Player player){
        hiddenPlayerUUIDs.add(player.getUniqueId().toString());
    }

    @Override
    public void addHiddenPlayer(OfflinePlayer offlinePlayer){
        hiddenPlayerUUIDs.add(offlinePlayer.getUniqueId().toString());
    }

    @Override
    public void removeHiddenPlayer(Player player){
        hiddenPlayerUUIDs.remove(player.getUniqueId().toString());
    }

    @Override
    public void removeHiddenPlayer(OfflinePlayer offlinePlayer){
        hiddenPlayerUUIDs.remove(offlinePlayer.getUniqueId().toString());
    }

    @Override
    public boolean isHidden(Player player) {
        String uuid = player.getUniqueId().toString();
        return hiddenPlayerUUIDs.contains(uuid);
    }

    @Override
    public boolean isHidden(OfflinePlayer offlinePlayer) {
        String uuid = offlinePlayer.getUniqueId().toString();
        return hiddenPlayerUUIDs.contains(uuid);
    }

    @Override
    public Set<Player> getOnlineHiddenPlayers() {
        return plugin.getServer().getOnlinePlayers().stream().filter(this::isHidden).collect(Collectors.toSet());
    }

    @Override
    public Set<OfflinePlayer> getOfflineHiddenPlayers() {
        return Arrays.stream(plugin.getServer().getOfflinePlayers()).filter(Predicates.not(OfflinePlayer::isOnline)).filter(this::isHidden).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getHiddenPlayerUUIDs() {
        return hiddenPlayerUUIDs;
    }
}