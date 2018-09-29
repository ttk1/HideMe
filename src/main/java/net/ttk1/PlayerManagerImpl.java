package net.ttk1;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.UUID;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.ttk1.api.PlayerManager;
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

    private Set<String> hiddenPlayers;
    private Set<String> onlineHiddenPlayers;

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
                hiddenPlayers = new HashSet<>(conf.getStringList(HIDDEN_PLAYERS_KEY));
            } catch (Exception e) {
                e.printStackTrace();
                hiddenPlayers = new HashSet<>();
            }

        } else {
            hiddenPlayers = new HashSet<>();
        }

        onlineHiddenPlayers = new HashSet<>();
    }

    @Override
    public void save() {
        if (dataFile.exists()) {
            FileConfiguration conf = new YamlConfiguration();
            try {
                conf.set(HIDDEN_PLAYERS_KEY, new ArrayList<String>(hiddenPlayers));
                conf.save(dataFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addHiddenPlayer(Player player){
        String uuid = player.getUniqueId().toString();
        if (!isHidden(player)) {
            hiddenPlayers.add(uuid);
            if(player.isOnline()){
                onlineHiddenPlayers.add(uuid);
            }
        }
    }

    @Override
    public void removeHiddenPlayer(Player player){
        String uuid = player.getUniqueId().toString();
        if (isHidden(player)) {
            hiddenPlayers.remove(uuid);
            if(player.isOnline()){
                onlineHiddenPlayers.remove(uuid);
            }
        }
    }

    @Override
    public void login(Player player){
        String uuid = player.getUniqueId().toString();
        if (isHidden(player)){
            onlineHiddenPlayers.add(uuid);
        }
    }

    @Override
    public void logout(Player player){
        String uuid = player.getUniqueId().toString();
        if (isHidden(player)){
            onlineHiddenPlayers.remove(uuid);
        }
    }

    @Override
    public boolean isHidden(Player player) {
        String uuid = player.getUniqueId().toString();
        return hiddenPlayers.contains(uuid);
    }

    @Override
    public Set<String> getHiddenPlayerUUIDs(){
        return hiddenPlayers;
    }

    @Override
    public Set<String> getHiddenPlayerNames() {
        Set<String> hiddenPlayerNames = new HashSet<>();
        for (String playerUUID: hiddenPlayers) {
            String playerName = plugin.getServer().getPlayer(UUID.fromString(playerUUID)).getName();
            hiddenPlayerNames.add(playerName);
        }
        return hiddenPlayerNames;
    }

    @Override
    public Set<String> getOnlineHiddenPlayerUUIDs(){
        return onlineHiddenPlayers;
    }

    @Override
    public Set<String> getOnlineHiddenPlayerNames() {
        Set<String> onlineHiddenPlayerNames = new HashSet<>();
        for (String playerUUID: onlineHiddenPlayers) {
            String playerName = plugin.getServer().getPlayer(UUID.fromString(playerUUID)).getName();
            onlineHiddenPlayerNames.add(playerName);
        }
        return onlineHiddenPlayerNames;
    }

    @Override
    public int getHiddenPlayerCount(){
        return hiddenPlayers.size();
    }

    @Override
    public int getOnlineHiddenPlayerCount(){
        return onlineHiddenPlayers.size();
    }
}