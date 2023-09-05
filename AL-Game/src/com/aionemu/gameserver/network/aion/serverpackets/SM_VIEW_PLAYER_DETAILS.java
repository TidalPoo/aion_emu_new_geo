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

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.iteminfo.ItemInfoBlob;
import java.util.List;

/**
 * @author Avol, xTz
 * @modified Alex
 */
public class SM_VIEW_PLAYER_DETAILS extends AionServerPacket {

    private final List<Item> items;
    private final int itemSize;
    private final int targetObjId;
    private final Player player;
    private final boolean gmpanel;

    public SM_VIEW_PLAYER_DETAILS(List<Item> items, Player player, boolean gmpanel) {
        this.player = player;
        this.targetObjId = player.getObjectId();
        this.items = items;
        this.itemSize = items.size();
        this.gmpanel = gmpanel;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeD(targetObjId);
        writeC(gmpanel ? 10 : 11);//10 gm panel, 11 player info
        writeH(itemSize);
        for (Item item : items) {
            writeItemInfo(item);
        }
    }

    private void writeItemInfo(Item item) {
        ItemTemplate template = item.getItemTemplate();
        writeD(item.getObjectId());
        writeD(template.getTemplateId());
        writeNameId(template.getNameId());
        ItemInfoBlob itemInfoBlob = ItemInfoBlob.getFullBlob(player, item);
        itemInfoBlob.writeMe(getBuf());
    }
}
