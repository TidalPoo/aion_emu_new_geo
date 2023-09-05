/*
 * AionLight project
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
public class CM_ADMIN_PANEL extends AionClientPacket {

    public CM_ADMIN_PANEL(int opcode, AionConnection.State state, AionConnection.State... restStates) {
        super(opcode, state, restStates);
    }
    private int s;
    private int c;

    @Override
    protected void readImpl() {
        s = readH();
        c = readC();
    }

    @Override
    protected void runImpl() {
        Player gm = getConnection().getActivePlayer();
        PacketSendUtility.sendMessage(gm, "CM_ADMIN_PANEL readH: " + s);
        PacketSendUtility.sendMessage(gm, "CM_ADMIN_PANEL readC: " + c);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
