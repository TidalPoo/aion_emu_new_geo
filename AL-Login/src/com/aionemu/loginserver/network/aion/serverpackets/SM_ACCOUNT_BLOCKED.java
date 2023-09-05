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
public class SM_ACCOUNT_BLOCKED extends AionServerPacket {

    private final AionAuthResponse response;

    public SM_ACCOUNT_BLOCKED(AionAuthResponse response) {
        super(0x02);
        this.response = response;
    }

    @Override
    protected void writeImpl(LoginConnection con) {
        writeD(response.getMessageId());
    }
}
