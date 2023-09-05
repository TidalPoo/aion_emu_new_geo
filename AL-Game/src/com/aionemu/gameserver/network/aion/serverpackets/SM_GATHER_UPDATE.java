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

import com.aionemu.gameserver.model.templates.gather.GatherableTemplate;
import com.aionemu.gameserver.model.templates.gather.Material;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author ATracer, orz
 */
public class SM_GATHER_UPDATE extends AionServerPacket {

    private final GatherableTemplate template;
    private final int action;
    private final int itemId;
    private final int success;
    private final int failure;
    private final int nameId;

    public SM_GATHER_UPDATE(GatherableTemplate template, Material material, int success, int failure, int action) {
        this.action = action;
        this.template = template;
        this.itemId = material.getItemid();
        this.success = success;
        this.failure = failure;
        this.nameId = material.getNameid();
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeH(template.getHarvestSkill());
        writeC(action);
        writeD(itemId);

        int gatherSpeed = 700;//700
        int timer = 1200;
        switch (action) {
            case 0: {
                // Идет сбор ресурса: %0.
                writeD(template.getSuccessAdj());
                writeD(template.getFailureAdj());
                writeD(0);
                writeD(timer); // timer??
                writeD(1330011); // Идет сбор ресурса: %0.
                writeH(0x24); // 0x24
                writeD(nameId);
                writeH(0); // 0x24
                break;
            }
            case 1: {
                writeD(success);
                writeD(failure);
                writeD(gatherSpeed); // unk timer??
                writeD(timer); // unk timer??
                writeD(0); // unk timer??writeD(700);
                writeH(0);
                break;
            }
            case 2: {
                writeD(template.getSuccessAdj());
                writeD(failure);
                writeD(gatherSpeed);// unk timer??
                writeD(timer); // unk timer??
                writeD(0); // unk timer??writeD(700);
                writeH(0);
                break;
            }
            case 5: {
                // Сбор прекращен.
                writeD(0);
                writeD(0);
                writeD(gatherSpeed);// unk timer??
                writeD(timer); // unk timer??
                writeD(1330080); // Сбор прекращен.
                writeH(0);
                break;
            }
            case 6: {
                writeD(template.getSuccessAdj());
                writeD(failure);
                writeD(gatherSpeed); // unk timer??
                writeD(timer); // unk timer??
                writeD(0); // unk timer??writeD(700);
                writeH(0);
                break;
            }
            case 7: {
                // Не удалось собрать: %0.
                writeD(success);
                writeD(template.getFailureAdj());
                writeD(0);
                writeD(timer); // timer??
                writeD(1330079); // Не удалось собрать: %0.
                writeH(0x24); // 0x24
                writeD(nameId);
                writeH(0); // 0x24
                break;
            }
        }
    }
}
