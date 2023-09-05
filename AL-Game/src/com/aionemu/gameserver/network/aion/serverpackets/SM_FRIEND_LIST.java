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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.player.Friend;
import com.aionemu.gameserver.model.gameobjects.player.FriendList;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.HousingService;

/**
 * Sends a friend list to the client
 *
 * @author Ben
 * @modified Alex
 */
public class SM_FRIEND_LIST extends AionServerPacket {

    private FriendList friendList;
    private boolean gmpanel;

    public SM_FRIEND_LIST() {
    }

    public SM_FRIEND_LIST(FriendList friendList, boolean gmpanel) {
        this.friendList = friendList;
        this.gmpanel = gmpanel;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        FriendList list = friendList == null ? con.getActivePlayer().getFriendList() : friendList;
        writeH((0 - list.getSize()));
        writeC(gmpanel ? 1 : 0);// gm panel
        for (Friend friend : list) {
            writeD(friend.getOid());
            writeS(friend.getName());
            writeD(friend.getLevel());
            writeD(friend.getPlayerClass().getClassId());
            writeC(friend.isOnline() ? 1 : 0);
            writeD(friend.getMapId());
            writeD(friend.getLastOnlineTime()); // Date friend was last online as a Unix timestamp.
            writeS(friend.getNote()); // Friend note
            writeC(friend.getStatus().getId());

            int address = HousingService.getInstance().getPlayerAddress(friend.getOid());
            if (address > 0) {
                House house = HousingService.getInstance().getPlayerStudio(friend.getOid());
                if (house == null) {
                    house = HousingService.getInstance().getHouseByAddress(address);
                    writeD(house.getAddress().getId());
                } else {
                    writeD(address);
                }
                writeC(house.getDoorState().getPacketValue());
            } else {
                writeD(0);
                writeC(0);
            }
        }
    }
}
