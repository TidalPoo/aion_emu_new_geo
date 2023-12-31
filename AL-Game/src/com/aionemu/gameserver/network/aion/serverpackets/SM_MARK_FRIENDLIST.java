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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.player.RequestFriend;
import com.aionemu.gameserver.model.gameobjects.player.RequestFriendList;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 *
 * @author Alex
 */
public class SM_MARK_FRIENDLIST extends AionServerPacket {

    @Override
    protected void writeImpl(AionConnection con) {
        RequestFriendList list = con.getActivePlayer().getRequestFriendList();
        writeD(con.getActivePlayer().getObjectId());
        writeC(1);
        writeH(list.getSize());
        for (RequestFriend request : list) {
            writeD(request.getOid());
            writeS(request.getName());
            writeS(request.getMessage());
            writeD(request.getLevel());
            writeD(request.getPlayerClass().getClassId());
            writeC(request.isOnline() ? 1 : 0);
        }
    }
}
