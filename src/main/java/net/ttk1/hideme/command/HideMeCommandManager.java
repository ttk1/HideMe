package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMe;

import java.util.ArrayList;
import java.util.List;

public class HideMeCommandManager {
    private final List<HideMeCommand> commands;

    public HideMeCommandManager(HideMe plugin) {
        commands = new ArrayList<>();
        commands.add(new VersionCommand(plugin));
        commands.add(new ListCommand(plugin));
        commands.add(new HideCommand(plugin));
        commands.add(new ShowCommand(plugin));
        commands.add(new StatusCommand(plugin));
        commands.add(new HidePlayerCommand(plugin));
        commands.add(new ShowPlayerCommand(plugin));
        commands.add(new ReapplyCommand(plugin));
    }

    public List<HideMeCommand> getCommands() {
        return commands;
    }
}
