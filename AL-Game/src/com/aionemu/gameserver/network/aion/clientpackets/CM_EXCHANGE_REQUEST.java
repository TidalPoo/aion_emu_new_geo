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

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.DeniedStatus;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.SystemMessageId;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.ExchangeService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author -Avol-
 * @modified Alex
 */
public class CM_EXCHANGE_REQUEST extends AionClientPacket {

    public Integer targetObjectId;

    private static final Logger log = LoggerFactory.getLogger(CM_EXCHANGE_REQUEST.class);

    public CM_EXCHANGE_REQUEST(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    @Override
    protected void readImpl() {
        targetObjectId = readD();
    }

    @Override
    protected void runImpl() {
        final Player activePlayer = getConnection().getActivePlayer();
        final Player targetPlayer = World.getInstance().findPlayer(targetObjectId);

        if (targetPlayer == null) {
            PacketSendUtility.sendMessage(activePlayer, "No such target player!");
            return;
        }

        /**
         * check if not trading with yourself.
         */
        if (activePlayer.equals(targetPlayer)) {
            PacketSendUtility.sendMessage(activePlayer, "Cannot use on yourself!");
            return;
        }

        /**
         * check distance between players.
         */
        if (activePlayer.getKnownList().getObject(targetPlayer.getObjectId()) == null) {
            PacketSendUtility.sendMessage(activePlayer, "Target not from knownlist!");
            return;
        }

        if (AdminConfig.GM_NO_TRADE && activePlayer.getAccessLevel() != 0 && activePlayer.getAccessLevel() <= AdminConfig.GM_NO_TRADE_LEVEL) {
            PacketSendUtility.sendMessage(activePlayer, "DEVELOPER: You can use trade!");
            return;
        }

        if (!activePlayer.getRace().equals(targetPlayer.getRace()) && !activePlayer.isGM()) {
            PacketSendUtility.sendMessage(activePlayer, "Your race does not coincide with the race Target!");
            return;
        }

        /**
         * check if trade partner exists or is he/she a player.
         */
        if (targetPlayer.getPlayerSettings().isInDeniedStatus(activePlayer, DeniedStatus.TRADE)) {
            sendPacket(SM_SYSTEM_MESSAGE.STR_MSG_REJECTED_TRADE(targetPlayer.getName()));
            return;
        }

        sendPacket(SM_SYSTEM_MESSAGE.STR_EXCHANGE_ASKED_EXCHANGE_TO_HIM(targetPlayer.getName()));
        RequestResponseHandler responseHandler = new RequestResponseHandler(activePlayer) {

            @Override
            public void acceptRequest(Creature requester, Player responder) {
                ExchangeService.getInstance().registerExchange(activePlayer, targetPlayer);
            }

            @Override
            public void denyRequest(Creature requester, Player responder) {
                PacketSendUtility.sendPacket(activePlayer, new SM_SYSTEM_MESSAGE(
                        SystemMessageId.EXCHANGE_HE_REJECTED_EXCHANGE, targetPlayer.getName()));
            }
        };

        boolean requested = targetPlayer.getResponseRequester().putRequest(
                SM_QUESTION_WINDOW.STR_EXCHANGE_DO_YOU_ACCEPT_EXCHANGE, responseHandler);
        if (requested) {
            PacketSendUtility.sendPacket(targetPlayer, new SM_QUESTION_WINDOW(
                    SM_QUESTION_WINDOW.STR_EXCHANGE_DO_YOU_ACCEPT_EXCHANGE, 0, 0, activePlayer.getName()));
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
