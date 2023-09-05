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
 * @author synchro2
 *
 */
public class WeddingsConfig {

    @Property(key = "gameserver.weddings.enable", defaultValue = "false")
    public static boolean WEDDINGS_ENABLE;

    @Property(key = "gameserver.weddings.gift.enable", defaultValue = "false")
    public static boolean WEDDINGS_GIFT_ENABLE;

    @Property(key = "gameserver.weddings.gift", defaultValue = "0")
    public static int WEDDINGS_GIFT;

    @Property(key = "gameserver.weddings.suit.enable", defaultValue = "false")
    public static boolean WEDDINGS_SUIT_ENABLE;

    @Property(key = "gameserver.weddings.suit", defaultValue = "110900415")
    public static String WEDDINGS_SUITS;

    @Property(key = "gameserver.weddings.membership", defaultValue = "0")
    public static byte WEDDINGS_MEMBERSHIP;

    @Property(key = "gameserver.weddings.same_sex", defaultValue = "false")
    public static boolean WEDDINGS_SAME_SEX;

    @Property(key = "gameserver.weddings.races", defaultValue = "false")
    public static boolean WEDDINGS_DIFF_RACES;

    @Property(key = "gameserver.weddings.kinah", defaultValue = "0")
    public static int WEDDINGS_KINAH;

    @Property(key = "gameserver.weddings.announce", defaultValue = "true")
    public static boolean WEDDINGS_ANNOUNCE;

    @Property(key = "gameserver.weddings.tag", defaultValue = "\uE020 %s")
    public static String TAG_WEDDING;

    @Property(key = "gameserver.aionlight.wedding.html_show", defaultValue = "true")
    public static boolean HTML_SHOW;

    @Property(key = "gameserver.aionlight.wedding.item_suit", defaultValue = "110900135")
    public static String ITEM_SUIT;

    @Property(key = "gameserver.new_weddings.enable", defaultValue = "true")
    public static boolean NEW_WEDDINGS_ENABLE;
}
