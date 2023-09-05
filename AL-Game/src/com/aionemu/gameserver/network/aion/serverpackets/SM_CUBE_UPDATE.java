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
import com.aionemu.gameserver.model.items.storage.StorageType;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Sweetkr
 */
public class SM_CUBE_UPDATE extends AionServerPacket {

    private int action;
    /**
     * for action 0 - its storage type<br>
     * for action 6 - its advanced stigma count
     */
    private int actionValue;

    private int itemsCount;
    private int npcExpands;
    private int questExpands;
    private boolean gmpanel;

    public static SM_CUBE_UPDATE stigmaSlots(int slots, boolean gmpanel) {
        return new SM_CUBE_UPDATE(6, slots, gmpanel);
    }

    public static SM_CUBE_UPDATE cubeSize(StorageType type, Player player, boolean gmpanel) {
        int itemsCount = 0;
        int npcExpands = 0;
        int questExpands = 0;
        switch (type) {
            case CUBE:
                itemsCount = player.getInventory().size();
                npcExpands = player.getNpcExpands();
                questExpands = player.getQuestExpands();
                break;
            case REGULAR_WAREHOUSE:
                itemsCount = player.getWarehouse().size();
                npcExpands = player.getWhNpcExpands();
                questExpands = player.getWhBonusExpands();
                break;
            case LEGION_WAREHOUSE:
                itemsCount = player.getLegion().getLegionWarehouse().size();
                npcExpands = player.getLegion().getWarehouseLevel();
                break;
        }

        return new SM_CUBE_UPDATE(0, type.ordinal(), itemsCount, npcExpands, questExpands, gmpanel);
    }


    private SM_CUBE_UPDATE(int action, int actionValue, int itemsCount, int npcExpands, int questExpands, boolean gmpanel) {
        this(action, actionValue, false);
        this.itemsCount = itemsCount;
        this.npcExpands = npcExpands;
        this.questExpands = questExpands;
        this.gmpanel = gmpanel;
    }

    private SM_CUBE_UPDATE(int action, int actionValue, boolean gmpanel) {
        this.action = action;
        this.actionValue = actionValue;
        this.gmpanel = gmpanel;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeC(action);
        writeC(actionValue);
        switch (action) {
            case 0:
                writeD(itemsCount);
                writeC(npcExpands); // cube size from npc (so max 5 for now)
                writeC(questExpands); // cube size from quest (so max 2 for now)
                writeC(gmpanel ? 1 : 0); // unk - gm panel?
                break;
            case 6:
                //writeC(gmpanel ? 1 : 0); // unk - gm panel?
                break;
            default:
                break;
        }
    }
}
