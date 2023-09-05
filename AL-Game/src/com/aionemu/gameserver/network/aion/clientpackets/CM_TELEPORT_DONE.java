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
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_SPAWN;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapType;

/**
 * @author Rolandas
 */
public class CM_TELEPORT_DONE extends AionClientPacket {

    public CM_TELEPORT_DONE(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    @Override
    protected void readImpl() {
    }

    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        PacketSendUtility.sendMessage(player, "CM_TELEPORT_DONE");
        if (WorldMapType.getWorld(player.getWorldId()).isPersonal()) {
            PacketSendUtility.sendPacket(player, new SM_PLAYER_SPAWN(player));
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
