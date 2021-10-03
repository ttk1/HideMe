package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMe;
import net.ttk1.hideme.HideMeManager;
import org.bukkit.command.ConsoleCommandSender;
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
        HideMe plugin = mock(HideMe.class);
        ShowCommand command = new ShowCommand(plugin);
        assertThat(command.commandName, is("show"));
        assertThat(command.permission, is("hideme.show"));
    }

    @Test
    public void executeImplTest() {
        HideMe plugin = mock(HideMe.class);
        HideMeManager manager = mock(HideMeManager.class);
        when(plugin.getManager()).thenReturn(manager);
        ShowCommand command = new ShowCommand(plugin);
        Player player = mock(Player.class);
        ConsoleCommandSender console = mock(ConsoleCommandSender.class);

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

        // コンソール
        reset(manager);
        command.executeImpl(console, new String[]{});
        verify(console).sendMessage("This is player command!");
        verify(manager, never()).hidePlayer(any());
    }

    @Test
    public void tabCompleteImplTest() {
        HideMe plugin = mock(HideMe.class);
        ShowCommand command = new ShowCommand(plugin);
        Player player = mock(Player.class);
        ConsoleCommandSender console = mock(ConsoleCommandSender.class);
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

        // console（マッチしない）
        candidates.clear();
        command.tabCompleteImpl(console, new String[]{}, candidates);
        assertThat(candidates, is(new HashSet<>()));
        candidates.clear();
        command.tabCompleteImpl(console, new String[]{"s"}, candidates);
        assertThat(candidates, is(new HashSet<>()));
    }
}
