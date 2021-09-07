package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMe;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class StatusCommand extends AbstractCommand {
    public StatusCommand(HideMe plugin) {
        super(plugin, "status", "hideme.status", 0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (checkPermission(sender)) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (manager.isHidden(player)) {
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
        if (sender instanceof Player && checkPermission(sender)) {
            if (args.length == 0) {
                candidates.add(commandName);
            } else if (args.length == 1 && commandName.startsWith(args[0])) {
                candidates.add(commandName);
            }
        }
        return candidates;
    }
}
