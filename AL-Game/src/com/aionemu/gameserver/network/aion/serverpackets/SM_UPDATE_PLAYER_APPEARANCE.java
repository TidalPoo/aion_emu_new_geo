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

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.items.GodStone;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import java.util.List;

/**
 * @author Avol modified by ATracer
 */
public class SM_UPDATE_PLAYER_APPEARANCE extends AionServerPacket {

    public int playerId;
    public int size;
    public List<Item> items;

    public SM_UPDATE_PLAYER_APPEARANCE(int playerId, List<Item> items) {
        this.playerId = playerId;
        this.items = items;
        this.size = items.size();
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeD(playerId);

        short mask = 0;
        for (Item item : items) {
            if (item.getItemTemplate().isTwoHandWeapon()) {
                ItemSlot[] slots = ItemSlot.getSlotsFor(item.getEquipmentSlot());
                mask |= slots[0].getSlotIdMask();
            } else {
                mask |= item.getEquipmentSlot();
            }
        }

        writeH(mask); // Wrong !!! It's item count, but doesn't work
        items.stream().map((item) -> {
            writeD(item.getItemSkinTemplate().getTemplateId());
            return item;
        }).map((item) -> {
            GodStone godStone = item.getGodStone();
            writeD(godStone != null ? godStone.getItemId() : 0);
            writeD(item.getItemColor());
            return item;
        }).forEach((item) -> {
            writeH(item.getEnchantView());// unk (0x00)
        });
    }
}
