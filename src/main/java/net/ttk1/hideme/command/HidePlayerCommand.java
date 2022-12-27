package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMeManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class HidePlayerCommand extends AbstractPlayerCommand {
    public HidePlayerCommand(HideMeManager manager) {
        super(manager, "hide", "hideme.hide.player", 1, false);
    }

    @Override
    protected void executeImpl(CommandSender sender, String[] args) {
        String playerName = args[1];
        OfflinePlayer player = manager.getOfflinePlayer(playerName);
        if (player != null) {
            if (!manager.isHidden(player)) {
                manager.hidePlayer(player);
                sender.sendMessage("Player " + playerName + " is now hidden.");
            } else {
                sender.sendMessage("Player " + playerName + " is already hidden.");
            }
        } else {
            // not found
            sender.sendMessage("Player " + playerName + " not found.");
        }
    }
}
