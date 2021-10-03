package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMe;
import net.ttk1.hideme.HideMeManager;
import org.bukkit.OfflinePlayer;
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

public class HidePlayerCommandTest {
    @Test
    public void commandNameAndPermissionTest() {
        HideMe plugin = mock(HideMe.class);
        HidePlayerCommand command = new HidePlayerCommand(plugin);
        assertThat(command.commandName, is("hide"));
        assertThat(command.permission, is("hideme.hide.player"));
    }

    @Test
    public void executeImplTest() {
        HideMe plugin = mock(HideMe.class);
        HideMeManager manager = mock(HideMeManager.class);
        when(plugin.getManager()).thenReturn(manager);
        HidePlayerCommand command = new HidePlayerCommand(plugin);
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

    @Test
    public void tabCompleteImplTest() {
        HideMe plugin = mock(HideMe.class);
        HideMeManager manager = mock(HideMeManager.class);
        Player john = mock(Player.class);
        when(john.getName()).thenReturn("john");
        Player michael = mock(Player.class);
        when(michael.getName()).thenReturn("michael");
        when(manager.getOnlinePlayers()).thenAnswer(x -> Arrays.asList(john, michael));
        when(plugin.getManager()).thenReturn(manager);
        HidePlayerCommand command = new HidePlayerCommand(plugin);
        CommandSender sender = mock(CommandSender.class);
        Set<String> candidates = new HashSet<>();

        // args.length == 0
        command.tabCompleteImpl(sender, new String[]{}, candidates);
        assertThat(candidates, is(new HashSet<>(Collections.singletonList("hide"))));

        // args.length == 1
        // コマンド名が前方一致する
        candidates.clear();
        command.tabCompleteImpl(sender, new String[]{"h"}, candidates);
        assertThat(candidates, is(new HashSet<>(Collections.singletonList("hide"))));

        // コマンド名が完全一致する
        candidates.clear();
        command.tabCompleteImpl(sender, new String[]{"hide"}, candidates);
        assertThat(candidates, is(new HashSet<>(Arrays.asList("john", "michael"))));

        // コマンド名が前方一致しない
        candidates.clear();
        command.tabCompleteImpl(sender, new String[]{"z"}, candidates);
        assertThat(candidates, is(new HashSet<>()));

        // args.length == 2
        candidates.clear();
        command.tabCompleteImpl(sender, new String[]{"hide", "j"}, candidates);
        assertThat(candidates, is(new HashSet<>(Collections.singletonList("john"))));
    }
}
