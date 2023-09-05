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

import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import java.sql.Timestamp;
import java.util.Map;

/**
 * @author Simple
 */
public class SM_LEGION_INFO extends AionServerPacket {

    /**
     * Legion information *
     */
    private final Legion legion;
    private final boolean gmpanel;

    /**
     * This constructor will handle legion info
     *
     * @param legion
     * @param gmpanel
     */
    public SM_LEGION_INFO(Legion legion, boolean gmpanel) {
        this.legion = legion;
        this.gmpanel = gmpanel;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeS(legion.getLegionName());
        writeC(legion.getLegionLevel());
        writeD(legion.getLegionRank());
        writeH(legion.getDeputyPermission());
        writeH(legion.getCenturionPermission());
        writeH(legion.getLegionaryPermission());
        writeH(legion.getVolunteerPermission());
        writeQ(legion.getContributionPoints());
        writeD(0x00); // unk
        writeD(0x00); // unk
        writeD(0x00); // unk 3.0
        //Обьявление: До расформирования осталось
        /**
         * Get Announcements List From DB By Legion *
         */
        Map<Timestamp, String> announcementList = legion.getAnnouncementList().descendingMap();

        /**
         * Show max 7 announcements *
         */
        int i = 0;
        for (Timestamp unixTime : announcementList.keySet()) {
            writeS(announcementList.get(unixTime));
            writeD((int) (unixTime.getTime() / 1000));
            i++;
            if (i >= 7) {
                break;
            }
        }
    }
}
