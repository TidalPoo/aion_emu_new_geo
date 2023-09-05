package com.aionemu.gameserver.eventengine.events.holders;

import com.aionemu.gameserver.eventengine.events.enums.EventPlayerLevel;
import com.aionemu.gameserver.eventengine.events.enums.EventRergisterState;
import com.aionemu.gameserver.eventengine.events.enums.EventType;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 *
 * @author f14shm4n
 */
public class _1x1SamePlayerClassEventHolder extends SinglePlayerHolder {

    private PlayerClass holderClass = PlayerClass.ALL;

    public _1x1SamePlayerClassEventHolder(int index, EventType etype, EventPlayerLevel epl) {
        super(index, etype, epl);
    }

    @Override
    public boolean canAddPlayer(Player player) {
        if (this.contains(player)) {
            return false;
        }
        /* проверка класса игрока, раскомментировать для того чтобы ивент стал классовым
         if (this.holderClass != PlayerClass.ALL && this.holderClass != player.getPlayerClass()) {
         return false;
         }
         */
        return this.allPlayers.size() != 2;
    }

    @Override
    public EventRergisterState addPlayer(Player player) {
        if (this.holderClass == PlayerClass.ALL) {
            this.holderClass = player.getPlayerClass();
        }
        this.allPlayers.add(player);
        return EventRergisterState.HOLDER_ADD_PLAYER;
    }

    @Override
    public boolean deletePlayer(Player player) {
        boolean r = super.deletePlayer(player);
        if (this.allPlayers.isEmpty()) {
            this.holderClass = PlayerClass.ALL;
        }
        return r;
    }

    @Override
    public boolean isReadyToGo() {
        return this.allPlayers.size() == 2;
    }
}
