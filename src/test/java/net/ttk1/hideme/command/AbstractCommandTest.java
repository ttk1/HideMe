package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMe;
import org.bukkit.command.CommandSender;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class AbstractCommandTest {
    static class HideMeCommandImpl extends AbstractCommand {
        public HideMeCommandImpl(HideMe plugin, String commandName, String permission, int argc, boolean playerOnly) {
            super(plugin, commandName, permission, argc, playerOnly);
        }

        @Override
        protected void executeImpl(CommandSender sender, String[] args) {
        }

        @Override
        protected void tabCompleteImpl(CommandSender sender, String[] args, Set<String> candidates) {
            candidates.add("test");
        }
    }

    @Test
    public void checkPermissionTest() {
        HideMe plugin = mock(HideMe.class);
        AbstractCommand command = new HideMeCommandImpl(plugin, "command", "permission", 0, false);
        CommandSender sender = mock(CommandSender.class);
        // 権限なしの場合
        when(sender.hasPermission(anyString())).thenReturn(false);
        assertThat(command.checkPermission(sender), is(false));
        // 権限有りの場合
        when(sender.hasPermission("permission")).thenReturn(true);
        assertThat(command.checkPermission(sender), is(true));
    }

    @Test
    public void matchTest() {
        HideMe plugin = mock(HideMe.class);

        // 引数なしのコマンド
        AbstractCommand command1 = new HideMeCommandImpl(plugin, "command1", "permission", 0, false);
        // マッチ
        assertThat(command1.match(new String[]{"command1"}), is(true));
        // 引数多い
        assertThat(command1.match(new String[]{"command1", "arg1"}), is(false));
        // コマンド違い
        assertThat(command1.match(new String[]{"commandX"}), is(false));

        // 引数有りのコマンド
        AbstractCommand command2 = new HideMeCommandImpl(plugin, "command2", "permission", 1, false);
        // マッチ
        assertThat(command2.match(new String[]{"command2", "arg1"}), is(true));
        // 引数少ない
        assertThat(command2.match(new String[]{"command2"}), is(false));
        // 引数多い
        assertThat(command2.match(new String[]{"command2", "arg1", "arg2"}), is(false));
    }

    @Test
    public void executeTest() {
        HideMe plugin = mock(HideMe.class);
        AbstractCommand command = new HideMeCommandImpl(plugin, "command", "permission", 0, false);
        AbstractCommand playerCommand = new HideMeCommandImpl(plugin, "command", "permission", 0, true);
        CommandSender sender = mock(CommandSender.class);

        // 権限あり
        when(sender.hasPermission(anyString())).thenReturn(true);
        command.execute(sender, new String[]{"command"});
        verify(sender, never()).sendMessage(anyString());

        // playerOnly
        reset(sender);
        when(sender.hasPermission(anyString())).thenReturn(true);
        playerCommand.execute(sender, new String[]{"command"});
        verify(sender, times(1)).sendMessage("This is player command!");

        // 権限なし
        reset(sender);
        when(sender.hasPermission(anyString())).thenReturn(false);
        command.execute(sender, new String[]{"command"});
        verify(sender, times(1)).sendMessage("You don't hove permission to perform this command!");
    }

    @Test
    public void tabCompleteTest() {
        HideMe plugin = mock(HideMe.class);
        AbstractCommand command = new HideMeCommandImpl(plugin, "command", "permission", 0, false);
        AbstractCommand playerCommand = new HideMeCommandImpl(plugin, "command", "permission", 0, true);
        CommandSender sender = mock(CommandSender.class);
        Set<String> candidates = new HashSet<>();

        // 権限あり
        when(sender.hasPermission(anyString())).thenReturn(true);
        command.tabComplete(sender, new String[]{"command"}, candidates);
        assertThat(candidates, is(new HashSet<>(Collections.singletonList("test"))));

        // playerOnly
        candidates.clear();
        when(sender.hasPermission(anyString())).thenReturn(true);
        playerCommand.tabComplete(sender, new String[]{"command"}, candidates);
        assertThat(candidates, is(new HashSet<>()));

        // 引数多い
        candidates.clear();
        when(sender.hasPermission(anyString())).thenReturn(true);
        command.tabComplete(sender, new String[]{"command", "arg1"}, candidates);
        assertThat(candidates, is(new HashSet<>()));

        // 権限なし
        candidates.clear();
        when(sender.hasPermission(anyString())).thenReturn(false);
        command.tabComplete(sender, new String[]{"command"}, candidates);
        assertThat(candidates, is(new HashSet<>()));
    }
}
