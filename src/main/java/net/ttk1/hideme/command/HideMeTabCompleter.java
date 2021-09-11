package net.ttk1.hideme.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HideMeTabCompleter implements TabCompleter {
    private final HideMeCommandManager commandManager;

    public HideMeTabCompleter(HideMeCommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command bukkitCommand, String alias, String[] args) {
        List<HideMeCommand> commands = commandManager.getCommands();
        Set<String> candidates = new HashSet<>();
        for (HideMeCommand command : commands) {
            command.tabComplete(sender, args, candidates);
        }
        return new ArrayList<>(candidates);
    }
}
