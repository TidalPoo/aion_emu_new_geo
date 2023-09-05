/*
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 * aion-lightning is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-lightning is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.ingameshop.InGameShopEn;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_IN_GAME_SHOP_CATEGORY_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_IN_GAME_SHOP_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_IN_GAME_SHOP_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TOLL_INFO;
import com.aionemu.gameserver.restrictions.RestrictionsManager;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author xTz, KID
 * @modified Alex
 */
public class CM_IN_GAME_SHOP_INFO extends AionClientPacket {

    private int actionId;
    private int categoryId;
    private int listInCategory;
    private String senderName;
    private String senderMessage;

    public CM_IN_GAME_SHOP_INFO(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    @Override
    protected void readImpl() {
        actionId = readC();
        categoryId = readD();
        listInCategory = readD();
        senderName = readS();
        senderMessage = readS();
    }

    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        if (!RestrictionsManager.canShowIngameshop(player)) {
            return;
        }
        showWindow(player, actionId, categoryId, listInCategory, senderName, senderMessage);
    }

    private void showWindow(Player player, int actionId, int categoryId, int listInCategory, String senderName, String senderMessage) {
        player.inGameShop.setListCategory(0);
        switch (actionId) {
            case 0x01: // item info
                PacketSendUtility.sendPacket(player, new SM_IN_GAME_SHOP_ITEM(player, categoryId));
                break;
            case 0x02: // change category
                int m = player.getMembership();
                if (categoryId == 3 && m < 2) {
                    PacketSendUtility.sendBrightYellowMessageOnCenter(player, "Эта категория доступна только вип аккаунтам!");
                    return;
                }
                PacketSendUtility.sendPacket(player, new SM_IN_GAME_SHOP_CATEGORY_LIST(2, categoryId));
                player.inGameShop.setCategory((byte) categoryId);
                break;
            case 0x04: // category list
                PacketSendUtility.sendPacket(player, new SM_IN_GAME_SHOP_CATEGORY_LIST(0, categoryId));
                break;
            case 0x08: // showcat
                if (categoryId > 1) {
                    player.inGameShop.setSubCategory((byte) categoryId);
                }
                player.inGameShop.setListCategory(listInCategory);
                PacketSendUtility.sendPacket(player, new SM_IN_GAME_SHOP_LIST(player, listInCategory, 1));
                PacketSendUtility.sendPacket(player, new SM_IN_GAME_SHOP_LIST(player, listInCategory, 0));
                break;
            case 0x10: // balance
                PacketSendUtility.sendPacket(player, new SM_TOLL_INFO(player.getClientConnection().getAccount().getToll()));
                break;
            case 0x20: // buy купить
                InGameShopEn.getInstance().acceptRequest(player, categoryId);
                break;
            case 0x40: // gift подарить
                InGameShopEn.getInstance().sendRequest(player, senderName, senderMessage, categoryId);
                break;
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
