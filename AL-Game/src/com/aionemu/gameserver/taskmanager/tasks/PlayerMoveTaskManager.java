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
package com.aionemu.gameserver.taskmanager.tasks;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.taskmanager.AbstractPeriodicTaskManager;
import javolution.util.FastMap;

/**
 * @author ATracer
 */
public class PlayerMoveTaskManager extends AbstractPeriodicTaskManager {

    private final FastMap<Integer, Creature> movingPlayers = new FastMap<Integer, Creature>().shared();

    private PlayerMoveTaskManager() {
        super(200);
    }

    public void addPlayer(Creature player) {
        movingPlayers.put(player.getObjectId(), player);
    }

    public void removePlayer(Creature player) {
        movingPlayers.remove(player.getObjectId());
    }

    @Override
    public void run() {
        for (FastMap.Entry<Integer, Creature> e = movingPlayers.head(), mapEnd = movingPlayers.tail(); (e = e.getNext()) != mapEnd;) {
            Creature player = e.getValue();
            player.getMoveController().moveToDestination();
        }
    }

    public static final PlayerMoveTaskManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {

        private static final PlayerMoveTaskManager INSTANCE = new PlayerMoveTaskManager();
    }
}
