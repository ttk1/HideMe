package net.ttk1.hideme.command.subcommand;

import net.ttk1.hideme.HideMe;
import net.ttk1.hideme.api.PlayerManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ShowPlayerCommand extends AbstractSubCommand {
    private final String SUB_COMMAND = "show";
    private final String PERMISSION = "hideme.show.player";

    private HideMe plugin;
    private PlayerManager playerManager;

    public ShowPlayerCommand(HideMe plugin, PlayerManager playerManager) {
        super(plugin, playerManager);
        this.plugin = plugin;
        this.playerManager = playerManager;
    }

    @Override
    public boolean match(String[] args) {
        if (args.length == 2 && args[0].equals(SUB_COMMAND)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission(PERMISSION)) {
            String playerName = args[1];

            Player player = plugin.getServer().getPlayer(playerName);

            if (player != null) {
                // オンラインプレーヤー
                if(playerManager.isHidden(player)) {
                    playerManager.removeHiddenPlayer(player);
                    showPlayer(player);
                    player.sendMessage("You are now visible.");
                    sender.sendMessage("Player "+playerName+" is now visible.");
                } else {
                    sender.sendMessage("Player "+playerName+" is already visible.");
                }
            } else {
                // オフラインプレーヤー
                for (OfflinePlayer offlinePlayer: plugin.getServer().getOfflinePlayers()) {
                    if (offlinePlayer.getName().equals(playerName)) {
                        if (playerManager.isHidden(offlinePlayer)) {
                            playerManager.removeHiddenPlayer(offlinePlayer);
                            sender.sendMessage("Player "+playerName+" is now visible.");
                        }
                        sender.sendMessage("Player "+playerName+" is already visible.");
                        return;
                    }
                }

                // not found
                sender.sendMessage("Player "+playerName+" not found.");
            }
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
                if (args[0].equals(SUB_COMMAND)) {
                    // すべてのオンラインプレーヤーを候補とする
                    candidates.addAll(plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toSet()));
                } else {
                    // コマンドを候補とする
                    candidates.add(SUB_COMMAND);
                }
            } else if (args.length == 2) {
                // プレフィックスが一致するオンラインプレーヤーを候補とする
                candidates.addAll(plugin.getServer().getOnlinePlayers().stream().map(Player::getName).filter(i -> i.startsWith(args[1])).collect(Collectors.toSet()));
            }
        }
        return candidates;
    }
}
