/*
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 * aion-lightning is private software: you can redistribute it and or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Private Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-lightning is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.ai2.handler;

import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.NpcAI2;

/**
 * @author ATracer
 */
public class SpawnEventHandler {

    /**
     * @param npcAI
     */
    public static void onSpawn(NpcAI2 npcAI) {
        if (npcAI.setStateIfNot(AIState.IDLE)) {
            if (npcAI.getOwner().getPosition().isMapRegionActive()) {
                npcAI.think();
            }
        }
    }

    /**
     * @param npcAI
     */
    public static void onDespawn(NpcAI2 npcAI) {
        npcAI.setStateIfNot(AIState.DESPAWNED);
    }

    /**
     * @param npcAI
     */
    public static void onRespawn(NpcAI2 npcAI) {
        npcAI.getOwner().getMoveController().resetMove();
    }

}