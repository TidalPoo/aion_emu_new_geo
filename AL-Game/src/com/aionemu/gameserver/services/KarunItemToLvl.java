/*
 * AionLight project
 */
package com.aionemu.gameserver.services;

import com.aionemu.gameserver.cardinal.AddItemType;
import com.aionemu.gameserver.model.account.PlayerAccountData;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.item.ItemService;
import java.util.Iterator;

/**
 *
 * @author Alex
 */
public class KarunItemToLvl {

    public static boolean addItem(Player player) {
        Iterator<PlayerAccountData> data = player.getClientConnection().getAccount().iterator();
        if (player.getClientConnection().getAccount().size() == 1) {
            return true;
        }

        while (data.hasNext()) {
            PlayerAccountData d = data.next();
            if (d != null && d.getPlayerCommonData() != null && d.getPlayerCommonData() != player.getCommonData()) {
                if (d.getPlayerCommonData().getLevel() >= 60) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void addItems(Player player) {
        int[] items = new int[]{101300932, 1, 101300932, 1};
        for (int i = 0; i < items.length - 1; i++) {
            ItemService.addItem(player, items[i % 2], items[i + 1], AddItemType.CUSTOM, null);
        }
    }
}
