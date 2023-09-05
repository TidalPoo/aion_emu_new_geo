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

import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_INFO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author nrg
 */
public class CM_INSTANCE_INFO extends AionClientPacket {

    private static final Logger log = LoggerFactory.getLogger(CM_INSTANCE_INFO.class);

    @SuppressWarnings("unused")
    private int unk1, unk2;

    public CM_INSTANCE_INFO(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        unk1 = readD();
        unk2 = readC(); // team?
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        if (unk2 == 1 && !getConnection().getActivePlayer().isInTeam()) {
            log.debug("Received CM_INSTANCE_INFO with teamdata request but player has no team!");
        }
        sendPacket(new SM_INSTANCE_INFO(getConnection().getActivePlayer(), true, getConnection().getActivePlayer().getCurrentTeam()));
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
