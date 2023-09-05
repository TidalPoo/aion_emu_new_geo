package com.aionemu.gameserver.eventengine.events;

/**
 *
 * @author f14shm4n
 */
public class EventScore {

    public final int PlayerObjectId;
    public int Kills;
    public int Death;
    public int Wins;
    public int Loses;
    public boolean isWinner = false;

    public EventScore(int id) {
        this.PlayerObjectId = id;
        Kills = Death = Wins = Loses = 0;
    }
}
