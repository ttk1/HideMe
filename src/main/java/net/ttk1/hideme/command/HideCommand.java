package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMe;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class HideCommand extends AbstractCommand {
    public HideCommand(HideMe plugin) {
        super(plugin, "hide", "hideme.hide", 0);
    }

    @Override
    protected void executeImpl(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!manager.isHidden(player)) {
                manager.hidePlayer(player);
                player.sendMessage("You are now hidden.");
            } else {
                player.sendMessage("You are already hidden.");
            }
        } else {
            sender.sendMessage("This is player command!");
        }
    }

    @Override
    protected void tabCompleteImpl(CommandSender sender, String[] args, Set<String> candidates) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                candidates.add(commandName);
            } else if (args.length == 1 && commandName.startsWith(args[0])) {
                candidates.add(commandName);
            }
        }
    }
}
