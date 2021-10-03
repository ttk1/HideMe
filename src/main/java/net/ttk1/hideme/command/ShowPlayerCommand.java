package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMe;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.stream.Collectors;

public class ShowPlayerCommand extends AbstractCommand {
    public ShowPlayerCommand(HideMe plugin) {
        super(plugin, "show", "hideme.show.player", 1, false);
    }

    @Override
    protected void executeImpl(CommandSender sender, String[] args) {
        String playerName = args[1];
        OfflinePlayer player = manager.getOfflinePlayer(playerName);
        if (player != null) {
            if (manager.isHidden(player)) {
                manager.showPlayer(player);
                sender.sendMessage("Player " + playerName + " is now visible.");
            } else {
                sender.sendMessage("Player " + playerName + " is already visible.");
            }
        } else {
            // not found
            sender.sendMessage("Player " + playerName + " not found.");
        }
    }

    @Override
    protected void tabCompleteImpl(CommandSender sender, String[] args, Set<String> candidates) {
        if (args.length == 0) {
            candidates.add(commandName);
        } else if (args.length == 1 && commandName.startsWith(args[0])) {
            if (args[0].equals(commandName)) {
                // すべてのオンラインプレーヤーを候補とする
                candidates.addAll(manager.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toSet()));
            } else {
                // コマンドを候補とする
                candidates.add(commandName);
            }
        } else if (args.length == 2) {
            // プレフィックスが一致するオンラインプレーヤーを候補とする
            candidates.addAll(manager.getOnlinePlayers().stream().map(Player::getName).filter(i -> i.startsWith(args[1])).collect(Collectors.toSet()));
        }
    }
}
