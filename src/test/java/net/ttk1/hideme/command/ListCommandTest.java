package net.ttk1.hideme.command;

import net.ttk1.hideme.HideMeManager;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class ListCommandTest {
    @Test
    public void commandNameAndPermissionTest() {
        HideMeManager manager = mock(HideMeManager.class);
        ListCommand command = new ListCommand(manager);
        assertThat(command.commandName, is("list"));
        assertThat(command.permission, is("hideme.list"));
    }

    @Ignore
    @Test
    public void executeImplTest() {
        // 画面に表示するだけの機能なので一旦置いておく
    }
}
