/*
 * This file is part of aion-lightning <aion-lightning.com>
 *
 * aion-lightning is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-lightning is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-lightning. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.assemblednpc;

import javolution.util.FastList;

/**
 *
 * @author xTz
 */
public class AssembledNpc {

    private FastList<AssembledNpcPart> assembledPatrs = new FastList<>();
    private long spawnTime = System.currentTimeMillis();
    private int routeId;
    private int mapId;

    public AssembledNpc(int routeId, int mapId, int liveTime, FastList<AssembledNpcPart> assembledPatrs) {
        this.assembledPatrs = assembledPatrs;
        this.routeId = routeId;
        this.mapId = mapId;
    }

    public FastList<AssembledNpcPart> getAssembledParts() {
        return assembledPatrs;
    }

    public int getRouteId() {
        return routeId;
    }

    public int getMapId() {
        return mapId;
    }

    public long getTimeOnMap() {
        return System.currentTimeMillis() - spawnTime;
    }
}
