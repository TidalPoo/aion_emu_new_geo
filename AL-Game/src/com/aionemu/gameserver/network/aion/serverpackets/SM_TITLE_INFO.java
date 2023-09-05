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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.title.Title;
import com.aionemu.gameserver.model.gameobjects.player.title.TitleList;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author cura, xTz
 * @modified -Enomine-
 */
public class SM_TITLE_INFO extends AionServerPacket {

    private TitleList titleList;
    private final int action; // 0: list, 1: self set, 3: broad set
    private int titleId;
    private int bonusTitleId;
    private int playerObjId;
    private final boolean gmpanel;

    /**
     * title list
     *
     * @param player
     * @param gmpanel
     */
    public SM_TITLE_INFO(Player player, boolean gmpanel) {
        this.action = 0;
        this.titleList = player.getTitleList();
        this.gmpanel = gmpanel;
    }

    /**
     * self title set
     *
     * @param titleId
     * @param gmpanel
     */
    public SM_TITLE_INFO(int titleId, boolean gmpanel) {
        this.action = 1;
        this.titleId = titleId;
        this.gmpanel = gmpanel;
    }

    /**
     * broad title set
     *
     * @param player
     * @param titleId
     * @param gmpanel
     */
    public SM_TITLE_INFO(Player player, int titleId, boolean gmpanel) {
        this.action = 3;
        this.playerObjId = player.getObjectId();
        this.titleId = titleId;
        this.gmpanel = gmpanel;
    }

    public SM_TITLE_INFO(boolean flag, boolean gmpanel) {
        this.action = 4;
        this.titleId = flag ? 1 : 0;
        this.gmpanel = gmpanel;
    }

    public SM_TITLE_INFO(Player player, boolean flag, boolean gmpanel) {
        this.action = 5;
        this.playerObjId = player.getObjectId();
        this.titleId = flag ? 1 : 0;
        this.gmpanel = gmpanel;
    }

    public SM_TITLE_INFO(int action, int bonusTitleId, boolean gmpanel) {
        this.action = action;
        this.bonusTitleId = bonusTitleId;
        this.gmpanel = gmpanel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con) {
        writeC(action);
        switch (action) {
            case 0:
                writeC(gmpanel ? 1 : 0);// gmpanel 0 player 1 admin
                writeH(titleList.size());
                for (Title title : titleList.getTitles()) {
                    writeD(title.getId());
                    writeD(title.getRemainingTime());
                }
                break;
            case 1: // self set
                writeH(titleId);
                break;
            case 3: // broad set
                writeD(playerObjId);
                writeH(titleId);
                break;
            case 4: // Mentor flag self
                writeH(titleId);
                break;
            case 5: // broad set mentor fleg
                writeD(playerObjId);
                writeH(titleId);
                break;
            case 6://Title wich will take BonusStats from
                writeH(bonusTitleId);
                break;
        }
    }
}
