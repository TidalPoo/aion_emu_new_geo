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

import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xXMashUpXx
 *
 */
public class CM_AFTER_RECONNECT extends AionClientPacket {

    private int unk;//TODO what's this value!?
    private static final Logger log = LoggerFactory.getLogger(CM_AFTER_RECONNECT.class);

    /**
     * @param opcode
     * @param state
     * @param restStates
     */
    public CM_AFTER_RECONNECT(int opcode, State state,
            State... restStates) {
        super(opcode, state, restStates);
    }

    /* (non-Javadoc)
     * @see com.aionemu.commons.network.packet.BaseClientPacket#readImpl()
     */
    @Override
    protected void readImpl() {
        unk = readC();
    }

    /* (non-Javadoc)
     * @see com.aionemu.commons.network.packet.BaseClientPacket#runImpl()
     */
    @Override
    protected void runImpl() {
        log.info("[CM_AFTER_RECONNECT] Packet received with value: " + unk + ".");//For researching
        //TODO

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
