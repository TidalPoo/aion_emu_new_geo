/**
 * SAO Project
 */
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 *
 * @author Alex
 */
public class CM_GM_COMMAND_ACTION extends AionClientPacket {

    private int action;
    private int targetObjId;

    public CM_GM_COMMAND_ACTION(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    @Override
    protected void readImpl() {
        Player player = getConnection().getActivePlayer();
        action = readC();
        switch (action) {
            case 0:
                //следить
                targetObjId = readD();
                break;
            case 1:
                //отменить слежку
                break;
            default:
                break;
        }
    }

    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        if (targetObjId == player.getObjectId()) {
            PacketSendUtility.sendMessage(player, "Нельзя использовать на себя.");
            //return;
        }

        /*if (action == 0) {
         player.setGMSPY(0);
         } else {
         player.setGMSPY(targetObjId);
         }*/
        // sendPacket(new SM_GM_COMMAND_ACTION(action));
        //SM_GM_SPY
        // PacketSendUtility.sendMessage(player, "t: " + targetObjId + "\n"
        //        + "ac: " + action);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
