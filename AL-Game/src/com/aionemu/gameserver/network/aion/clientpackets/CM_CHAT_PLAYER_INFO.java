package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CHAT_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.services.weddings.WeddingService;
import com.aionemu.gameserver.utils.ChatUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

/**
 * @author prix
 * @modified Alex
 */
public class CM_CHAT_PLAYER_INFO extends AionClientPacket {

    private String playerName;

    public CM_CHAT_PLAYER_INFO(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    @Override
    protected void readImpl() {
        playerName = readS();
    }

    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        playerName = ChatUtil.getRealAdminName(playerName);
        //NEW WEDDINGS
        if (playerName.contains("\ue020")) {
            playerName = WeddingService.getRealWeddingsName(playerName);
        }
        final Player target = World.getInstance().findPlayer(playerName);
        if (target == null) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ASK_PCINFO_LOGOFF);
            return;
        }
        if (!player.getKnownList().knowns(target)) {
            if (player.isGM() || WeddingService.getInstance().isMarriedTo(player, target)) {
                PacketSendUtility.sendMessage(player, "Player: " + playerName);
                RequestResponseHandler responseHandler = new RequestResponseHandler(player) {
                    @Override
                    public void acceptRequest(Creature p2, Player p) {
                        if (!p.isGM()) {
                            WeddingService.getInstance().teleport(p);
                        } else {
                            TeleportService2.teleportTo(p, target.getWorldId(), target.getX(),
                                    target.getY(), target.getZ(), (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                        }
                    }

                    @Override
                    public void denyRequest(Creature p2, Player p) {
                    }
                };
                boolean requested = player.getResponseRequester().putRequest(902247, responseHandler);
                if (requested) {
                    PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(902247, 0, 0, "Совершить телепорт к персонажу: " + playerName + " ?"));
                }
            }
            PacketSendUtility.sendPacket(player, new SM_CHAT_WINDOW(target, false));
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
