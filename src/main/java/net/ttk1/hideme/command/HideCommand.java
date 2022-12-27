package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMeManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HideCommand extends AbstractCommand {
    public HideCommand(HideMeManager manager) {
        super(manager, "hide", "hideme.hide", 0, true);
    }

    @Override
    protected void executeImpl(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (!manager.isHidden(player)) {
            manager.hidePlayer(player);
            player.sendMessage("You are now hidden.");
        } else {
            player.sendMessage("You are already hidden.");
        }
    }
}
