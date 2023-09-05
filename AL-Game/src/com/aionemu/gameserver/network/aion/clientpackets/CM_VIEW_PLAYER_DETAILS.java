/**
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 * aion-lightning is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * aion-lightning is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * aion-lightning. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.DeniedStatus;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_VIEW_PLAYER_DETAILS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Avol
 */
public class CM_VIEW_PLAYER_DETAILS extends AionClientPacket {

    private static final Logger log = LoggerFactory.getLogger(CM_VIEW_PLAYER_DETAILS.class);

    private int targetObjectId;

    public CM_VIEW_PLAYER_DETAILS(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        targetObjectId = readD();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        // команда: /изучить или кнопка "Подробная информация"
        Player player = this.getConnection().getActivePlayer();
        if (targetObjectId == player.getObjectId()) {
            sendPacket(new SM_VIEW_PLAYER_DETAILS(player.getEquipment().getEquippedItemsWithoutStigma(), player, false));
            return;
        }
        VisibleObject obj = player.getKnownList().getObject(targetObjectId);
        if (obj == null) {
            // probably targetObjectId can be 0
            log.warn("CHECKPOINT: can't show player details for " + targetObjectId);
            return;
        }

        if (obj instanceof Player) {
            Player target = (Player) obj;
            if (target.getRace() != player.getRace()) {
                sendPacket(new SM_SYSTEM_MESSAGE(1300045));
                return;
            }
            if (!target.getPlayerSettings().isInDeniedStatus(player, DeniedStatus.VIEW_DETAILS) || player.getAccessLevel() >= AdminConfig.ADMIN_VIEW_DETAILS) {
                sendPacket(new SM_VIEW_PLAYER_DETAILS(target.getEquipment().getEquippedItemsWithoutStigma(), target, false));
                //deny = true;
            } else {
                sendPacket(SM_SYSTEM_MESSAGE.STR_MSG_REJECTED_WATCH(target.getName()));
            }
            /*if (target.isGM() || target.isDeveloper()) {
             PacketSendUtility.sendMessage(target, player.getName() + " желает посмотреть ваши надетые предметы (" + (deny ? "успешно" : "отказ") + ")");
             }*/
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
