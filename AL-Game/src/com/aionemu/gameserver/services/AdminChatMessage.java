package com.aionemu.gameserver.services;

import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import java.util.List;
import javolution.util.FastList;

/**
 *
 * @author Alex
 */
public class AdminChatMessage {

    private final ChatType mtype;
    private final Player admin;
    private final List<Player> players = new FastList<>();
    private final Race race;

    public AdminChatMessage(Player admin, Race race, ChatType mtype) {
        this.admin = admin;
        this.race = race;
        this.mtype = mtype;
    }

    public ChatType getChatType() {
        return mtype;
    }

    public Player getAdmin() {
        return admin;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Race getRace() {
        return race;
    }

    public void addPlayer(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
    }

    public void removePlayer(Player player) {
        if (!players.isEmpty() && players.contains(player)) {
            players.remove(player);
        }
    }

    public void clearList() {
        if (!players.isEmpty()) {
            players.clear();
        }
    }
}
