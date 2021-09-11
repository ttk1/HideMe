package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMe;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class ReapplyCommand extends AbstractCommand {
    public ReapplyCommand(HideMe plugin) {
        super(plugin, "reapply", "hideme.reapply", 0);
    }

    @Override
    protected void executeImpl(CommandSender sender, String[] args) {
        for (Player player : getServer().getOnlinePlayers()) {
            manager.apply(player);
        }
        sender.sendMessage("Reapplied!");
    }

    @Override
    protected void tabCompleteImpl(CommandSender sender, String[] args, Set<String> candidates) {
        if (args.length == 0) {
            candidates.add(commandName);
        } else if (args.length == 1 && commandName.startsWith(args[0])) {
            candidates.add(commandName);
        }
    }
}
