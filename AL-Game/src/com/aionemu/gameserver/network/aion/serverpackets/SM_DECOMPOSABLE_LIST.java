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

import com.aionemu.gameserver.model.templates.item.ResultedItem;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Steve
 */
public class SM_DECOMPOSABLE_LIST extends AionServerPacket {

    private final int objectId;
    private Collection<ResultedItem> items = new ArrayList<>();

    public SM_DECOMPOSABLE_LIST(int objectId, Collection<ResultedItem> items) {
        this.objectId = objectId;
        this.items = items;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeD(objectId);
        writeD(0);
        writeC(items.size());

        int i = 0;
        for (ResultedItem item : items) {
            writeC(i);
            writeD(item.getItemId());
            writeD(item.getCount());
            writeH(0);
            writeC(0);
            writeC(1);
            i++;
        }
    }
}
