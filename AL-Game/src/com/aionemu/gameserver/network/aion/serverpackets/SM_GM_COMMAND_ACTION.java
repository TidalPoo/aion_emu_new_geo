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
public class SM_GM_COMMAND_ACTION extends AionServerPacket {

    private int action = 0;

    public SM_GM_COMMAND_ACTION(int action) {
        this.action = action;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeC(action);
        switch (action) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                writeC(action);
                break;
            case 3:
                break;
            default:
                break;
        }
    }
}
