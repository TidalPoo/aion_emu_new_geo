/*
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.ReviveType;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.custom.MixFight;
import com.aionemu.gameserver.services.custom.MixFight6;
import com.aionemu.gameserver.services.custom.NewPlayerZone;
import com.aionemu.gameserver.services.custom.PvPLocationService;
import com.aionemu.gameserver.services.custom.ffa.FfaGroupService;
import com.aionemu.gameserver.services.custom.ffa.FfaLegionService;
import com.aionemu.gameserver.services.custom.ffa.FfaPlayers;
import com.aionemu.gameserver.services.player.PlayerReviveService;

/**
 * @author ATracer, orz, avol, Simple
 * @modified Alex
 */
public class CM_REVIVE extends AionClientPacket {

    private int reviveId;

    /**
     * Constructs new instance of <tt>CM_REVIVE </tt> packet
     *
     * @param opcode
     * @param state
     * @param restStates
     */
    public CM_REVIVE(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        reviveId = readC();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        Player activePlayer = getConnection().getActivePlayer();

        if (!activePlayer.getLifeStats().isAlreadyDead()) {
            return;
        }

        if (FfaPlayers.isInFFA(activePlayer)) {
            PlayerReviveService.FFARevive(activePlayer);
            return;
        }
        if (FfaGroupService.isInFFA(activePlayer)) {
            FfaGroupService.FFARevive(activePlayer);
            return;
        }
        if (FfaLegionService.isInFFA(activePlayer)) {
            FfaLegionService.FFARevive(activePlayer);
            return;
        }

        if (PvPLocationService.isPvPLocation(activePlayer)) {
            PvPLocationService.onRevive(activePlayer);
            return;
        }
        if (NewPlayerZone.isInZone(activePlayer)) {
            NewPlayerZone.FFARevive(activePlayer);
            return;
        }
        if (MixFight.isInFFA(activePlayer)) {
            if (activePlayer.getInstanceId() == MixFight.getFFAInstanceId()) {
                MixFight.FFARevive(activePlayer);
            } else {
                MixFight6.FFARevive(activePlayer);
            }
            return;
        }
        ReviveType reviveType = ReviveType.getReviveTypeById(reviveId);
        switch (reviveType) {
            case BIND_REVIVE:
            case OBELISK_REVIVE:
                PlayerReviveService.bindRevive(activePlayer);
                break;
            case REBIRTH_REVIVE:
                PlayerReviveService.rebirthRevive(activePlayer);
                break;
            case ITEM_SELF_REVIVE:
                PlayerReviveService.itemSelfRevive(activePlayer);
                break;
            case SKILL_REVIVE:
                PlayerReviveService.skillRevive(activePlayer);
                break;
            case KISK_REVIVE:
                PlayerReviveService.kiskRevive(activePlayer);
                break;
            case INSTANCE_REVIVE:
                PlayerReviveService.instanceRevive(activePlayer);
                break;
            default:
                break;
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); 
    }
}
