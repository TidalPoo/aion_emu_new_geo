/**
 * SAO Project
 */
package com.aionemu.gameserver.services;

import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 21.12.2014.
 */
public class DanceService {

    public DanceService() {
        initializeNpcs();
    }
    private static final List<Npc> list = new ArrayList<>();
    public final int[] dance = {19, 64, 65, 66, 67, 68, 84, 116, 117, 118, 119, 120, 124, 125, 126, 127, 128, 129, 133, 134, 135, 136, 137, 138, 139};
    private int save = 0;

    private int generateDanceId() {
        int danceId = dance[save];
        if (save == dance.length - 1) {
            save = 0;
        } else {
            save++;
        }
        return danceId;
    }

    public void startGenerateToDanceInIDUI() {
        int danceId = generateDanceId();
        for (Npc npc : list) {
            PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.EMOTE, danceId, npc.getObjectId()));
        }
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                for (Npc npc : list) {
                    PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.EMOTE, 0, npc.getObjectId()));
                }
                ThreadPoolManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        startGenerateToDanceInIDUI();
                    }
                }, 15000);
            }
        }, 40000);
    }

    public void onEnterZone(final Player player) {
        for (Npc npc : list) {
            PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.EMOTE, 0, npc.getObjectId()));
        }
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (list != null) {
                    startGenerateToDanceInIDUI();
                }
            }
        }, 10 * 1000);

    }

    public final void initializeNpcs() {
        int[] npcIds = {831633, 831634, 831635, 831636, 831637, 831638, 831639, 831640, 831641, 831642,
            831643, 831644, 831645, 831646, 831647, 831648, 831649, 831650, 831651, 831652, 831653};
        for (Npc npc : World.getInstance().getNpcs()) {
            if (npc.getWorldId() == 600080000) {
                for (int npcId : npcIds) {
                    if (npc.getNpcId() == npcId) {
                        list.add(npc);
                    }
                }
            }
        }
        startGenerateToDanceInIDUI();
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final DanceService instance = new DanceService();
    }

    public static DanceService getInstance() {
        return SingletonHolder.instance;
    }
}
