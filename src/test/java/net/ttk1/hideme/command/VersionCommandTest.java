package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMeManager;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class VersionCommandTest {
    @Test
    public void commandNameAndPermissionTest() {
        HideMeManager manager = mock(HideMeManager.class);
        VersionCommand command = new VersionCommand(manager);
        assertThat(command.commandName, is("version"));
        assertThat(command.permission, is("hideme.version"));
    }

    @Ignore
    @Test
    public void executeImplTest() {
        // 画面に表示するだけの機能なので一旦置いておく
    }
}
