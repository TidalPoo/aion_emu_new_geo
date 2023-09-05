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

/**
 * @author ATracer
 * @modified Alex
 */
public class CustomConfig {

    /**
     * Enables challenge tasks
     */
    @Property(key = "gameserver.challenge.tasks.enabled", defaultValue = "false")
    public static boolean CHALLENGE_TASKS_ENABLED;

    /**
     * Show premium account details on login
     */
    @Property(key = "gameserver.premium.notify", defaultValue = "false")
    public static boolean PREMIUM_NOTIFY;

    @Property(key = "gameserver.display.rate", defaultValue = "false")
    public static boolean DISPLAY_RATE;

    /**
     * Enable announce when a player succes enchant item 15
     */
    @Property(key = "gameserver.enchant.announce.enable", defaultValue = "true")
    public static boolean ENABLE_ENCHANT_ANNOUNCE;

    /**
     * Enable speaking between factions
     */
    @Property(key = "gameserver.chat.factions.enable", defaultValue = "false")
    public static boolean SPEAKING_BETWEEN_FACTIONS;

    /**
     * Minimum level to use whisper
     */
    @Property(key = "gameserver.chat.whisper.level", defaultValue = "10")
    public static int LEVEL_TO_WHISPER;

    /**
     * Factions search mode
     */
    @Property(key = "gameserver.search.factions.mode", defaultValue = "false")
    public static boolean FACTIONS_SEARCH_MODE;

    /**
     * list gm when search players
     */
    @Property(key = "gameserver.search.gm.list", defaultValue = "false")
    public static boolean SEARCH_GM_LIST;

    /**
     * Minimum level to use search
     */
    @Property(key = "gameserver.search.player.level", defaultValue = "10")
    public static int LEVEL_TO_SEARCH;

    /**
     * Allow opposite factions to bind in enemy territories
     */
    @Property(key = "gameserver.cross.faction.binding", defaultValue = "false")
    public static boolean ENABLE_CROSS_FACTION_BINDING;

    /**
     * Enable second class change without quest
     */
    @Property(key = "gameserver.simple.secondclass.enable", defaultValue = "false")
    public static boolean ENABLE_SIMPLE_2NDCLASS;

    /**
     * Disable chain trigger rate (chain skill with 100% success)
     */
    @Property(key = "gameserver.skill.chain.triggerrate", defaultValue = "true")
    public static boolean SKILL_CHAIN_TRIGGERRATE;

    /**
     * Unstuck delay
     */
    @Property(key = "gameserver.unstuck.delay", defaultValue = "3600")
    public static int UNSTUCK_DELAY;

    /**
     * The price for using dye command
     */
    @Property(key = "gameserver.admin.dye.price", defaultValue = "1000000")
    public static int DYE_PRICE;

    /**
     * Disable prevention using old names with coupon & command
     */
    @Property(key = "gameserver.oldnames.coupon.disable", defaultValue = "false")
    public static boolean OLD_NAMES_COUPON_DISABLED;
    @Property(key = "gameserver.oldnames.command.disable", defaultValue = "true")
    public static boolean OLD_NAMES_COMMAND_DISABLED;

    /**
     * Friendlist size
     */
    @Property(key = "gameserver.friendlist.size", defaultValue = "90")
    public static int FRIENDLIST_SIZE;

    /**
     * Basic Quest limit size
     */
    @Property(key = "gameserver.basic.questsize.limit", defaultValue = "40")
    public static int BASIC_QUEST_SIZE_LIMIT;

    /**
     * Basic Quest limit size
     */
    @Property(key = "gameserver.basic.cubesize.limit", defaultValue = "12")
    public static int BASIC_CUBE_SIZE_LIMIT;

    /**
     * Npc Cube Expands limit size
     */
    @Property(key = "gameserver.npcexpands.limit", defaultValue = "5")
    public static int NPC_CUBE_EXPANDS_SIZE_LIMIT;

    /**
     * Enable instances mob always aggro player ignore level
     */
    @Property(key = "gameserver.instances.mob.aggro", defaultValue = "300080000,300090000,300060000")
    public static String INSTANCES_MOB_AGGRO;

