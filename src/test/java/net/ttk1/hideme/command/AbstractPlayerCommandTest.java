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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractPlayerCommandTest {
    static class HideMeCommandImpl extends AbstractPlayerCommand {
        public HideMeCommandImpl(HideMeManager manager, String commandName, String permission, int argc, boolean playerOnly) {
            super(manager, commandName, permission, argc, playerOnly);
        }

        @Override
        protected void executeImpl(CommandSender sender, String[] args) {
        }
    }

    @Test
    public void tabCompleteImplTest() {
        HideMeManager manager = mock(HideMeManager.class);
        Player john = mock(Player.class);
        when(john.getName()).thenReturn("john");
        Player michael = mock(Player.class);
        when(michael.getName()).thenReturn("michael");
        when(manager.getOnlinePlayers()).thenAnswer(x -> Arrays.asList(john, michael));
        AbstractPlayerCommand command = new HideMeCommandImpl(manager, "command", "permission", 0, false);
        CommandSender sender = mock(CommandSender.class);
        Set<String> candidates = new HashSet<>();

        // コマンド名が空文字列
        command.tabCompleteImpl(sender, new String[]{""}, candidates);
        assertThat(candidates, is(new HashSet<>(Collections.singletonList("command"))));

        // コマンド名が前方一致する
        candidates.clear();
        command.tabCompleteImpl(sender, new String[]{"c"}, candidates);
        assertThat(candidates, is(new HashSet<>(Collections.singletonList("command"))));

        // コマンド名が前方一致しない
        candidates.clear();
        command.tabCompleteImpl(sender, new String[]{"x"}, candidates);
        assertThat(candidates, is(new HashSet<>()));

        // プレイヤー名が空文字列
        candidates.clear();
        command.tabCompleteImpl(sender, new String[]{"command", ""}, candidates);
        assertThat(candidates, is(new HashSet<>(Arrays.asList("john", "michael"))));

        // プレイヤー名が前方一致
        candidates.clear();
        command.tabCompleteImpl(sender, new String[]{"command", "j"}, candidates);
        assertThat(candidates, is(new HashSet<>(Collections.singletonList("john"))));
    }
}
