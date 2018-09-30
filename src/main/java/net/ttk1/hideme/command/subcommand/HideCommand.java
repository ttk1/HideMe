package net.ttk1.hideme.command.subcommand;

import net.ttk1.hideme.HideMe;
import net.ttk1.hideme.api.PlayerManager;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class HideCommand implements SubCommand {
    private final String SUB_COMMAND = "hide";
    private final String PERMISSION = "hideme.hide";

    private HideMe plugin;
    private PlayerManager playerManager;

    public HideCommand(HideMe plugin, PlayerManager playerManager) {
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
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if(!playerManager.isHidden(player)) {
                    playerManager.addHiddenPlayer(player);
                    plugin.hideMe(player);
                    player.sendMessage("You are now hidden.");
                } else {
                    player.sendMessage("You are already hidden.");
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
        HashSet<String> candidate = new HashSet<>();
        if (sender.hasPermission(PERMISSION)) {
            if (args.length == 0) {
                candidate.add(SUB_COMMAND);
            } else if (args.length == 1 && SUB_COMMAND.startsWith(args[0])) {
                candidate.add(SUB_COMMAND);
            }
        }
        return candidate;
    }
}