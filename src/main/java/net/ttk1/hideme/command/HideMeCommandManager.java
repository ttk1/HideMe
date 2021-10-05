package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMeManager;

import java.util.ArrayList;
import java.util.List;

public class HideMeCommandManager {
    private final List<HideMeCommand> commands;

    public HideMeCommandManager(HideMeManager manager) {
        commands = new ArrayList<>();
        commands.add(new VersionCommand(manager));
        commands.add(new ListCommand(manager));
        commands.add(new HideCommand(manager));
        commands.add(new ShowCommand(manager));
        commands.add(new StatusCommand(manager));
        commands.add(new HidePlayerCommand(manager));
        commands.add(new ShowPlayerCommand(manager));
        commands.add(new ReapplyCommand(manager));
    }

    public List<HideMeCommand> getCommands() {
        return commands;
    }
}
