package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMeManager;
import org.bukkit.entity.Player;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class HideCommandTest {
    @Test
    public void commandNameAndPermissionTest() {
        HideMeManager manager = mock(HideMeManager.class);
        HideCommand command = new HideCommand(manager);
        assertThat(command.commandName, is("hide"));
        assertThat(command.permission, is("hideme.hide"));
    }

    @Test
    public void executeImplTest() {
        HideMeManager manager = mock(HideMeManager.class);
        HideCommand command = new HideCommand(manager);
        Player player = mock(Player.class);

        // すでに hidden
        when(manager.isHidden(player)).thenReturn(true);
        command.executeImpl(player, new String[]{});
        verify(player).sendMessage("You are already hidden.");
        verify(manager, never()).hidePlayer(any());

        // 通常パターン
        reset(player, manager);
        when(manager.isHidden(player)).thenReturn(false);
        command.executeImpl(player, new String[]{});
        verify(player, times(1)).sendMessage("You are now hidden.");
        verify(manager, times(1)).hidePlayer(player);
    }
}
