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
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.gameobjects.player.DeniedStatus;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceService;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.model.team2.league.LeagueService;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.weddings.WeddingService;
import com.aionemu.gameserver.utils.ChatUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.world.World;

/**
 * @author Lyahim, ATracer Modified by Simple
 */
public class CM_INVITE_TO_GROUP extends AionClientPacket {

    private String name;
    private int inviteType;

    public CM_INVITE_TO_GROUP(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        inviteType = readC();
        name = readS();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {

        name = ChatUtil.getRealAdminName(name);
        //NEW WEDDINGS
        if (name.contains("\ue020")) {
            name = WeddingService.getRealWeddingsName(name);
        }

        final String playerName = CustomConfig.ENABLE_CONVERT_NAME ? Util.convertName(name) : name;

        final Player inviter = getConnection().getActivePlayer();
        if (inviter.getLifeStats().isAlreadyDead()) {
            // You cannot issue an invitation while you are dead.
            PacketSendUtility.sendPacket(inviter, new SM_SYSTEM_MESSAGE(1300163));
            return;
        }

        final Player invited = World.getInstance().findPlayer(playerName);
        if (invited == null) {
            inviter.getClientConnection().sendPacket(SM_SYSTEM_MESSAGE.STR_NO_SUCH_USER(name));
            return;
        }

        if (invited.getPlayerSettings().isInDeniedStatus(inviter, DeniedStatus.GROUP)) {
            sendPacket(SM_SYSTEM_MESSAGE.STR_MSG_REJECTED_INVITE_PARTY(invited.getName()));
            return;
        }

        if (AdminConfig.GM_NO_GROUP && inviter.getAccessLevel() != 0 && inviter.getAccessLevel() <= AdminConfig.GM_NO_GROUP_LEVEL) {
            PacketSendUtility.sendMessage(inviter, "DEVELOPER: You can use group!");
            return;
        }

        switch (inviteType) {
            case 0:
                PlayerGroupService.inviteToGroup(inviter, invited);
                break;
            case 12: // 2.5
                PlayerAllianceService.inviteToAlliance(inviter, invited);
                break;
            case 28:
                LeagueService.inviteToLeague(inviter, invited);
                break;
            default:
                PacketSendUtility.sendMessage(inviter, "You used an unknown invite type: " + inviteType);
                break;
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
