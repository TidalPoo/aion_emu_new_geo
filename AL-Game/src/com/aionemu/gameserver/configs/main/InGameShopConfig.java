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
 * @author xTz
 * @modified Alex
 */
public class InGameShopConfig {

    /**
     * Enable in game shop
     */
    @Property(key = "gameserver.ingameshop.enable", defaultValue = "false")
    public static boolean ENABLE_IN_GAME_SHOP;

    /**
     * Enable gift system between factions
     */
    @Property(key = "gameserver.ingameshop.gift", defaultValue = "false")
    public static boolean ENABLE_GIFT_OTHER_RACE;

    @Property(key = "gameserver.ingameshop.allow.gift", defaultValue = "true")
    public static boolean ALLOW_GIFTS;
    //show
    @Property(key = "gameserver.ingameshop.disable.show-item.other.race", defaultValue = "true")
    public static boolean DISABLE_SHOW_ITEM_OTHER_RACE;
    @Property(key = "gameserver.ingameshop.disable.show-item.other.class", defaultValue = "true")
    public static boolean DISABLE_SHOW_ITEM_OTHER_CLASS;
    @Property(key = "gameserver.ingameshop.disable.show-item.other.gender", defaultValue = "true")
    public static boolean DISABLE_SHOW_ITEM_OTHER_GENDER;
    @Property(key = "gameserver.ingameshop.disable.show-item.other.armor", defaultValue = "true")
    public static boolean DISABLE_SHOW_ITEM_OTHER_ARMOR;
    @Property(key = "gameserver.ingameshop.disable.show-item.other.weapon", defaultValue = "true")
    public static boolean DISABLE_SHOW_ITEM_OTHER_WEAPON;
    //buy
    @Property(key = "gameserver.ingameshop.disable.buy-item.other.race", defaultValue = "true")
    public static boolean DISABLE_BUY_ITEM_OTHER_RACE;
    @Property(key = "gameserver.ingameshop.disable.buy-item.other.class", defaultValue = "true")
    public static boolean DISABLE_BUY_ITEM_OTHER_CLASS;
    @Property(key = "gameserver.ingameshop.disable.buy-item.other.gender", defaultValue = "true")
    public static boolean DISABLE_BUY_ITEM_OTHER_GENDER;
    @Property(key = "gameserver.ingameshop.disable.buy-item.other.armor", defaultValue = "true")
    public static boolean DISABLE_BUY_ITEM_OTHER_ARMOR;
    @Property(key = "gameserver.ingameshop.disable.buy-item.other.weapon", defaultValue = "true")
    public static boolean DISABLE_BUY_ITEM_OTHER_WEAPON;
    @Property(key = "gameserver.ingameshop.show.type", defaultValue = "1")
    public static int SHOW_TYPE;
    @Property(key = "gameserver.ingameshop.membership.show.type", defaultValue = "1")
    public static int SHOP_MEMBERSHIP_TYPE;
}