    /**
     * Enable instances cooldown filtring
     */
    @Property(key = "gameserver.instances.cooldown.filter", defaultValue = "0")
    public static String INSTANCES_COOL_DOWN_FILTER;

    /**
     * Instances formula
     */
    @Property(key = "gameserver.instances.cooldown.rate", defaultValue = "1")
    public static int INSTANCES_RATE;

    /**
     * Enable Kinah cap
     */
    @Property(key = "gameserver.enable.kinah.cap", defaultValue = "false")
    public static boolean ENABLE_KINAH_CAP;

    /**
     * Kinah cap value
     */
    @Property(key = "gameserver.kinah.cap.value", defaultValue = "999999999")
    public static long KINAH_CAP_VALUE;

    /**
     * Enable AP cap
     */
    @Property(key = "gameserver.enable.ap.cap", defaultValue = "false")
    public static boolean ENABLE_AP_CAP;

    /**
     * AP cap value
     */
    @Property(key = "gameserver.ap.cap.value", defaultValue = "1000000")
    public static long AP_CAP_VALUE;

    /**
     * Enable EXP cap
     */
    @Property(key = "gameserver.enable.exp.cap", defaultValue = "false")
    public static boolean ENABLE_EXP_CAP;

    /**
     * EXP cap value
     */
    @Property(key = "gameserver.exp.cap.value", defaultValue = "48000000")
    public static long EXP_CAP_VALUE;

    /**
     * Enable no AP in mentored group.
     */
    @Property(key = "gameserver.noap.mentor.group", defaultValue = "false")
    public static boolean MENTOR_GROUP_AP;

    /**
     * .faction cfg
     */
    @Property(key = "gameserver.faction.free", defaultValue = "true")
    public static boolean FACTION_FREE_USE;

    @Property(key = "gameserver.faction.prices", defaultValue = "10000")
    public static int FACTION_USE_PRICE;

    @Property(key = "gameserver.faction.cmdchannel", defaultValue = "true")
    public static boolean FACTION_CMD_CHANNEL;

    @Property(key = "gameserver.faction.chatchannels", defaultValue = "false")
    public static boolean FACTION_CHAT_CHANNEL;

    /**
     * Time in milliseconds in which players are limited for killing one player
     */
    @Property(key = "gameserver.pvp.dayduration", defaultValue = "86400000")
    public static long PVP_DAY_DURATION;

    /**
     * Allowed Kills in configuered time for full AP. Move to separate config
     * when more pvp options.
     */
    @Property(key = "gameserver.pvp.maxkills", defaultValue = "5")
    public static int MAX_DAILY_PVP_KILLS;

    /**
     * Add a reward to player for pvp kills
     */
    @Property(key = "gameserver.kill.reward.enable", defaultValue = "false")
    public static boolean ENABLE_KILL_REWARD;

    /**
     * Kills needed for item reward
     */
    @Property(key = "gameserver.kills.needed1", defaultValue = "5")
    public static int KILLS_NEEDED1;
    @Property(key = "gameserver.kills.needed2", defaultValue = "10")
    public static int KILLS_NEEDED2;
    @Property(key = "gameserver.kills.needed3", defaultValue = "15")
    public static int KILLS_NEEDED3;

    /**
     * Item Rewards
     */
    @Property(key = "gameserver.item.reward1", defaultValue = "186000031")
    public static int REWARD1;
    @Property(key = "gameserver.item.reward2", defaultValue = "186000030")
    public static int REWARD2;
    @Property(key = "gameserver.item.reward3", defaultValue = "186000096")
    public static int REWARD3;

    /**
     * Show dialog id and quest id
     */
    @Property(key = "gameserver.dialog.showid", defaultValue = "true")
    public static boolean ENABLE_SHOW_DIALOGID;

    /**
     * Enable one kisk restriction
     */
    @Property(key = "gameserver.kisk.restriction.enable", defaultValue = "true")
    public static boolean ENABLE_KISK_RESTRICTION;

    @Property(key = "gameserver.dispute.enable", defaultValue = "true")
    public static boolean DISPUTE_ENABLED;
    @Property(key = "gameserver.dispute.random.chance", defaultValue = "50")
    public static int DISPUTE_RND_CHANCE;
    @Property(key = "gameserver.dispute.random.schedule", defaultValue = "0 0 2 ? * *")
    public static String DISPUTE_RND_SCHEDULE;
    @Property(key = "gameserver.dispute.fixed.schedule", defaultValue = "0 0 4 ? * *")
    public static String DISPUTE_FXD_SCHEDULE;

