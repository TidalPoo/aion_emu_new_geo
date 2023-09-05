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
package com.aionemu.gameserver.configs.administration;

import com.aionemu.commons.configuration.Property;

/**
 * @author ATracer
 */
public class DeveloperConfig {

    /**
     * if false - not spawns will be loaded
     */
    @Property(key = "gameserver.developer.spawn.enable", defaultValue = "true")
    public static boolean SPAWN_ENABLE;

    /**
     * if true - checks spawns being outside any known zones
     */
    @Property(key = "gameserver.developer.spawn.check", defaultValue = "false")
    public static boolean SPAWN_CHECK;

    /**
     * if set, adds specified stat bonus for items with random bonusess
     */
    @Property(key = "gameserver.developer.itemstat.id", defaultValue = "0")
    public static int ITEM_STAT_ID;

    /**
     * Display random number of item bonus for devs
     */
    @Property(key = "gameserver.developer.itemstat.rnd", defaultValue = "false")
    public static boolean SHOW_ITEM_STAT_RND;
    @Property(key = "gameserver.developer.visibility.distance", defaultValue = "95")
    public static float VISIBILITY_DISTANCE;
    @Property(key = "gameserver.developer.max.z.visibility.distance", defaultValue = "95")
    public static float MAX_Z_VISIBILITY_DISTANCE;
    @Property(key = "gameserver.gm-pvp", defaultValue = "true")
    public static boolean ENABLE_GM_PVP;

    @Property(key = "gameserver.player.settings.id", defaultValue = "100500")
    public static int CUSTOM_SETTINGS_ID;
}
