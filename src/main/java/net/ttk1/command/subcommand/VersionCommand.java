package net.ttk1.command.subcommand;

import net.ttk1.HideMe;
import net.ttk1.api.PlayerManager;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VersionCommand implements SubCommand {
    private final String SUB_COMMAND = "version";
    private final String PERMISSION = "hideme.version";

    private HideMe plugin;
    private PlayerManager playerManager;

    public VersionCommand(HideMe plugin, PlayerManager playerManager) {
        this.plugin = plugin;
        this.playerManager = playerManager;
    }

    @Override
    public boolean match(String[] args) {
        if (args.length == 1 && args[0].equals(SUB_COMMAND)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission(PERMISSION)) {
            sender.sendMessage(plugin.getDescription().getVersion());
        } else {
            sender.sendMessage("You don't hove permission to perform this command!");
        }
    }

    @Override
    public Set<String> tabComplete(String[] args) {
        HashSet<String> candidate = new HashSet<>();
        if (args.length == 0) {
            candidate.add(SUB_COMMAND);
        } else if (args.length == 1 && SUB_COMMAND.startsWith(args[0])) {
            candidate.add(SUB_COMMAND);
        }
        return candidate;
    }
}