package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMe;
import net.ttk1.hideme.HideMeManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class ReapplyCommand implements HideMeCommand {
    private final String SUB_COMMAND = "reapply";
    private final String PERMISSION = "hideme.reapply";

    private final HideMe plugin;
    private final HideMeManager hideMeManager;

    public ReapplyCommand(HideMe plugin, HideMeManager hideMeManager) {
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
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                hideMeManager.apply(player);
            }
            sender.sendMessage("Reapplied!");
        } else {
            sender.sendMessage("You don't hove permission to perform this command!");
        }
    }

    @Override
    public Set<String> tabComplete(CommandSender sender, String[] args) {
        HashSet<String> candidates = new HashSet<>();
        if (sender.hasPermission(PERMISSION)) {
            if (args.length == 0) {
                candidates.add(SUB_COMMAND);
            } else if (args.length == 1 && SUB_COMMAND.startsWith(args[0])) {
                candidates.add(SUB_COMMAND);
            }
        }
        return candidates;
    }
}