    @Property(key = "gameserver.rift.enable", defaultValue = "true")
    public static boolean RIFT_ENABLED;
    @Property(key = "gameserver.rift.duration", defaultValue = "1")
    public static int RIFT_DURATION;

    @Property(key = "gameserver.vortex.enable", defaultValue = "true")
    public static boolean VORTEX_ENABLED;
    @Property(key = "gameserver.vortex.brusthonin.schedule", defaultValue = "0 0 16 ? * SAT")
    public static String VORTEX_THEOBOMOS_SCHEDULE;
    @Property(key = "gameserver.vortex.theobomos.schedule", defaultValue = "0 0 16 ? * SUN")
    public static String VORTEX_BRUSTHONIN_SCHEDULE;
    @Property(key = "gameserver.vortex.duration", defaultValue = "1")
    public static int VORTEX_DURATION;

    @Property(key = "gameserver.serialkiller.enable", defaultValue = "true")
    public static boolean SERIALKILLER_ENABLED;
    @Property(key = "gameserver.serialkiller.handledworlds", defaultValue = "")
    public static String SERIALKILLER_WORLDS = "";
    @Property(key = "gameserver.serialkiller.kills.refresh", defaultValue = "5")
    public static int SERIALKILLER_REFRESH;
    @Property(key = "gameserver.serialkiller.kills.decrease", defaultValue = "1")
    public static int SERIALKILLER_DECREASE;
    @Property(key = "gameserver.serialkiller.level.diff", defaultValue = "10")
    public static int SERIALKILLER_LEVEL_DIFF;
    @Property(key = "gameserver.serialkiller.1st.rank.kills", defaultValue = "25")
    public static int KILLER_1ST_RANK_KILLS;
    @Property(key = "gameserver.serialkiller.2nd.rank.kills", defaultValue = "50")
    public static int KILLER_2ND_RANK_KILLS;

    @Property(key = "gameserver.reward.service.enable", defaultValue = "false")
    public static boolean ENABLE_REWARD_SERVICE;

    /**
     * Limits Config
     */
    @Property(key = "gameserver.limits.enable", defaultValue = "true")
    public static boolean LIMITS_ENABLED;

    @Property(key = "gameserver.limits.update", defaultValue = "0 0 0 * * ?")
    public static String LIMITS_UPDATE;

    @Property(key = "gameserver.chat.text.length", defaultValue = "150")
    public static int MAX_CHAT_TEXT_LENGHT;

    @Property(key = "gameserver.abyssxform.afterlogout", defaultValue = "false")
    public static boolean ABYSSXFORM_LOGOUT;

    @Property(key = "gameserver.instance.duel.enable", defaultValue = "true")
    public static boolean INSTANCE_DUEL_ENABLE;

    @Property(key = "gameserver.ride.restriction.enable", defaultValue = "true")
    public static boolean ENABLE_RIDE_RESTRICTION;

    @Property(key = "gameserver.quest.questdatakills", defaultValue = "true")
    public static boolean QUESTDATA_MONSTER_KILLS;

    @Property(key = "gameserver.commands.admin.dot.enable", defaultValue = "false")
    public static boolean ENABLE_ADMIN_DOT_COMMANDS;

    //new buff 2592000000
    @Property(key = "gameserver.player.bonus.time", defaultValue = "86400000")//12 hour = 43200000 millisec
    public static long PLAYER_BONUS_TIME;

    @Property(key = "gameserver.sequrity.member.buff", defaultValue = "true")
    public static boolean SECURITY_BUFF_ENABLE;

    //new
    @Property(key = "gameserver.forbidden_rus_words_in_context", defaultValue = "true")
    public static boolean ENABLE_FORBIDDEN_RUS_CONTEXT;

    @Property(key = "gameserver.forbidden_rus_words_in_text", defaultValue = "true")
    public static boolean ENABLE_FORBIDDEN_RUS_TEXT;

    @Property(key = "gameserver.auto.gag.chat", defaultValue = "false")
    public static boolean ENABLE_GAG_CHAT;

