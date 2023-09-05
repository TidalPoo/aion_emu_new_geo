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

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Sweetkr, Alex
 */
public class SM_DP_INFO extends AionServerPacket {

    private int playerObjectId;
    private int currentDp;
    private byte key = 0;
    private int houseId;
    private int locId;
    private int unk1;
    private int week;

    public SM_DP_INFO(int playerObjectId, int currentDp) {
        this.playerObjectId = playerObjectId;
        this.currentDp = currentDp;
    }

    public SM_DP_INFO(int locId, int houseId, int unk1, int week) {
        this.houseId = houseId;
        this.key = 1;
        this.locId = locId;
        this.unk1 = unk1;
        this.week = week;
    }

    @Override
    protected void writeImpl(AionConnection paramAionConnection) {
        switch (this.key) {
            case 0:
                writeD(this.playerObjectId);
                writeH(this.currentDp);
                break;
            case 1:
                writeD(this.locId);
                writeD(this.houseId);
                writeC(this.unk1);
                writeD(this.week);
                writeD(0);
                writeD(0);
                writeD(0);
        }
    }
}
