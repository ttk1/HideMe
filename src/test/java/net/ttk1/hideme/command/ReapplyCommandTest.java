package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMeManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

    @Test
    public void tabCompleteImplTest() {
        HideMeManager manager = mock(HideMeManager.class);
        ReapplyCommand command = new ReapplyCommand(manager);
        CommandSender sender = mock(CommandSender.class);
        Set<String> candidates = new HashSet<>();

        // args.length == 0
        command.tabCompleteImpl(sender, new String[]{}, candidates);
        assertThat(candidates, is(new HashSet<>(Collections.singletonList("reapply"))));

        // args.length == 1
        // コマンド名が前方一致する
        candidates.clear();
        command.tabCompleteImpl(sender, new String[]{"r"}, candidates);
        assertThat(candidates, is(new HashSet<>(Collections.singletonList("reapply"))));

        // コマンド名が前方一致しない
        candidates.clear();
        command.tabCompleteImpl(sender, new String[]{"z"}, candidates);
        assertThat(candidates, is(new HashSet<>()));
    }
}
