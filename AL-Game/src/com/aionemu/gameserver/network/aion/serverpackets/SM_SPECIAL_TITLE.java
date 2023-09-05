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
public class SM_SPECIAL_TITLE extends AionServerPacket {

    String title;
    int objectId;

    public SM_SPECIAL_TITLE(String title, int objectId) {
        this.title = title;
        this.objectId = objectId;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeD(objectId);
        writeS(title);
        writeC(0);
    }
}
