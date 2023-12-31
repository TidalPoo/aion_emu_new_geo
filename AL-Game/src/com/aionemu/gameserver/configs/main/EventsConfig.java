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
package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

/**
 * @author Rolandas
 */
public class EventsConfig {

    /**
     * Event Enabled
     */
    @Property(key = "gameserver.event.enable", defaultValue = "false")
    public static boolean EVENT_ENABLED;

    /**
     * 0 = no decoration 1 = Christmas 2 = Halloween 3 = Halloween and Christmas
     * 4 = Valentine 5 = Valentine and Christmas 6 = Valentine and Halloween 7 =
     * Valentine and Christmas and Halloween 8 = Valentine and Christmas and
     * Halloween 9 = Braxcafe 10 = Braxcafe and Christmas 11 = Braxcafe and
     * Halloween 12 = Braxcafe and Christmas and Halloween 13 = Braxcafe and
     * Valentine 14 = Braxcafe and Christmas and Valentine 15 = Braxcafe and
     * Halloween and Valentine 16 = Braxcafe and Christmas and Halloween and
     * Valentine
     */
    @Property(key = "gameserver.enable.decor", defaultValue = "0")
    public static int ENABLE_DECOR;

    /**
     * Event Rewarding Membership
     */
    @Property(key = "gameserver.event.membership", defaultValue = "0")
    public static int EVENT_REWARD_MEMBERSHIP;

    @Property(key = "gameserver.event.membership.rate", defaultValue = "false")
    public static boolean EVENT_REWARD_MEMBERSHIP_RATE;

    /**
     * Event Rewarding Period
     */
    @Property(key = "gameserver.event.period", defaultValue = "60")
    public static int EVENT_PERIOD;

    /**
     * Event Reward Values
     */
    @Property(key = "gameserver.event.item.elyos", defaultValue = "141000001")
    public static int EVENT_ITEM_ELYOS;

    @Property(key = "gameserver.event.item.asmo", defaultValue = "141000001")
    public static int EVENT_ITEM_ASMO;

    @Property(key = "gameserver.events.givejuice", defaultValue = "160009017")
    public static int EVENT_GIVEJUICE;

    @Property(key = "gameserver.events.givecake", defaultValue = "160010073")
    public static int EVENT_GIVECAKE;

    @Property(key = "gameserver.event.count", defaultValue = "1")
    public static int EVENT_ITEM_COUNT;

    @Property(key = "gameserver.event.service.enable", defaultValue = "false")
    public static boolean ENABLE_EVENT_SERVICE;

}
