/*
 * AionLight project
 */
package com.aionemu.gameserver.services.fishing;

import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 *
 * @author Alex
 */
public class FishingService {

    private static int timePeriod = 30 * 1000;// 30 * 1000 = 30 seconds

    public static void start(Player player) {
        if (!player.isFishing()) {
            if (player.isWeater()) {
                RequestResponseHandler responseHandler = new RequestResponseHandler(player) {
                    @Override
                    public void acceptRequest(Creature requester, Player player) {
                        player.setFishing(true);
                        startFishing(player);
                    }

                    @Override
                    public void denyRequest(Creature requester, Player player) {
                        player.setFishing(false);
                    }
                };
                boolean requested = player.getResponseRequester().putRequest(902247, responseHandler);
                if (requested) {
                    PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(902247, 0, 0,
                            "Желаете начать рыбалку?"));
                }
            }
        }
    }

    public static void stop(Player player) {
        if (player.isWeater()) {
        }
    }

    /**
     * @param player
     */
    private static void startFishing(Player player) {
        if (!isFishing(player)) {
            scheduleFishingTask(player);
        }
    }

    /**
     * @param player
     */
    public static void stopFishing(Player player) {
        if (isFishing(player)) {
            player.getController().cancelTask(TaskId.FISHING);
        }
        player.setFishing(false);
        PacketSendUtility.sendMessage(player, "Рыбалка завершена");
    }

    /**
     * @param player
     * @return
     */
    private static boolean isFishing(Player player) {
        return player.getController().getTask(TaskId.FISHING) != null;
    }

    /**
     * @param player
     */
    private static void scheduleFishingTask(final Player player) {
        player.getController().addTask(TaskId.FISHING, ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {

                if (!player.getLifeStats().isAlreadyDead()) {
                    if (player.isFishing()) {

                    }
                } else {
                    stopFishing(player);
                }
            }
        }, 0, timePeriod));//2 sec
    }
}
