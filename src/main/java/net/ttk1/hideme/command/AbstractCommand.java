package net.ttk1.hideme.command;

import com.google.common.annotations.VisibleForTesting;
import net.ttk1.hideme.HideMe;
import net.ttk1.hideme.HideMeManager;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractCommand implements HideMeCommand {
    private final HideMe plugin;
    protected final HideMeManager manager;
    protected final String commandName;
    protected final String permission;
    private final int argc;

    public AbstractCommand(HideMe plugin, String commandName, String permission, int argc) {
        this.plugin = plugin;
        this.manager = plugin.getManager();
        this.commandName = commandName;
        this.permission = permission;
        this.argc = argc;
    }

    @VisibleForTesting
    final boolean checkPermission(CommandSender sender) {
        return sender.hasPermission(permission);
    }

    protected Server getServer() {
        return plugin.getServer();
    }

    @Override
    public final boolean match(String[] args) {
        return args.length - 1 == argc && args[0].equals(commandName);
    }

    @Override
    public final void execute(CommandSender sender, String[] args) {
        if (checkPermission(sender)) {
            executeImpl(sender, args);
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
    abstract protected void executeImpl(CommandSender sender, String[] args);

    @Override
    public final Set<String> tabComplete(CommandSender sender, String[] args) {
        if (checkPermission(sender)) {
            return tabCompleteImpl(sender, args);
        } else {
            return new HashSet<>();
        }
    }

    /**
     * タブ補完の実装をここに記述する
     *
     * @param sender タブ補完実行者
     * @param args   コマンド引数
     * @return 補完候補のセット
     */
    abstract protected Set<String> tabCompleteImpl(CommandSender sender, String[] args);
}
