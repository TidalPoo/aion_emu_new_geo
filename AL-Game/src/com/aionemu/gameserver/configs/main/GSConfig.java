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
package com.aionemu.gameserver.configs.main;

/*
 * @author ATracer
 */
import com.aionemu.commons.configuration.Property;
import java.util.Calendar;

public class GSConfig {

    /**
     * Gameserver
     */
    @Property(key = "gameserver.start.time", defaultValue = "29.11.2014")
    public static String SERVER_START_TIME;

    /* Server Country Code */
    @Property(key = "gameserver.country.code", defaultValue = "1")
    public static int SERVER_COUNTRY_CODE;

    /* Server Credits Name */
    @Property(key = "gameserver.name", defaultValue = "mmo-sao.ru")
    public static String SERVER_NAME;
    /* Server Credits Name */
    @Property(key = "gameserver.worldname", defaultValue = "Aion Light")
    public static String SERVER_WORLDNAME;
    /* Server Language */
    @Property(key = "gameserver.language", defaultValue = "ru")
    public static String SERVER_LANGUAGE;
    /* Server Announce Info Display */
    @Property(key = "gameserver.announceinfo.enable", defaultValue = "false")
    public static boolean SERVER_ANNOUNCE_INFO;
    /* Players Max Level */
    @Property(key = "gameserver.players.max.level", defaultValue = "65")
    public static int PLAYER_MAX_LEVEL;

    /* Time Zone name (used for events & timed spawns) */
    @Property(key = "gameserver.timezone", defaultValue = "")
    public static String TIME_ZONE_ID = Calendar.getInstance().getTimeZone().getID();

    /* Enable connection with CS (ChatServer) */
    @Property(key = "gameserver.chatserver.enable", defaultValue = "false")
    public static boolean ENABLE_CHAT_SERVER;

    /* Server MOTD Display revision */
    @Property(key = "gameserver.revisiondisplay.enable", defaultValue = "false")
    public static boolean SERVER_MOTD_DISPLAYREV;

    /**
     * Character creation
     */
    @Property(key = "gameserver.character.creation.mode", defaultValue = "0")
    public static int CHARACTER_CREATION_MODE;

    @Property(key = "gameserver.character.limit.count", defaultValue = "8")
    public static int CHARACTER_LIMIT_COUNT;

    @Property(key = "gameserver.character.faction.limitation.mode", defaultValue = "0")
    public static int CHARACTER_FACTION_LIMITATION_MODE;

    @Property(key = "gameserver.ratio.limitation.enable", defaultValue = "false")
    public static boolean ENABLE_RATIO_LIMITATION;

    @Property(key = "gameserver.ratio.min.value", defaultValue = "60")
    public static int RATIO_MIN_VALUE;

    @Property(key = "gameserver.ratio.min.required.level", defaultValue = "10")
    public static int RATIO_MIN_REQUIRED_LEVEL;

    @Property(key = "gameserver.ratio.min.characters_count", defaultValue = "50")
    public static int RATIO_MIN_CHARACTERS_COUNT;

    @Property(key = "gameserver.ratio.high_player_count.disabling", defaultValue = "500")
    public static int RATIO_HIGH_PLAYER_COUNT_DISABLING;

    /**
     * Misc
     */
    @Property(key = "gameserver.character.reentry.time", defaultValue = "20")
    public static int CHARACTER_REENTRY_TIME;

    @Property(key = "gameserver.restart.time", defaultValue = "00:00:00")
    public static String RESTART_SERVER;

    @Property(key = "gameserver.execute.params", defaultValue = "10 60 120")
    public static String RESTART_SERVER_EXEC_PARAMS;

    @Property(key = "gameserver.type.params", defaultValue = "RESTART")
    public static String TASK_SERVER_TYPE_PARAMS;

    @Property(key = "gameserver.active_anticheat.enable", defaultValue = "true")
    public static boolean ENABLE_ACTIVE_ANTICHEAT;
    @Property(key = "gameserver.idfacroty.error", defaultValue = "true")
    public static boolean ENABLE_ERROR_ID_FACTORY;
}
