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
public class SM_95 extends AionServerPacket {

    private int action = 0;

    public SM_95(int action) {
        this.action = action;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeC(action);
        switch (action) {
            case 0:
                writeC(0);
                writeD(1);
                break;
            case 1:
                writeD(0);
                writeC(4);
                writeH(5);
                break;
            case 2:
                writeD(0);
                break;
        }
    }
}
