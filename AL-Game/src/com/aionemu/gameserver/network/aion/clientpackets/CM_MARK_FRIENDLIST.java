/*
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 * aion-lightning is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-lightning is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FRIEND_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MARK_FRIENDLIST;

/**
 *
 * @author xTz, Rolandas GAMESHOP LIST FRIEND GIFT
 */
public class CM_MARK_FRIENDLIST extends AionClientPacket {

    public CM_MARK_FRIENDLIST(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    @Override
    protected void readImpl() {
        // nothing to read
        //PacketSendUtility.sendMessage(getConnection().getActivePlayer(), "Mark Friend List Read");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        final Player activePlayer = getConnection().getActivePlayer();
        //PacketSendUtility.sendMessage(activePlayer, "Mark Friend List Open");
        if (activePlayer != null) {
            if (!activePlayer.getFriendList().getIsFriendListSent()) {
                getConnection().sendPacket(new SM_FRIEND_LIST());
            }
            getConnection().sendPacket(new SM_MARK_FRIENDLIST());
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
