package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Ever'
 */
public class SM_FAST_TRACK extends AionServerPacket {

    public SM_FAST_TRACK() { // SM_UNK_3_5_1
    }

    @Override
    protected void writeImpl(AionConnection con) {
        Player player = con.getActivePlayer();
        writeD(1); //unk
        writeD(0); //unk
        writeD(player.getObjectId());
        writeH(NetworkConfig.GAMESERVER_ID);
        writeD(0); //unk
        writeD(0); //unk
    }
}
