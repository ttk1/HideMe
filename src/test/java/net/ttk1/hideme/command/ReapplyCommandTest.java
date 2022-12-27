package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMeManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class ReapplyCommandTest {
    @Test
    public void commandNameAndPermissionTest() {
        HideMeManager manager = mock(HideMeManager.class);
        ReapplyCommand command = new ReapplyCommand(manager);
        assertThat(command.commandName, is("reapply"));
        assertThat(command.permission, is("hideme.reapply"));
    }

    @Test
    public void executeImplTest() {
        HideMeManager manager = mock(HideMeManager.class);
        Player john = mock(Player.class);
        Player michael = mock(Player.class);
        when(manager.getOnlinePlayers()).thenAnswer(x -> Arrays.asList(john, michael));
        ReapplyCommand command = new ReapplyCommand(manager);
        CommandSender sender = mock(CommandSender.class);

        command.executeImpl(sender, new String[]{"reapply"});
        verify(manager, times(2)).apply(any());
        verify(manager, times(1)).apply(john);
        verify(manager, times(1)).apply(michael);
    }
}
