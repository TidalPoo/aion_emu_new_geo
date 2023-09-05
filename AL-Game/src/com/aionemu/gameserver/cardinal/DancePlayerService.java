/*
 * SAO Project
 */
package com.aionemu.gameserver.cardinal;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import java.util.Map;
import javolution.util.FastMap;

/**
 *
 * @author Alex
 */
public class DancePlayerService {

    private static final Map<Integer, DancePlayer> map = new FastMap<>();

    public static void add(Player admin, Player player) {
        int id = admin.getObjectId();
        if (map.get(id) == null) {
            map.put(id, new DancePlayer());
        }
        map.get(id).addPlayer(player);
    }

    public static void remove(Player admin, Player player) {
        int id = admin.getObjectId();
        if (map.get(id) == null) {
            return;
        }
        map.get(id).removePlayer(player);
    }

    public static void clear(Player admin) {
        int id = admin.getObjectId();
        if (map.get(id) != null) {
            map.remove(id);
        }
    }

    public static DancePlayer getDance(Player admin) {
        return map.get(admin.getObjectId());
    }
}
