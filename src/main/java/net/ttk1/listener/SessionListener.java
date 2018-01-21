package net.ttk1.listener;

import net.ttk1.HideMe;
import net.ttk1.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

/**
 * プレーヤーのログイン・ログアウトイベントの処理
 */
public class SessionListener implements Listener {
    private HideMe plg;
    private PlayerManager manager;

    public SessionListener(HideMe plg) {
        this.plg = plg;
        this.manager = plg.getManager();
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (manager.isHidden(player)) {
            manager.login(player);
            plg.sendMsg(event.getJoinMessage());
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
        if (manager.isHidden(player)) {
            manager.logout(player);
            plg.sendMsg(event.getQuitMessage());
            event.setQuitMessage(null);
        }
    }
}