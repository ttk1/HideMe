package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMe;
import net.ttk1.hideme.HideMeManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ListCommand implements HideMeCommand {
    private final String SUB_COMMAND = "list";
    private final String PERMISSION = "hideme.list";

    private final HideMe plugin;
    private final HideMeManager hideMeManager;

    public ListCommand(HideMe plugin, HideMeManager hideMeManager) {
        this.plugin = plugin;
        this.hideMeManager = hideMeManager;
    }

    @Override
    public boolean match(String[] args) {
        return args.length == 1 && args[0].equals(SUB_COMMAND);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission(PERMISSION)) {
            sender.sendMessage("Online:" + hideMeManager.getHiddenPlayers().stream().filter(OfflinePlayer::isOnline).map(OfflinePlayer::getName).collect(Collectors.toList()));
            sender.sendMessage("Offline:" + hideMeManager.getHiddenPlayers().stream().filter(((Predicate<OfflinePlayer>) OfflinePlayer::isOnline).negate()).map(OfflinePlayer::getName).collect(Collectors.toList()));
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
                candidates.add(SUB_COMMAND);
            }
        }
        return candidates;
    }
}
