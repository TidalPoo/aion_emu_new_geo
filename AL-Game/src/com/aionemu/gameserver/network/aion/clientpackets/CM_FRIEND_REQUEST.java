/*
 * SAO Project
 */
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.dao.BlockListDAO;
import com.aionemu.gameserver.dao.FriendListDAO;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dao.RequestFriendListDAO;
import com.aionemu.gameserver.model.gameobjects.player.BlockedPlayer;
import com.aionemu.gameserver.model.gameobjects.player.Friend;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.gameobjects.player.RequestFriend;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.serverpackets.SM_BLOCK_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_BLOCK_RESPONSE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FRIEND_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FRIEND_RESPONSE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MARK_FRIENDLIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.SocialService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

/**
 *
 * @author Alex
 */
public class CM_FRIEND_REQUEST extends AionClientPacket {

    public CM_FRIEND_REQUEST(int opcode, AionConnection.State state, AionConnection.State... restStates) {
        super(opcode, state, restStates);
    }
    private int playerObjectId;
    private String playerName;
    private int action;

    @Override
    protected void readImpl() {
        playerObjectId = readD();
        playerName = readS();
        action = readC();
    }

    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        Player target = World.getInstance().findPlayer(playerObjectId);
        RequestFriend rflist = player.getRequestFriendList().getRequest(playerObjectId);
        PlayerCommonData pcd = DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonData(playerObjectId);
        if (pcd == null) {
            player.getRequestFriendList().delRequest(playerObjectId);
            DAOManager.getDAO(RequestFriendListDAO.class).delRequests(player.getObjectId(), playerObjectId);
            PacketSendUtility.sendPacket(player, new SM_MARK_FRIENDLIST());
            return;
        }
        switch (action) {
            case 0:
                //Подтвердить
                if (player.getBlockList().contains(player.getObjectId())) {
                    //Нельзя внести в список друзей персонажа из игнор-листа.
                    sendPacket(new SM_SYSTEM_MESSAGE(1300884));
                    return;
                }
                int MAX_FRIENDS = CustomConfig.FRIENDLIST_SIZE;
                if (pcd.getFriends() >= MAX_FRIENDS) {
                    //Список друзей персонажа %0 полон, поэтому он не может получать запросы на дружбу.
                    sendPacket(new SM_SYSTEM_MESSAGE(1400543, playerName));
                    return;
                }
                if (target != null) {
                    SocialService.makeFriends(player, target);
                    target.getRequestSenderList().delFriend(player.getObjectId());
                } else {
                    DAOManager.getDAO(FriendListDAO.class).addFriendOffline(player.getObjectId(), rflist.getOid());
                    player.getFriendList().addFriend(new Friend(rflist.getPCD()));
                    player.getClientConnection().sendPacket(new SM_FRIEND_LIST());
                    player.getClientConnection().sendPacket(new SM_FRIEND_RESPONSE(rflist.getName(), SM_FRIEND_RESPONSE.TARGET_ADDED));
                }
                //TODO
                //Данный персонаж уже в списке друзей.
                break;
            case 1:
                //Отказаться
                if (target != null) {
                    target.getRequestSenderList().delFriend(player.getObjectId());
                    //%0 отказывается внести вас в список друзей.
                    sendPacket(new SM_SYSTEM_MESSAGE(1300886, player.getName()));
                }
                //Вы отказали в дружбе персонажу %0.
                sendPacket(new SM_SYSTEM_MESSAGE(1401517, playerName));
                break;
            case 2:
                //игнор-лист
                if (DAOManager.getDAO(BlockListDAO.class).addBlockedUser(player.getObjectId(), playerObjectId, "")) {
                    player.getBlockList().add(new BlockedPlayer(pcd, ""));
                    player.getClientConnection().sendPacket(new SM_BLOCK_RESPONSE(SM_BLOCK_RESPONSE.BLOCK_SUCCESSFUL, playerName));
                    player.getClientConnection().sendPacket(new SM_BLOCK_LIST(player.getBlockList(), false));
                }
                break;
        }
        player.getRequestFriendList().delRequest(playerObjectId);
        DAOManager.getDAO(RequestFriendListDAO.class).delRequests(player.getObjectId(), playerObjectId);
        PacketSendUtility.sendPacket(player, new SM_MARK_FRIENDLIST());
        /*<string>
         <id>1401672</id>
         <name>STR_MSG_OFFLINE_BUDDY_REQUEST_NOTICE</name>
         <body>Пришло заявок на добавление в друзья: %0 шт. Ответить на них можно в окне "Друзья" во вкладке "Запросы на добавление в друзья".</body>
         <message_type>53</message_type>
         <display_type>3</display_type>
         </string>
         <string>
         <id>1401498</id>
         <name>STR_MSG_OFFLINE_BUDDY_REQUEST_TO_ADD</name>
         <body>%0 хочет попасть в ваш список друзей.</body>
         <message_type>53</message_type>
         <display_type>3</display_type>
         <__review__>1</__review__>
         </string>
         <string>
         <id>1401499</id>
         <name>STR_MSG_OFFLINE_BUDDY_REQUEST_TIMEOUT</name>
         <body>%0: закончилось время запроса на дружбу (7 дн.).</body>
         <message_type>53</message_type>
         <display_type>3</display_type>
         <__review__>1</__review__>
         </string>
         <string>
         <id>1401500</id>
         <name>STR_MSG_OFFLINE_BUDDY_REQUEST_ALREADY</name>
         <body>%0: вы уже предложили дружбу данному персонажу.</body>
         <message_type>53</message_type>
         <display_type>3</display_type>
         <__review__>1</__review__>
         </string>
         <string>
         <id>1401501</id>
         <name>STR_MSG_OFFLINE_BUDDY_REQUEST_ADD</name>
         <body>%0: персонаж сейчас вне игры, ему передано сообщение о предложении дружбы.</body>
         <message_type>53</message_type>
         <display_type>3</display_type>
         <__review__>1</__review__>
         </string>
         <string>
         <id>1401502</id>
         <name>STR_MSG_OFFLINE_BUDDY_REQUEST_FULL</name>
         <body>Вы не можете посылать запросы о дружбе, так как превысили допустимое их количество.</body>
         <message_type>53</message_type>
         <display_type>3</display_type>
         <__review__>1</__review__>
         </string>
         <string>
         <id>1401503</id>
         <name>STR_MSG_OFFLINE_BUDDY_BUDDYS_LIST_FULL</name>
         <body>Список друзей персонажа %0 полон, поэтому он не может получать запросы на дружбу.</body>
         <message_type>53</message_type>
         <display_type>3</display_type>
         <__review__>1</__review__>
         <string>
         <id>1401517</id>
         <name>STR_MSG_BUDDYLIST_REQUEST_REJECTED</name>
         <body>Вы отказали в дружбе персонажу %0.</body>
         <message_type>53</message_type>
         <display_type>3</display_type>
         <__review__>1</__review__>
         </string>
         <string>
         <id>1401518</id>
         <name>STR_MSG_BUDDYLIST_REQUEST_STANDBY</name>
         <body>%0: вы уже получили запрос на дружбу от данного персонажа.</body>
         <message_type>53</message_type>
         <display_type>3</display_type>
         <__review__>1</__review__>
         </string>
         </string>*/
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
