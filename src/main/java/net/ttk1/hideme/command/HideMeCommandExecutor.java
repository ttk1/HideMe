package net.ttk1.hideme.command;

import com.google.inject.Inject;

import net.ttk1.hideme.command.subcommand.SubCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class HideMeCommandExecutor implements CommandExecutor {
    private SubCommandManager subCommandManager;

    @Inject
    private void setSubCommandManager(SubCommandManager subCommandManager) {
        this.subCommandManager = subCommandManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<SubCommand> subCommands = subCommandManager.getSubCommands();
        List<SubCommand> matchedCommands = subCommands.stream().filter(i -> i.match(args)).collect(Collectors.toList());
        if (matchedCommands.size() == 1) {
            SubCommand matchedCommand = matchedCommands.get(0);
            matchedCommand.execute(sender, args);
        } else {
            // コマンドがマッチしなかったときの処理
            sender.sendMessage("Command not found!");
        }

        return true;
    }
}
