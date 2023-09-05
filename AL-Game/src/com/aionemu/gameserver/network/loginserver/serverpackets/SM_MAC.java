package com.aionemu.gameserver.network.loginserver.serverpackets;

import com.aionemu.gameserver.network.loginserver.LoginServerConnection;
import com.aionemu.gameserver.network.loginserver.LsServerPacket;

/**
 *
 * @author nrg, Alex
 *
 */
public class SM_MAC extends LsServerPacket {

    private final int accountId;
    private final String address;
    private final String hdd;

    public SM_MAC(int accountId, String address, String hdd) {
        super(13);
        this.accountId = accountId;
        this.address = address;
        this.hdd = hdd;
    }

    @Override
    protected void writeImpl(LoginServerConnection con) {
        writeD(accountId);
        writeS(address);
        writeS(hdd);
    }
}
