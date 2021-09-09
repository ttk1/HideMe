package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMe;
import net.ttk1.hideme.HideMeManager;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

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
    public void executeTest() {
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
    public void tabCompleteTest() {
        HideMe plugin = mock(HideMe.class);
        HideCommand command = new HideCommand(plugin);

        // 権限なし（マッチしない）
        Player player = mock(Player.class);
        when(player.hasPermission("hideme.hide")).thenReturn(false);
        assertThat(command.tabComplete(player, new String[]{"h"}), is(new HashSet<>()));

        // コマンド違い（マッチしない）
        when(player.hasPermission("hideme.hide")).thenReturn(true);
        assertThat(command.tabComplete(player, new String[]{"x"}), is(new HashSet<>()));

        // 引数多い（マッチしない）
        assertThat(command.tabComplete(player, new String[]{"hide", "x"}), is(new HashSet<>()));

        // マッチ
        assertThat(command.tabComplete(player, new String[]{}), is(new HashSet<>(Collections.singletonList("hide"))));
        assertThat(command.tabComplete(player, new String[]{"h"}), is(new HashSet<>(Collections.singletonList("hide"))));

        // console（マッチしない）
        ConsoleCommandSender console = mock(ConsoleCommandSender.class);
        assertThat(command.tabComplete(console, new String[]{}), is(new HashSet<>()));
        assertThat(command.tabComplete(console, new String[]{"h"}), is(new HashSet<>()));
    }
}
