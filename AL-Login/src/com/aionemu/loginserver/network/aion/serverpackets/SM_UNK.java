/*
 * SAO Project
 */
package com.aionemu.loginserver.network.aion.serverpackets;

import com.aionemu.loginserver.network.aion.AionServerPacket;
import com.aionemu.loginserver.network.aion.LoginConnection;

/**
 *
 * @author Alex
 */
public class SM_UNK extends AionServerPacket {

    private final int r1;
    private final int r2;

    public SM_UNK(int r1, int r2) {
        super(0x0a);
        this.r1 = r1;
        this.r2 = r2;
    }

    @Override
    protected void writeImpl(LoginConnection con) {
        writeD(r1);
        writeC(r2);
    }
}
