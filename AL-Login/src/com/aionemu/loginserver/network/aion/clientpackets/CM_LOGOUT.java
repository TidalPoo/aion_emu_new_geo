/*
 * SAO Project
 */
package com.aionemu.loginserver.network.aion.clientpackets;

import com.aionemu.loginserver.network.aion.AionClientPacket;
import com.aionemu.loginserver.network.aion.LoginConnection;

/**
 *
 * @author Alex
 */
public class CM_LOGOUT extends AionClientPacket {

    public CM_LOGOUT(java.nio.ByteBuffer buf, LoginConnection client) {
        super(buf, client, 0x03);
    }

    @Override
    protected void readImpl() {
    }

    @Override
    protected void runImpl() {
    }
}
