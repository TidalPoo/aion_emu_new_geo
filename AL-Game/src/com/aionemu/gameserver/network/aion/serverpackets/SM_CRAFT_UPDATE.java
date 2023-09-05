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

import com.aionemu.gameserver.configs.main.CraftConfig;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Mr. Poke
 */
public class SM_CRAFT_UPDATE extends AionServerPacket {

    private final int skillId;
    private final int itemId;
    private final int action;
    private final int success;
    private final int failure;
    private final int nameId;
    private final int executionSpeed;

    /**
     * @param skillId
     * @param item
     * @param success
     * @param failure
     * @param action
     */
    public SM_CRAFT_UPDATE(int skillId, ItemTemplate item, int success, int failure, int action) {
        this.action = action;
        this.skillId = skillId;
        this.itemId = item.getTemplateId();
        this.success = success;
        this.failure = failure;
        this.nameId = item.getNameId();

        if (skillId == 40009) {
            this.executionSpeed = CraftConfig.CRAFT_SPEED_NEW;//1500
        } else {
            this.executionSpeed = CraftConfig.CRAFT_SPEED;//700
        }
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeH(skillId);
        writeC(action);
        writeD(itemId);

        switch (action) {
            case 0: // init
            {
                writeD(success);
                writeD(failure);
                writeD(0);
                writeD(1200); // timer??
                writeD(1330048);
                writeH(0x24); // 0x24
                writeD(nameId);
                writeH(0);
                break;
            }
            case 1: // update
            case 2: // crit
            case 5: // sucess
            {
                writeD(success);
                writeD(failure);
                writeD(executionSpeed);
                writeD(1200);
                writeD(0);
                writeH(0);
                break;
            }
            case 3: //crit
            {
                writeD(success);
                writeD(failure);
                writeD(0);
                writeD(1200);
                writeD(1330048); //message
                writeH(0x24);
                writeD(nameId);
                writeH(0);
                break;
            }
            case 4: //cancel
            {
                writeD(success);
                writeD(failure);
                writeD(0);
                writeD(0);
                writeD(1330051);
                writeH(0);
                break;
            }
            case 6: // failed
            {
                writeD(success);
                writeD(failure);
                writeD(executionSpeed);
                writeD(1200);
                writeD(1330050);
                writeH(0x24);
                writeD(nameId);
                writeH(0);
                break;
            }
            case 7: {
                writeD(success);
                writeD(failure);
                writeD(0);
                writeD(1200);
                writeD(1330050);
                writeH(0x24);
                writeD(nameId);
                writeH(0);
                break;
            }
        }
    }
}
