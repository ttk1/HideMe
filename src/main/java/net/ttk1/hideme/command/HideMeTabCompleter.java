package net.ttk1.hideme.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

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
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command bukkitCommand, @NotNull String alias, @NotNull String[] args) {
        List<HideMeCommand> commands = commandManager.getCommands();
        Set<String> candidates = new HashSet<>();
        for (HideMeCommand command : commands) {
            command.tabComplete(sender, args, candidates);
        }
        return new ArrayList<>(candidates);
    }
}
