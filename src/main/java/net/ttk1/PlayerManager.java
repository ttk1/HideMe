package net.ttk1;

import java.util.Set;
import java.util.HashSet;

import org.bukkit.entity.Player;

/**
 * 隠れたプレーヤーを管理するクラス
 */
public class PlayerManager {
    private Set<String> hidden;
    private Set<String> online;

    public PlayerManager(Set<String> hidden){
        this.hidden = hidden;
        this.online = new HashSet<>();
    }

    public void add(Player player){
        String uuid = player.getUniqueId().toString();
        if (!isHidden(player)) {
            hidden.add(uuid);
            if(player.isOnline()){
                online.add(uuid);
            }
        }
    }

    public void remove(Player player){
        String uuid = player.getUniqueId().toString();
        if (isHidden(player)) {
            hidden.remove(uuid);
            if(player.isOnline()){
                online.remove(uuid);
            }
        }
    }

    public void login(Player player){
        String uuid = player.getUniqueId().toString();
        if (isHidden(player)){
            online.add(uuid);
        }
    }

    public void logout(Player player){
        String uuid = player.getUniqueId().toString();
        if (isHidden(player)){
            online.remove(uuid);
        }
    }

    public boolean isHidden(Player player) {
        String uuid = player.getUniqueId().toString();
        return hidden.contains(uuid);
    }
    public Set<String> getHiddenSet(){
        return hidden;
    }
    public Set<String> getOnlineHiddenSet(){
        return online;
    }
    public int getHiddenCount(){
        return hidden.size();
    }
    public int getOnlineHiddenCount(){
        return online.size();
    }
}