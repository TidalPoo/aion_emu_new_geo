/*
 * AionLight project
 */
package com.aionemu.gameserver.services;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javolution.util.FastList;

/**
 *
 * @author Alex
 */
public class RaceChatMap {

    public static RaceChatMap getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {

        protected static final RaceChatMap instance = new RaceChatMap();
    }
    private Map<String, RaceChat> text = new HashMap<>();

    public void addChache(Player player, String t) {
        if (text.containsKey(player.getName())) {
            text.get(player.getName()).getText().add(t);
        } else {
            List<String> tt = new FastList<>();
            tt.add(t);
            text.put(player.getName(), new RaceChat(player.getName(), tt));
        }
    }

    public RaceChat getText(Player player) {
        return text.get(player.getName());
    }

    public Map<String, RaceChat> getAllText() {
        return text;
    }
}
