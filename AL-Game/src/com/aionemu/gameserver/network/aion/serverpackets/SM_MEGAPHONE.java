/*
 * This file is part of aion-engine <aion-engine.net>
 *
 * aion-engine is private software: you can redistribute it and or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Private Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with aion-engine.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Ever'
 */
public class SM_MEGAPHONE extends AionServerPacket {

    private final String playerName;
    private final String message;
    private final int itemId;

    public SM_MEGAPHONE(String playerName, String message, int itemId) {
        this.playerName = playerName;
        this.message = message;
        this.itemId = itemId;
    }

    @Override
    protected void writeImpl(AionConnection client) {
        writeS(playerName);
        writeS(message);
        writeD(itemId);
        writeC(1);
    }
}
