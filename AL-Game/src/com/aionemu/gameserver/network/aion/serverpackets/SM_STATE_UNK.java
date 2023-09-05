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
public class SM_STATE_UNK extends AionServerPacket {

    private int unk = 1;
    public SM_STATE_UNK(int unk) {
        this.unk = unk;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeD(unk);//objId?
    }
}
