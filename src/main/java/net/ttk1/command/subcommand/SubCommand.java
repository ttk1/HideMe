package net.ttk1.command.subcommand;

import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Set;

public interface SubCommand {
    boolean match(String[] args);

    void execute(CommandSender sender, String[] args);

    Set<String> tabComplete(CommandSender sender, String[] args);
}
