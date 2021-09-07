package net.ttk1.hideme.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class HideMeCommandExecutor implements CommandExecutor {
    private final HideMeCommandManager commandManager;

    public HideMeCommandExecutor(HideMeCommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command bukkitCommand, String label, String[] args) {
        List<HideMeCommand> commands = commandManager.getCommands();
        List<HideMeCommand> matchedCommands = commands.stream().filter(i -> i.match(args)).collect(Collectors.toList());
        if (matchedCommands.size() == 1) {
            HideMeCommand matchedCommand = matchedCommands.get(0);
            matchedCommand.execute(sender, args);
        } else {
            sender.sendMessage("Command not found!");
        }
        return true;
    }
}
