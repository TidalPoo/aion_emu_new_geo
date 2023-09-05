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
package com.aionemu.gameserver.model.templates.item.actions;

import com.aionemu.gameserver.cardinal.AddItemType;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Rolandas
 */
public class ExpExtractAction extends AbstractItemAction {

    @XmlAttribute
    protected int cost;

    @XmlAttribute(name = "percent")
    protected boolean isPercent;

    @XmlAttribute(name = "item_id")
    protected int itemId;

    @Override
    public boolean canAct(Player player, Item parentItem, Item targetItem) {
        return true;
    }

    @Override
    public void act(Player player, Item parentItem, Item targetItem) {
        long exp = player.getCommonData().getExpNeed() * cost / 100;
        if (!isPercent) {
            exp = cost;
        }
        if (player.getInventory().decreaseByObjectId(parentItem.getObjectId(), 1)) {
            player.getCommonData().setExp(player.getCommonData().getExp() - exp);
            ItemTemplate itemTemplate = parentItem.getItemTemplate();
            PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), itemTemplate.getTemplateId()), true);
            if (itemId != 0) {
                ItemService.addItem(player, itemId, 1, AddItemType.USEITEM, "expextract: " + parentItem.getItemId());
                ItemTemplate item2 = DataManager.ITEM_DATA.getItemTemplate(itemId);
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401705, new DescriptionId(itemTemplate.getNameId()), exp, new DescriptionId(item2.getNameId())));
            }
        }
    }
}
