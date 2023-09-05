package com.aionemu.gameserver.eventengine.events.holders;

import com.aionemu.gameserver.eventengine.events.enums.EventPlayerLevel;
import com.aionemu.gameserver.eventengine.events.enums.EventRergisterState;
import com.aionemu.gameserver.eventengine.events.enums.EventType;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 *
 * @author f14shm4n
 */
public class SimpleSinglePlayerEventHolder extends SinglePlayerHolder {

    public SimpleSinglePlayerEventHolder(int index, EventType etype, EventPlayerLevel epl) {
        super(index, etype, epl);
    }

    @Override
    public boolean canAddPlayer(Player player) {
        if (this.contains(player)) {
            return false;
        }
        return this.allPlayers.size() != this.getStartCondition().getSinglePlayersToStart();
    }

    @Override
    public EventRergisterState addPlayer(Player player) {
        this.allPlayers.add(player);
        return EventRergisterState.HOLDER_ADD_PLAYER;
    }

    @Override
    public boolean deletePlayer(Player player) {
        boolean r = super.deletePlayer(player);
        return r;
    }

    @Override
    public boolean isReadyToGo() {
        return this.allPlayers.size() == this.getStartCondition().getSinglePlayersToStart();
    }
}
