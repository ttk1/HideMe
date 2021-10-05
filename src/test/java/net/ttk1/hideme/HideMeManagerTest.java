package net.ttk1.hideme;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class HideMeManagerTest {
    private final String UUID_JOHN = "c75fb5ce-25af-11ec-9621-0242ac130002";
    private final String UUID_MICHEAL = "c75fb948-25af-11ec-9621-0242ac130002";
    private final String UUID_PAUL = "c75fba42-25af-11ec-9621-0242ac130002";
    private Player john;
    private Player michael;
    private Player paul;

    @Before
    public void setUp() {
        john = mockPlayer("john", UUID_JOHN);
        michael = mockPlayer("michael", UUID_MICHEAL);
        paul = mockPlayer("paul", UUID_PAUL);
    }

    private Player mockPlayer(String name, String uuidString) {
        Player player = mock(Player.class);
        UUID uuid = UUID.fromString(uuidString);
        when(player.getName()).thenReturn(name);
        when(player.getPlayer()).thenReturn(player);
        when(player.getUniqueId()).thenReturn(uuid);
        when(player.hasPermission("hideme.bypass")).thenReturn(false);
        return player;
    }

    @Test
    public void getOfflinePlayerTest() throws IOException, InvalidConfigurationException {
        PluginAdapter adapter = mock(PluginAdapter.class);
        when(adapter.getOfflinePlayers()).thenReturn(new OfflinePlayer[]{john});
        HideMeManager manager = new HideMeManager(adapter);

        // 見つかる
        assertThat(manager.getOfflinePlayer("john"), is(john));

        // 見つからない
        assertThat(manager.getOfflinePlayer("michael"), nullValue());
    }

    @Test
    public void hidePlayerTest() throws IOException, InvalidConfigurationException {
        PluginAdapter adapter = mock(PluginAdapter.class);
        when(paul.hasPermission("hideme.bypass")).thenReturn(true);
        when(adapter.getOnlinePlayers()).thenAnswer(x -> Arrays.asList(john, michael, paul));
        HideMeManager manager = new HideMeManager(adapter);

        manager.hidePlayer(john);
        assertThat(manager.hiddenPlayerUUIDs, is(new HashSet<>(Collections.singletonList(UUID_JOHN))));
        // 自分自身
        verify(john, never()).hidePlayer(any(), any());
        // hideme.bypass なし
        verify(michael, times(1)).hidePlayer(any(), eq(john));
        // hideme.bypass あり
        verify(paul, never()).hidePlayer(any(), any());
    }

    @Test
    public void showPlayerTest() throws IOException, InvalidConfigurationException {
        PluginAdapter adapter = mock(PluginAdapter.class);
        when(adapter.getOnlinePlayers()).thenAnswer(x -> Arrays.asList(john, michael));
        HideMeManager manager = new HideMeManager(adapter);

        manager.hiddenPlayerUUIDs.add(UUID_JOHN);
        manager.showPlayer(john);
        assertThat(manager.hiddenPlayerUUIDs, is(new HashSet<>()));
        // 自分自身
        verify(john, never()).showPlayer(any(), any());
        // 他プレーヤー
        verify(michael, times(1)).showPlayer(any(), eq(john));
    }

    @Test
    public void applyTest() throws IOException, InvalidConfigurationException {
        PluginAdapter adapter = mock(PluginAdapter.class);
        when(paul.hasPermission("hideme.bypass")).thenReturn(true);
        when(adapter.getOnlinePlayers()).thenAnswer(x -> Arrays.asList(john, michael, paul));
        HideMeManager manager = new HideMeManager(adapter);

        manager.hiddenPlayerUUIDs.add(UUID_JOHN);
        // hideme.bypass なし
        manager.apply(michael);
        assertThat(manager.hiddenPlayerUUIDs, is(new HashSet<>(Collections.singletonList(UUID_JOHN))));
        // 自分
        verify(michael, never()).showPlayer(any(), eq(michael));
        verify(michael, never()).hidePlayer(any(), eq(michael));
        // 他プレーヤー
        verify(michael, never()).showPlayer(any(), eq(john));
        verify(michael, times(1)).hidePlayer(any(), eq(john));
        verify(michael, times(1)).showPlayer(any(), eq(paul));
        verify(michael, never()).hidePlayer(any(), eq(paul));

        // hideme.bypass あり
        manager.apply(paul);
        assertThat(manager.hiddenPlayerUUIDs, is(new HashSet<>(Collections.singletonList(UUID_JOHN))));
        // 自分
        verify(paul, never()).showPlayer(any(), eq(paul));
        verify(paul, never()).hidePlayer(any(), eq(paul));
        // 他プレーヤー
        verify(paul, times(1)).showPlayer(any(), eq(john));
        verify(paul, never()).hidePlayer(any(), eq(john));
        verify(paul, times(1)).showPlayer(any(), eq(michael));
        verify(paul, never()).hidePlayer(any(), eq(michael));
    }

    @Test
    public void isHiddenTest() throws IOException, InvalidConfigurationException {
        PluginAdapter adapter = mock(PluginAdapter.class);
        HideMeManager manager = new HideMeManager(adapter);

        manager.hiddenPlayerUUIDs.add(UUID_JOHN);
        assertThat(manager.isHidden(john), is(true));
        assertThat(manager.isHidden(michael), is(false));
    }

    @Test
    public void bypassMessageTest() throws IOException, InvalidConfigurationException {
        PluginAdapter adapter = mock(PluginAdapter.class);
        when(michael.hasPermission("hideme.bypass")).thenReturn(true);
        when(adapter.getOnlinePlayers()).thenAnswer(x -> Arrays.asList(john, michael));
        HideMeManager manager = new HideMeManager(adapter);

        manager.bypassMessage("hello!");
        // hideme.bypass なし
        verify(john, never()).sendMessage(anyString());
        // hideme.bypass あり
        verify(michael, times(1)).sendMessage("hello!");
    }

    @Test
    public void getHiddenPlayersTest() throws IOException, InvalidConfigurationException {
        PluginAdapter adapter = mock(PluginAdapter.class);
        when(adapter.getOfflinePlayers()).thenReturn(new OfflinePlayer[]{john, michael, paul});
        HideMeManager manager = new HideMeManager(adapter);

        manager.hiddenPlayerUUIDs.add(UUID_JOHN);
        manager.hiddenPlayerUUIDs.add(UUID_MICHEAL);
        assertThat(manager.getHiddenPlayers(), is(new HashSet<>(Arrays.asList(john, michael))));
    }
}
