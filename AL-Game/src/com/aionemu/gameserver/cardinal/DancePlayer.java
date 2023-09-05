/*
 * SAO Project
 */
package com.aionemu.gameserver.cardinal;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alex
 */
public class DancePlayer {

    private final List<Player> list;

    DancePlayer() {
        list = new ArrayList<>();
    }

    public List<Player> getList() {
        return list;
    }

    public void addPlayer(Player player) {
        if (!list.contains(player)) {
            list.add(player);
        }
    }

    public boolean removePlayer(Player player) {
        if (list.contains(player)) {
            return list.remove(player);
        }
        return false;
    }
}
