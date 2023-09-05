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
public class SM_1B3 extends AionServerPacket {

    public SM_1B3() {
    }

    @Override
    protected void writeImpl(AionConnection client) {
        writeD(0x00);// mask
        writeH(0x00); // size
        writeH(0x00);
        writeH(0x00);
    }
}
