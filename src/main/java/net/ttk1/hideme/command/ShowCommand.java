package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMeManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShowCommand extends AbstractCommand {
    public ShowCommand(HideMeManager manager) {
        super(manager, "show", "hideme.show", 0, true);
    }

    @Override
    protected void executeImpl(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (manager.isHidden(player)) {
            manager.showPlayer(player);
            player.sendMessage("You are now visible.");
        } else {
            player.sendMessage("You are already visible.");
        }
    }
}
