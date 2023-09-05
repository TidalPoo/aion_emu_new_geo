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
public class GMPlayerChat {

    private final List<Player> players = new ArrayList<>();

    public List<Player> getPlayers() {
        return players;
    }

    public boolean addPlayer(Player player) {
        return players.add(player);
    }

    public boolean delPlayer(Player player) {
        return players.remove(player);
    }
}
