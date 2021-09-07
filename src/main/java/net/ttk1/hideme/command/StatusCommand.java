package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMe;
import net.ttk1.hideme.HideMeManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class StatusCommand implements HideMeCommand {
    private final String SUB_COMMAND = "status";
    private final String PERMISSION = "hideme.status";

    private final HideMe plugin;
    private final HideMeManager hideMeManager;

    public StatusCommand(HideMe plugin, HideMeManager hideMeManager) {
        this.plugin = plugin;
        this.hideMeManager = hideMeManager;
    }

    @Override
    public boolean match(String[] args) {
        return args.length == 1 && args[0].equals(SUB_COMMAND);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission(PERMISSION)) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (hideMeManager.isHidden(player)) {
                    player.sendMessage("You are now hidden.");
                } else {
                    player.sendMessage("You are now visible.");
                }
            } else {
                sender.sendMessage("This is player command!");
            }
        } else {
            sender.sendMessage("You don't hove permission to perform this command!");
        }
    }

    @Override
    public Set<String> tabComplete(CommandSender sender, String[] args) {
        HashSet<String> candidates = new HashSet<>();
        if (sender instanceof Player && sender.hasPermission(PERMISSION)) {
            if (args.length == 0) {
                candidates.add(SUB_COMMAND);
            } else if (args.length == 1 && SUB_COMMAND.startsWith(args[0])) {
                candidates.add(SUB_COMMAND);
            }
        }
        return candidates;
    }
}
