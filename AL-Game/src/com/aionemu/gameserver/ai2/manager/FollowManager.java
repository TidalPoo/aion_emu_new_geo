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
package com.aionemu.gameserver.ai2.manager;

import com.aionemu.gameserver.ai2.AI2Logger;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Npc;

/**
 * @author ATracer
 */
public class FollowManager {

    public static void targetTooFar(NpcAI2 npcAI) {
        Npc npc = npcAI.getOwner();
        if (npcAI.isLogging()) {
            AI2Logger.info(npcAI, "Follow manager - targetTooFar");
        }
        if (npcAI.isMoveSupported()) {
            npc.getMoveController().moveToTargetObject();
        }
    }
}
