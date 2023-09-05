package com.aionemu.gameserver.eventengine.events;

import com.aionemu.gameserver.cardinal.AddItemType;
import com.aionemu.gameserver.eventengine.events.enums.EventType;
import com.aionemu.gameserver.eventengine.events.xml.EventRankTemplate;
import com.aionemu.gameserver.eventengine.events.xml.EventRewardItem;
import com.aionemu.gameserver.eventengine.events.xml.EventRewardItemGroup;
import com.aionemu.gameserver.eventengine.events.xml.EventRewardTemplate;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.ingameshop.InGameShopEn;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.item.ItemService;

/**
 *
 * @author f14shm4n
 */
public class EventRewardHelper {

    public static void GiveRewardFor(Player player, EventType etype, EventScore score, int rank) {
        EventRewardTemplate rt = etype.getEventTemplate().getRewardInfo();
        if (rt == null) {
            return;
        }
        EventRankTemplate rw = rt.getRewardByRank(rank);
        if (rw == null) {
            // no rewatd in template for this rank
            return;
        }
        if (rw.getAp() > 0) { // abyss point reward
            AbyssPointsService.addAp(player, rw.getAp());
        }
        if (rw.getGamePoint() > 0) { // toll point reward
            InGameShopEn.getInstance().addToll(player, rw.getGamePoint());
        }
        for (EventRewardItemGroup gr : rw.getRewards()) { // items reward
            for (EventRewardItem item : gr.getItems()) {
                ItemService.addItem(player, item.getItemId(), item.getCount(), AddItemType.EVENT, "CUSTOM");
            }
        }
    }
}
