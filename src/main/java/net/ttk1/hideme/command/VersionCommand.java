package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMe;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.Set;

public class VersionCommand extends AbstractCommand {
    public VersionCommand(HideMe plugin) {
        super(plugin, "version", "hideme.version", 0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (checkPermission(sender)) {
            sender.sendMessage(plugin.getDescription().getVersion());
        } else {
            sender.sendMessage("You don't hove permission to perform this command!");
        }
    }

    @Override
    public Set<String> tabComplete(CommandSender sender, String[] args) {
        HashSet<String> candidates = new HashSet<>();
        if (checkPermission(sender)) {
            if (args.length == 0) {
                candidates.add(commandName);
            } else if (args.length == 1 && commandName.startsWith(args[0])) {
                candidates.add(commandName);
            }
        }
        return candidates;
    }
}
