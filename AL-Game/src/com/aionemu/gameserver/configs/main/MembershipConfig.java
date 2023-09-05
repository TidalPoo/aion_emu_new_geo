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

import com.aionemu.commons.configuration.Property;

public class MembershipConfig {

    @Property(key = "gameserver.instances.title.requirement", defaultValue = "10")
    public static byte INSTANCES_TITLE_REQ;

    @Property(key = "gameserver.instances.race.requirement", defaultValue = "10")
    public static byte INSTANCES_RACE_REQ;

    @Property(key = "gameserver.instances.level.requirement", defaultValue = "10")
    public static byte INSTANCES_LEVEL_REQ;

    @Property(key = "gameserver.instances.group.requirement", defaultValue = "10")
    public static byte INSTANCES_GROUP_REQ;

    @Property(key = "gameserver.instances.quest.requirement", defaultValue = "10")
    public static byte INSTANCES_QUEST_REQ;

    @Property(key = "gameserver.instances.cooldown", defaultValue = "10")
    public static byte INSTANCES_COOLDOWN;

    @Property(key = "gameserver.store.wh.all", defaultValue = "10")
    public static byte STORE_WH_ALL;

    @Property(key = "gameserver.store.accountwh.all", defaultValue = "10")
    public static byte STORE_AWH_ALL;

    @Property(key = "gameserver.store.legionwh.all", defaultValue = "10")
    public static byte STORE_LWH_ALL;

    @Property(key = "gameserver.trade.all", defaultValue = "10")
    public static byte TRADE_ALL;

    @Property(key = "gameserver.disable.soulbind", defaultValue = "10")
    public static byte DISABLE_SOULBIND;

    @Property(key = "gameserver.remodel.all", defaultValue = "10")
    public static byte REMODEL_ALL;

    @Property(key = "gameserver.emotions.all", defaultValue = "10")
    public static byte EMOTIONS_ALL;

    @Property(key = "gameserver.quest.stigma.slot", defaultValue = "10")
    public static byte STIGMA_SLOT_QUEST;

    @Property(key = "gameserver.soulsickness.disable", defaultValue = "10")
    public static byte DISABLE_SOULSICKNESS;

    @Property(key = "gameserver.autolearn.skill", defaultValue = "10")
    public static byte SKILL_AUTOLEARN;

    @Property(key = "gameserver.autolearn.stigma", defaultValue = "10")
    public static byte STIGMA_AUTOLEARN;

    @Property(key = "gameserver.quest.limit.disable", defaultValue = "10")
    public static byte QUEST_LIMIT_DISABLED;

    @Property(key = "gameserver.titles.additional.enable", defaultValue = "10")
    public static byte TITLES_ADDITIONAL_ENABLE;

    @Property(key = "gameserver.character.additional.enable", defaultValue = "10")
    public static byte CHARACTER_ADDITIONAL_ENABLE;

    @Property(key = "gameserver.advanced.friendlist.enable", defaultValue = "10")
    public static byte ADVANCED_FRIENDLIST_ENABLE;

    @Property(key = "gameserver.character.additional.count", defaultValue = "8")
    public static byte CHARACTER_ADDITIONAL_COUNT;

    @Property(key = "gameserver.advanced.friendlist.size", defaultValue = "90")
    public static int ADVANCED_FRIENDLIST_SIZE;

    @Property(key = "gameserver.membership.tag.premium", defaultValue = "\uE02D %s")
    public static String TAG_PREMIUM;

    @Property(key = "gameserver.membership.tag.vip", defaultValue = "\uE02E %s")
    public static String TAG_VIP;

    @Property(key = "gameserver.membership.tag.player", defaultValue = "%s")
    public static String TAG_PLAYER;

    @Property(key = "gameserver.membership.tag.craft", defaultValue = "\uE00E %s")
    public static String TAG_CRAFT;

    @Property(key = "gameserver.membership.tag.ap", defaultValue = "\uE00A %s")
    public static String TAG_AP;

    @Property(key = "gameserver.membership.tag.collection", defaultValue = "\uE010 %s")
    public static String TAG_COLLECTION;

    @Property(key = "gameserver.membership.tag.display", defaultValue = "false")
    public static boolean PREMIUM_TAG_DIPLAY;

    @Property(key = "gameserver.edit_gender", defaultValue = "2")
    public static int EDIT_GENDER;

    @Property(key = "gameserver.edit_character", defaultValue = "2")
    public static int EDIT_CHARACTER;

    @Property(key = "gameserver.money.premium.30-day", defaultValue = "200")
    public static int MONEY_PREMIUM_30_DAY;

    @Property(key = "gameserver.money.premium.7-day", defaultValue = "65")
    public static int MONEY_PREMIUM_7_DAY;

    @Property(key = "gameserver.money.vip.30-day", defaultValue = "400")
    public static int MONEY_VIP_30_DAY;

    @Property(key = "gameserver.money.vip.7-day", defaultValue = "150")
    public static int MONEY_VIP_7_DAY;

    @Property(key = "gameserver.premium.day1", defaultValue = "3")
    public static long PREMIUM_DAY_1;

    @Property(key = "gameserver.premium.day2", defaultValue = "30")
    public static long PREMIUM_DAY_2;

    @Property(key = "gameserver.vip.day1", defaultValue = "3")
    public static long VIP_DAY_1;

    @Property(key = "gameserver.vip.day2", defaultValue = "30")
    public static long VIP_DAY_2;
}
