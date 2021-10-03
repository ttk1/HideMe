package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMe;
import org.bukkit.command.CommandSender;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class VersionCommandTest {
    @Test
    public void commandNameAndPermissionTest() {
        HideMe plugin = mock(HideMe.class);
        VersionCommand command = new VersionCommand(plugin);
        assertThat(command.commandName, is("version"));
        assertThat(command.permission, is("hideme.version"));
    }

    @Ignore
    @Test
    public void executeImplTest() {
        // 画面に表示するだけの機能なので一旦置いておく
    }

    @Test
    public void tabCompleteImplTest() {
        HideMe plugin = mock(HideMe.class);
        VersionCommand command = new VersionCommand(plugin);
        CommandSender sender = mock(CommandSender.class);
        Set<String> candidates = new HashSet<>();

        // args.length == 0
        command.tabCompleteImpl(sender, new String[]{}, candidates);
        assertThat(candidates, is(new HashSet<>(Collections.singletonList("version"))));

        // args.length == 1
        // コマンド名が前方一致する
        candidates.clear();
        command.tabCompleteImpl(sender, new String[]{"v"}, candidates);
        assertThat(candidates, is(new HashSet<>(Collections.singletonList("version"))));

        // コマンド名が前方一致しない
        candidates.clear();
        command.tabCompleteImpl(sender, new String[]{"z"}, candidates);
        assertThat(candidates, is(new HashSet<>()));
    }
}
