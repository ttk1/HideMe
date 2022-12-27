package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMeManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class HidePlayerCommandTest {
    @Test
    public void commandNameAndPermissionTest() {
        HideMeManager manager = mock(HideMeManager.class);
        HidePlayerCommand command = new HidePlayerCommand(manager);
        assertThat(command.commandName, is("hide"));
        assertThat(command.permission, is("hideme.hide.player"));
    }

    @Test
    public void executeImplTest() {
        HideMeManager manager = mock(HideMeManager.class);
        HidePlayerCommand command = new HidePlayerCommand(manager);
        CommandSender sender = mock(CommandSender.class);
        OfflinePlayer player = mock(OfflinePlayer.class);
        when(player.getName()).thenReturn("playerA");

        // すでに hidden
        when(manager.getOfflinePlayer("playerA")).thenReturn(player);
        when(manager.isHidden(player)).thenReturn(true);
        command.executeImpl(sender, new String[]{"hide", "playerA"});
        verify(sender, times(1)).sendMessage("Player playerA is already hidden.");

        // 通常パターン
        reset(sender, manager);
        when(manager.getOfflinePlayer("playerA")).thenReturn(player);
        when(manager.isHidden(player)).thenReturn(false);
        command.executeImpl(sender, new String[]{"hide", "playerA"});
        verify(sender, times(1)).sendMessage("Player playerA is now hidden.");
        verify(manager, times(1)).hidePlayer(player);

        // not found
        reset(sender, manager);
        when(manager.getOfflinePlayer("playerA")).thenReturn(null);
        command.executeImpl(sender, new String[]{"hide", "playerA"});
        verify(sender, times(1)).sendMessage("Player playerA not found.");
    }
}
