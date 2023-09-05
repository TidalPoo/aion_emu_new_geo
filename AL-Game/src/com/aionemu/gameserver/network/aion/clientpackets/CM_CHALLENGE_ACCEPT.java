/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
public class CM_CHALLENGE_ACCEPT extends AionClientPacket {

    private int playerObjId;

    public CM_CHALLENGE_ACCEPT(int opcode, AionConnection.State state, AionConnection.State... restStates) {
        super(opcode, state, restStates);
    }

    @Override
    protected void readImpl() {
        playerObjId = readD();
        readD();
        readD();
        readD();
    }

    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        PacketSendUtility.sendMessage(player, "playerObjId: " + playerObjId);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
