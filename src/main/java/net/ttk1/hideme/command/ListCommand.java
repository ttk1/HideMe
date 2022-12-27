package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMeManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ListCommand extends AbstractCommand {
    public ListCommand(HideMeManager manager) {
        super(manager, "list", "hideme.list", 0, false);
    }

    @Override
    protected void executeImpl(CommandSender sender, String[] args) {
        sender.sendMessage("Online:" + manager.getHiddenPlayers().stream().filter(OfflinePlayer::isOnline).map(OfflinePlayer::getName).collect(Collectors.toList()));
        sender.sendMessage("Offline:" + manager.getHiddenPlayers().stream().filter(((Predicate<OfflinePlayer>) OfflinePlayer::isOnline).negate()).map(OfflinePlayer::getName).collect(Collectors.toList()));
    }
}
