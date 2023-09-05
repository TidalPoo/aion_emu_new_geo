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
public class SM_1FE extends AionServerPacket {

    public SM_1FE() {

    }

    @Override
    protected void writeImpl(AionConnection client) {
        writeD(0x00);
        writeD(0x00);
        writeC(0x00);
        writeD(0x00);
        writeD(0x00);
        writeD(0x00);
        writeD(0x00);
        writeD(0x00);
    }
}
