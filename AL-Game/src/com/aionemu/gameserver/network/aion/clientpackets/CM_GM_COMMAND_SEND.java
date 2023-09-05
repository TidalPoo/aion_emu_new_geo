/*
 * This file is part of aion-engine <aion-engine.net>
 *
 * aion-engine is private software: you can redistribute it and or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Private Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with aion-engine.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.gmInterface.GMInterfaceCommand;
import com.aionemu.gameserver.utils.PacketSendUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xXMashUpXx, Alex
 *
 */
public class CM_GM_COMMAND_SEND extends AionClientPacket {

    private String Command;
    private int type;
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(CM_GM_COMMAND_SEND.class);

    /**
     * @param opcode
     * @param state
     * @param restStates
     */
    public CM_GM_COMMAND_SEND(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    /* (non-Javadoc)
     * @see com.aionemu.commons.network.packet.BaseClientPacket#readImpl()
     */
    @Override
    protected void readImpl() {
        Command = readS();
    }

    /* (non-Javadoc)
     * @see com.aionemu.commons.network.packet.BaseClientPacket#runImpl()
     */
    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        if (player.getAccessLevel() == 0 && !player.isDeveloper()) {
            //TODO AutoBan
            log.info("WARNING NO PLAYER IS GM : " + player.getName() + " id: " + player.getObjectId());
            return;
        }

        GMInterfaceCommand.getInstance().toCommand(player, Command);
        PacketSendUtility.sendMessage(player, "Command: " + Command);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
