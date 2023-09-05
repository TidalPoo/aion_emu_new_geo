package com.aionemu.gameserver.services;

import com.aionemu.gameserver.configs.main.FastTrackConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SERVER_IDS;
import com.aionemu.gameserver.services.transfers.FastTrack;
import com.aionemu.gameserver.utils.PacketSendUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alex
 */
public class FastTrackService {

    private static final FastTrackService instance = new FastTrackService();
    private Logger log = LoggerFactory.getLogger(FastTrackService.class);

    public static FastTrackService getInstance() {
        return instance;
    }

    /**
     *
     * @param player
     */
    public void checkAuthorizationRequest(Player player) {
        int upto = FastTrackConfig.FASTTRACK_MAX_LEVEL;
        if (player.getLevel() > upto) {
            return;
        }
        PacketSendUtility.sendPacket(player, new SM_SERVER_IDS(new FastTrack(FastTrackConfig.FASTTRACK_SERVER_ID, true, 1, upto)));
    }

    public void handleMoveThere(Player player) {
        log.info("Fast Track Service : Move Player " + player.getName());
    }
}
