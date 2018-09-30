package net.ttk1.command;

import com.google.inject.Inject;
import net.ttk1.HideMe;
import net.ttk1.api.PlayerManager;
import net.ttk1.command.subcommand.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HideMeTabCompleter implements TabCompleter {
    private HideMe plugin;
    private PlayerManager playerManager;
    private SubCommandManager subCommandManager;

    @Inject
    private void setPlugin(HideMe plugin) {
        this.plugin = plugin;
    }

    @Inject
    private void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Inject
    private void setSubCommandManager(SubCommandManager subCommandManager) {
        this.subCommandManager = subCommandManager;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<SubCommand> subCommands = subCommandManager.getSubCommands();
        Set<String> candidateSet = new HashSet<>();

        for (SubCommand subCommand: subCommands) {
            candidateSet.addAll(subCommand.tabComplete(sender, args));
        }

        return new ArrayList<>(candidateSet);
    }
}
