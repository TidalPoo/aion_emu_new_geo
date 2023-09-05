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
package com.aionemu.gameserver.world;

import com.aionemu.gameserver.instance.InstanceEngine;
import com.aionemu.gameserver.instance.handlers.InstanceHandler;

/**
 * @author ATracer
 */
public class WorldMapInstanceFactory {

    /**
     * @param parent
     * @param instanceId
     * @return
     */
    public static WorldMapInstance createWorldMapInstance(WorldMap parent, int instanceId) {
        return createWorldMapInstance(parent, instanceId, 0, InstanceType.NORMAL);
    }

    public static WorldMapInstance createWorldMapInstance(WorldMap parent, int instanceId, int ownerId) {
        return createWorldMapInstance(parent, instanceId, ownerId, InstanceType.NORMAL);
    }

    public static WorldMapInstance createEventWorldMapInstance(WorldMap parent, int instanceId, int eventHandlerId) {
        WorldMapInstance worldMapInstance = null;
        if (parent.getMapId() == WorldMapType.RESHANTA.getId()) {
            worldMapInstance = new WorldMap3DInstance(parent, instanceId);
        } else {
            worldMapInstance = new WorldMap2DInstance(parent, instanceId, 0);
        }
        InstanceHandler instanceHandler = InstanceEngine.getInstance().getNewEventInstanceHandler(eventHandlerId);
        worldMapInstance.setInstanceHandler(instanceHandler);
        return worldMapInstance;
    }

    public static WorldMapInstance createFfaWorldMapInstance(WorldMap parent, int instanceId, int ffaTypeId) {
        WorldMapInstance worldMapInstance = null;
        if (parent.getMapId() == WorldMapType.RESHANTA.getId()) {
            worldMapInstance = new WorldMap3DInstance(parent, instanceId);
        } else {
            worldMapInstance = new WorldMap2DInstance(parent, instanceId, 0);
        }
        InstanceHandler instanceHandler = InstanceEngine.getInstance().getNewFfaInstanceHandler(ffaTypeId);
        worldMapInstance.setInstanceHandler(instanceHandler);
        return worldMapInstance;
    }

    public static WorldMapInstance createWorldMapInstance(WorldMap parent, int instanceId, int ownerId, InstanceType type) {
        WorldMapInstance worldMapInstance = null;
        if (parent.getMapId() == WorldMapType.RESHANTA.getId()) {
            worldMapInstance = new WorldMap3DInstance(parent, instanceId);
        } else {
            worldMapInstance = new WorldMap2DInstance(parent, instanceId, ownerId);
        }
        InstanceHandler instanceHandler = InstanceEngine.getInstance().getNewInstanceHandler(parent.getMapId());
        worldMapInstance.setInstanceHandler(instanceHandler);
        worldMapInstance.setInstanceType(type);
        return worldMapInstance;
    }
}
