package net.ttk1.hideme.api;

import com.google.inject.ImplementedBy;

import net.ttk1.hideme.PlayerManagerImpl;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Set;

@ImplementedBy(PlayerManagerImpl.class)
public interface PlayerManager {
    /**
     * hidden_playersをYamlファイルに書き出す
     */
    void save();

    void addHiddenPlayer(Player player);

    void addHiddenPlayer(OfflinePlayer offlinePlayer);

    void removeHiddenPlayer(Player player);

    void removeHiddenPlayer(OfflinePlayer offlinePlayer);

    boolean isHidden(Player player);

    boolean isHidden(OfflinePlayer offlinePlayer);

    Set<Player> getOnlineHiddenPlayers();

    Set<OfflinePlayer> getOfflineHiddenPlayers();

    Set<String> getHiddenPlayerUUIDs();
}
