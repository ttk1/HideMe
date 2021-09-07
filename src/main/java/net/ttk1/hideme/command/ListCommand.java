package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMe;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ListCommand extends AbstractCommand {
    public ListCommand(HideMe plugin) {
        super(plugin, "list", "hideme.list", 0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (checkPermission(sender)) {
            sender.sendMessage("Online:" + manager.getHiddenPlayers().stream().filter(OfflinePlayer::isOnline).map(OfflinePlayer::getName).collect(Collectors.toList()));
            sender.sendMessage("Offline:" + manager.getHiddenPlayers().stream().filter(((Predicate<OfflinePlayer>) OfflinePlayer::isOnline).negate()).map(OfflinePlayer::getName).collect(Collectors.toList()));
        } else {
            sender.sendMessage("You don't hove permission to perform this command!");
        }
    }

    @Override
    public Set<String> tabComplete(CommandSender sender, String[] args) {
        HashSet<String> candidates = new HashSet<>();
        if (checkPermission(sender)) {
            if (args.length == 0) {
                candidates.add(commandName);
            } else if (args.length == 1 && commandName.startsWith(args[0])) {
                candidates.add(commandName);
            }
        }
        return candidates;
    }
}
