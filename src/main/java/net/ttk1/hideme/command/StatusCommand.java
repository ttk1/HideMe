package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMeManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatusCommand extends AbstractCommand {
    public StatusCommand(HideMeManager manager) {
        super(manager, "status", "hideme.status", 0, true);
    }

    @Override
    protected void executeImpl(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (manager.isHidden(player)) {
            player.sendMessage("You are now hidden.");
        } else {
            player.sendMessage("You are now visible.");
        }
    }
}
