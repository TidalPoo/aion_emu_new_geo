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
 * This packet is used to teleport player and port animation
 *
 * @author Luno, orz, xTz
 */
public class SM_TELEPORT_LOC extends AionServerPacket {

    private final int portAnimation;
    private final int mapId;
    private final int instanceId;
    private final float x, y, z;
    private final byte heading;
    private final boolean isInstance;

    public SM_TELEPORT_LOC(boolean isInstance, int instanceId, int mapId, float x, float y, float z, byte heading, int portAnimation) {
        this.isInstance = isInstance;
        this.instanceId = instanceId;
        this.mapId = mapId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.heading = heading;
        this.portAnimation = portAnimation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con) {
        writeC(portAnimation); // portAnimation
        writeD(mapId);//new 4.3 NA    -->old //writeH(mapId & 0xFFFF);
        writeD(isInstance ? instanceId : mapId); // mapId | instanceId
        writeF(x); // x
        writeF(y); // y
        writeF(z); // z
        writeC(heading); // headling
    }
}
