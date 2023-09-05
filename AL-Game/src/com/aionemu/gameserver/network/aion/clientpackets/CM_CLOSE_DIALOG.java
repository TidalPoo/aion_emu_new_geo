/*
 * This file is part of aion-lightning <aionunique.smfnew.com>.
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

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_HEADING_UPDATE;
import com.aionemu.gameserver.services.DialogService;
import com.aionemu.gameserver.services.player.PlayerMailboxState;

public class CM_CLOSE_DIALOG extends AionClientPacket {

    /**
     * Target object id that client wants to TALK WITH or 0 if wants to unselect
     */
    private int targetObjectId;

    /**
     * Constructs new instance of <tt>CM_CM_REQUEST_DIALOG </tt> packet
     *
     * @param opcode
     * @param state
     * @param restStates
     */
    public CM_CLOSE_DIALOG(int opcode, State state, State... restStates) {
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
        Player player = getConnection().getActivePlayer();
        if (player == null) {
            return;
        }
        final VisibleObject obj = player.getKnownList().getObject(targetObjectId);
        final AionConnection client = getConnection();
        if (obj == null) {
            return;
        }

        if (obj instanceof Npc) {
            Npc npc = (Npc) obj;
            npc.getAi2().onCreatureEvent(AIEventType.DIALOG_FINISH, player);
            DialogService.onCloseDialog(npc, player);

            ThreadPoolManager.getInstance().schedule(new Runnable() {

                @Override
                public void run() {
                    client.sendPacket(new SM_HEADING_UPDATE(targetObjectId, obj.getHeading()));
                }
            }, 1200);

        }

        if (player.getMailbox().mailBoxState != 0) {
            player.getMailbox().mailBoxState = PlayerMailboxState.CLOSED;
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
