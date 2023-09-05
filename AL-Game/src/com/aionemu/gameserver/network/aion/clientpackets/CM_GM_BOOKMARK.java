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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gm.GmCommands;
import com.aionemu.gameserver.model.templates.QuestTemplate;
import com.aionemu.gameserver.model.templates.quest.QuestExtraCategory;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ABYSS_RANK;
import com.aionemu.gameserver.network.aion.serverpackets.SM_BLOCK_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FRIEND_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MAIL_SERVICE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_COMPLETED_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TARGET_SELECTED;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TARGET_UPDATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TITLE_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_VIEW_PLAYER_DETAILS;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.world.World;
import javolution.util.FastList;

/**
 *
 * @author Alex
 */
public class CM_GM_BOOKMARK extends AionClientPacket {

    private String request;
    private String[] forSplit;
    private int commandId;
    private String playerName;
    private GmCommands command;

    public CM_GM_BOOKMARK(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    @Override
    protected void readImpl() {
        request = readS();
        forSplit = request.split(" ");
        command = GmCommands.valueOf(forSplit[0]);
        commandId = command.getValue();
        playerName = forSplit[1];
    }

    @Override
    protected void runImpl() {
        Player admin = getConnection().getActivePlayer();

        if (admin == null) {
            return;
        }

        Player player = World.getInstance().findPlayer(CustomConfig.ENABLE_CONVERT_NAME ? Util.convertName(playerName) : playerName);

        if (player == null) {
            PacketSendUtility.sendMessage(admin, "Could not find an online player with that name.");
            return;
        }
        if (admin == player) {
            return;
        }
        //admin.setPlayerGmPanel(player);

        if (admin.getTarget() != player) {
            admin.setTarget(player);
            sendPacket(new SM_TARGET_SELECTED(admin));
            PacketSendUtility.broadcastPacket(admin, new SM_TARGET_UPDATE(admin));
            PacketSendUtility.sendMessage(admin, "target update ");
        }
        //sendPacket(new SM_GM_COMMAND_ACTION(commandId));
        PacketSendUtility.sendMessage(admin, "commandId " + commandId + " command " + request);
        switch (commandId) {
            case 0:
                //mailbox
                sendPacket(new SM_MAIL_SERVICE(player, player.getMailbox().getLetters(), true));
                //sendPacket(new SM_MAIL_SERVICE(admin, admin.getMailbox().getLetters(), false));
                break;
            case 1:
                sendPacket(new SM_VIEW_PLAYER_DETAILS(player.getEquipment().getEquippedItemsWithoutStigma(), player, true));
                break;
            case 2:
                //skills
                //sendPacket(new SM_STATS_STATUS_UNK(65, 2000));
                break;
            case 3:
                //teleport
                TeleportService2.teleportTo(admin, player.getWorldId(), player.getX(), player.getY(), player.getZ());
                break;
            case 4:
                //sendPacket(new SM_GM_COMMAND_ACTION(4));
                //Status
                //sendPacket(SM_CUBE_UPDATE.stigmaSlots(player.getCommonData().getAdvancedStigmaSlotSize(), true));
                sendPacket(new SM_ABYSS_RANK(player.getAbyssRank(), true));

                //sendPacket(new SM_ABYSS_RANK(admin.getAbyssRank(), false));
                //PacketSendUtility.sendPacket(player, new SM_ABYSS_RANK(player.getAbyssRank(), false));
                //sendPacket(new SM_STATS_INFO(player));
                sendPacket(new SM_TITLE_INFO(player, true));

                // sendPacket(new SM_TITLE_INFO(admin, false));
                //PacketSendUtility.sendPacket(player, new SM_TITLE_INFO(player, false));
                //sendPacket(new SM_GM_SPY(1, 1, "status", admin.getObjectId()));
                break;
            case 5:
                //search
                admin.setTarget(player);
                sendPacket(new SM_TARGET_SELECTED(admin));
                PacketSendUtility.broadcastPacket(admin, new SM_TARGET_UPDATE(admin));
                PacketSendUtility.sendMessage(admin, "target update ");
                break;
            case 6:
                //quest
                FastList<QuestState> questList = FastList.newInstance();
                FastList<QuestState> completeQuestList = FastList.newInstance();
                for (QuestState qs : player.getQuestStateList().getAllQuestState()) {
                    QuestTemplate questTemplate = DataManager.QUEST_DATA.getQuestById(qs.getQuestId());
                    if (questTemplate.getExtraCategory() != QuestExtraCategory.NONE) {
                        continue;
                    }
                    if (qs.getStatus() == QuestStatus.NONE && qs.getCompleteCount() == 0) {
                        continue;
                    }
                    if (qs.getStatus() != QuestStatus.COMPLETE && qs.getStatus() != QuestStatus.NONE) {
                        questList.add(qs);
                    }
                    if (qs.getCompleteCount() > 0) {
                        completeQuestList.add(qs);
                    }
                }
                sendPacket(new SM_QUEST_COMPLETED_LIST(completeQuestList, true));
                sendPacket(new SM_QUEST_LIST(questList, true));
            case 7:
                //TODE Player Legion Info history?
                if (player.getLegion() != null) {

                }
                break;
            case 8:
                //friend
                if (player.getFriendList() != null) {
                    sendPacket(new SM_FRIEND_LIST(player.getFriendList(), true));
                    //sendPacket(new SM_FRIEND_LIST(admin.getFriendList(), false));
                    //PacketSendUtility.sendPacket(player, new SM_FRIEND_LIST(player.getFriendList(), false));
                }
                if (player.getBlockList() != null) {
                    sendPacket(new SM_BLOCK_LIST(player.getBlockList(), true));
                    //sendPacket(new SM_BLOCK_LIST(admin.getBlockList(), false));
                    //PacketSendUtility.sendPacket(player, new SM_BLOCK_LIST(player.getBlockList(), false));
                }
                break;
            case 9:
                port(admin, player);
            case 10:
                break;
            case 11:
                //памятка
                break;
            case 12:
                break;
            case 13:
                break;
            case 14:
                //sendPacket(new SM_GM_SPY(player.getObjectId(), player.getObjectId(), request, player.getObjectId()));
                break;
            case 15:
                //legion
                // if (player.getLegion() != null) {
                //sendPacket(new SM_LEGION_UPDATE_MEMBER(player, 0, ""));
                //sendPacket(new SM_LEGION_ADD_MEMBER(player, true, 0, ""));
                // sendPacket(new SM_LEGION_INFO(player.getLegion(), true));
                //}
                break;
            default:
                break;
        }
    }

    private void port(Player admin, Player player) {
        TeleportService2.teleportTo(player, admin.getWorldId(), admin.getInstanceId(), admin.getX(), admin.getY(), admin.getZ(), admin.getHeading());
        PacketSendUtility.sendMessage(admin, "Teleported player " + player.getName() + " to your location.");
        PacketSendUtility.sendMessage(player, "You have been teleported by " + admin.getName() + ".");
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
