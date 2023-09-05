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
public class CM_CHALLENGE_LEGION  extends AionClientPacket {

    private int action;
    private int taskOwner;
    private int ownerType;
    private int objectId;
    private int date;

    public CM_CHALLENGE_LEGION(int opcode, AionConnection.State state, AionConnection.State... restStates) {
        super(opcode, state, restStates);
    }

    @Override
    protected void readImpl() {
        action = readC();
        taskOwner = readD();
        ownerType = readC();//MultiValue - challengeType
        objectId = readD();//player_id/legion_id
        date = readD();//UnixDate
    }

    @Override
    protected void runImpl() {
        Player p = getConnection().getActivePlayer();
        PacketSendUtility.sendMessage(p, action + "");
    }
}
