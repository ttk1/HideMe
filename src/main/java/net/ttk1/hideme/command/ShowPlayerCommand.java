package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMeManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class ShowPlayerCommand extends AbstractPlayerCommand {
    public ShowPlayerCommand(HideMeManager manager) {
        super(manager, "show", "hideme.show.player", 1, false);
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
}
