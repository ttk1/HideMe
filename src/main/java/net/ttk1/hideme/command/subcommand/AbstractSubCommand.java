package net.ttk1.hideme.command.subcommand;

import net.ttk1.hideme.HideMe;
import net.ttk1.hideme.api.PlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public abstract class AbstractSubCommand {
    private HideMe plugin;
    private PlayerManager playerManager;

    public AbstractSubCommand(HideMe plugin, PlayerManager playerManager) {
        this.plugin = plugin;
        this.playerManager = playerManager;
    }

    abstract boolean match(String[] args);

    abstract void execute(CommandSender sender, String[] args);

    abstract Set<String> tabComplete(CommandSender sender, String[] args);

    /**
     * 指定したプレーヤーを他のプレーヤーから隠す
     * @param playerUUID プレーヤーのUUID String
     */
    protected void hidePlayer(String playerUUID) {
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
    protected void showPlayer(String playerUUID) {
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
    protected void hideOthers(String playerUUID) {
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
    protected void sendMsg(String msg) {
        for (Player player: plugin.getServer().getOnlinePlayers()) {
            if(player.hasPermission("hideme.bypass")) {
                player.sendMessage(msg);
            }
        }
    }
}
