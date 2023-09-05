/*
 * This file is part of aion-engine <aion-engine.com>
 *
 * aion-engine is private software: you can redistribute it and or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Private Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with aion-engine.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.questEngine.task;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.templates.spawns.SpawnSearchResult;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.zone.ZoneName;
import java.util.concurrent.Future;

/**
 * @author ATracer
 */
public class QuestTasks {

    /**
     * Schedule new following checker task
     *
     * @param env
     * @param player
     * @param npc
     * @param target
     * @return
     */
    public static final Future<?> newFollowingToTargetCheckTask(final QuestEnv env, Npc npc, Npc target) {
        return ThreadPoolManager.getInstance().scheduleAtFixedRate(
                new FollowingNpcCheckTask(env, new TargetDestinationChecker(npc, target)), 1000, 1000);
    }

    /**
     * Schedule new following checker task
     *
     * @param env
     * @param player
     * @param npc
     * @param npcTargetId
     * @return
     */
    public static final Future<?> newFollowingToTargetCheckTask(final QuestEnv env, Npc npc, int npcTargetId) {
        SpawnSearchResult searchResult = DataManager.SPAWNS_DATA2.getFirstSpawnByNpcId(npc.getWorldId(), npcTargetId);
        if (searchResult == null) {
            throw new IllegalArgumentException("Supplied npc doesn't exist: " + npcTargetId);
        }
        return ThreadPoolManager.getInstance().scheduleAtFixedRate(
                new FollowingNpcCheckTask(env, new CoordinateDestinationChecker(npc, searchResult.getSpot().getX(), searchResult
                                .getSpot().getY(), searchResult.getSpot().getZ())), 1000, 1000);
    }

    /**
     * Schedule new following checker task
     *
     * @param env
     * @param npc
     * @param x
     * @param y
     * @param z
     * @return
     */
    public static final Future<?> newFollowingToTargetCheckTask(final QuestEnv env, Npc npc, float x, float y, float z) {
        return ThreadPoolManager.getInstance().scheduleAtFixedRate(
                new FollowingNpcCheckTask(env, new CoordinateDestinationChecker(npc, x, y, z)), 1000, 1000);
    }

    public static final Future<?> newFollowingToTargetCheckTask(final QuestEnv env, Npc npc, ZoneName zoneName) {
        return ThreadPoolManager.getInstance().scheduleAtFixedRate(
                new FollowingNpcCheckTask(env, new ZoneChecker(npc, zoneName)), 1000, 1000);
    }
}
