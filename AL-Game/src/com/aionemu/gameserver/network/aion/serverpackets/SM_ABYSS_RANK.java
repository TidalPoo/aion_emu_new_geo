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

import com.aionemu.gameserver.model.gameobjects.player.AbyssRank;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.utils.stats.AbyssRankEnum;

/**
 * @author Nemiroff Date: 25.01.2010
 */
public class SM_ABYSS_RANK extends AionServerPacket {

    private final AbyssRank rank;
    private final int currentRankId;
    private final boolean gmpanel;

    public SM_ABYSS_RANK(AbyssRank rank, boolean gmpanel) {
        this.rank = rank;
        this.currentRankId = rank.getRank().getId();
        this.gmpanel = gmpanel;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeQ(rank.getAp()); // curAP
        writeD(currentRankId); // curRank
        writeD(rank.getTopRanking()); // curRating

        int nextRankId = currentRankId < AbyssRankEnum.values().length ? currentRankId + 1 : currentRankId;
        writeD(100 * rank.getAp() / AbyssRankEnum.getRankById(nextRankId).getRequired()); // exp %

        writeD(rank.getAllKill()); // allKill
        writeD(rank.getMaxRank()); // maxRank

        writeD(rank.getDailyKill()); // dayKill
        writeQ(rank.getDailyAP()); // dayAP

        writeD(rank.getWeeklyKill()); // weekKill
        writeQ(rank.getWeeklyAP()); // weekAP

        writeD(rank.getLastKill()); // laterKill
        writeQ(rank.getLastAP()); // laterAP

        writeC(gmpanel ? 1 : 0); // gm panel 0 player 1 admin
    }
}
