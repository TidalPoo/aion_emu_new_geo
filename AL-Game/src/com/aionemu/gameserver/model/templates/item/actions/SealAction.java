/*
 * SAO Project
 */
package com.aionemu.gameserver.model.templates.item.actions;

import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import java.sql.Timestamp;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Alex
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SealAction")
public class SealAction extends AbstractItemAction {

    @XmlAttribute(name = "day")
    protected int day;

    @XmlAttribute(name = "maxcount")
    protected int maxcount;

    @XmlAttribute(name = "unseal")
    protected boolean unseal;

    @Override
    public boolean canAct(Player player, Item parentItem, Item targetItem) {
        if (targetItem.getSealTime() != null && day != 0) {
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400830, new DescriptionId(parentItem.getNameId())));
            long d = (targetItem.getSealTime().getTime() - System.currentTimeMillis()) / 1000 / 60 / 60 / 24;
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400804, d));
            return false;
        }
        if (maxcount > 0) {
            for (Item item : player.getAllItems()) {
                if (item.getSeal() == 1 && item.getSealTime() != null) {
                    player.sealcount++;
                }
            }
            if (player.sealcount >= 3) {
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400828, new DescriptionId(targetItem.getNameId())));
                return false;
            }
        }
        if (targetItem.getSeal() == 2) {
            //%0%: печать наложена администратором, ее нельзя снять.
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400901, new DescriptionId(targetItem.getNameId())));
            return false;
        }
        //TODO
        //1401545
        //%0%: невозможно использовать с особым предметом, ожидающим снятия печати.
        return true;
    }

    @Override
    public void act(Player player, Item parentItem, Item targetItem) {
        if (unseal && day != 0) {
            int free = (3 - player.sealcount);
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400805, free));
        }
        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 5000, 0, 0), true);
        final ItemUseObserver observer = new ItemUseObserver() {

            @Override
            public void abort() {
                player.getController().cancelTask(TaskId.ITEM_USE);
                player.removeItemCoolDown(parentItem.getItemTemplate().getUseLimits().getDelayId());
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 0, 2, 0), true);
                player.getObserveController().removeObserver(this);
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(!unseal ? 1400798 : 1400829, new DescriptionId(targetItem.getNameId())));
                if (!unseal) {
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400831, new DescriptionId(targetItem.getNameId())));
                }
            }
        };
        player.getObserveController().attach(observer);
        player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                player.getObserveController().removeObserver(observer);
                if (/*!unseal &&*/!player.getInventory().decreaseByItemId(parentItem.getItemId(), 1)) {
                    PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemId(), 0, 1, 1), true);
                    return;
                }
                if (day != 0) {
                    targetItem.setSealTime(new Timestamp(System.currentTimeMillis() + (day * 1L) * 24 * 60 * 60 * 1000));
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400804, 7));
                } else {
                    targetItem.setSeal(unseal ? 0 : 1);
                    if (unseal) {
                        targetItem.setSealTime(null);
                        PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400808, new DescriptionId(targetItem.getNameId())));
                    }
                }
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemId(), 0, 1, 1), true);
                PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE_ITEM(player, targetItem));
                if (!unseal) {
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400797, new DescriptionId(targetItem.getNameId())));
                }
            }
        }, 5000));
    }
}
