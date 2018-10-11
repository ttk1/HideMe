package net.ttk1.hideme.listener;

import com.google.inject.Inject;
import net.ttk1.hideme.HideMe;
import net.ttk1.hideme.api.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

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
            sendMsg(event.getJoinMessage());
            event.setJoinMessage(null);
            hidePlayer(player.getUniqueId().toString());
        } else {
            showPlayer(event.getPlayer().getUniqueId().toString());
        }
        hideOthers(player.getUniqueId().toString());
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (playerManager.isHidden(player)) {
            sendMsg(event.getQuitMessage());
            event.setQuitMessage(null);
        }
    }

    /**
     * 指定したプレーヤーを他のプレーヤーから隠す
     * @param playerUUID プレーヤーのUUID String
     */
    private void hidePlayer(String playerUUID) {
        Player player = plugin.getServer().getPlayer(UUID.fromString(playerUUID));

        if (player != null) {
            for (Player p: plugin.getServer().getOnlinePlayers()) {
                if (!player.equals(p) && !p.hasPermission("hideme.bypass")) {
                    p.hidePlayer(plugin, player);
                }
            }
        }
    }

    /**
     * 指定したプレーヤーを他のプレーヤーから見えるようにする
     * @param playerUUID プレーヤーのUUID String
     */
    private void showPlayer(String playerUUID) {
        Player player = plugin.getServer().getPlayer(UUID.fromString(playerUUID));

        if (player != null) {
            for (Player p: plugin.getServer().getOnlinePlayers()) {
                if (!player.equals(p)) {
                    p.showPlayer(plugin, player);
                }
            }
        }
    }

    /**
     * 指定したプレーヤーから"隠れた"状態のプレーヤーを見えないようにする。
     * ログインの度に実行する必要あり。
     * @param playerUUID プレーヤーのUUID String
     */
    private void hideOthers(String playerUUID) {
        Player player = plugin.getServer().getPlayer(UUID.fromString(playerUUID));

        if (player == null || player.hasPermission("hideme.bypass")) {
            return;
        }

        for (Player p: plugin.getServer().getOnlinePlayers()) {
            if (!player.equals(p) && playerManager.isHidden(p)) {
                player.hidePlayer(plugin, p);
            }
        }
    }

    /**
     * ログイン・ログアウトメッセージの抑止をバイパスして送信する
     * @param msg バイパスするメッセージ
     */
    private void sendMsg(String msg) {
        for (Player player: plugin.getServer().getOnlinePlayers()) {
            if(player.hasPermission("hideme.bypass")) {
                player.sendMessage(msg);
            }
        }
    }
}