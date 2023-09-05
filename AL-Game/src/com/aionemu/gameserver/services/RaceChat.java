/*
 * AionLight project
 */
package com.aionemu.gameserver.services;

import java.util.List;

/**
 *
 * @author Alex
 */
public class RaceChat {

    private final String playerName;
    private final List<String> text;

    public RaceChat(String playerName, List<String> text) {
        this.playerName = playerName;
        this.text = text;
    }

    public String getPlayerName() {
        return playerName;
    }

    public List<String> getText() {
        return text;
    }
}
