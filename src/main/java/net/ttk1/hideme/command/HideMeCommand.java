package net.ttk1.hideme.command;

import org.bukkit.command.CommandSender;

import java.util.Set;

public interface HideMeCommand {
    /**
     * コマンド引数がこのコマンドにマッチするか否かを返す
     *
     * @param args コマンド引数
     * @return マッチする場合 true、それ以外の場合 false
     */
    boolean match(String[] args);

    /**
     * コマンドを実行する
     *
     * @param sender コマンド実行者
     * @param args   コマンド引数
     */
    void execute(CommandSender sender, String[] args);

    /**
     * コマンドのタブ補完をする
     *
     * @param sender     タブ補完実行者
     * @param args       コマンド引数
     * @param candidates タブ補完候補をこれに詰める
     */
    void tabComplete(CommandSender sender, String[] args, Set<String> candidates);
}
