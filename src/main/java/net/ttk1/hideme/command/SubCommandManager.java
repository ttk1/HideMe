package net.ttk1.hideme.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import net.ttk1.hideme.HideMe;
import net.ttk1.hideme.api.HideMeManager;
import net.ttk1.hideme.command.subcommand.*;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class SubCommandManager {
    private List<SubCommand> subCommands;

    @Inject
    public SubCommandManager(HideMe plugin, HideMeManager hideMeManager) {
        subCommands = new ArrayList<>();
        subCommands.add(new VersionCommand(plugin, hideMeManager));
        subCommands.add(new ListCommand(plugin, hideMeManager));
        subCommands.add(new HideCommand(plugin, hideMeManager));
        subCommands.add(new ShowCommand(plugin, hideMeManager));
        subCommands.add(new StatusCommand(plugin, hideMeManager));
        subCommands.add(new HidePlayerCommand(plugin, hideMeManager));
        subCommands.add(new ShowPlayerCommand(plugin, hideMeManager));
        subCommands.add(new ReloadCommand(plugin, hideMeManager));
    }

    public List<SubCommand> getSubCommands() {
        return subCommands;
    }
}
