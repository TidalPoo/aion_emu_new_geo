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
package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

/**
 *
 * @author xTz
 */
public class AutoGroupConfig {

    @Property(key = "gameserver.autogroup.enable", defaultValue = "true")
    public static boolean AUTO_GROUP_ENABLE;

    @Property(key = "gameserver.startTime.enable", defaultValue = "true")
    public static boolean START_TIME_ENABLE;

    @Property(key = "gameserver.dredgion.timer", defaultValue = "120")
    public static long DREDGION_TIMER;

    @Property(key = "gameserver.dredgion2.enable", defaultValue = "true")
    public static boolean DREDGION2_ENABLE;

    @Property(key = "gameserver.dredgion.time", defaultValue = "0 0 0,12,20 ? * *")
    public static String DREDGION_TIMES;

    @Property(key = "gameserver.kamar.timer", defaultValue = "120")
    public static long KAMAR_TIMER;

    @Property(key = "gameserver.kamar.enable", defaultValue = "true")
    public static boolean KAMAR_ENABLE;

    @Property(key = "gameserver.kamar.time", defaultValue = "0 0 0,20 ? * *")//MON,WED,SAT
    public static String KAMAR_TIMES;
}