    @Property(key = "gameserver.pvp.enable", defaultValue = "false")
    public static boolean PVP_MODE;

    @Property(key = "gameserver.ffa.enable", defaultValue = "false")
    public static boolean FFA_MODE;

    @Property(key = "gameserver.events.enable", defaultValue = "true")
    public static boolean EVENT_MODE;

    @Property(key = "gameserver.spawn.enable", defaultValue = "false")
    public static boolean SPAWN_MODE;

    @Property(key = "gameserver.legion.bonus.kinah.warehouse", defaultValue = "10000000")// bonus * legion level
    public static int ADD_KINAH_LEVEL;

    @Property(key = "gameserver.convert.name.enable", defaultValue = "false")
    public static boolean ENABLE_CONVERT_NAME;

    @Property(key = "gameserver.kill.announce.all.map", defaultValue = "true")
    public static boolean ENABLE_ANNOUNCE_ALL_MAP;

    @Property(key = "gameserver.site.parse", defaultValue = "http://aion-light.ru/x5/")
    public static String SITE;

    @Property(key = "gameserver.forum.parse", defaultValue = "http://aionli.ru/index.php?app=core&module=search&do=viewNewContent&search_app=forums&sid=806ecdcbcb431d6bea9f9e4d093f101d&search_app_filters[forums][searchInKey]=&change=1&period=today&userMode=&followedItemsOnly=0")
    public static String FORUM;

    @Property(key = "gameserver.player.start.level", defaultValue = "1")
    public static int START_LEVEL;

    @Property(key = "gameserver.paket.npc.master.name.enable", defaultValue = "true")
    public static boolean ENABLE_NPC_MASTER_NAME;

    @Property(key = "gameserver.paket.npc.master.name", defaultValue = "Aion-Light")
    public static String NPC_MASTER_NAME;

    @Property(key = "gameserver.decompasible_items.open.box.time", defaultValue = "3000")
    public static int OPEN_BOX_TIME;

    @Property(key = "gameserver.decompasible_items.open.box.membership.level", defaultValue = "1")
    public static int OPEN_BOX_MEMBERSHIP_LEVEL;

    @Property(key = "gameserver.decompasible_items.open.box.membership.time", defaultValue = "300")
    public static int OPEN_BOX_MEMBERSHIP_TIME;

    @Property(key = "gameserver.enable.max.level", defaultValue = "true")
    public static boolean ENABLE_MAX_LEVEL;

    @Property(key = "gameserver.enable.add.cube.max", defaultValue = "true")
    public static boolean ENABLE_CUBE_MAX_LIMIT;

    @Property(key = "gameserver.decompasible_items.open.tune.time", defaultValue = "5000")// 5000
    public static int OPEN_TUNE_TIME;

    @Property(key = "gameserver.decompasible_items.open.tune.membership.level", defaultValue = "1")
    public static int OPEN_TUNE_MEMBERSHIP_LEVEL;

    @Property(key = "gameserver.decompasible_items.open.tune.membership.time", defaultValue = "5000")
    public static int OPEN_TUNE_MEMBERSHIP_TIME;

    @Property(key = "gameserver.verify.rank.limits", defaultValue = "1")
    public static byte VERIFY_RANK_LIMITS;

    @Property(key = "gameserver.enable.unlimit.fly.location", defaultValue = "true")
    public static boolean ENABLE_UNLIMIT_FLY_LOC;

    @Property(key = "gameserver.enable.ann.player.enter.world", defaultValue = "true")
    public static boolean ENABLE_ANNOUNCE_ALL_ON_PLAYER_ENTER_WORLD;

    @Property(key = "gameserver.enable.ann.player.leave.world", defaultValue = "true")
    public static boolean ENABLE_ANNOUNCE_ALL_ON_PLAYER_LEAVE_WORLD;

    @Property(key = "gameserver.enable.ffa.group", defaultValue = "false")
    public static boolean ENABLE_FFA_GROUP;

    @Property(key = "gameserver.enable.house.no.price", defaultValue = "true")
    public static boolean ENABLE_HOUSE_NO_PRICE;

    @Property(key = "gameserver.bonus.col.online", defaultValue = "1")
    public static int BONUS_TOLL;

