package net.ttk1.hideme.command.subcommand;

import net.ttk1.hideme.HideMe;
import net.ttk1.hideme.api.PlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class ReloadCommand extends AbstractSubCommand {
    private final String SUB_COMMAND = "reload";
    private final String PERMISSION = "hideme.reload";

    private HideMe plugin;
    private PlayerManager playerManager;

    public ReloadCommand(HideMe plugin, PlayerManager playerManager) {
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
            for (Player player: plugin.getServer().getOnlinePlayers()) {
                if (playerManager.isHidden(player)) {
                    hidePlayer(player);
                } else {
                    showPlayer(player);
                }
            }
            sender.sendMessage("Reloaded!");
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
