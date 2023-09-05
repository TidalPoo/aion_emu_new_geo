/*
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.services.html;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.cache.HTMLCache;
import com.aionemu.gameserver.cardinal.AddItemType;
import com.aionemu.gameserver.configs.main.LoggingConfig;
import com.aionemu.gameserver.dao.GuideDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.guide.Guide;
import com.aionemu.gameserver.model.templates.Guides.GuideTemplate;
import com.aionemu.gameserver.model.templates.Guides.SurveyTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTIONNAIRE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.SurveyService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Use this service to send raw html to the client.
 *
 * @author lhw, xTz
 */
public class HTMLService {

    private static final Logger log = LoggerFactory.getLogger("ITEM_HTML_LOG");

    public static String getHTMLTemplate(GuideTemplate template) {
        String context = HTMLCache.getInstance().getHTML("guideTemplate.xhtml");

        StringBuilder sb = new StringBuilder();
        sb.append("<reward_items multi_count='").append(template.getRewardCount()).append("'>\n");
        for (SurveyTemplate survey : template.getSurveys()) {
            sb.append("<item_id count='").append(survey.getCount()).append("'>").append(survey.getItemId())
                    .append("</item_id>\n");
        }
        sb.append("</reward_items>\n");
        context = context.replace("%reward%", sb);
        context = context.replace("%radio%", template.getSelect().isEmpty() ? " " : template.getSelect());
        context = context.replace("%html%", template.getMessage().isEmpty() ? " " : template.getMessage());
        context = context.replace("%rewardInfo%", template.getRewardInfo().isEmpty() ? " " : template.getRewardInfo());
        return context;
    }

    public static void pushSurvey(final String html) {
        final int messageId = IDFactory.getInstance().nextId();
        World.getInstance().doOnAllPlayers(new Visitor<Player>() {

            @Override
            public void visit(Player player) {
                sendData(player, messageId, html);
            }
        });
    }

    public static void showHTML(Player player, String html) {
        sendData(player, IDFactory.getInstance().nextId(), html);
    }

    public static void sendData(Player player, int messageId, String html) {
        byte packet_count = (byte) Math.ceil(html.length() / (Short.MAX_VALUE - 8) + 1);
        if (packet_count < 256) {
            for (byte i = 0; i < packet_count; i++) {
                try {
                    int from = i * (Short.MAX_VALUE - 8), to = (i + 1) * (Short.MAX_VALUE - 8);
                    if (from < 0) {
                        from = 0;
                    }
                    if (to > html.length()) {
                        to = html.length();
                    }
                    String sub = html.substring(from, to);
                    player.getClientConnection().sendPacket(new SM_QUESTIONNAIRE(messageId, i, packet_count, sub));
                } catch (Exception e) {
                    log.error("htmlservice.sendData", e);
                }
            }
        }
    }

    public static void sendGuideHtml(Player player) {
        if (player.getLevel() > 1) {
            GuideTemplate[] surveyTemplate = DataManager.GUIDE_HTML_DATA.getTemplatesFor(player.getPlayerClass(), player
                    .getRace(), player.getLevel());

            for (GuideTemplate template : surveyTemplate) {
                if (!template.isActivated()) {
                    continue;
                }
                int id = IDFactory.getInstance().nextId();
                sendData(player, id, getHTMLTemplate(template));
                DAOManager.getDAO(GuideDAO.class).saveGuide(id, player, template.getTitle());
            }
        }
    }

