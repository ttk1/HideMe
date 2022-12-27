package net.ttk1.hideme.command;

import com.google.common.annotations.VisibleForTesting;
import net.ttk1.hideme.HideMeManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public abstract class AbstractCommand implements HideMeCommand {
    protected final HideMeManager manager;
    protected final String commandName;
    protected final String permission;
    private final int argc;
    private final boolean playerOnly;

    public AbstractCommand(HideMeManager manager, String commandName, String permission, int argc, boolean playerOnly) {
        this.manager = manager;
        this.commandName = commandName;
        this.permission = permission;
        this.argc = argc;
        this.playerOnly = playerOnly;
    }

    @VisibleForTesting
    final boolean checkPermission(CommandSender sender) {
        return sender.hasPermission(permission);
    }

    @Override
    public final boolean match(String[] args) {
        return args.length - 1 == argc && args[0].equals(commandName);
    }

    @Override
    public final void execute(CommandSender sender, String[] args) {
        if (checkPermission(sender)) {
            if (!playerOnly || sender instanceof Player) {
                executeImpl(sender, args);
            } else {
                sender.sendMessage("This is player command!");
            }
        } else {
            sender.sendMessage("You don't hove permission to perform this command!");
        }
    }

    /**
     * コマンドの実装をここに記述する
     *
     * @param sender コマンド実行者
     * @param args   コマンド引数
     */
    protected abstract void executeImpl(CommandSender sender, String[] args);

    @Override
    public final void tabComplete(CommandSender sender, String[] args, Set<String> candidates) {
        if (args.length - 1 <= argc && (!playerOnly || sender instanceof Player) && checkPermission(sender)) {
            tabCompleteImpl(sender, args, candidates);
        }
    }

    /**
     * タブ補完の実装
     *
     * @param sender     タブ補完実行者
     * @param args       コマンド引数
     * @param candidates タブ補完候補をこれに詰める
     */
    protected void tabCompleteImpl(CommandSender sender, String[] args, Set<String> candidates) {
        if (args.length == 1 && commandName.startsWith(args[0])) {
            // コマンド名を候補とする
            candidates.add(commandName);
        }
    }
}
