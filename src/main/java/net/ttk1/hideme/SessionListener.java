package net.ttk1.hideme;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * プレーヤーのログイン・ログアウトイベントの処理
 */
public class SessionListener implements Listener {
    private final HideMeManager manager;

    public SessionListener(HideMe plugin) {
        this.manager = plugin.getManager();
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (manager.isHidden(player)) {
            manager.bypassMessage(event.getJoinMessage());
            event.setJoinMessage(null);
            manager.hidePlayer(player);
        }
        manager.apply(player);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (manager.isHidden(player)) {
            manager.bypassMessage(event.getQuitMessage());
            event.setQuitMessage(null);
        }
    }
}