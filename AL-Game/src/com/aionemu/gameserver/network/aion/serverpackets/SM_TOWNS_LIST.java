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

import com.aionemu.gameserver.model.town.Town;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import java.util.Map;

/**
 * @author ViAl
 *
 */
public class SM_TOWNS_LIST extends AionServerPacket {

    private final Map<Integer, Town> towns;

    public SM_TOWNS_LIST(Map<Integer, Town> towns) {
        this.towns = towns;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeH(towns.size());
        for (Town town : towns.values()) {
            writeD(town.getId());
            writeD(town.getLevel());
            writeD((int) (town.getLevelUpDate().getTime() / 1000));
        }
    }
}
