package net.ttk1.hideme.api;

import com.google.inject.ImplementedBy;

import net.ttk1.hideme.HideMeManagerImpl;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Set;

@ImplementedBy(HideMeManagerImpl.class)
public interface HideMeManager {
    /**
     * hidden_playersをYamlファイルに書き出す
     */
    void save();

    /**
     * 指定したプレーヤーを他のプレーヤーから隠す
     * @param player プレーヤー
     */
    void hidePlayer(Player player);

    /**
     * 指定したプレーヤーを他のプレーヤーから隠す
     * @param offlinePlayer プレーヤー
     */
    void hidePlayer(OfflinePlayer offlinePlayer);

    /**
     * 指定したプレーヤーを他のプレーヤーから見えるようにする
     * @param player プレーヤー
     */
    void showPlayer(Player player);

    /**
     * 指定したプレーヤーを他のプレーヤーから見えるようにする
     * @param offlinePlayer プレーヤー
     */
    void showPlayer(OfflinePlayer offlinePlayer);

    /**
     * 指定したプレーヤーから他のプレーヤーの見え方を現在の状態に更新する。
     * ログイン直後に実行する必要あり。
     * @param player プレーヤー
     */
    void refresh(Player player);

    /**
     * 指定したプレーヤーがhide状態かどうかを返す
     * @param player プレーヤー
     * @return 指定したプレーヤーがhide状態どうか
     */
    boolean isHidden(Player player);

    /**
     * 指定したプレーヤーがhide状態かどうかを返す
     * @param offlinePlayer プレーヤー
     * @return 指定したプレーヤーがhide状態どうか
     */
    boolean isHidden(OfflinePlayer offlinePlayer);

    /**
     * バイパス権限を持つプレーヤーのみにメッセージを送信する
     * @param message バイパスするメッセージ
     */
    void bypassMessage(String message);

    /**
     * hide状態のプレーヤーのリストを返す
     * @return hide状態のプレーヤーのリスト
     */
    Set<OfflinePlayer> getHiddenPlayers();
}
