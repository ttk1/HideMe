package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMeManager;
import org.bukkit.command.CommandSender;

public class VersionCommand extends AbstractCommand {

    public VersionCommand(HideMeManager manager) {
        super(manager, "version", "hideme.version", 0, false);
    }

    @Override
    protected void executeImpl(CommandSender sender, String[] args) {
        sender.sendMessage(manager.getVersion());
    }
}
