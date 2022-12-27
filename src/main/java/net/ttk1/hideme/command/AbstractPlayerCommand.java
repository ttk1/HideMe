package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMeManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractPlayerCommand extends AbstractCommand {
    public AbstractPlayerCommand(HideMeManager manager, String commandName, String permission, int argc, boolean playerOnly) {
        super(manager, commandName, permission, argc, playerOnly);
    }

    @Override
    protected void tabCompleteImpl(CommandSender sender, String[] args, Set<String> candidates) {
        if (args.length == 1 && commandName.startsWith(args[0])) {
            // コマンド名を候補とする
            candidates.add(commandName);
        } else if (args.length == 2 && commandName.equals(args[0])) {
            // プレフィックスが一致するオンラインプレーヤーを候補とする
            candidates.addAll(manager.getOnlinePlayers().stream().map(Player::getName).filter(i -> i.startsWith(args[1])).collect(Collectors.toSet()));
        }
    }
}
