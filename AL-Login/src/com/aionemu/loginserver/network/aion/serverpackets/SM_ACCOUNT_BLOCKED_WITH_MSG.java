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
public class SM_ACCOUNT_BLOCKED_WITH_MSG extends AionServerPacket {

    private final String msg;

    public SM_ACCOUNT_BLOCKED_WITH_MSG(String msg) {
        super(0x09);
        this.msg = msg;
    }

    @Override
    protected void writeImpl(LoginConnection con) {
        writeC(1);//size
        writeH(0x00);
        writeS(msg);
    }
}
