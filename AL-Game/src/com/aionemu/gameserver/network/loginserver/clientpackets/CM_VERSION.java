/*
 * SAO Project
 */
package com.aionemu.gameserver.network.loginserver.clientpackets;

import com.aionemu.gameserver.network.loginserver.LsClientPacket;

/**
 *
 * @author Alex
 */
public class CM_VERSION extends LsClientPacket {

    public CM_VERSION(int opCode) {
        super(opCode);
    }
    private int countryCode;

    @Override
    protected void readImpl() {
        countryCode = readD();
    }

    @Override
    protected void runImpl() {
        getConnection().setCC(countryCode);
    }
}
