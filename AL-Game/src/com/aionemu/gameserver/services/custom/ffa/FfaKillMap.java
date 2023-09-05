package com.aionemu.gameserver.services.custom.ffa;

/**
 *
 * @author Alex
 */
public class FfaKillMap {

    private int kills;
    private int death;

    public FfaKillMap(int kills, int death) {
        this.kills = kills;
        this.death = death;
    }

    public void increaseKills() {
        this.kills++;
    }

    public void increaseDeath() {
        this.death++;
    }

    public int getKills() {
        return kills;
    }

    public int getDeath() {
        return death;
    }
}
