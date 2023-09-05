/*
 * SAO Project
 */
package com.aionemu.loginserver.network.aion.serverpackets;

import com.aionemu.loginserver.network.aion.AionAuthResponse;
import com.aionemu.loginserver.network.aion.AionServerPacket;
import com.aionemu.loginserver.network.aion.LoginConnection;

/**
 *
 * @author Alex
 */
public class SM_SERVER_LIST_FAIL extends AionServerPacket {

    private final AionAuthResponse response;

    public SM_SERVER_LIST_FAIL(AionAuthResponse response) {
        super(0x05);
        this.response = response;
    }

    @Override
    protected void writeImpl(LoginConnection con) {
        writeD(response.getMessageId());
    }
}
