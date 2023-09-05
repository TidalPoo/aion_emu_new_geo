/**
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 * aion-lightning is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * aion-lightning is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * aion-lightning. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * @author MrPoke, Rolandas
 */
public class SM_NEARBY_QUESTS extends AionServerPacket {

    private final HashMap<Integer, Integer> nearbyQuestList;

    public SM_NEARBY_QUESTS(HashMap<Integer, Integer> nearbyQuestList) {
        this.nearbyQuestList = nearbyQuestList;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        if (nearbyQuestList == null || con.getActivePlayer() == null) {
            return;
        }

        writeC(0);
        writeH(-nearbyQuestList.size() & 0xFFFF);
        for (Entry<Integer, Integer> nearbyQuest : nearbyQuestList.entrySet()) {
            if (nearbyQuest.getValue() > 0) {
                writeH(nearbyQuest.getKey());
                writeH(0x2); // To show grey icons for future quests
            } else {
                // Quests are displayed on map
                writeD(nearbyQuest.getKey());
            }
        }
    }
}
