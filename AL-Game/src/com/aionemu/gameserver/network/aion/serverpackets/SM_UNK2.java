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
public class SM_UNK2 extends AionServerPacket {

    public SM_UNK2() {
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeD(0);
        writeH(1);
        writeH(1);
        writeH(0);
    }
}
