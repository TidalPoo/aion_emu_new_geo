/*
 * This file is part of aion-lightning <aionunique.smfnew.com>.
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

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.gameobjects.player.DeniedStatus;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.DuelService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

/**
 * @author xavier
 * @modified Alex
 */
public class CM_DUEL_REQUEST extends AionClientPacket {

    /**
     * Target object id that client wants to start duel with
     */
    private int objectId;

    /**
     * Constructs new instance of <tt>CM_DUEL_REQUEST</tt> packet
     *
     * @param opcode
     * @param state
     * @param restStates
     */
    public CM_DUEL_REQUEST(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        objectId = readD();
    }

    @Override
    protected void runImpl() {
        Player activePlayer = getConnection().getActivePlayer();
        Player target = World.getInstance().findPlayer(objectId);

        if (target == null) {
            return;
        }

        if (!CustomConfig.INSTANCE_DUEL_ENABLE && activePlayer.isInInstance()) {
            return;
        }

        if (target.equals(activePlayer)) {
            sendPacket(SM_SYSTEM_MESSAGE.STR_DUEL_PARTNER_INVALID(target.getName()));
            return;
        }
        DuelService duelService = DuelService.getInstance();

        Player targetPlayer = target;

        if (duelService.isDueling(activePlayer.getObjectId())) {
            sendPacket(SM_SYSTEM_MESSAGE.STR_DUEL_YOU_ARE_IN_DUEL_ALREADY);
            return;
        }
        if (duelService.isDueling(targetPlayer.getObjectId())) {
            sendPacket(SM_SYSTEM_MESSAGE.STR_DUEL_PARTNER_IN_DUEL_ALREADY(target.getName()));
            return;
        }
        if (targetPlayer.getPlayerSettings().isInDeniedStatus(activePlayer, DeniedStatus.DUEL)) {
            sendPacket(SM_SYSTEM_MESSAGE.STR_MSG_REJECTED_DUEL(targetPlayer.getName()));
            return;
        }

        if (AdminConfig.GM_NO_DUEL && activePlayer.getAccessLevel() != 0 && activePlayer.getAccessLevel() <= AdminConfig.GM_NO_DUEL_LEVEL) {
            PacketSendUtility.sendMessage(activePlayer, "DEVELOPER: You can use duel! пфф");
            return;
        }

        duelService.confirmDuelWith(activePlayer, targetPlayer);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
