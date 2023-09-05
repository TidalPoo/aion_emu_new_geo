/*
 * SAO Project by Alex
 */
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 *
 * @author Alex
 */
public class CM_LEVEL_VERSION_SEND extends AionClientPacket {

    private int unk;

    public CM_LEVEL_VERSION_SEND(int opcode, AionConnection.State state, AionConnection.State... restStates) {
        super(opcode, state, restStates);
    }

    @Override
    protected void readImpl() {
        unk = readC();
    }

    @Override
    protected void runImpl() {
        Player p = getConnection().getActivePlayer();
        PacketSendUtility.sendMessage(p, unk + "");
    }
}
