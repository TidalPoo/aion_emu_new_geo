package com.aionemu.gameserver.model.templates.item.actions;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Created by Alex on 23.12.2014.
 */
public class ExpAddAction extends AbstractItemAction {

    @XmlAttribute
    protected int cost;

    @XmlAttribute(name = "percent")
    protected boolean isPercent;

    @Override
    public boolean canAct(Player player, Item parentItem, Item targetItem) {
        return cost > 0;
    }

    @Override
    public void act(Player player, Item parentItem, Item targetItem) {
        long exp = player.getCommonData().getExpNeed();
        long expPercent = isPercent ? (exp * cost / 100) : cost;
        if (player.getInventory().decreaseByObjectId(parentItem.getObjectId(), 1)) {
            player.getCommonData().setExp(player.getCommonData().getExp() + expPercent);
            player.getObserveController().notifyItemuseObservers(parentItem);
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GET_EXP2(expPercent));
            ItemTemplate itemTemplate = parentItem.getItemTemplate();
            PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), itemTemplate.getTemplateId()), true);
            //PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_USE_ITEM(new DescriptionId(itemTemplate.getNameId())));
        }
    }
}
