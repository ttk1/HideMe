package net.ttk1.listener;

import com.google.inject.Inject;
import net.ttk1.HideMe;
import net.ttk1.api.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * プレーヤーのログイン・ログアウトイベントの処理
 */
public class SessionListener implements Listener {
    private HideMe plugin;
    private PlayerManager playerManager;

    @Inject
    private void setPlugin(HideMe plugin) {
        this.plugin = plugin;
    }

    @Inject
    private void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (playerManager.isHidden(player)) {
            playerManager.login(player);
            plugin.sendMsg(event.getJoinMessage());
            event.setJoinMessage(null);
            plugin.hideMe(player);
        } else {
            plugin.showMe(event.getPlayer());
        }
        plugin.hideOthers(player);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (playerManager.isHidden(player)) {
            playerManager.logout(player);
            plugin.sendMsg(event.getQuitMessage());
            event.setQuitMessage(null);
        }
    }
}