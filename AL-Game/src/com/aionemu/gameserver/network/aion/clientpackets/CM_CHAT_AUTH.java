package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.chatserver.ChatServer;

/**
 * Client sends this only once.
 *
 * @author Luno
 * @modified Alex
 */
public class CM_CHAT_AUTH extends AionClientPacket {

    /**
     * Constructor
     *
     * @param opcode
     * @param state
     * @param restStates
     */
    public CM_CHAT_AUTH(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    @Override
    protected void readImpl() {
        Player player = getConnection().getActivePlayer();
        @SuppressWarnings("unused")
        int objectId = readD(); // lol NC
        StringBuilder sb = new StringBuilder();
        byte[] macAddress = readB(6);
        int c = 0;
        for (byte b : macAddress) {
            c++;
            sb.append(String.format("%X", b));
            if (c < 6) {
                sb.append("-");
            }
        }
        /* if (!getConnection().getMacAddress().equals(sb.toString())) {
         for (Player gm : GMService.getInstance().getGMs()) {
         PacketSendUtility.sendMessage(gm, "WARNING " + player.getName() + " MacAddressTo: " + sb.toString() + "\nMacAddressConnect: " + getConnection().getMacAddress());
         }
         //getConnection().setMacAddress(sb.toString());
         }*/
        //todo mb online bonus?
    }

    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        if (GSConfig.ENABLE_CHAT_SERVER) {
            // this packet is sent sometimes after logout from world
            if (!player.isInPrison()) {
                ChatServer.getInstance().sendPlayerLoginRequst(player);
            }
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
