package net.ttk1.hideme;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * ServerListPingを書き換えて、プレーヤー数を偽装する
 */
public class ServerPingPacketAdapter extends PacketAdapter {
    private final HideMe plugin;
    private final HideMeManager manager;

    public ServerPingPacketAdapter(HideMe plugin) {
        super(plugin, PacketType.Status.Server.SERVER_INFO);
        this.plugin = plugin;
        this.manager = plugin.getManager();
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        StructureModifier<WrappedServerPing> pings = event.getPacket().getServerPings();
        WrappedServerPing ping = pings.read(0);

        int fakedPlayersOnline = ping.getPlayersOnline() - (int) manager.getHiddenPlayers().stream().filter(OfflinePlayer::isOnline).count();
        if (fakedPlayersOnline < 0) {
            plugin.getLogger().warning("プレーヤー数が異常です");
            ping.setPlayersOnline(0);
        } else {
            ping.setPlayersOnline(fakedPlayersOnline);
        }

        List<Player> players = new ArrayList<>();
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (!manager.isHidden(player)) {
                players.add(player);
            }
        }
        ping.setBukkitPlayers(players);

        pings.write(0, ping);
    }
}