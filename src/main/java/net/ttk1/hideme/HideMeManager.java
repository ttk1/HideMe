package net.ttk1.hideme;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * 隠れたプレーヤーを管理するクラス
 */

public class HideMeManager {
    private final HideMe plugin;
    private final Server server;
    private final Logger logger;
    private final File dataFile;
    private Set<String> hiddenPlayerUUIDs;

    public HideMeManager(HideMe plugin) throws IOException, InvalidConfigurationException {
        this.plugin = plugin;
        this.server = plugin.getServer();
        this.logger = plugin.getLogger();
        this.dataFile = new File(plugin.getDataFolder(), "hidden_players.yml");
        load();
    }

    private void load() throws IOException, InvalidConfigurationException {
        if (dataFile.exists()) {
            FileConfiguration config = new YamlConfiguration();
            config.load(dataFile);
            hiddenPlayerUUIDs = new HashSet<>(config.getStringList("hidden_player_uuids"));
        } else {
            hiddenPlayerUUIDs = new HashSet<>();
        }
    }

    /**
     * hidden_player_uuids を Yaml ファイルに書き出す
     * 保存時の例外は、スタックトレースの表示だけやって握りつぶす
     */
    public void save() {
        FileConfiguration config = new YamlConfiguration();
        config.set("hidden_player_uuids", new ArrayList<>(hiddenPlayerUUIDs));
        try {
            config.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
            logger.severe("データの保存に失敗しました。");
        }
    }

    public Collection<? extends Player> getOnlinePlayers() {
        return server.getOnlinePlayers();
    }

    public OfflinePlayer[] getOfflinePlayers() {
        return server.getOfflinePlayers();
    }

    /**
     * 名前を指定して OfflinePlayer を取得する。
     * 名前の変更時に、名前の重複が起こる可能性はあるが、その場合は最初に見つかった方を返す。
     * Server::getOfflinePlayer(String name) は Deprecated なので、とりあえず自前で実装しておく。
     *
     * @param playerName 取得したい Player の名前
     * @return 見つかれば OfflinePlayer を、それ以外は null を返す
     */
    public OfflinePlayer getOfflinePlayer(String playerName) {
        for (OfflinePlayer offlinePlayer : server.getOfflinePlayers()) {
            if (playerName.equals(offlinePlayer.getName())) {
                return offlinePlayer;
            }
        }
        return null;
    }

    private void addHiddenPlayer(String playerUUID) {
        hiddenPlayerUUIDs.add(playerUUID);
    }

    private void removeHiddenPlayer(String playerUUID) {
        hiddenPlayerUUIDs.remove(playerUUID);
    }

    /**
     * 指定したプレーヤーを他のプレーヤーから見えないようにする
     *
     * @param offlinePlayer 対象のプレーヤー
     */
    public void hidePlayer(@NotNull OfflinePlayer offlinePlayer) {
        addHiddenPlayer(offlinePlayer.getUniqueId().toString());
        Player player = offlinePlayer.getPlayer();
        if (player != null) {
            for (Player p : getOnlinePlayers()) {
                if (!player.equals(p)) {
                    if (p.hasPermission("hideme.bypass")) {
                        p.showPlayer(plugin, player);
                    } else {
                        p.hidePlayer(plugin, player);
                    }
                }
            }
        }
    }

    /**
     * 指定したプレーヤーを他のプレーヤーから見えるようにする
     *
     * @param offlinePlayer 対象のプレーヤー
     */
    public void showPlayer(@NotNull OfflinePlayer offlinePlayer) {
        removeHiddenPlayer(offlinePlayer.getUniqueId().toString());
        Player player = offlinePlayer.getPlayer();
        if (player != null) {
            for (Player p : getOnlinePlayers()) {
                if (!player.equals(p)) {
                    p.showPlayer(plugin, player);
                }
            }
        }
    }

    /**
     * 指定したプレーヤーから他のプレーヤーの見え方を現在の状態に更新する。
     * ログイン直後に実行する必要あり。
     *
     * @param player 対象のプレーヤー
     */
    public void apply(@NotNull Player player) {
        for (Player p : getOnlinePlayers()) {
            if (!player.equals(p)) {
                if (isHidden(p) && !player.hasPermission("hideme.bypass")) {
                    player.hidePlayer(plugin, p);
                } else {
                    player.showPlayer(plugin, p);
                }
            }
        }
    }

    /**
     * 指定したプレーヤーが他のプレーヤーから見えない状態かどうかを返す
     *
     * @param player 対象のプレーヤー
     * @return 指定したプレーヤーが見えない状態どうか（true = 見えない）
     */
    public boolean isHidden(@NotNull OfflinePlayer player) {
        return hiddenPlayerUUIDs.contains(player.getUniqueId().toString());
    }

    /**
     * バイパス権限を持つプレーヤーにメッセージをバイパスする
     *
     * @param message バイパスするメッセージ
     */
    public void bypassMessage(String message) {
        for (Player player : getOnlinePlayers()) {
            if (player.hasPermission("hideme.bypass")) {
                player.sendMessage(message);
            }
        }
    }

    /**
     * 見えない状態のプレーヤーのリストを返す
     *
     * @return 見えない状態のプレーヤーのリスト
     */
    public Set<OfflinePlayer> getHiddenPlayers() {
        return Arrays.stream(getOfflinePlayers()).filter(this::isHidden).collect(Collectors.toSet());
    }
}