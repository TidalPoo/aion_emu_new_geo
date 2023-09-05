/*
 * AionLight project
 */
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 *
 * @author Alex
 */
public class SM_ONLINE_STATUS extends AionServerPacket {

    private final int status;

    public SM_ONLINE_STATUS(int status) {
        this.status = status;
    }

    @Override
    protected void writeImpl(AionConnection client) {
        writeC(status);
    }
}
