package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.player.SecurityTokenService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author xXMashUpXx Что это блять за пакет?
 */
public class CM_SECURITY_TOKEN_REQUEST extends AionClientPacket {

    private String wtf;

    /**
     * @param opcode
     * @param state
     * @param restStates
     */
    public CM_SECURITY_TOKEN_REQUEST(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    /* (non-Javadoc)
     * @see com.aionemu.commons.network.packet.BaseClientPacket#readImpl()
     */
    @Override
    protected void readImpl() {
    }

    /* (non-Javadoc)
     * @see com.aionemu.commons.network.packet.BaseClientPacket#runImpl()
     */
    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        if (player == null) {
            return;
        }

        if (wtf != null) {
            PacketSendUtility.sendMessage(player, wtf);
        }

        if (player.getPlayerAccount().getSecurityToken() == null) {
            SecurityTokenService.getInstance().generateToken(player);
        } else {
            SecurityTokenService.getInstance().sendToken(player, player.getPlayerAccount().getSecurityToken());
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
