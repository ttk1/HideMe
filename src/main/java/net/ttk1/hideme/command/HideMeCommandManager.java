package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMe;
import net.ttk1.hideme.HideMeManager;

import java.util.ArrayList;
import java.util.List;

public class HideMeCommandManager {
    private final List<HideMeCommand> commands;

    public HideMeCommandManager(HideMe plugin) {
        HideMeManager manager = plugin.getManager();
        commands = new ArrayList<>();
        commands.add(new VersionCommand(plugin, manager));
        commands.add(new ListCommand(plugin, manager));
        commands.add(new HideCommand(plugin, manager));
        commands.add(new ShowCommand(plugin, manager));
        commands.add(new StatusCommand(plugin, manager));
        commands.add(new HidePlayerCommand(plugin, manager));
        commands.add(new ShowPlayerCommand(plugin, manager));
        commands.add(new ReloadCommand(plugin, manager));
    }

    public List<HideMeCommand> getCommands() {
        return commands;
    }
}
