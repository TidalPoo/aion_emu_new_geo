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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dao.RequestFriendListDAO;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.DeniedStatus;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.gameobjects.player.Request;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FRIEND_RESPONSE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.SocialService;
import com.aionemu.gameserver.services.weddings.WeddingService;
import com.aionemu.gameserver.utils.ChatUtil;
import com.aionemu.gameserver.world.World;

/**
 * Received when a user tries to add someone as his friend
 *
 * @author Ben
 * @modified Alex
 */
public class CM_FRIEND_ADD extends AionClientPacket {

    private String targetName;
    private String text;

    public CM_FRIEND_ADD(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        targetName = readS();//target Name
        text = readS();//text
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {

        targetName = ChatUtil.getRealAdminName(targetName);
        //NEW WEDDINGS
        if (targetName.contains("\ue020")) {
            targetName = WeddingService.getRealWeddingsName(targetName);
        }

        final Player activePlayer = getConnection().getActivePlayer();
        final Player targetPlayer = World.getInstance().findPlayer(targetName);

        if (targetName.equalsIgnoreCase(activePlayer.getName())) {
            return;
        }

        if (activePlayer.getRequestFriendList().getRequest(targetName) != null) {
            //%0: вы уже получили запрос на дружбу от данного персонажа.
            sendPacket(new SM_SYSTEM_MESSAGE(1401518, targetName));
            return;
        }
        // if offline
        if (targetPlayer == null) {
            if (activePlayer.getFriendList().getFriend(targetName) != null) {
                sendPacket(new SM_FRIEND_RESPONSE(targetName, SM_FRIEND_RESPONSE.TARGET_ALREADY_FRIEND));
                return;
            }
            PlayerCommonData pcd = DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonDataByName(targetName);
            if (pcd == null) {
                //Персонажа с именем %0 не существует.
                sendPacket(new SM_SYSTEM_MESSAGE(1300625, targetName));
                return;
            }
            sendPacket(new SM_FRIEND_RESPONSE(targetName, SM_FRIEND_RESPONSE.TARGET_OFFLINE));
            if (activePlayer.getRequestSenderList().getFriend(targetName) != null) {
                //%0: вы уже предложили дружбу данному персонажу.
                sendPacket(new SM_SYSTEM_MESSAGE(1401500, targetName));
                return;
            }
            if (activePlayer.getRequestSenderList().getSize() >= 20) {
                //Вы не можете посылать запросы о дружбе, так как превысили допустимое их количество.
                sendPacket(new SM_SYSTEM_MESSAGE(1401502));
                return;
            }
            int MAX_FRIENDS = CustomConfig.FRIENDLIST_SIZE;
            if (pcd.getFriends() >= MAX_FRIENDS) {
                //Список друзей персонажа %0 полон, поэтому он не может получать запросы на дружбу.
                sendPacket(new SM_SYSTEM_MESSAGE(1401503, targetName));
                return;
            }
            if (activePlayer.getFriendList().getSize() >= MAX_FRIENDS) {
                sendPacket(new SM_SYSTEM_MESSAGE(1300887));
                return;
            }
            if (activePlayer.getBlockList().contains(activePlayer.getObjectId())) {
                //Нельзя внести в список друзей персонажа из игнор-листа.
                sendPacket(new SM_SYSTEM_MESSAGE(1300884));
                return;
            }
            if (pcd.getRace() != activePlayer.getRace()){
                return;
            }
            activePlayer.getRequestSenderList().addFriend(new Request(pcd));
            activePlayer.getCommonData().setRequestMessage(text);
            DAOManager.getDAO(RequestFriendListDAO.class).addRequests(pcd.getPlayerObjId(), activePlayer);
            //%0: персонаж сейчас вне игры, ему передано сообщение о предложении дружбы.
            sendPacket(new SM_SYSTEM_MESSAGE(1401501, targetName));
        } else if (activePlayer.getFriendList().getFriend(targetPlayer.getObjectId()) != null) {
            sendPacket(new SM_FRIEND_RESPONSE(targetPlayer.getName(), SM_FRIEND_RESPONSE.TARGET_ALREADY_FRIEND));
        } else if (activePlayer.getFriendList().isFull()) {
            sendPacket(SM_SYSTEM_MESSAGE.STR_BUDDYLIST_LIST_FULL);
        } else if (activePlayer.getCommonData().getRace() != targetPlayer.getCommonData().getRace() && !activePlayer.isGM()) {
            sendPacket(new SM_FRIEND_RESPONSE(targetPlayer.getName(), SM_FRIEND_RESPONSE.TARGET_NOT_FOUND));
        } else if (targetPlayer.getFriendList().isFull()) {
            sendPacket(new SM_FRIEND_RESPONSE(targetPlayer.getName(), SM_FRIEND_RESPONSE.TARGET_LIST_FULL));
        } else if (activePlayer.getBlockList().contains(targetPlayer.getObjectId())) {
            sendPacket(new SM_FRIEND_RESPONSE(targetPlayer.getName(), SM_FRIEND_RESPONSE.TARGET_BLOCKED));
        } else if (targetPlayer.getBlockList().contains(activePlayer.getObjectId())) {
            sendPacket(SM_SYSTEM_MESSAGE.STR_YOU_EXCLUDED(targetName));
        } else {
            sendPacket(SM_SYSTEM_MESSAGE.STR_BUDDY_REQUEST_ADD(targetName));
            // Send request
            RequestResponseHandler responseHandler = new RequestResponseHandler(activePlayer) {

                @Override
                public void acceptRequest(Creature requester, Player responder) {
                    if (!targetPlayer.getCommonData().isOnline()) {
                        sendPacket(new SM_FRIEND_RESPONSE(targetName, SM_FRIEND_RESPONSE.TARGET_OFFLINE));
                    } else if (!activePlayer.getFriendList().isFull() && !responder.getFriendList().isFull()) {
                        SocialService.makeFriends((Player) requester, responder);
                    }
                }

                @Override
                public void denyRequest(Creature requester, Player responder) {
                    sendPacket(new SM_FRIEND_RESPONSE(targetName, SM_FRIEND_RESPONSE.TARGET_DENIED));
                }
            };

            boolean requested = targetPlayer.getResponseRequester().putRequest(
                    SM_QUESTION_WINDOW.STR_BUDDYLIST_ADD_BUDDY_REQUEST, responseHandler);
            // If the player is busy and could not be asked
            if (!requested) {
                sendPacket(SM_SYSTEM_MESSAGE.STR_BUDDYLIST_BUSY);
                //STR_BUDDY_REQUEST_ADD(name)
            } else {
                if (targetPlayer.getPlayerSettings().isInDeniedStatus(activePlayer, DeniedStatus.FRIEND)) {
                    sendPacket(SM_SYSTEM_MESSAGE.STR_MSG_REJECTED_FRIEND(targetPlayer.getName()));
                    return;
                }
                // Send question packet to buddy
                targetPlayer.getClientConnection().sendPacket(
                        new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_BUDDYLIST_ADD_BUDDY_REQUEST, activePlayer.getObjectId(), 0,
                                activePlayer.getName()));
            }
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
