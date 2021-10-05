package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMeManager;
import org.bukkit.entity.Player;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

    @Test
    public void tabCompleteImplTest() {
        HideMeManager manager = mock(HideMeManager.class);
        StatusCommand command = new StatusCommand(manager);
        Player player = mock(Player.class);
        Set<String> candidates = new HashSet<>();

        // コマンド違い（マッチしない）
        command.tabCompleteImpl(player, new String[]{"x"}, candidates);
        assertThat(candidates, is(new HashSet<>()));

        // マッチ
        candidates.clear();
        command.tabCompleteImpl(player, new String[]{}, candidates);
        assertThat(candidates, is(new HashSet<>(Collections.singletonList("status"))));
        candidates.clear();
        command.tabCompleteImpl(player, new String[]{"s"}, candidates);
        assertThat(candidates, is(new HashSet<>(Collections.singletonList("status"))));
    }
}
