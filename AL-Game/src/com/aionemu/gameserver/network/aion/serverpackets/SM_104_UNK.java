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
public class SM_104_UNK extends AionServerPacket {

    public SM_104_UNK() {
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeH(1);
        writeH(1);
        writeD(1);
    }
}
