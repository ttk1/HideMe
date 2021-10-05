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

public class ShowCommandTest {
    @Test
    public void commandNameAndPermissionTest() {
        HideMeManager manager = mock(HideMeManager.class);
        ShowCommand command = new ShowCommand(manager);
        assertThat(command.commandName, is("show"));
        assertThat(command.permission, is("hideme.show"));
    }

    @Test
    public void executeImplTest() {
        HideMeManager manager = mock(HideMeManager.class);
        ShowCommand command = new ShowCommand(manager);
        Player player = mock(Player.class);

        // すでに visible
        when(manager.isHidden(player)).thenReturn(false);
        command.executeImpl(player, new String[]{});
        verify(player).sendMessage("You are already visible.");
        verify(manager, never()).showPlayer(any());

        // 通常パターン
        reset(player, manager);
        when(manager.isHidden(player)).thenReturn(true);
        command.executeImpl(player, new String[]{});
        verify(player, times(1)).sendMessage("You are now visible.");
        verify(manager, times(1)).showPlayer(player);
    }

    @Test
    public void tabCompleteImplTest() {
        HideMeManager manager = mock(HideMeManager.class);
        ShowCommand command = new ShowCommand(manager);
        Player player = mock(Player.class);
        Set<String> candidates = new HashSet<>();

        // コマンド違い（マッチしない）
        command.tabCompleteImpl(player, new String[]{"x"}, candidates);
        assertThat(candidates, is(new HashSet<>()));

        // マッチ
        candidates.clear();
        command.tabCompleteImpl(player, new String[]{}, candidates);
        assertThat(candidates, is(new HashSet<>(Collections.singletonList("show"))));
        candidates.clear();
        command.tabCompleteImpl(player, new String[]{"s"}, candidates);
        assertThat(candidates, is(new HashSet<>(Collections.singletonList("show"))));
    }
}
