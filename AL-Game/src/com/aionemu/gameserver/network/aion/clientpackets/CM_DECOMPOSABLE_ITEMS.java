/*
 * This file is part of [JS]Emulator Copyright (C) 2014 <http://www.js-emu.ru>
 *
 * [JS]Emulator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * [JS]Emulator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with [JS]Emulator.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.cardinal.AddItemType;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.templates.item.ExtractedItemsCollection;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.item.ResultedItem;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DECOMPOSABLE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import java.util.List;

/**
 *
 * @author Steve
 */
public class CM_DECOMPOSABLE_ITEMS extends AionClientPacket {

    private int objectId;
    private int index;

    public CM_DECOMPOSABLE_ITEMS(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    @Override
    protected void readImpl() {
        this.objectId = readD();
        readD();
        this.index = readC();
    }

    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        if (player == null) {
            return;
        }

        Object obj = player.getTempStorage(objectId);
        if (obj == null) {
            return;
        }
        ResultedItem resultItem = null;
        if (obj instanceof ExtractedItemsCollection) {
            ExtractedItemsCollection collection = (ExtractedItemsCollection) obj;
            Storage inventory = player.getInventory();
            List<ResultedItem> resultItems = (List) collection.getItems();
            resultItem = resultItems.get(index);

            int slotReq = calcMaxCountOfSlots(resultItem, player, false);
            int specialSlotreq = calcMaxCountOfSlots(resultItem, player, true);
            if (slotReq > 0 && inventory.getFreeSlots() < slotReq) {
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DECOMPRESS_INVENTORY_IS_FULL);
                return;
            }
            if (specialSlotreq > 0 && inventory.getSpecialCubeFreeSlots() < specialSlotreq) {
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DECOMPRESS_INVENTORY_IS_FULL);
                return;
            }

        }
        if (resultItem == null) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_DECOMPOSE_ITEM_NO_TARGET_ITEM);
            return;
        }

        Item item = player.getInventory().getItemByObjId(objectId);
        final int itemId = item == null ? 0 : item.getItemId();
        if (!player.getInventory().decreaseByObjectId(objectId, 1)) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_DECOMPOSE_ITEM_NO_TARGET_ITEM);
            return;
        }

        PacketSendUtility.sendPacket(player, new SM_DECOMPOSABLE(objectId));

        player.clearTempStorage(index);

        ItemService.addItem(player, resultItem.getItemId(), resultItem.getResultCount(), AddItemType.USEITEM, "decompasible: " + itemId);
    }

    private int calcMaxCountOfSlots(ResultedItem item, Player player, boolean special) {
        int resulCount = 0;
        if (item.getRace().equals(Race.PC_ALL)
                || player.getRace().equals(item.getRace())) {
            if (item.getPlayerClass().equals(PlayerClass.ALL)
                    || player.getPlayerClass().equals(item.getPlayerClass())) {
                ItemTemplate template = DataManager.ITEM_DATA.getItemTemplate(item.getItemId());
                if (special && template.getExtraInventoryId() > 0) {
                    resulCount = item.getResultCount();
                } else if (template.getExtraInventoryId() < 1) {
                    resulCount = item.getResultCount();
                }
            }
        }
        return resulCount;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
