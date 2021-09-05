package net.ttk1.hideme.listener;

import net.ttk1.hideme.HideMe;
import net.ttk1.hideme.api.HideMeManager;
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
    private HideMeManager hideMeManager;

    private void setPlugin(HideMe plugin) {
        this.plugin = plugin;
    }

    private void setHideMeManager(HideMeManager hideMeManager) {
        this.hideMeManager = hideMeManager;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (hideMeManager.isHidden(player)) {
            hideMeManager.bypassMessage(event.getJoinMessage());
            event.setJoinMessage(null);
            hideMeManager.hidePlayer(player);
        }
        hideMeManager.refresh(player);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (hideMeManager.isHidden(player)) {
            hideMeManager.bypassMessage(event.getQuitMessage());
            event.setQuitMessage(null);
        }
    }
}