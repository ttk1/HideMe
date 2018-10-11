package net.ttk1.hideme.command.subcommand;

import net.ttk1.hideme.HideMe;
import net.ttk1.hideme.api.PlayerManager;
import org.bukkit.entity.Player;

public abstract class AbstractSubCommand implements SubCommand {
    private HideMe plugin;
    private PlayerManager playerManager;

    public AbstractSubCommand(HideMe plugin, PlayerManager playerManager) {
        this.plugin = plugin;
        this.playerManager = playerManager;
    }

    /**
     * 指定したプレーヤーを他のプレーヤーから隠す
     * @param player プレーヤー
     */
    protected void hidePlayer(Player player) {
        if (player != null) {
            for (Player p: plugin.getServer().getOnlinePlayers()) {
                if (!player.equals(p)) {
                    if (p.hasPermission("hideme.bypass")) {
                        p.showPlayer(plugin, player);
                    } else {
                        p.hidePlayer(plugin, player);
                    }
                }
            }
        }
    }

    /**
     * 指定したプレーヤーを他のプレーヤーから見えるようにする
     * @param player プレーヤー
     */
    protected void showPlayer(Player player) {
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
     * @param player プレーヤー
     */
    protected void hideOthers(Player player) {
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
    protected void sendMsg(String msg) {
        for (Player player: plugin.getServer().getOnlinePlayers()) {
            if(player.hasPermission("hideme.bypass")) {
                player.sendMessage(msg);
            }
        }
    }
}
