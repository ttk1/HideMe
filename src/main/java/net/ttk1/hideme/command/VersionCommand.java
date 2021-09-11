package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMe;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.Set;

public class VersionCommand extends AbstractCommand {
    private final String pluginVersionString;

    public VersionCommand(HideMe plugin) {
        super(plugin, "version", "hideme.version", 0);
        this.pluginVersionString = plugin.getDescription().getVersion();
    }

    @Override
    public void executeImpl(CommandSender sender, String[] args) {
        sender.sendMessage(pluginVersionString);
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
