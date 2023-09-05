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
package com.aionemu.gameserver.network.factories;

import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.AionPacketHandler;
import com.aionemu.gameserver.network.aion.clientpackets.CM_ABYSS_RANKING_LEGIONS;
import com.aionemu.gameserver.network.aion.clientpackets.CM_ABYSS_RANKING_PLAYERS;
import com.aionemu.gameserver.network.aion.clientpackets.CM_AFTER_RECONNECT;
import com.aionemu.gameserver.network.aion.clientpackets.CM_APPEARANCE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_ATTACK;
import com.aionemu.gameserver.network.aion.clientpackets.CM_AUTO_GROUP;
import com.aionemu.gameserver.network.aion.clientpackets.CM_BLOCK_ADD;
import com.aionemu.gameserver.network.aion.clientpackets.CM_BLOCK_DEL;
import com.aionemu.gameserver.network.aion.clientpackets.CM_BLOCK_SET_REASON;
import com.aionemu.gameserver.network.aion.clientpackets.CM_BONUS_TITLE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_BREAK_WEAPONS;
import com.aionemu.gameserver.network.aion.clientpackets.CM_BROKER_CANCEL_REGISTERED;
import com.aionemu.gameserver.network.aion.clientpackets.CM_BROKER_LIST;
import com.aionemu.gameserver.network.aion.clientpackets.CM_BROKER_REGISTERED;
import com.aionemu.gameserver.network.aion.clientpackets.CM_BROKER_SEARCH;
import com.aionemu.gameserver.network.aion.clientpackets.CM_BROKER_SETTLE_ACCOUNT;
import com.aionemu.gameserver.network.aion.clientpackets.CM_BROKER_SETTLE_LIST;
import com.aionemu.gameserver.network.aion.clientpackets.CM_BUY_BROKER_ITEM;
import com.aionemu.gameserver.network.aion.clientpackets.CM_BUY_ITEM;
import com.aionemu.gameserver.network.aion.clientpackets.CM_BUY_TRADE_IN_TRADE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CAPTCHA;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CASTSPELL;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CHALLENGE_ACCEPT;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CHALLENGE_CITY;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CHALLENGE_LEGION;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CHALLENGE_LIST;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CHANGE_CHANNEL;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CHARACTER_EDIT;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CHARACTER_LIST;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CHARACTER_PASSKEY;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CHARGE_ITEM;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CHAT_AUTH;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CHAT_GROUP_INFO;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CHAT_MESSAGE_PUBLIC;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CHAT_MESSAGE_WHISPER;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CHAT_PLAYER_INFO;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CHECK_MAIL_SIZE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CHECK_MAIL_SIZE2;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CHECK_NICKNAME;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CLIENT_COMMAND_ACCUSE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CLIENT_COMMAND_LOC;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CLIENT_COMMAND_ROLL;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CLOSE_DIALOG;
import com.aionemu.gameserver.network.aion.clientpackets.CM_COMPOSITE_STONES;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CRAFT;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CREATE_CHARACTER;
import com.aionemu.gameserver.network.aion.clientpackets.CM_CUSTOM_SETTINGS;
import com.aionemu.gameserver.network.aion.clientpackets.CM_DECOMPOSABLE_ITEMS;
import com.aionemu.gameserver.network.aion.clientpackets.CM_DELETE_CHARACTER;
import com.aionemu.gameserver.network.aion.clientpackets.CM_DELETE_ITEM;
import com.aionemu.gameserver.network.aion.clientpackets.CM_DELETE_MAIL;
import com.aionemu.gameserver.network.aion.clientpackets.CM_DELETE_QUEST;
import com.aionemu.gameserver.network.aion.clientpackets.CM_DIALOG_SELECT;
import com.aionemu.gameserver.network.aion.clientpackets.CM_DISTRIBUTION_SETTINGS;
import com.aionemu.gameserver.network.aion.clientpackets.CM_DUEL_REQUEST;
import com.aionemu.gameserver.network.aion.clientpackets.CM_EMOTION;
import com.aionemu.gameserver.network.aion.clientpackets.CM_ENTER_WORLD;
import com.aionemu.gameserver.network.aion.clientpackets.CM_EQUIP_ITEM;
import com.aionemu.gameserver.network.aion.clientpackets.CM_EXCHANGE_ADD_ITEM;
import com.aionemu.gameserver.network.aion.clientpackets.CM_EXCHANGE_ADD_KINAH;
import com.aionemu.gameserver.network.aion.clientpackets.CM_EXCHANGE_CANCEL;
import com.aionemu.gameserver.network.aion.clientpackets.CM_EXCHANGE_LOCK;
import com.aionemu.gameserver.network.aion.clientpackets.CM_EXCHANGE_OK;
import com.aionemu.gameserver.network.aion.clientpackets.CM_EXCHANGE_REQUEST;
import com.aionemu.gameserver.network.aion.clientpackets.CM_FAST_TRACK;
import com.aionemu.gameserver.network.aion.clientpackets.CM_FILE_VERIFY;
import com.aionemu.gameserver.network.aion.clientpackets.CM_FIND_GROUP;
import com.aionemu.gameserver.network.aion.clientpackets.CM_FRIEND_ADD;
import com.aionemu.gameserver.network.aion.clientpackets.CM_FRIEND_DEL;
import com.aionemu.gameserver.network.aion.clientpackets.CM_FRIEND_REQUEST;
import com.aionemu.gameserver.network.aion.clientpackets.CM_FRIEND_STATUS;
import com.aionemu.gameserver.network.aion.clientpackets.CM_FUSION_WEAPONS;
import com.aionemu.gameserver.network.aion.clientpackets.CM_GATHER;
import com.aionemu.gameserver.network.aion.clientpackets.CM_GET_HOUSE_BIDS;
import com.aionemu.gameserver.network.aion.clientpackets.CM_GET_MAIL_ATTACHMENT;
import com.aionemu.gameserver.network.aion.clientpackets.CM_GM_ACTION_FAIL;
import com.aionemu.gameserver.network.aion.clientpackets.CM_GM_BOOKMARK;
import com.aionemu.gameserver.network.aion.clientpackets.CM_GM_COMMAND_ACTION;
import com.aionemu.gameserver.network.aion.clientpackets.CM_GM_COMMAND_SEND;
import com.aionemu.gameserver.network.aion.clientpackets.CM_GODSTONE_SOCKET;
import com.aionemu.gameserver.network.aion.clientpackets.CM_GROUP_DATA_EXCHANGE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_GROUP_DISTRIBUTION;
import com.aionemu.gameserver.network.aion.clientpackets.CM_GROUP_LOOT;
import com.aionemu.gameserver.network.aion.clientpackets.CM_HOUSE_DECORATE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_HOUSE_EDIT;
import com.aionemu.gameserver.network.aion.clientpackets.CM_HOUSE_KICK;
import com.aionemu.gameserver.network.aion.clientpackets.CM_HOUSE_OPEN_DOOR;
import com.aionemu.gameserver.network.aion.clientpackets.CM_HOUSE_PAY_RENT;
import com.aionemu.gameserver.network.aion.clientpackets.CM_HOUSE_SCRIPT;
import com.aionemu.gameserver.network.aion.clientpackets.CM_HOUSE_SETTINGS;
import com.aionemu.gameserver.network.aion.clientpackets.CM_HOUSE_TELEPORT;
import com.aionemu.gameserver.network.aion.clientpackets.CM_HOUSE_TELEPORT_BACK;
import com.aionemu.gameserver.network.aion.clientpackets.CM_INSTANCE_INFO;
import com.aionemu.gameserver.network.aion.clientpackets.CM_INSTANCE_LEAVE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_INVITE_TO_GROUP;
import com.aionemu.gameserver.network.aion.clientpackets.CM_IN_GAME_SHOP_INFO;
import com.aionemu.gameserver.network.aion.clientpackets.CM_ITEM_REMODEL;
import com.aionemu.gameserver.network.aion.clientpackets.CM_L2AUTH_LOGIN_CHECK;
import com.aionemu.gameserver.network.aion.clientpackets.CM_LEGION;
import com.aionemu.gameserver.network.aion.clientpackets.CM_LEGION_MODIFY_EMBLEM;
import com.aionemu.gameserver.network.aion.clientpackets.CM_LEGION_SEND_EMBLEM;
import com.aionemu.gameserver.network.aion.clientpackets.CM_LEGION_SEND_EMBLEM_INFO;
import com.aionemu.gameserver.network.aion.clientpackets.CM_LEGION_TABS;
import com.aionemu.gameserver.network.aion.clientpackets.CM_LEGION_UPLOAD_EMBLEM;
import com.aionemu.gameserver.network.aion.clientpackets.CM_LEGION_UPLOAD_INFO;
import com.aionemu.gameserver.network.aion.clientpackets.CM_LEGION_WH_KINAH;
import com.aionemu.gameserver.network.aion.clientpackets.CM_LEVEL_READY;
import com.aionemu.gameserver.network.aion.clientpackets.CM_LEVEL_VERSION_SEND;
import com.aionemu.gameserver.network.aion.clientpackets.CM_LOOT_ITEM;
import com.aionemu.gameserver.network.aion.clientpackets.CM_MACRO_CREATE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_MACRO_DELETE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_MAC_ADDRESS;
import com.aionemu.gameserver.network.aion.clientpackets.CM_MANASTONE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_MARK_FRIENDLIST;
import com.aionemu.gameserver.network.aion.clientpackets.CM_MAY_LOGIN_INTO_GAME;
import com.aionemu.gameserver.network.aion.clientpackets.CM_MAY_QUIT;
import com.aionemu.gameserver.network.aion.clientpackets.CM_MEGAPHONE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_MOTION;
import com.aionemu.gameserver.network.aion.clientpackets.CM_MOVE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_MOVE_IN_AIR;
import com.aionemu.gameserver.network.aion.clientpackets.CM_MOVE_ITEM;
import com.aionemu.gameserver.network.aion.clientpackets.CM_OBJECT_SEARCH;
import com.aionemu.gameserver.network.aion.clientpackets.CM_OPEN_STATICDOOR;
import com.aionemu.gameserver.network.aion.clientpackets.CM_PET;
import com.aionemu.gameserver.network.aion.clientpackets.CM_PETITION;
import com.aionemu.gameserver.network.aion.clientpackets.CM_PET_EMOTE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_PING;
import com.aionemu.gameserver.network.aion.clientpackets.CM_PING_REQUEST;
import com.aionemu.gameserver.network.aion.clientpackets.CM_PLACE_BID;
import com.aionemu.gameserver.network.aion.clientpackets.CM_PLAYER_LISTENER;
import com.aionemu.gameserver.network.aion.clientpackets.CM_PLAYER_SEARCH;
import com.aionemu.gameserver.network.aion.clientpackets.CM_PLAYER_STATUS_INFO;
import com.aionemu.gameserver.network.aion.clientpackets.CM_PLAY_MOVIE_END;
import com.aionemu.gameserver.network.aion.clientpackets.CM_POWERBOOK;
import com.aionemu.gameserver.network.aion.clientpackets.CM_PRIVATE_STORE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_PRIVATE_STORE_NAME;
import com.aionemu.gameserver.network.aion.clientpackets.CM_QUESTIONNAIRE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_QUESTION_RESPONSE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_QUEST_SHARE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_QUIT;
import com.aionemu.gameserver.network.aion.clientpackets.CM_READ_EXPRESS_MAIL;
import com.aionemu.gameserver.network.aion.clientpackets.CM_READ_MAIL;
import com.aionemu.gameserver.network.aion.clientpackets.CM_RECIPE_DELETE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_RECONNECT_AUTH;
import com.aionemu.gameserver.network.aion.clientpackets.CM_REGISTER_BROKER_ITEM;
import com.aionemu.gameserver.network.aion.clientpackets.CM_REGISTER_HOUSE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_RELEASE_OBJECT;
import com.aionemu.gameserver.network.aion.clientpackets.CM_REMOVE_ALTERED_STATE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_REPLACE_ITEM;
import com.aionemu.gameserver.network.aion.clientpackets.CM_REPORT_PLAYER;
import com.aionemu.gameserver.network.aion.clientpackets.CM_RESTORE_CHARACTER;
import com.aionemu.gameserver.network.aion.clientpackets.CM_REVIVE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_SECURITY_TOKEN_REQUEST;
import com.aionemu.gameserver.network.aion.clientpackets.CM_SEND_MAIL;
import com.aionemu.gameserver.network.aion.clientpackets.CM_SET_NOTE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_SHOW_BLOCKLIST;
import com.aionemu.gameserver.network.aion.clientpackets.CM_SHOW_BRAND;
import com.aionemu.gameserver.network.aion.clientpackets.CM_SHOW_DIALOG;
import com.aionemu.gameserver.network.aion.clientpackets.CM_SHOW_FRIENDLIST;
import com.aionemu.gameserver.network.aion.clientpackets.CM_SHOW_MAP;
import com.aionemu.gameserver.network.aion.clientpackets.CM_SPLIT_ITEM;
import com.aionemu.gameserver.network.aion.clientpackets.CM_STAGGER;
import com.aionemu.gameserver.network.aion.clientpackets.CM_START_LOOT;
import com.aionemu.gameserver.network.aion.clientpackets.CM_STOP_TRAINING;
import com.aionemu.gameserver.network.aion.clientpackets.CM_SUBZONE_CHANGE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_SUMMON_ATTACK;
import com.aionemu.gameserver.network.aion.clientpackets.CM_SUMMON_CASTSPELL;
import com.aionemu.gameserver.network.aion.clientpackets.CM_SUMMON_COMMAND;
import com.aionemu.gameserver.network.aion.clientpackets.CM_SUMMON_EMOTION;
import com.aionemu.gameserver.network.aion.clientpackets.CM_SUMMON_MOVE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_TARGET_SELECT;
import com.aionemu.gameserver.network.aion.clientpackets.CM_TELEPORT_DONE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_TELEPORT_SELECT;
import com.aionemu.gameserver.network.aion.clientpackets.CM_TIME_CHECK;
import com.aionemu.gameserver.network.aion.clientpackets.CM_TITLE_SET;
import com.aionemu.gameserver.network.aion.clientpackets.CM_TOGGLE_SKILL_DEACTIVATE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_TUNE;
import com.aionemu.gameserver.network.aion.clientpackets.CM_UI_SETTINGS;
import com.aionemu.gameserver.network.aion.clientpackets.CM_UNK;
import com.aionemu.gameserver.network.aion.clientpackets.CM_UNK2;
import com.aionemu.gameserver.network.aion.clientpackets.CM_UNK3;
import com.aionemu.gameserver.network.aion.clientpackets.CM_UNK4;
import com.aionemu.gameserver.network.aion.clientpackets.CM_USE_CHARGE_SKILL;
import com.aionemu.gameserver.network.aion.clientpackets.CM_USE_HOUSE_OBJECT;
import com.aionemu.gameserver.network.aion.clientpackets.CM_USE_ITEM;
import com.aionemu.gameserver.network.aion.clientpackets.CM_USE_PACK_ITEM;
import com.aionemu.gameserver.network.aion.clientpackets.CM_VERSION_CHECK;
import com.aionemu.gameserver.network.aion.clientpackets.CM_VIEW_PLAYER_DETAILS;
import com.aionemu.gameserver.network.aion.clientpackets.CM_WINDSTREAM;

