/*
 * This file is part of [JS]Emulator Copyright (C) 2014 <http://www.js-emu.ru>
 *
 * [JS]Emulator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * [JS]Emulator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with [JS]Emulator.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 *
 * @author Steve
 */
public class SM_DECOMPOSABLE extends AionServerPacket {

    private final int objectId;

    public SM_DECOMPOSABLE(int objectId) {
        this.objectId = objectId;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeD(objectId);
        writeD(0);
        writeC(0);
    }
}
