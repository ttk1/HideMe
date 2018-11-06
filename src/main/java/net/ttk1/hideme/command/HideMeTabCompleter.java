package net.ttk1.hideme.command;

import com.google.inject.Inject;

import net.ttk1.hideme.command.subcommand.SubCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HideMeTabCompleter implements TabCompleter {
    private SubCommandManager subCommandManager;

    @Inject
    private void setSubCommandManager(SubCommandManager subCommandManager) {
        this.subCommandManager = subCommandManager;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<SubCommand> subCommands = subCommandManager.getSubCommands();
        Set<String> candidates = new HashSet<>();

        for (SubCommand subCommand: subCommands) {
            candidates.addAll(subCommand.tabComplete(sender, args));
        }

        return new ArrayList<>(candidates);
    }
}