    public static void onPlayerLogin(Player player) {
        if (player == null) {
            return;
        }

        int count = player.getCountHtmlGuides();
        List<Guide> guides = DAOManager.getDAO(GuideDAO.class).loadGuides(player.getObjectId());
        for (Guide guide : guides) {
            GuideTemplate template = DataManager.GUIDE_HTML_DATA.getTemplateByTitle(guide.getTitle());
            if (template != null) {
                if (template.isActivated()) {
                    player.increaseCountGuide();
                    sendData(player, guide.getGuideId(), getHTMLTemplate(template));
                }
            } else {
                log.warn("Null guide template for title: {}", guide.getTitle());
            }
        }
        if (count > 0) {
            //PacketSendUtility.sendMessage(player, "Откройте окно голосования для получения бонусов за уровень!\nКол-во бонусов доступных для получения: " + count + " шт");
            PacketSendUtility.sendMessage(player, "[color:\u041e\u0442\u043a\u0440\u043e;0 255 0][color:\u0439\u0442\u0435;0 255 0] [color:\u043e\u043a\u043d\u043e;0 255 0] [color:\u0433\u043e\u043b\u043e\u0441;0 255 0][color:\u043e\u0432\u0430\u043d\u0438;0 255 0][color:\u044f \u0434\u043b\u044f;0 255 0] [color:\u043f\u043e\u043b\u0443\u0447;0 255 0][color:\u0435\u043d\u0438\u044f;0 255 0] [color:\u0431\u043e\u043d\u0443\u0441;0 255 0][color:\u043e\u0432 \u0437\u0430;0 255 0] [color:\u0443\u0440\u043e\u0432\u0435;0 255 0][color:\u043d\u044c;0 255 0]!\n[color:\u041a\u043e\u043b-\u0432;0 255 0][color:\u043e;0 255 0] [color:\u0431\u043e\u043d\u0443\u0441;0 255 0][color:\u043e\u0432 \u0434\u043e;0 255 0][color:\u0441\u0442\u0443\u043f\u043d;0 255 0][color:\u044b\u0445 \u0434\u043b;0 255 0][color:\u044f \u043f\u043e\u043b;0 255 0][color:\u0443\u0447\u0435\u043d\u0438;0 255 0][color:\u044f:;0 255 0] " + count + " [color:\u0448\u0442;0 255 0]");
        }
    }

    public static void getReward(Player player, int messageId, List<Integer> items) {
        if (player == null || messageId < 1) {
            return;
        }

        if (SurveyService.getInstance().isActive(player, messageId)) {
            return;
        }

        Guide guide = DAOManager.getDAO(GuideDAO.class).loadGuide(player.getObjectId(), messageId);

        if (guide != null) {
            GuideTemplate template = DataManager.GUIDE_HTML_DATA.getTemplateByTitle(guide.getTitle());
            if (template == null) {
                return;
            }

            if (items.size() > template.getRewardCount()) {
                return;
            }

            if (items.size() > player.getInventory().getFreeSlots()) {
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DICE_INVEN_ERROR);
                return;
            }
            List<SurveyTemplate> templates = null;
            if (template.getSurveys().size() != template.getRewardCount()) {
                templates = getSurveyTemplates(template.getSurveys(), items);
            } else {
                templates = template.getSurveys();
            }
            if (templates.isEmpty()) {
                return;
            }
            for (SurveyTemplate item : templates) {
                ItemService.addItem(player, item.getItemId(), item.getCount(), AddItemType.HTMLBONUS, null);
                if (LoggingConfig.LOG_ITEM) {
                    log.info(String.format("[ITEM] Item Guide ID/Count - %d/%d to player %s.", item.getItemId(), item.getCount(), player.getName()));
                }
            }
            DAOManager.getDAO(GuideDAO.class).deleteGuide(guide.getGuideId());
            player.uncreaseCountGuide();
            if (player.getCountHtmlGuides() > 0) {
                PacketSendUtility.sendMessage(player, "[color:\u041e\u0441\u0442\u0430\u043b;0 255 0][color:\u043e\u0441\u044c;0 255 0] [color:\u0431\u043e\u043d\u0443\u0441;0 255 0][color:\u043e\u0432;0 255 0] [color:\u0434\u043b\u044f;0 255 0] [color:\u043f\u043e\u043b\u0443\u0447;0 255 0][color:\u0435\u043d\u0438\u044f:;0 255 0] [color:" + player.getCountHtmlGuides() + ";255 255 0] [color:\u0448\u0442;0 255 0]");

            }
            items.clear();
        }
    }

    private static List<SurveyTemplate> getSurveyTemplates(List<SurveyTemplate> surveys, List<Integer> items) {
        List<SurveyTemplate> templates = new ArrayList<>();
        for (SurveyTemplate survey : surveys) {
            if (items.contains(survey.getItemId())) {
                templates.add(survey);
            }
        }
        return templates;
    }
}
