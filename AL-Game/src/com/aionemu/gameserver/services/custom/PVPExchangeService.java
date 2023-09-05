/*
 * This file is part of gtemu.net <gtemu.net>.
 *
 *  gtemu.net is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  gtemu.net is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with gtemu.net.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.services.custom;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.cardinal.AddItemType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author cheatkiller
 *
 */
public class PVPExchangeService {

    public static void exchangeMedal(Player player) {
        if (hasItem(player, 186000147)) {
            if (player.getInventory().isFull() || player.getInventory().isFullSpecialCube()) {
                PacketSendUtility.sendMessage(player, "You Bag is Full. You not can Trade");
                return;
            }
            player.getInventory().decreaseByItemId(186000147, 1);
            int rnd = Rnd.get(0, 100);
            if (rnd < 25) {
                ItemService.addItem(player, 186000242, 1, AddItemType.PVP, null);
            } else {
                ItemService.addItem(player, 182005205, 1, AddItemType.PVP, null);
            }
        } else {
            PacketSendUtility.sendMessage(player, "You not have Medal");
        }
    }

    private static boolean hasItem(Player player, int itemId) {
        return player.getInventory().getItemCountByItemId(itemId) > 0;
    }

}