    @Property(key = "gameserver.enable.auto.run.craft", defaultValue = "false")
    public static boolean ENABLE_AUTO_RUN_CRAFT;

    @Property(key = "gameserver.enable.ffa.legion", defaultValue = "false")
    public static boolean ENABLE_FFA_LEGION;

    @Property(key = "gameserver.max.hp.npc", defaultValue = "350000")
    public static int MAX_HP_NPC;

    @Property(key = "gameserver.enable.tp.fly", defaultValue = "true")
    public static boolean ENABLE_TP_FLY;
    @Property(key = "gameserver.http.mmotop.votes", defaultValue = "https://mmotop.ru/votes/0a49d39f97c12a5892ce961ab4a4b6c35b3a8786.txt")
    public static String HTTP_MMOTOP_VOTES;
    @Property(key = "gameserver.new.pvp", defaultValue = "false")
    public static boolean NEW_PVP_MODE;
    @Property(key = "gameserver.game_point_name", defaultValue = "Col")
    public static String COL;
    @Property(key = "gameserver.ffullddropplayer", defaultValue = "false")
    public static boolean ENABLE_FULL_DROP_PLAYER;

    @Property(key = "gameserver.aion_magadan", defaultValue = "false")
    public static boolean AION_MAGADAN;

    @Property(key = "gameserver.item.ffa_legion", defaultValue = "0")
    public static int ITEM_FFA_LEGION;
    @Property(key = "gameserver.item.ffa_legion_count", defaultValue = "1")
    public static int ITEM_FFA_LEGION_COUNT;

    @Property(key = "gameserver.item.ffa_group", defaultValue = "0")
    public static int ITEM_FFA_GROUP;
    @Property(key = "gameserver.item.ffa_group_count", defaultValue = "1")
    public static int ITEM_FFA_GROUP_COUNT;

    @Property(key = "gameserver.newpvp2", defaultValue = "true")
    public static boolean NEW_PVP_2;
    @Property(key = "gameserver.pvp.location.cron.minute", defaultValue = "60")
    public static int PVP_LOCATION_CRON_TIME;
    @Property(key = "gameserver.ffa.location.cron.minute", defaultValue = "120")
    public static int FFA_LOCATION_CRON_TIME;
    @Property(key = "gameserver.aioninfinite", defaultValue = "true")
    public static boolean AION_INFINITE;

    @Property(key = "gameserver.online.item.ely", defaultValue = "0")
    public static int ITEM_ELY_ONLINE;
    @Property(key = "gameserver.online.item.asmo", defaultValue = "0")
    public static int ITEM_ASMO_ONLINE;
    @Property(key = "gameserver.online.item.count", defaultValue = "0")
    public static int ITEM_COUNT_ONLINE;
    @Property(key = "gameserver.online.time", defaultValue = "60")
    public static int ONLINE_BONUS_TIME;
    @Property(key = "gameserver.mixfight", defaultValue = "true")
    public static boolean MIX_FIGHT;
    @Property(key = "gameserver.duel.message", defaultValue = "true")
    public static boolean ENABLE_DUEL_MESSAGE;
    @Property(key = "gameserver.duel.start.healhpmp", defaultValue = "true")
    public static boolean ENABLE_START_DUEL_HEAL_PLAYERS;
    @Property(key = "gameserver.manastone.remove.add", defaultValue = "true")
    public static boolean ENABLE_MANASTONE_ADD_TO_REMOVE;
    @Property(key = "gameserver.buy.private.shop.level", defaultValue = "30")
    public static byte BUY_PRIVATE_SHOP_LEVEL;
    @Property(key = "gameserver.express.bonus", defaultValue = "false")
    public static boolean EXPRESS_BONUS;
    @Property(key = "gameserver.auto-ban.radar", defaultValue = "false")
    public static boolean ENABLE_AUTO_BAN_RADAR;

    @Property(key = "gameserver.newplayer.zone", defaultValue = "true")
    public static boolean ENABLE_NEW_PLAYER_ZONE;

    @Property(key = "gameserver.mathobject.spawn.enable", defaultValue = "false")
    public static boolean ENABLE_MATHOBJECT_SPAWN;
    @Property(key = "gameserver.vote", defaultValue = "")
    public static String VOTE;
}
