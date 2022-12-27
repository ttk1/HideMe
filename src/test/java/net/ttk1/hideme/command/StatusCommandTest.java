package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMeManager;
import org.bukkit.entity.Player;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class StatusCommandTest {
    @Test
    public void commandNameAndPermissionTest() {
        HideMeManager manager = mock(HideMeManager.class);
        StatusCommand command = new StatusCommand(manager);
        assertThat(command.commandName, is("status"));
        assertThat(command.permission, is("hideme.status"));
    }

    @Test
    public void executeImplTest() {
        HideMeManager manager = mock(HideMeManager.class);
        StatusCommand command = new StatusCommand(manager);
        Player player = mock(Player.class);

        // hidden
        when(manager.isHidden(player)).thenReturn(true);
        command.executeImpl(player, new String[]{});
        verify(player).sendMessage("You are now hidden.");

        // visible
        reset(player);
        when(manager.isHidden(player)).thenReturn(false);
        command.executeImpl(player, new String[]{});
        verify(player).sendMessage("You are now visible.");
    }
}
