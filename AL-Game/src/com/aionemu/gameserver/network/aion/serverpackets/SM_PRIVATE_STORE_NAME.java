/*
 * This file is part of aion-lightning <www.aion-lightning>.
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
 * @author Simple
 */
public class SM_PRIVATE_STORE_NAME extends AionServerPacket {

    /**
     * Private store Information *
     */
    private final int playerObjId;
    private final String name;

    public SM_PRIVATE_STORE_NAME(int playerObjId, String name) {
        this.playerObjId = playerObjId;
        this.name = name;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeD(playerObjId);
        writeS(name);
    }
}
