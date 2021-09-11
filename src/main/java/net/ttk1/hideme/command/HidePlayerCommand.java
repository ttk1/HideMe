package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMe;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class HidePlayerCommand extends AbstractCommand {
    public HidePlayerCommand(HideMe plugin) {
        super(plugin, "hide", "hideme.hide.player", 1);
    }

    @Override
    public void executeImpl(CommandSender sender, String[] args) {
        String playerName = args[1];
        Player player = plugin.getServer().getPlayer(playerName);
        if (player != null) {
            // オンラインプレーヤー
            if (!manager.isHidden(player)) {
                manager.hidePlayer(player);
                player.sendMessage("You are now hidden.");
                sender.sendMessage("Player " + playerName + " is now hidden.");
            } else {
                sender.sendMessage("Player " + playerName + " is already hidden.");
            }
        } else {
            // オフラインプレーヤー
            for (OfflinePlayer offlinePlayer : plugin.getServer().getOfflinePlayers()) {
                if (offlinePlayer.getName().equals(playerName)) {
                    if (!manager.isHidden(offlinePlayer)) {
                        manager.hidePlayer(offlinePlayer);
                        sender.sendMessage("Player " + playerName + " is now hidden.");
                    } else {
                        sender.sendMessage("Player " + playerName + " is already hidden.");
                    }
                    return;
                }
            }
            // not found
            sender.sendMessage("Player " + playerName + " not found.");
        }
    }

    @Override
    public Set<String> tabCompleteImpl(CommandSender sender, String[] args) {
        HashSet<String> candidates = new HashSet<>();
        if (args.length == 0) {
            candidates.add(commandName);
        } else if (args.length == 1 && commandName.startsWith(args[0])) {
            if (args[0].equals(commandName)) {
                // すべてのオンラインプレーヤーを候補とする
                candidates.addAll(plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toSet()));
            } else {
                // コマンドを候補とする
                candidates.add(commandName);
            }
        } else if (args.length == 2) {
            // プレフィックスが一致するオンラインプレーヤーを候補とする
            candidates.addAll(plugin.getServer().getOnlinePlayers().stream().map(Player::getName).filter(i -> i.startsWith(args[1])).collect(Collectors.toSet()));
        }
        return candidates;
    }
}
