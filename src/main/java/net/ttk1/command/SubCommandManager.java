package net.ttk1.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import net.ttk1.HideMe;
import net.ttk1.api.PlayerManager;
import net.ttk1.command.subcommand.*;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class SubCommandManager {
    private List<SubCommand> subCommands;
    private HideMe plugin;
    private PlayerManager playerManager;

    //@Inject
    private void setPlugin(HideMe plugin) {
        this.plugin = plugin;
    }

    //@Inject
    private void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Inject
    public SubCommandManager(HideMe plugin, PlayerManager playerManager) {
        setPlugin(plugin);
        setPlayerManager(playerManager);

        subCommands = new ArrayList<>();
        subCommands.add(new VersionCommand(plugin, playerManager));
        subCommands.add(new ListCommand(plugin, playerManager));
        subCommands.add(new HideCommand(plugin, playerManager));
        subCommands.add(new ShowCommand(plugin, playerManager));
        subCommands.add(new StatusCommand(plugin, playerManager));
    }

    public List<SubCommand> getSubCommands() {
        return subCommands;
    }
}
