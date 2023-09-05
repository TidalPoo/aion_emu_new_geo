/*
 * SAO Project by Alex
 */
package com.aionemu.gameserver.cardinal;

/**
 *
 * @author Alex
 */
public class Copy {

    private final int id;
    private final String name;
    private final boolean isPlayer;

    public Copy(int id, String name, boolean isPlayer) {
        this.id = id;
        this.name = name;
        this.isPlayer = isPlayer;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isIsPlayer() {
        return isPlayer;
    }
}
