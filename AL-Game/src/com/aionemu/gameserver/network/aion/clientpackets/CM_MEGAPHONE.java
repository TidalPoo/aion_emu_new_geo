package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MEGAPHONE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import java.util.Iterator;

/**
 * @author xXMashUpXx, Alex
 */
public class CM_MEGAPHONE extends AionClientPacket {

    private String chatMessage;
    private int itemObjectId;

    public CM_MEGAPHONE(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    @Override
    protected void readImpl() {
        chatMessage = readS();
        itemObjectId = readD();
    }

    @Override
    protected void runImpl() {
        Player activePlayer = getConnection().getActivePlayer();
        Item item = activePlayer.getInventory().getItemByObjId(itemObjectId);
        if (item == null) {
            return;
        }

        int useDelay = activePlayer.getItemCooldown(item.getItemTemplate());
        if (activePlayer.isItemUseDisabled(item.getItemTemplate().getUseLimits())) {
            PacketSendUtility.sendPacket(activePlayer, SM_SYSTEM_MESSAGE.STR_ITEM_CANT_USE_UNTIL_DELAY_TIME);
            return;
        }
        if (useDelay > 0) {
            activePlayer.addItemCoolDown(item.getItemTemplate().getUseLimits().getDelayId(), System.currentTimeMillis() + useDelay,
                    useDelay / 1000);
        }

        // notify item use observer
        activePlayer.getObserveController().notifyItemuseObservers(item);
        ItemTemplate itemTemplate = item.getItemTemplate();
        PacketSendUtility.broadcastPacket(activePlayer, new SM_ITEM_USAGE_ANIMATION(activePlayer.getObjectId(), item.getObjectId(), itemTemplate.getTemplateId()), true);

        //remove item
        activePlayer.getInventory().decreaseByObjectId(itemObjectId, 1);
        Iterator<Player> iter = World.getInstance().getPlayersIterator();
        while (iter.hasNext()) {
            Player race = iter.next();
            if (race.getRace() == activePlayer.getRace() || race.isGM()) {
                PacketSendUtility.sendPacket(race, new SM_MEGAPHONE(activePlayer.getName(), chatMessage, itemObjectId));
            }
        }

        PacketSendUtility.sendPacket(activePlayer, SM_SYSTEM_MESSAGE.STR_USE_ITEM(new DescriptionId(item.getItemTemplate().getNameId())));

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
