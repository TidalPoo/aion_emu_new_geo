/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.services;

import com.aionemu.gameserver.cardinal.AddItemType;
import com.aionemu.gameserver.cardinal.SkillsEnterWorld;
import com.aionemu.gameserver.configs.administration.DeveloperConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_UPDATE_PLAYER_APPEARANCE;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.custom.NewPvP;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author ATracer, sweetkr
 */
public class ClassChangeService {

    //TODO dialog enum
    /**
     * @param player
     */
    public static void showClassChangeDialog(Player player) {
        if (CustomConfig.ENABLE_SIMPLE_2NDCLASS) {
            PlayerClass playerClass = player.getPlayerClass();
            Race playerRace = player.getRace();
            if (player.getLevel() >= 9 && playerClass.isStartingClass()) {
                if (playerRace == Race.ELYOS) {
                    switch (playerClass) {
                        case WARRIOR:
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 2375, 1006));
                            break;
                        case SCOUT:
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 2716, 1006));
                            break;
                        case MAGE:
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 3057, 1006));
                            break;
                        case PRIEST:
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 3398, 1006));
                            break;
                        case ENGINEER:
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 3825, 1006));
                            break;
                        case ARTIST:
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 4080, 1006));
                            break;
                    }
                } else if (playerRace == Race.ASMODIANS) {
                    switch (playerClass) {
                        case WARRIOR:
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 3057, 2008));
                            break;
                        case SCOUT:
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 3398, 2008));
                            break;
                        case MAGE:
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 3739, 2008));
                            break;
                        case PRIEST:
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 4080, 2008));
                            break;
                        case ENGINEER:
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 3612, 2008));
                            break;
                        case ARTIST:
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 3910, 2008));
                            break;
                    }
                }
            }
        }
    }

    /**
     * @param player
     * @param dialogId
     */
    public static void changeClassToSelection(final Player player, final int dialogId) {
        if (dialogId == 0) {
            return;
        }
        Race playerRace = player.getRace();
        if (CustomConfig.ENABLE_SIMPLE_2NDCLASS) {
            int[] items = null;
            if (playerRace == Race.ELYOS) {
                switch (dialogId) {
                    case 2376:
                        setClass(player, PlayerClass.getPlayerClassById((byte) 1));
                        items = new int[]{101300587};
                        break;
                    case 2461:
                        setClass(player, PlayerClass.getPlayerClassById((byte) 2));
                        items = new int[]{100900615};
                        break;
                    case 2717:
                        setClass(player, PlayerClass.getPlayerClassById((byte) 4));
                        items = new int[]{100200731, 100000805};
                        break;
                    case 2802:
                        setClass(player, PlayerClass.getPlayerClassById((byte) 5));
                        items = new int[]{101700650};
                        break;
                    case 3058:
                        setClass(player, PlayerClass.getPlayerClassById((byte) 7));
                        items = new int[]{100500625};
                        break;
                    case 3143:
                        setClass(player, PlayerClass.getPlayerClassById((byte) 8));
                        items = new int[]{100600684};
                        break;
                    case 3399:
                        setClass(player, PlayerClass.getPlayerClassById((byte) 10));
                        items = new int[]{100100604, 115000893};
                        break;
                    case 3484:
                        setClass(player, PlayerClass.getPlayerClassById((byte) 11));
                        items = new int[]{101500628};
                        break;
                    case 3740:
                        setClass(player, PlayerClass.getPlayerClassById((byte) 14));
                        items = new int[]{101800594, 101800594};
                        break;
                    case 4081:
                        setClass(player, PlayerClass.getPlayerClassById((byte) 16));
                        items = new int[]{102000622};
                        break;
                }

                completeQuest(player, 1006);
                completeQuest(player, 1007);

                // Stigma Quests Elyos
                if (player.havePermission(MembershipConfig.STIGMA_SLOT_QUEST)) {
                    completeQuest(player, 1929);
                }
            } else if (playerRace == Race.ASMODIANS) {
                switch (dialogId) {
                    case 3058:
                        setClass(player, PlayerClass.getPlayerClassById((byte) 1));
                        items = new int[]{101300587};
                        break;
                    case 3143:
                        setClass(player, PlayerClass.getPlayerClassById((byte) 2));
                        items = new int[]{100900615};
                        break;
                    case 3399:
                        setClass(player, PlayerClass.getPlayerClassById((byte) 4));
                        items = new int[]{100200731, 100000805};
                        break;
                    case 3484:
                        setClass(player, PlayerClass.getPlayerClassById((byte) 5));
                        items = new int[]{101700650};
                        break;
                    case 3740:
                        setClass(player, PlayerClass.getPlayerClassById((byte) 7));
                        items = new int[]{100500625};
                        break;
                    case 3825:
                        setClass(player, PlayerClass.getPlayerClassById((byte) 8));
                        items = new int[]{100600684};
                        break;
                    case 4081:
                        setClass(player, PlayerClass.getPlayerClassById((byte) 10));
                        items = new int[]{100100604, 115000893};
                        break;
                    case 4166:
                        setClass(player, PlayerClass.getPlayerClassById((byte) 11));
                        items = new int[]{101500628};
                        break;
                    case 3570:
                        setClass(player, PlayerClass.getPlayerClassById((byte) 14));
                        items = new int[]{101800594, 101800594};
                        break;
                    case 3911:
                        setClass(player, PlayerClass.getPlayerClassById((byte) 16));
                        items = new int[]{102000622};
                        break;
                }

                //Optimate @Enomine
                completeQuest(player, 2008);
                completeQuest(player, 2009);

                // Stigma Quests Asmodians
                if (player.havePermission(MembershipConfig.STIGMA_SLOT_QUEST)) {
                    completeQuest(player, 2900);
                }
            }
            // set level 65 if enable config parameter
            if (CustomConfig.ENABLE_MAX_LEVEL && player.getLevel() < GSConfig.PLAYER_MAX_LEVEL) {
                player.getCommonData().setChangeClass(true);
                player.getCommonData().setLevel(GSConfig.PLAYER_MAX_LEVEL);
                player.getCommonData().setChangeClass(false);
                SkillLearnService.addNewSkills(player);
                if (CustomConfig.NEW_PVP_MODE) {
                    NewPvP.addItem(player);
                }
            }
            ThreadPoolManager.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    SkillLearnService.addMissingSkills(player);
                }
            }, 3 * 1000);
            if (DeveloperConfig.ENABLE_GM_PVP) {
                //CardinalManager.ps(player);
                if (items != null) {
                    for (int itemId : items) {
                        //"SAO-PROJECT"
                        ItemService.addItem(player, itemId, 1, AddItemType.STARTEQUIP, GSConfig.SERVER_NAME);
                        Item it = player.getInventory().getFirstItemByItemId(itemId);
                        if (it != null) {
                            it.setEnchantLevel(15);
                            player.getEquipment().equipItem(it.getObjectId(), it.getItemTemplate().getItemSlot());
                        }
                    }
                    PacketSendUtility.broadcastPacket(player, new SM_UPDATE_PLAYER_APPEARANCE(player.getObjectId(),
                            player.getEquipment().getEquippedForApparence()), true);
                }
                if (player.getMembership() == 2) {
                    for (int i = 0; i < SkillsEnterWorld.skillVip.length; i++) {
                        player.getSkillList().addSkill(player, SkillsEnterWorld.skillVip[i], SkillsEnterWorld.skillVipLevel[i]);
                    }
                } else {
                    for (int i = 0; i < SkillsEnterWorld.skillPlayer.length; i++) {
                        player.getSkillList().addSkill(player, SkillsEnterWorld.skillPlayer[i], SkillsEnterWorld.skillPlayerLevel[i]);
                    }
                }
            }
        }
    }

    public static void completeQuest(Player player, int questId) {
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            qs = new QuestState(questId, QuestStatus.COMPLETE, 0, 0, null, 0, null);
            player.getQuestStateList().addQuest(questId, qs);
        } else {
            qs.setStatus(QuestStatus.COMPLETE);
        }
        qs.setCompleteCount(qs.getCompleteCount() + 1);
        if (qs.isQuestCheat()) {
            //TODO BANNED!
        }
        PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(questId, qs.getStatus().value(), 0));
        PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(questId, qs.getStatus(), qs.getQuestVars().getQuestVars()));
        player.getController().updateNearbyQuests();
    }

    public static void setClass(Player player, PlayerClass playerClass) {
        if (validateSwitch(player, playerClass)) {
            player.getCommonData().setPlayerClass(playerClass);
            player.getController().upgradePlayer();
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0, 0));
        }
    }

    private static boolean validateSwitch(Player player, PlayerClass playerClass) {
        int level = player.getLevel();
        PlayerClass oldClass = player.getPlayerClass();
        if (level != 9 && !DeveloperConfig.ENABLE_GM_PVP) {
            PacketSendUtility.sendMessage(player, "You can only switch class at level 9");
            return false;
        }
        if (!oldClass.isStartingClass()) {
            PacketSendUtility.sendMessage(player, "You already switched class");
            return false;
        }
        switch (oldClass) {
            case WARRIOR:
                if (playerClass == PlayerClass.GLADIATOR || playerClass == PlayerClass.TEMPLAR) {
                    break;
                }
            case SCOUT:
                if (playerClass == PlayerClass.ASSASSIN || playerClass == PlayerClass.RANGER) {
                    break;
                }
            case MAGE:
                if (playerClass == PlayerClass.SORCERER || playerClass == PlayerClass.SPIRIT_MASTER) {
                    break;
                }
            case PRIEST:
                if (playerClass == PlayerClass.CLERIC || playerClass == PlayerClass.CHANTER) {
                    break;
                }
            case ENGINEER:
                if (playerClass == PlayerClass.GUNNER || playerClass == PlayerClass.RIDER) {
                    break;
                }
            case ARTIST:
                if (playerClass == PlayerClass.BARD) {
                    break;
                }
            default:
                PacketSendUtility.sendMessage(player, "Invalid class switch chosen");
                return false;
        }
        return true;
    }
}
