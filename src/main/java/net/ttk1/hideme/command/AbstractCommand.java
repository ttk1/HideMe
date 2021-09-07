package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMe;
import net.ttk1.hideme.HideMeManager;
import org.bukkit.command.CommandSender;

public abstract class AbstractCommand implements HideMeCommand {
    protected final HideMe plugin;
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

    protected boolean checkPermission(CommandSender sender) {
        return sender.hasPermission(permission);
    }

    @Override
    public boolean match(String[] args) {
        return args.length - 1 == argc && args[0].equals(commandName);
    }
}