/**
 * This factory is responsible for creating {@link AionPacketHandler} object. It
 * also initializes created handler with a set of packet prototypes.<br>
 * Object of this classes uses <tt>Injector</tt> for injecting dependencies into
 * prototype objects.<br>
 * <br>
 *
 * @author Luno
 * @modified Alex
 */
public class AionPacketHandlerFactory {

    private final AionPacketHandler handler;

    public static AionPacketHandlerFactory getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Creates new instance of <tt>AionPacketHandlerFactory</tt><br>
     */
    public AionPacketHandlerFactory() {
        handler = new AionPacketHandler();

        // ////////////////////// 4.0 ///////////////////////
        addPacket(new CM_UI_SETTINGS(0xAD, State.IN_GAME));
        addPacket(new CM_MOTION(0x2EE, State.IN_GAME));
        addPacket(new CM_WINDSTREAM(0x119, State.IN_GAME));
        addPacket(new CM_STOP_TRAINING(0x2EB, State.IN_GAME));
        addPacket(new CM_REVIVE(0xD8, State.IN_GAME));
        addPacket(new CM_DUEL_REQUEST(0x135, State.IN_GAME));
        addPacket(new CM_CRAFT(0x120, State.IN_GAME));
        addPacket(new CM_QUESTION_RESPONSE(0xF5, State.IN_GAME));
        addPacket(new CM_SHOW_FRIENDLIST(0x1B9, State.IN_GAME));
        addPacket(new CM_OPEN_STATICDOOR(0xFE, State.IN_GAME));
        addPacket(new CM_SPLIT_ITEM(0x170, State.IN_GAME));
        addPacket(new CM_CUSTOM_SETTINGS(0xA3, State.IN_GAME));
        addPacket(new CM_PLAY_MOVIE_END(0x114, State.IN_GAME));
        addPacket(new CM_LEVEL_READY(0xAC, State.IN_GAME));
        addPacket(new CM_ENTER_WORLD(0xAF, State.AUTHED, State.IN_GAME));
        addPacket(new CM_PING(0xC3, State.AUTHED, State.IN_GAME));
        addPacket(new CM_VERSION_CHECK(0xC7, State.CONNECTED));
        addPacket(new CM_TIME_CHECK(0xD5, State.CONNECTED, State.AUTHED, State.IN_GAME));
        addPacket(new CM_LEGION_SEND_EMBLEM_INFO(0xD7, State.IN_GAME));
        addPacket(new CM_QUIT(0xDA, State.AUTHED, State.IN_GAME));
        addPacket(new CM_L2AUTH_LOGIN_CHECK(0x128, State.CONNECTED));
        addPacket(new CM_CHARACTER_LIST(0x129, State.AUTHED));
        addPacket(new CM_FRIEND_STATUS(0x14D, State.IN_GAME));
        addPacket(new CM_CREATE_CHARACTER(0x17E, State.AUTHED));
        addPacket(new CM_MAC_ADDRESS(0x190, State.CONNECTED, State.AUTHED, State.IN_GAME));
        addPacket(new CM_CHARACTER_PASSKEY(0x195, State.AUTHED));
        addPacket(new CM_MAY_LOGIN_INTO_GAME(0x19D, State.AUTHED, State.IN_GAME));
        addPacket(new CM_MOVE(0xF7, State.IN_GAME, State.AUTHED));
        addPacket(new CM_CASTSPELL(0xE4, State.IN_GAME));
        addPacket(new CM_CHAT_MESSAGE_PUBLIC(0xF2, State.IN_GAME));
        addPacket(new CM_EMOTION(0xC2, State.IN_GAME, State.AUTHED));
        addPacket(new CM_TITLE_SET(0x122, State.IN_GAME));
        addPacket(new CM_DELETE_ITEM(0x10B, State.IN_GAME));
        addPacket(new CM_SHOW_MAP(0x19B, State.IN_GAME));
        addPacket(new CM_CHAT_MESSAGE_WHISPER(0xF3, State.IN_GAME));
        addPacket(new CM_PING_REQUEST(0x10E, State.IN_GAME, State.AUTHED));
        addPacket(new CM_QUEST_SHARE(0x17B, State.IN_GAME));
        addPacket(new CM_PLAYER_SEARCH(0x166, State.IN_GAME));
        addPacket(new CM_DELETE_QUEST(0x117, State.IN_GAME));
        addPacket(new CM_ABYSS_RANKING_PLAYERS(0x193, State.IN_GAME));
        addPacket(new CM_ABYSS_RANKING_LEGIONS(0x109, State.IN_GAME));
        addPacket(new CM_PRIVATE_STORE(0x15E, State.IN_GAME));
        addPacket(new CM_USE_ITEM(0xF8, State.IN_GAME));
        addPacket(new CM_TARGET_SELECT(0xE6, State.IN_GAME, State.AUTHED));
        addPacket(new CM_SHOW_DIALOG(0xCB, State.IN_GAME));
        addPacket(new CM_CHECK_NICKNAME(0x174, State.AUTHED));
        addPacket(new CM_PRIVATE_STORE_NAME(0x15F, State.IN_GAME));
        addPacket(new CM_DELETE_CHARACTER(0x17F, State.AUTHED));
        addPacket(new CM_RESTORE_CHARACTER(0x17C, State.AUTHED));
        addPacket(new CM_MOVE_ITEM(0x173, State.IN_GAME));
        addPacket(new CM_MACRO_CREATE(0x176, State.IN_GAME));
        addPacket(new CM_MACRO_DELETE(0x177, State.IN_GAME));
        addPacket(new CM_FRIEND_ADD(0x136, State.IN_GAME));
        addPacket(new CM_GATHER(0xAA, State.IN_GAME));
        addPacket(new CM_INSTANCE_INFO(0x187, State.IN_GAME));
        addPacket(new CM_CLIENT_COMMAND_ROLL(0x102, State.IN_GAME));
        addPacket(new CM_START_LOOT(0x17D, State.IN_GAME));
        addPacket(new CM_LEGION_MODIFY_EMBLEM(0x112, State.IN_GAME));
        addPacket(new CM_CLOSE_DIALOG(0xC8, State.IN_GAME, State.AUTHED));
        addPacket(new CM_DIALOG_SELECT(0xC9, State.IN_GAME, State.AUTHED));
        addPacket(new CM_SET_NOTE(0x11D, State.IN_GAME));
        addPacket(new CM_FIND_GROUP(0x2E0, State.AUTHED, State.IN_GAME)); // TODO its sent also on AUTHED state
        addPacket(new CM_BUY_ITEM(0xCA, State.IN_GAME));
        addPacket(new CM_IN_GAME_SHOP_INFO(0x184, State.IN_GAME, State.AUTHED));
        addPacket(new CM_EQUIP_ITEM(0xF9, State.IN_GAME));
        addPacket(new CM_TELEPORT_SELECT(0x12B, State.IN_GAME));
        addPacket(new CM_LOOT_ITEM(0x172, State.IN_GAME));
        addPacket(new CM_QUESTIONNAIRE(0x154, State.IN_GAME));
        addPacket(new CM_ATTACK(0xE7, State.IN_GAME));
        addPacket(new CM_AUTO_GROUP(0x16F, State.IN_GAME));
        addPacket(new CM_PET(0xA9, State.IN_GAME));
        addPacket(new CM_BLOCK_ADD(0x179, State.IN_GAME));
        addPacket(new CM_TUNE(0x182, State.IN_GAME));
        addPacket(new CM_FRIEND_DEL(0x137, State.IN_GAME));
        addPacket(new CM_PET_EMOTE(0xA8, State.IN_GAME));
        addPacket(new CM_LEGION(0xC0, State.IN_GAME));
        addPacket(new CM_CHAT_AUTH(0x141, State.IN_GAME, State.AUTHED));
        addPacket(new CM_MARK_FRIENDLIST(0x101, State.IN_GAME));// Thx Rolandas
        addPacket(new CM_BLOCK_DEL(0x14E, State.IN_GAME));
        addPacket(new CM_SUMMON_MOVE(0x16C, State.IN_GAME));
        addPacket(new CM_SUMMON_COMMAND(0x15C, State.IN_GAME));
        addPacket(new CM_SUMMON_EMOTION(0x16D, State.IN_GAME));
        addPacket(new CM_SUMMON_ATTACK(0x162, State.IN_GAME));
        addPacket(new CM_SUMMON_CASTSPELL(0x160, State.IN_GAME));
        addPacket(new CM_LEGION_SEND_EMBLEM(0xF6, State.IN_GAME));
        addPacket(new CM_CHALLENGE_LIST(0x18F, State.IN_GAME));
        addPacket(new CM_GM_BOOKMARK(0xCC, State.IN_GAME));
        addPacket(new CM_CHECK_MAIL_SIZE(0x158, State.IN_GAME));
        addPacket(new CM_CHECK_MAIL_SIZE2(0x168, State.IN_GAME));
        addPacket(new CM_SEND_MAIL(0x15B, State.IN_GAME));
        addPacket(new CM_READ_MAIL(0x159, State.IN_GAME));
        addPacket(new CM_READ_EXPRESS_MAIL(0x165, State.IN_GAME));
        addPacket(new CM_DELETE_MAIL(0x12C, State.IN_GAME));
        addPacket(new CM_GET_MAIL_ATTACHMENT(0x12F, State.IN_GAME));
        addPacket(new CM_MOVE_IN_AIR(0xF4, State.IN_GAME));
        addPacket(new CM_VIEW_PLAYER_DETAILS(0x13B, State.IN_GAME));
        addPacket(new CM_INVITE_TO_GROUP(0x124, State.IN_GAME));
        addPacket(new CM_EXCHANGE_REQUEST(0x106, State.IN_GAME));
        addPacket(new CM_TELEPORT_DONE(0xD6, State.IN_GAME));
        addPacket(new CM_LEGION_WH_KINAH(0x2E3, State.IN_GAME));
        addPacket(new CM_CHARACTER_EDIT(0xAE, State.AUTHED));
        addPacket(new CM_EXCHANGE_ADD_ITEM(0x107, State.IN_GAME));
        addPacket(new CM_EXCHANGE_ADD_KINAH(0x105, State.IN_GAME));
        addPacket(new CM_EXCHANGE_LOCK(0x11A, State.IN_GAME));
        addPacket(new CM_EXCHANGE_CANCEL(0x118, State.IN_GAME));
        addPacket(new CM_EXCHANGE_OK(0x11B, State.IN_GAME));
        addPacket(new CM_GROUP_DISTRIBUTION(0x103, State.IN_GAME));
        addPacket(new CM_DISTRIBUTION_SETTINGS(0x19C, State.IN_GAME));
        addPacket(new CM_PLAYER_STATUS_INFO(0x127, State.IN_GAME, State.AUTHED));
        addPacket(new CM_SHOW_BRAND(0x148, State.IN_GAME));
        addPacket(new CM_GROUP_LOOT(0x19F, State.IN_GAME));
        addPacket(new CM_GROUP_DATA_EXCHANGE(0x116, State.IN_GAME));
        addPacket(new CM_MANASTONE(0x2ED, State.IN_GAME));
        addPacket(new CM_GODSTONE_SOCKET(0x132, State.IN_GAME));
        addPacket(new CM_FUSION_WEAPONS(0x161, State.IN_GAME));
        addPacket(new CM_ITEM_REMODEL(0x13D, State.IN_GAME));
        addPacket(new CM_TOGGLE_SKILL_DEACTIVATE(0xE5, State.IN_GAME));
        addPacket(new CM_HOUSE_TELEPORT_BACK(0x126, State.IN_GAME));
        addPacket(new CM_OBJECT_SEARCH(0xA2, State.IN_GAME));
        addPacket(new CM_HOUSE_OPEN_DOOR(0x1A5, State.IN_GAME));
        addPacket(new CM_HOUSE_SCRIPT(0xF1, State.IN_GAME));
        addPacket(new CM_HOUSE_TELEPORT(0x1B1, State.IN_GAME));
        addPacket(new CM_HOUSE_EDIT(0x115, State.IN_GAME));
        addPacket(new CM_USE_HOUSE_OBJECT(0x1A7, State.IN_GAME));
        addPacket(new CM_HOUSE_SETTINGS(0x2EC, State.IN_GAME));
        addPacket(new CM_HOUSE_KICK(0x2EF, State.IN_GAME));
        addPacket(new CM_GET_HOUSE_BIDS(0x1BD, State.AUTHED, State.IN_GAME));//TODO! AUTHED
        addPacket(new CM_HOUSE_PAY_RENT(0x1A6, State.IN_GAME));
        addPacket(new CM_REGISTER_HOUSE(0x1B2, State.IN_GAME));
        addPacket(new CM_HOUSE_DECORATE(0x2E2, State.IN_GAME));
        addPacket(new CM_RELEASE_OBJECT(0x1A4, State.IN_GAME));
        addPacket(new CM_PLACE_BID(0x1B0, State.IN_GAME));
        addPacket(new CM_RECIPE_DELETE(0x13C, State.IN_GAME));
        addPacket(new CM_BROKER_SETTLE_LIST(0x144, State.IN_GAME));
        addPacket(new CM_REGISTER_BROKER_ITEM(0x146, State.IN_GAME));
        addPacket(new CM_BROKER_CANCEL_REGISTERED(0x147, State.IN_GAME));
        addPacket(new CM_BROKER_REGISTERED(0x150, State.IN_GAME));
        addPacket(new CM_BUY_BROKER_ITEM(0x151, State.IN_GAME));
        addPacket(new CM_BROKER_LIST(0x152, State.IN_GAME));
        addPacket(new CM_BROKER_SEARCH(0x153, State.IN_GAME));
        addPacket(new CM_REMOVE_ALTERED_STATE(0xFA, State.IN_GAME));
        addPacket(new CM_MAY_QUIT(0xDB, State.AUTHED, State.IN_GAME));
        addPacket(new CM_REPORT_PLAYER(0x186, State.IN_GAME));
        addPacket(new CM_PLAYER_LISTENER(0xCF, State.IN_GAME, State.AUTHED));
        addPacket(new CM_REPLACE_ITEM(0x175, State.IN_GAME));
        addPacket(new CM_LEGION_TABS(0x11E, State.IN_GAME));
        addPacket(new CM_BONUS_TITLE(0x18C, State.IN_GAME));
        addPacket(new CM_BUY_TRADE_IN_TRADE(0x13F, State.IN_GAME));
        addPacket(new CM_BREAK_WEAPONS(0x196, State.IN_GAME));
        addPacket(new CM_CHARGE_ITEM(0x2E1, State.IN_GAME));
        addPacket(new CM_USE_CHARGE_SKILL(0x18D, State.IN_GAME));
        addPacket(new CM_RECONNECT_AUTH(0x19E, State.AUTHED, State.IN_GAME));
        addPacket(new CM_SHOW_BLOCKLIST(0x171, State.IN_GAME));
        addPacket(new CM_BLOCK_SET_REASON(0x14A, State.IN_GAME));
        addPacket(new CM_LEGION_UPLOAD_INFO(0x167, State.IN_GAME));
        addPacket(new CM_LEGION_UPLOAD_EMBLEM(0x164, State.IN_GAME));
        addPacket(new CM_BROKER_SETTLE_ACCOUNT(0x145, State.IN_GAME));
        addPacket(new CM_INSTANCE_LEAVE(0xC1, State.IN_GAME));
        addPacket(new CM_APPEARANCE(0x198, State.IN_GAME));
        addPacket(new CM_CAPTCHA(0xA1, State.IN_GAME));
        addPacket(new CM_CHANGE_CHANNEL(0x143, State.IN_GAME));
        addPacket(new CM_CHAT_PLAYER_INFO(0xCE, State.IN_GAME));
        addPacket(new CM_CHAT_GROUP_INFO(0x110, State.IN_GAME));
        addPacket(new CM_DECOMPOSABLE_ITEMS(0x183, State.IN_GAME));
        addPacket(new CM_COMPOSITE_STONES(0x197, State.IN_GAME));
        addPacket(new CM_SUBZONE_CHANGE(0x17A, State.IN_GAME));
        addPacket(new CM_STAGGER(0x012A, State.IN_GAME));
        addPacket(new CM_UNK(0x10F, State.IN_GAME, State.CONNECTED, State.AUTHED));//TODO
        addPacket(new CM_SECURITY_TOKEN_REQUEST(0x1B8, State.IN_GAME));
        addPacket(new CM_MEGAPHONE(0x180, State.IN_GAME));
        addPacket(new CM_AFTER_RECONNECT(0xF4C2, State.CONNECTED));

        // ////////////////////// 3.0 ///////////////////////
        // /////////////////// GM PACKET ////////////////////

        //addPacket(new CM_ADMIN_PANEL(0x149, State.IN_GAME));
        // //////////////////////////////////////////////////
	/*0x00FD
         * addPacket(new CM_UNK(0x136, State.IN_GAME));
         */
        // =============== TEST ================
        addPacket(new CM_UNK2(0x00C5, State.IN_GAME, State.AUTHED));
        addPacket(new CM_UNK3(0x2F90, State.CONNECTED));
        addPacket(new CM_UNK4(0x131, State.IN_GAME));
        // addPacket(new CM_UNK5(0x0132, State.IN_GAME)); // GODSTONE SOCKET
        // ============= TESTED ==============
        addPacket(new CM_FAST_TRACK(0x1BF, State.IN_GAME)); //4.3

        //addPacket(new CM_CHECK_MAIL_LIST(0x1BC, State.IN_GAME)); //4.5
        addPacket(new CM_USE_PACK_ITEM(0x1B7, State.IN_GAME)); //4.5
        addPacket(new CM_CLIENT_COMMAND_LOC(0x0121, State.IN_GAME)); //4.3
        //addPacket(new CM_CLIENT_COMMAND_LOC(0x0121, State.IN_GAME)); //4.3
        addPacket(new CM_CLIENT_COMMAND_ACCUSE(0x0185, State.IN_GAME)); //4.3

        addPacket(new CM_PETITION(0x00FD, State.IN_GAME));// PETTITION
        addPacket(new CM_POWERBOOK(0x0133, State.IN_GAME, State.AUTHED)); //4.3
        addPacket(new CM_GM_COMMAND_SEND(0xCD, State.IN_GAME));
        addPacket(new CM_GM_ACTION_FAIL(0xD4, State.IN_GAME)); //4.3
        addPacket(new CM_GM_COMMAND_ACTION(0x149, State.IN_GAME));

        addPacket(new CM_FILE_VERIFY(0x10C, State.IN_GAME));
        addPacket(new CM_FRIEND_REQUEST(0x018E, State.IN_GAME));

        addPacket(new CM_LEVEL_VERSION_SEND(0xD9, State.IN_GAME));
        addPacket(new CM_CHALLENGE_CITY(0x111bd, State.IN_GAME));
        addPacket(new CM_CHALLENGE_LEGION(0xF422, State.IN_GAME));
        addPacket(new CM_CHALLENGE_ACCEPT(0x1DC8, State.IN_GAME));
        // /////////////////// GAMEGUARD ////////////////////
        //addPacket(new CM_GAME_GUARD(0x10F, State.IN_GAME)); //4.5
        // /////////////////// GM PACKET ////////////////////
        //addPacket(new CM_GM_ACTION_FAIL(0xA8, State.IN_GAME)); //4.5
        //addPacket(new CM_GM_COMMAND_ACTION(0x19D, State.IN_GAME)); //4.5
        // /////////////////// UNKNOWN P ////////////////////
        //addPacket(new CM_ADMIN_PANEL(0x149, State.IN_GAME));
        //addPacket(new CM_SELECT_DECOMPOSABLE(0x183, State.IN_GAME)); //4.5
        // /////////////////// REMOVED P ////////////////////
        //addPacket(new CM_DRAWING_TOOL(0x116, State.IN_GAME)); // Removed in 4.5
    }

    public AionPacketHandler getPacketHandler() {
        return handler;
    }

    private void addPacket(AionClientPacket prototype) {
        handler.addPacketPrototype(prototype);
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final AionPacketHandlerFactory instance = new AionPacketHandlerFactory();
    }
}
