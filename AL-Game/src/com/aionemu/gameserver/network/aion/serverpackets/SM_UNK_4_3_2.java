/*
 * SAO Project
 */
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 *
 * @author Alex
 */
public class SM_UNK_4_3_2 extends AionServerPacket {

    private final int unk;

    public SM_UNK_4_3_2(int unk) {
        this.unk = unk;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeH(unk);
    }
}
