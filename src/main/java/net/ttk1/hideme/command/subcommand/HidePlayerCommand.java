package net.ttk1.hideme.command.subcommand;

import net.ttk1.hideme.HideMe;
import net.ttk1.hideme.api.PlayerManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class HidePlayerCommand implements SubCommand {
    private final String SUB_COMMAND = "hide";
    private final String PERMISSION = "hideme.hide.player";

    private HideMe plugin;
    private PlayerManager playerManager;

    public HidePlayerCommand(HideMe plugin, PlayerManager playerManager) {
        this.plugin = plugin;
        this.playerManager = playerManager;
    }

    @Override
    public boolean match(String[] args) {
        if (args.length == 2 && args[0].equals(SUB_COMMAND)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission(PERMISSION)) {
            String playerName = args[1];

            Player player = plugin.getServer().getPlayer(playerName);
            OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(playerName);

            if (player != null) {
                // オンラインプレーヤー
                if(!playerManager.isHidden(player)) {
                    playerManager.addHiddenPlayer(player);
                    plugin.hideMe(player);
                    player.sendMessage("You are now hidden.");
                    sender.sendMessage("Player "+playerName+" is now hidden.");
                } else {
                    sender.sendMessage("Player "+playerName+" is already hidden.");
                }
            } else if (offlinePlayer != null) {
                // TODO
            } else {
                sender.sendMessage("Player "+playerName+" not found.");
            }
        } else {
            sender.sendMessage("You don't hove permission to perform this command!");
        }
    }

    @Override
    public Set<String> tabComplete(CommandSender sender, String[] args) {
        HashSet<String> candidates = new HashSet<>();
        if (sender.hasPermission(PERMISSION)) {
            if (args.length == 0) {
                candidates.add(SUB_COMMAND);
            } else if (args.length == 1 && SUB_COMMAND.startsWith(args[0])) {
                if (args[0].equals(SUB_COMMAND)) {
                    // すべてのオンラインプレーヤーを候補とする
                    candidates.addAll(plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toSet()));
                } else {
                    // コマンドを候補とする
                    candidates.add(SUB_COMMAND);
                }
            } else if (args.length == 2) {
                // プレフィックスが一致するプレーヤーを候補とする
                candidates.addAll(plugin.getServer().getOnlinePlayers().stream().map(Player::getName).filter(i -> i.startsWith(args[1])).collect(Collectors.toSet()));
            }
        }
        return candidates;
    }
}
