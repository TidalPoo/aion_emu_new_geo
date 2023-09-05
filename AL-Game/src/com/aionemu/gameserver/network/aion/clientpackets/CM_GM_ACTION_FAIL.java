/**
 * SAO Project
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
public class CM_GM_ACTION_FAIL extends AionClientPacket {

    public CM_GM_ACTION_FAIL(int opcode, AionConnection.State state, AionConnection.State... restStates) {
        super(opcode, state, restStates);
    }

    @Override
    protected void readImpl() {
        // empty packet
    }

    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        PacketSendUtility.sendMessage(player, "CM_GM_ACTION_FAIL");
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
