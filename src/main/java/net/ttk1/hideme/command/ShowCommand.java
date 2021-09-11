package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMe;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class ShowCommand extends AbstractCommand {
    public ShowCommand(HideMe plugin) {
        super(plugin, "show", "hideme.show", 0);
    }

    @Override
    public void executeImpl(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (manager.isHidden(player)) {
                manager.showPlayer(player);
                player.sendMessage("You are now visible.");
            } else {
                player.sendMessage("You are already visible.");
            }
        } else {
            sender.sendMessage("This is player command!");
        }
    }

    @Override
    public Set<String> tabCompleteImpl(CommandSender sender, String[] args) {
        HashSet<String> candidates = new HashSet<>();
        if (sender instanceof Player) {
            if (args.length == 0) {
                candidates.add(commandName);
            } else if (args.length == 1 && commandName.startsWith(args[0])) {
                candidates.add(commandName);
            }
        }
        return candidates;
    }
}