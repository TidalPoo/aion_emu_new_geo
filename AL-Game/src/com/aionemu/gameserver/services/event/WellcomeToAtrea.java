/*
 * AionLight project
 */
package com.aionemu.gameserver.services.event;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerBonusTimeStatus;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.ColorUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 *
 * @author Alex
 */
public class WellcomeToAtrea {

    public static boolean returned_inventory = true;
    public static boolean returned_inventory50 = true;
    public static boolean novice_inventory = true;

    public static void selectItem(Player player, int itemId, int maxCountOfDay, String rus) {
        if (itemId == 186000221 && player.getBonusTime().getStatus() != PlayerBonusTimeStatus.NEW
                || itemId == 186000222 && (maxCountOfDay != 2 && player.getBonusTime().getStatus() != PlayerBonusTimeStatus.COMEBACK
                || maxCountOfDay == 2 && player.getBonusTime().getStatus().isBonus())) {
            return;
        }
        ItemService.dropItemToInventory(player, itemId);
        player.addItemMaxCountOfDay(itemId, player.getItemMaxThisCount(itemId) + 1);
        PacketSendUtility.sendMessage(player, "\u0412\u044b \u043f\u043e\u043b\u0443\u0447\u0438\u043b\u0438 \u043f\u0440\u0435\u0434\u043c\u0435\u0442 \u043f\u043e \u0438\u0432\u0435\u043d\u0442\u0443 \"" + ColorUtil.convertFromUTF8(rus) + "\"."
                + "\n\u0416\u0435\u043b\u0430\u0435\u043c \u043f\u0440\u0438\u044f\u0442\u043d\u043e\u0439 \u0438\u0433\u0440\u044b " + player.getName() + "!\ue01E");
    }
}
