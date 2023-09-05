package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;

public class CM_STAGGER extends AionClientPacket {

    byte unk1;

    public CM_STAGGER(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);

    }

    @Override
    protected void readImpl() {
        unk1 = (byte) readC();
    }

    @Override
    protected void runImpl() {
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
