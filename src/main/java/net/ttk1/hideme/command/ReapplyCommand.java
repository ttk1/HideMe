package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMeManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReapplyCommand extends AbstractCommand {
    public ReapplyCommand(HideMeManager manager) {
        super(manager, "reapply", "hideme.reapply", 0, false);
    }

    @Override
    protected void executeImpl(CommandSender sender, String[] args) {
        for (Player player : manager.getOnlinePlayers()) {
            manager.apply(player);
        }
        sender.sendMessage("Reapplied!");
    }
}
