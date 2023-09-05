/**
 * SAO Project
 */
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 *
 * @author Alex
 */
public class SM_GM_SPY extends AionServerPacket {

    private final int unk3;

    private final String text;
    private final int unk1;
    private final int unk2;

    public SM_GM_SPY(int unk1, int unk2, String text, int unk3) {
        this.unk1 = unk1;
        this.unk2 = unk2;
        this.text = text;
        this.unk3 = unk3;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeC(unk1);//action
        writeD(unk2);//pObjId
        writeS(text);//name
        writeH(unk3);//adminObjId
    }
}
