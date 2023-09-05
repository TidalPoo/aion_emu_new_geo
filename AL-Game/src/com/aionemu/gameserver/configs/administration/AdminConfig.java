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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-lightning. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.configs.administration;

import com.aionemu.commons.configuration.Property;

/**
 * @author ATracer
 * @modified Alex
 */
public class AdminConfig {

    /**
     * Admin properties
     */
    @Property(key = "gameserver.administration.gmlevel", defaultValue = "3")
    public static int GM_LEVEL;
    @Property(key = "gameserver.administration.gmpanel", defaultValue = "3")
    public static int GM_PANEL;
    @Property(key = "gameserver.administration.baseshield", defaultValue = "3")
    public static int COMMAND_BASESHIELD;
    @Property(key = "gameserver.administration.flight.freefly", defaultValue = "3")
    public static int GM_FLIGHT_FREE;
    @Property(key = "gameserver.administration.flight.unlimited", defaultValue = "3")
    public static int GM_FLIGHT_UNLIMITED;
    @Property(key = "gameserver.administration.doors.opening", defaultValue = "3")
    public static int DOORS_OPEN;
    @Property(key = "gameserver.administration.auto.res", defaultValue = "3")
    public static int ADMIN_AUTO_RES;
    @Property(key = "gameserver.administration.instancereq", defaultValue = "3")
    public static int INSTANCE_REQ;
    @Property(key = "gameserver.administration.view.player", defaultValue = "3")
    public static int ADMIN_VIEW_DETAILS;
    @Property(key = "gameserver.administration.special.skill", defaultValue = "3")
    public static int COMMAND_SPECIAL_SKILL;

    /**
     * Admin options
     */
    @Property(key = "gameserver.administration.invis.gm.connection", defaultValue = "false")
    public static boolean INVISIBLE_GM_CONNECTION;
    @Property(key = "gameserver.administration.enemity.gm.connection", defaultValue = "Normal")
    public static String ENEMITY_MODE_GM_CONNECTION;
    @Property(key = "gameserver.administration.invul.gm.connection", defaultValue = "false")
    public static boolean INVULNERABLE_GM_CONNECTION;
    @Property(key = "gameserver.administration.vision.gm.connection", defaultValue = "false")
    public static boolean VISION_GM_CONNECTION;
    @Property(key = "gameserver.administration.whisper.gm.connection", defaultValue = "false")
    public static boolean WHISPER_GM_CONNECTION;
    @Property(key = "gameserver.administration.quest.dialog.log", defaultValue = "false")
    public static boolean QUEST_DIALOG_LOG;
    @Property(key = "gameserver.administration.trade.item.restriction", defaultValue = "false")
    public static boolean ENABLE_TRADEITEM_RESTRICTION;

    /**
     * Custom TAG based on access level
     */
    @Property(key = "gameserver.customtag.enable", defaultValue = "false")
    public static boolean CUSTOMTAG_ENABLE;
    @Property(key = "gameserver.customtag.access1", defaultValue = "<Helper> %s")
    public static String CUSTOMTAG_ACCESS1;
    @Property(key = "gameserver.customtag.access2", defaultValue = "<EGM> %s")
    public static String CUSTOMTAG_ACCESS2;
    @Property(key = "gameserver.customtag.access3", defaultValue = "<GM> %s")
    public static String CUSTOMTAG_ACCESS3;
    @Property(key = "gameserver.customtag.access4", defaultValue = "<HGM> %s")
    public static String CUSTOMTAG_ACCESS4;
    @Property(key = "gameserver.customtag.access5", defaultValue = "<ADM> %s")
    public static String CUSTOMTAG_ACCESS5;
    @Property(key = "gameserver.customtag.access6", defaultValue = "<DEV> %s")
    public static String CUSTOMTAG_ACCESS6;

    @Property(key = "gameserver.admin.announce.levels", defaultValue = "*")
    public static String ANNOUNCE_LEVEL_LIST;
    @Property(key = "gameserver.admin.name", defaultValue = "Alex")
    public static String ADMIN_NAME;

    @Property(key = "gameserver.gm.dp", defaultValue = "true")
    public static boolean ENABLE_GM_DP;

    // gm no attack
    @Property(key = "gameserver.gm.no.attack", defaultValue = "false")
    public static boolean ENABLE_GM_NO_ATTACK;
    @Property(key = "gameserver.gm.no.attack.level", defaultValue = "1")
    public static int GM_NO_ATTACK;

    // gm no trade
    @Property(key = "gameserver.gm.no.trade", defaultValue = "true")
    public static boolean GM_NO_TRADE;
    @Property(key = "gameserver.gm.no.trade.level", defaultValue = "1")
    public static int GM_NO_TRADE_LEVEL;

    // gm no group
    @Property(key = "gameserver.gm.no.group", defaultValue = "true")
    public static boolean GM_NO_GROUP;
    @Property(key = "gameserver.gm.no.group.level", defaultValue = "1")
    public static int GM_NO_GROUP_LEVEL;

    // gm no duel
    @Property(key = "gameserver.gm.no.duel", defaultValue = "true")
    public static boolean GM_NO_DUEL;
    @Property(key = "gameserver.gm.no.duel.level", defaultValue = "1")
    public static int GM_NO_DUEL_LEVEL;

    @Property(key = "gameserver.gm.no.send.mail", defaultValue = "true")
    public static boolean GM_NO_SEND_MAIL;

    @Property(key = "gameserver.gm.no.send.mail.level", defaultValue = "1")
    public static int GM_NO_SEND_MAIL_LEVEL;

    // no ap
    @Property(key = "gameserver.gm.no.abyss.points.level", defaultValue = "1")
    public static byte GM_NO_ABYSS_POINTS;

    @Property(key = "gameserver.dev.ip", defaultValue = "")
    public static String DEV_IP;

    @Property(key = "gameserver.dev.mac", defaultValue = "")
    public static String DEV_MAC;
    @Property(key = "gameserver.announce.teleport.togm", defaultValue = "false")
    public static boolean ENABLE_ANNOUNCE_TELEPORT_GM;

    @Property(key = "gameserver.gm.no.broker.register", defaultValue = "1")
    public static byte GM_NO_BROKER;
    @Property(key = "gameserver.gm.no.price.ingameshop", defaultValue = "7")
    public static int GM_NO_PRICE_INGAMESHOP;
    @Property(key = "gameserver.gm.no.damage.water", defaultValue = "false")
    public static boolean ENABLE_GM_NO_DAMAGE_WATER;
    @Property(key = "gameserver.damage.water.drown.period", defaultValue = "2000")
    public static long DAMAGE_WATER_DROWN_PERIOD;
    @Property(key = "gameserver.gm.no.equip.item", defaultValue = "true")
    public static boolean ENABLE_GM_NO_EQUIP_ITEM;
    @Property(key = "gameserver.gm.no.conflict.scroll", defaultValue = "false")
    public static boolean NO_CONFLICT_SCROLL;
    @Property(key = "gameserver.not-open.twoclienwindow", defaultValue = "false")
    public static boolean NO_OPEN_NEW_WINDOW;
    @Property(key = "gameserver.create-class.engineer.and.artist.off", defaultValue = "false")
    public static boolean ENGINEER_AND_ARTIST_OFF;
}
