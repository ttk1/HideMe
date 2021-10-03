package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMe;
import org.bukkit.command.CommandSender;

import java.util.Set;

public class VersionCommand extends AbstractCommand {

    public VersionCommand(HideMe plugin) {
        super(plugin, "version", "hideme.version", 0, false);
    }

    @Override
    protected void executeImpl(CommandSender sender, String[] args) {
        sender.sendMessage(manager.getVersion());
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
