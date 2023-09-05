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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.ColorUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.audit.GMService;
import java.sql.Timestamp;

/**
 * @author xavier Packet sent by client when player may quit game in 10 seconds
 * @modified Alex
 */
public class CM_MAY_QUIT extends AionClientPacket {

    /**
     * @param opcode
     * @param state
     * @param restStates
     */
    public CM_MAY_QUIT(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    /*
     * (non-Javadoc)
     * @see com.aionemu.commons.network.packet.BaseClientPacket#readImpl()
     */
    @Override
    protected void readImpl() {
        // empty
        //send damage player to online? xD
    }

    /*
     * (non-Javadoc)
     * @see com.aionemu.commons.network.packet.BaseClientPacket#runImpl()
     */
    @Override
    protected void runImpl() {
        // Nothing to do
        Player player = getConnection().getActivePlayer();
        if (GMService.getInstance().isMessageGM()) {
            //STR_CANT_CHAT_DURING_NOTIFICATION
            //Ожидается важное объявление от гейм-мастера. Пожалуйста, подождите немного.
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300626));
        } else {
            String text = ColorUtil.convertTextToColor("Заходите к нам еще!", "0 0.5 1");
            PacketSendUtility.sendYellowMessageOnCenter(player, text);
        }
        player.getCommonData().setLastOnline(new Timestamp(System.currentTimeMillis()));
        for (Player gm : GMService.getInstance().getGMs()) {
            if (gm != player) {
                PacketSendUtility.sendMessage(gm, player.getName() + " жмет кнопку выхода из игры.");
            }
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
