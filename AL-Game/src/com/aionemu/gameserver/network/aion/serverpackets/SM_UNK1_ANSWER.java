/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 *
 * @author Alex
 */
public class SM_UNK1_ANSWER extends AionServerPacket {

    private final int size;
    private final int type;

    public SM_UNK1_ANSWER(int size, int type) {
        this.size = size;
        this.type = type;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeD(size);//size
        //if (size == 0) {
            writeC(type);//unk
        //}
    }
}
