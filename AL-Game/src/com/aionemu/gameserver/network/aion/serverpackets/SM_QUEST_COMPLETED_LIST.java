/*
 * This file is part of aion-lightning <aionu-unique.org>.
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
import com.aionemu.gameserver.questEngine.model.QuestState;
import javolution.util.FastList;

/**
 * @author MrPoke
 */
public class SM_QUEST_COMPLETED_LIST extends AionServerPacket {

    private FastList<QuestState> questState;
    private final boolean gmpanel;

    public SM_QUEST_COMPLETED_LIST(FastList<QuestState> questState, boolean gmpanel) {
        this.questState = questState;
        this.gmpanel = gmpanel;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeH(gmpanel ? 0 : 1); // 2.1 gm panel =)
        writeH(-questState.size() & 0xFFFF);
        //QuestsData QUEST_DATA = DataManager.QUEST_DATA;
        for (QuestState qs : questState) {
            writeD(qs.getQuestId());
            //	writeH(QUEST_DATA.getQuestById(qs.getQuestId()).getCategory().getId());
            writeC(qs.getCompleteCount());
        }
        FastList.recycle(questState);
        questState = null;
    }
}
