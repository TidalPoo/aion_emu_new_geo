/*
 * AionLight project
 */
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection;

/**
 *
 * @author Alex
 */
public class CM_UNK3 extends AionClientPacket {

    public CM_UNK3(int opcode, AionConnection.State state, AionConnection.State... restStates) {
        super(opcode, state, restStates);
    }

    @Override
    protected void readImpl() {
        // empty
    }

    @Override
    protected void runImpl() {

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
