/*
 * Copyright (C) 2013 Steve
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.services;

import com.aionemu.gameserver.cache.HTMLCache;
import com.aionemu.gameserver.cardinal.AddItemType;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.ItemId;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.survey.SurveyActionType;
import com.aionemu.gameserver.model.templates.survey.SurveyItem;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.html.HTMLService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javolution.util.FastMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Steve
 */
public class AdvancedSurveyService {

    private static final Logger log = LoggerFactory.getLogger(AdvancedSurveyService.class);
    private final FastMap<Integer, SurveyItem> activeItems;
    private final String htmlTemplate;

    public boolean isActive(Player player, int survId, String response, List<Integer> items) {
        boolean avail = activeItems.containsKey(survId);
        if (avail) {
            requestSurvey(player, survId, response, items);
        }

        return avail;
    }

    public AdvancedSurveyService() {
        this.activeItems = FastMap.newInstance();
        this.htmlTemplate = HTMLCache.getInstance().getHTML("advanced.xhtml");
    }

    public void requestSurvey(Player player, int survId, String response, List<Integer> items) {
        SurveyItem item = activeItems.get(survId);
        if (item == null) {
            // There is no survey underway.
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300684));
            return;
        }
        if (item.items != null && items != null) {
            for (FastMap.Entry<Integer, Long> e = item.items.head(), end = item.items.tail(); (e = e.getNext()) != end;) {
                if (!items.contains(e.getKey())) {
                    continue;
                }
                ItemTemplate template = DataManager.ITEM_DATA.getItemTemplate(e.getKey());
                if (template != null) {
                    if (player.getInventory().isFull(template.getExtraInventoryId())) {
                        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_FULL_INVENTORY);
                        log.warn("[SurveyController] player " + player.getName() + " tried to receive item with full inventory.");
                        return;
                    }
                    ItemService.addItem(player, e.getKey(), e.getValue(), AddItemType.HTMLSURVEY, "survId: " + survId);
                    if (item.itemId == ItemId.KINAH.value()) // You received %num0 Kinah as reward for the survey.
                    {
                        PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300945, e.getValue()));
                    } else if (item.count == 1) // You received %0 item as reward for the survey.
                    {
                        PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300945, new DescriptionId(template.getNameId())));
                    } else // You received %num1 %0 items as reward for the survey.
                    {
                        PacketSendUtility.sendPacket(player,
                                new SM_SYSTEM_MESSAGE(1300946, e.getValue(), new DescriptionId(template.getNameId())));
                    }
                }
            }
        }

        SurveyActionType actionType = item.actionType;
        if (actionType != null) {
            switch (actionType) {
                case MOVE_TO_POSITION:
                    moveToPosition(item, player, response);
                    break;
            }
        }
    }

    public void addSurvey(SurveyItem item) {
        if (!activeItems.containsKey(item.uniqueId) && item.uniqueId > 0) {
            activeItems.put(item.uniqueId, item);
        }
    }

    public void removeSurvey(SurveyItem item) {
        if (activeItems.containsKey(item.uniqueId) && item.uniqueId > 0) {
            activeItems.remove(item.uniqueId);
        }
    }

    public void showAvailable(Player player) {
        for (SurveyItem item : activeItems.values()) {
            String context = htmlTemplate;
            context = context.replace("%title%", item.title != null ? item.title : "");
            if (item.items != null) {
                int count = item.multiCount > 0 ? item.multiCount : item.items.size();
                StringBuilder sb = new StringBuilder();
                sb.append("<reward_items multi_count=\"").append(count).append("\">");
                for (FastMap.Entry<Integer, Long> e = item.items.head(), end = item.items.tail(); (e = e.getNext()) != end;) {
                    sb.append("<item_id count=\"").append(e.getValue()).append("\">").append(e.getKey()).append("</item_id>");
                }
                sb.append("</reward_items>");
                context = context.replace("%reward_items%", sb.toString() + "");
            } else {
                context = context.replace("%reward_items%", "");
            }
            context = context.replace("%html%", item.html != null ? item.html : "");
            context = context.replace("%radio%", item.radio != null ? item.radio : "");

            HTMLService.sendData(player, item.uniqueId, context);
        }
    }

    private void moveToPosition(SurveyItem item, Player player, String response) {
        Pattern pattern = Pattern.compile("\"\\d{1}\"");
        Matcher matcher = pattern.matcher(response);
        if (!matcher.find()) {
            return;
        }
        if (!"\"1\"".equals(matcher.group(0))) {
            return;
        }
        if (item.action instanceof float[][]) {
            float position[][] = (float[][]) item.action;
            int raceId = player.getRace().getRaceId();
            TeleportService2.teleportTo(player, (int) position[raceId][0], position[raceId][1], position[raceId][2], position[raceId][3], (byte) position[raceId][4], TeleportAnimation.BEAM_ANIMATION);
        }
    }

    private static class SingletonHolder {

        protected static final AdvancedSurveyService instance = new AdvancedSurveyService();
    }

    public static final AdvancedSurveyService getInstance() {
        return SingletonHolder.instance;
    }
}
