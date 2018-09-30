package net.ttk1.hideme.adapter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedServerPing;

import com.google.inject.Inject;

import net.ttk1.hideme.api.PlayerManager;
import net.ttk1.hideme.HideMe;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * ServerListPingを書き換えて、プレーヤー数を偽装する
 */
public class ServerPingPacketAdapter extends PacketAdapter {
    private HideMe plugin;
    private PlayerManager playerManager;

    //@Inject
    private void setPlugin(HideMe plugin) {
        this.plugin = plugin;
    }

    //@Inject
    private void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Inject
    public ServerPingPacketAdapter(HideMe plugin) {
        super(plugin, PacketType.Status.Server.SERVER_INFO);
        setPlugin(plugin);
        setPlayerManager(plugin.getManager());
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        StructureModifier<WrappedServerPing> pings = event.getPacket().getServerPings();
        WrappedServerPing ping = pings.read(0);

        if (ping.getPlayersOnline() < playerManager.getOnlineHiddenPlayerCount()) {
            plugin.getLogger().warning("プレーヤー数が異常です");
        } else {
            ping.setPlayersOnline(ping.getPlayersOnline() - playerManager.getOnlineHiddenPlayerCount());
        }

        List<Player> players = new ArrayList<>();
        for (Player player: plugin.getServer().getOnlinePlayers()){
            if (!playerManager.isHidden(player)){
                players.add(player);
            }
        }
        ping.setBukkitPlayers(players);

        pings.write(0, ping);
    }
}