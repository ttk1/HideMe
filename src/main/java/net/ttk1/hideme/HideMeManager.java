package net.ttk1.hideme;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * 隠れたプレーヤーを管理するクラス
 */

public class HideMeManager {
    private final HideMe plugin;
    private final Logger logger;
    private final File dataFile;
    private Set<String> hiddenPlayerUUIDs;

    public HideMeManager(HideMe plugin) throws IOException, InvalidConfigurationException {
        this.plugin = plugin;
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

    private void addHiddenPlayer(String playerUUID) {
        hiddenPlayerUUIDs.add(playerUUID);
    }

    private void removeHiddenPlayer(String playerUUID) {
        hiddenPlayerUUIDs.remove(playerUUID);
    }

    /**
     * 指定したプレーヤーを他のプレーヤーから見えないようにする
     *
     * @param player 対象のプレーヤー
     */
    public void hidePlayer(Player player) {
        if (player != null) {
            addHiddenPlayer(player.getUniqueId().toString());
            for (Player p : plugin.getServer().getOnlinePlayers()) {
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
     * 指定したプレーヤーを他のプレーヤーから見えないようにする
     *
     * @param offlinePlayer 対象のプレーヤー
     */
    public void hidePlayer(OfflinePlayer offlinePlayer) {
        if (offlinePlayer != null) {
            if (offlinePlayer.isOnline()) {
                hidePlayer(offlinePlayer.getPlayer());
            } else {
                addHiddenPlayer(offlinePlayer.getUniqueId().toString());
            }
        }
    }

    /**
     * 指定したプレーヤーを他のプレーヤーから見えるようにする
     *
     * @param player 対象のプレーヤー
     */
    public void showPlayer(Player player) {
        if (player != null) {
            removeHiddenPlayer(player.getUniqueId().toString());
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                if (!player.equals(p)) {
                    p.showPlayer(plugin, player);
                }
            }
        }
    }

    /**
     * 指定したプレーヤーを他のプレーヤーから見えるようにする
     *
     * @param offlinePlayer 対象のプレーヤー
     */
    public void showPlayer(OfflinePlayer offlinePlayer) {
        if (offlinePlayer != null) {
            if (offlinePlayer.isOnline()) {
                showPlayer(offlinePlayer.getPlayer());
            } else {
                removeHiddenPlayer(offlinePlayer.getUniqueId().toString());
            }
        }
    }

    /**
     * 指定したプレーヤーから他のプレーヤーの見え方を現在の状態に更新する。
     * ログイン直後に実行する必要あり。
     *
     * @param player 対象のプレーヤー
     */
    public void apply(Player player) {
        if (player != null) {
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                if (!player.equals(p)) {
                    if (isHidden(p) && !player.hasPermission("hideme.bypass")) {
                        player.hidePlayer(plugin, p);
                    } else {
                        player.showPlayer(plugin, p);
                    }
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
    public boolean isHidden(Player player) {
        return hiddenPlayerUUIDs.contains(player.getUniqueId().toString());
    }

    /**
     * 指定したプレーヤーが他のプレーヤーから見えない状態かどうかを返す
     *
     * @param offlinePlayer 対象のプレーヤー
     * @return 指定したプレーヤーが見えない状態どうか（true = 見えない）
     */
    public boolean isHidden(OfflinePlayer offlinePlayer) {
        return hiddenPlayerUUIDs.contains(offlinePlayer.getUniqueId().toString());
    }

    /**
     * バイパス権限を持つプレーヤーにメッセージをバイパスする
     *
     * @param message バイパスするメッセージ
     */
    public void bypassMessage(String message) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
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
        return Arrays.stream(plugin.getServer().getOfflinePlayers()).filter(this::isHidden).collect(Collectors.toSet());
    }
}