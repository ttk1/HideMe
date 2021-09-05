package net.ttk1.hideme.adapter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import net.ttk1.hideme.HideMe;
import net.ttk1.hideme.api.HideMeManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * ServerListPingを書き換えて、プレーヤー数を偽装する
 */
public class ServerPingPacketAdapter extends PacketAdapter {
    private HideMe plugin;
    private HideMeManager hideMeManager;

    private void setPlugin(HideMe plugin) {
        this.plugin = plugin;
    }

    private void setHideMeManager(HideMeManager hideMeManager) {
        this.hideMeManager = hideMeManager;
    }

    public ServerPingPacketAdapter(HideMe plugin) {
        super(plugin, PacketType.Status.Server.SERVER_INFO);
        setPlugin(plugin);
        setHideMeManager(plugin.getManager());
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        StructureModifier<WrappedServerPing> pings = event.getPacket().getServerPings();
        WrappedServerPing ping = pings.read(0);
        int fakedPlayersOnline = ping.getPlayersOnline() - (int) hideMeManager.getHiddenPlayers().stream().filter(OfflinePlayer::isOnline).count();

        if (fakedPlayersOnline < 0) {
            plugin.getLogger().warning("プレーヤー数が異常です");
        } else {
            ping.setPlayersOnline(fakedPlayersOnline);
        }

        List<Player> players = new ArrayList<>();
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (!hideMeManager.isHidden(player)) {
                players.add(player);
            }
        }
        ping.setBukkitPlayers(players);

        pings.write(0, ping);
    }
}