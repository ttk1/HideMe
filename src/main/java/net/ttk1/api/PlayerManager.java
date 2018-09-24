package net.ttk1.api;

import com.google.inject.ImplementedBy;
import net.ttk1.PlayerManagerImpl;
import org.bukkit.entity.Player;

import java.util.Set;

@ImplementedBy(PlayerManagerImpl.class)
public interface PlayerManager {
    /**
     * hidden_playersをYamlファイルに書き出す
     */
    void save();

    void addHiddenPlayer(Player player);

    void removeHiddenPlayer(Player player);

    void login(Player player);

    void logout(Player player);

    boolean isHidden(Player player);

    Set<String> getHiddenPlayerUUIDs();

    Set<String> getOnlineHiddenPlayerUUIDs();

    int getHiddenPlayerCount();

    int getOnlineHiddenPlayerCount();
}
