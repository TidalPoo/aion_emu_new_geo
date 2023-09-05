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
public class SM_SERVER_TRANSFER extends AionServerPacket {

    private final int mapId;

    public SM_SERVER_TRANSFER(int mapId) {
        this.mapId = mapId;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeD(0);//unk
        writeD(0);//unk
        writeC(0);//unk
        writeD(mapId);
    }
}
