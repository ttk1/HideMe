package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMeManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class ShowPlayerCommandTest {
    @Test
    public void commandNameAndPermissionTest() {
        HideMeManager manager = mock(HideMeManager.class);
        ShowPlayerCommand command = new ShowPlayerCommand(manager);
        assertThat(command.commandName, is("show"));
        assertThat(command.permission, is("hideme.show.player"));
    }

    @Test
    public void executeImplTest() {
        HideMeManager manager = mock(HideMeManager.class);
        ShowPlayerCommand command = new ShowPlayerCommand(manager);
        CommandSender sender = mock(CommandSender.class);
        OfflinePlayer player = mock(OfflinePlayer.class);
        when(player.getName()).thenReturn("playerA");

        // すでに visible
        when(manager.getOfflinePlayer("playerA")).thenReturn(player);
        when(manager.isHidden(player)).thenReturn(false);
        command.executeImpl(sender, new String[]{"show", "playerA"});
        verify(sender, times(1)).sendMessage("Player playerA is already visible.");

        // 通常パターン
        reset(sender, manager);
        when(manager.getOfflinePlayer("playerA")).thenReturn(player);
        when(manager.isHidden(player)).thenReturn(true);
        command.executeImpl(sender, new String[]{"show", "playerA"});
        verify(sender, times(1)).sendMessage("Player playerA is now visible.");
        verify(manager, times(1)).showPlayer(player);

        // not found
        reset(sender, manager);
        when(manager.getOfflinePlayer("playerA")).thenReturn(null);
        command.executeImpl(sender, new String[]{"show", "playerA"});
        verify(sender, times(1)).sendMessage("Player playerA not found.");
    }
}
