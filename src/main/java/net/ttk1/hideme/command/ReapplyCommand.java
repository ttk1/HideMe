package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMe;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class ReapplyCommand extends AbstractCommand {
    public ReapplyCommand(HideMe plugin) {
        super(plugin, "reapply", "hideme.reapply", 0);
    }

    @Override
    public void executeImpl(CommandSender sender, String[] args) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            manager.apply(player);
        }
        sender.sendMessage("Reapplied!");
    }

    @Override
    public Set<String> tabCompleteImpl(CommandSender sender, String[] args) {
        HashSet<String> candidates = new HashSet<>();
        if (args.length == 0) {
            candidates.add(commandName);
        } else if (args.length == 1 && commandName.startsWith(args[0])) {
            candidates.add(commandName);
        }
        return candidates;
    }
}
