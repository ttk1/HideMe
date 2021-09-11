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

public class HideCommandTest {
    @Test
    public void commandNameAndPermissionTest() {
        HideMe plugin = mock(HideMe.class);
        HideCommand command = new HideCommand(plugin);
        assertThat(command.commandName, is("hide"));
        assertThat(command.permission, is("hideme.hide"));
    }

    @Test
    public void executeImplTest() {
        HideMe plugin = mock(HideMe.class);
        HideMeManager manager = mock(HideMeManager.class);
        when(plugin.getManager()).thenReturn(manager);
        HideMeCommand command = new HideCommand(plugin);

        // 権限なし
        Player player = mock(Player.class);
        when(player.hasPermission("hideme.hide")).thenReturn(false);
        command.execute(player, new String[]{});
        verify(player).sendMessage("You don't hove permission to perform this command!");
        verify(manager, never()).hidePlayer(any());

        // すでに hidden
        reset(player, manager);
        when(player.hasPermission("hideme.hide")).thenReturn(true);
        when(manager.isHidden(player)).thenReturn(true);
        command.execute(player, new String[]{});
        verify(player).sendMessage("You are already hidden.");
        verify(manager, never()).hidePlayer(any());

        // 通常パターン
        reset(player, manager);
        when(player.hasPermission("hideme.hide")).thenReturn(true);
        when(manager.isHidden(player)).thenReturn(false);
        command.execute(player, new String[]{});
        verify(player).sendMessage("You are now hidden.");
        verify(manager, times(1)).hidePlayer(any());

        // コンソール
        reset(manager);
        ConsoleCommandSender console = mock(ConsoleCommandSender.class);
        when(console.hasPermission(anyString())).thenReturn(true);
        command.execute(console, new String[]{});
        verify(console).sendMessage("This is player command!");
        verify(manager, never()).hidePlayer(any());
    }

    @Test
    public void tabCompleteImplTest() {
        HideMe plugin = mock(HideMe.class);
        HideCommand command = new HideCommand(plugin);
        Player player = mock(Player.class);
        ConsoleCommandSender console = mock(ConsoleCommandSender.class);
        Set<String> candidates = new HashSet<>();

        // コマンド違い（マッチしない）
        when(player.hasPermission("hideme.hide")).thenReturn(true);
        command.tabComplete(player, new String[]{"x"}, candidates);
        assertThat(candidates, is(new HashSet<>()));

        // 引数多い（マッチしない）
        candidates.clear();
        command.tabComplete(player, new String[]{"hide", "x"}, candidates);
        assertThat(candidates, is(new HashSet<>()));

        // マッチ
        candidates.clear();
        command.tabComplete(player, new String[]{}, candidates);
        assertThat(candidates, is(new HashSet<>(Collections.singletonList("hide"))));
        candidates.clear();
        command.tabComplete(player, new String[]{"h"}, candidates);
        assertThat(candidates, is(new HashSet<>(Collections.singletonList("hide"))));

        // console（マッチしない）
        candidates.clear();
        command.tabComplete(console, new String[]{}, candidates);
        assertThat(candidates, is(new HashSet<>()));
        candidates.clear();
        command.tabComplete(console, new String[]{"h"}, candidates);
        assertThat(candidates, is(new HashSet<>()));
    }
}
