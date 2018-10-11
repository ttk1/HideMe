package net.ttk1.hideme.command.subcommand;

import net.ttk1.hideme.HideMe;
import net.ttk1.hideme.api.PlayerManager;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class StatusCommand extends AbstractSubCommand {
    private final String SUB_COMMAND = "status";
    private final String PERMISSION = "hideme.status";

    private HideMe plugin;
    private PlayerManager playerManager;

    public StatusCommand(HideMe plugin, PlayerManager playerManager) {
        super(plugin, playerManager);
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
                if(playerManager.isHidden(player)) {
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
