package net.ttk1.adapter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedServerPing;

import org.bukkit.entity.Player;

import net.ttk1.HideMe;
import net.ttk1.PlayerManager;

import java.util.ArrayList;
import java.util.List;

/**
 * ServerListPingを書き換えて、プレーヤー数を偽装する
 */
public class ServerPingPacketAdapter extends PacketAdapter {
    private HideMe plg;
    private PlayerManager manager;

    public ServerPingPacketAdapter(HideMe plg) {
        super(plg, PacketType.Status.Server.SERVER_INFO);
        this.plg = plg;
        this.manager = plg.getManager();
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        StructureModifier<WrappedServerPing> pings = event.getPacket().getServerPings();
        WrappedServerPing ping = pings.read(0);

        // pingパケットの送信元をログに出力
        plg.getHideMeLogger().info(event.getPlayer().getAddress().toString());

        if (ping.getPlayersOnline() < manager.getOnlineHiddenCount()) {
            plg.getLogger().warning("プレーヤー数が異常です");
        } else {
            ping.setPlayersOnline(ping.getPlayersOnline() - manager.getOnlineHiddenCount());
        }

        List<Player> players = new ArrayList<>();
        for (Player player: plg.getServer().getOnlinePlayers()){
            if (!manager.isHidden(player)){
                players.add(player);
            }
        }
        ping.setBukkitPlayers(players);

        pings.write(0, ping);
    }
}