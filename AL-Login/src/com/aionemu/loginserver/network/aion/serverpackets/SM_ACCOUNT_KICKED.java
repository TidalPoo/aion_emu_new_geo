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
public class SM_ACCOUNT_KICKED extends AionServerPacket {

    private final AionAuthResponse response;

    public SM_ACCOUNT_KICKED(AionAuthResponse response) {
        super(0x08);
        this.response = response;
    }

    @Override
    protected void writeImpl(LoginConnection con) {
        writeD(response.getMessageId());
    }
}
