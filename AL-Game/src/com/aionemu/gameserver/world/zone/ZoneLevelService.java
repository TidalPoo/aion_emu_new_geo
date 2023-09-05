/*
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.world.zone;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;

/**
 * @author ATracer
 * @modified Alex
 */
public class ZoneLevelService {

    private static final long DROWN_PERIOD = AdminConfig.DAMAGE_WATER_DROWN_PERIOD;

    /**
     * Check water level (start drowning) and map death level (die)
     *
     * @param player
     */
    public static void checkZoneLevels(Player player) {
        World world = World.getInstance();
        float z = player.getZ();

        if (player.getLifeStats().isAlreadyDead() || AdminConfig.ENABLE_GM_NO_DAMAGE_WATER && player.isGM()) {
            return;
        }

        if (z < world.getWorldMap(player.getWorldId()).getDeathLevel()) {
            player.getController().die();
            return;
        }

        /*float ph = player.getPlayerAppearance().getHeight() * 1.8f;
         if (z < world.getWorldMap(player.getWorldId()).getWaterLevel() - ph) {
         FishingService.start(player);
         } else {
         FishingService.stopFishing(player);
         }*/
        /**
         * Damage to the under water
         */
        //if (!player.isFishing()) {
        float playerheight = player.getPlayerAppearance().getHeight() * 1.4f;
        if (z < world.getWorldMap(player.getWorldId()).getWaterLevel() - playerheight) {
            startDrowning(player);
            player.setWater(true);
        } else {
            stopDrowning(player);
            player.setWater(false);
        }
        //}
    }

    /**
     * @param player
     */
    private static void startDrowning(Player player) {
        if (!isDrowning(player)) {
            scheduleDrowningTask(player);
        }
    }

    /**
     * @param player
     */
    private static void stopDrowning(Player player) {
        if (isDrowning(player)) {
            player.getController().cancelTask(TaskId.DROWN);
        }

    }

    /**
     * @param player
     * @return
     */
    private static boolean isDrowning(Player player) {
        return player.getController().getTask(TaskId.DROWN) != null;
    }

    /**
     * @param player
     */
    private static void scheduleDrowningTask(final Player player) {
        player.getController().addTask(TaskId.DROWN, ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                int value = Math.round(player.getLifeStats().getMaxHp() / 10);
                // TODO retail emotion, attack_status packets sending
                if (!player.getLifeStats().isAlreadyDead()) {
                    if (!player.isInvul()) {
                        player.getLifeStats().reduceHp(value, player);
                        player.getLifeStats().sendHpPacketUpdate();
                    }
                } else {
                    stopDrowning(player);
                }
            }
        }, 0, DROWN_PERIOD));//2 sec
    }
}
