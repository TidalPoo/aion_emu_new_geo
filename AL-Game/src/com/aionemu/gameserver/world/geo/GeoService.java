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
package com.aionemu.gameserver.world.geo;

import com.aionemu.gameserver.configs.main.GeoDataConfig;
import com.aionemu.gameserver.geoEngine.math.Vector3f;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.network.rest.GeoHttp;
import com.aionemu.gameserver.utils.MathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ATracer
 */
public class GeoService {

    private static final Logger log = LoggerFactory.getLogger(GeoService.class);
    private GeoData geoData;

    /**
     * Initialize geodata based on configuration, load necessary structures
     */
    public void initializeGeo() {
        switch (getConfiguredGeoType()) {
            case GEO_MESHES:
                geoData = new RealGeoData();
                break;
            case NO_GEO:
                geoData = new DummyGeoData();
                break;
        }
        log.info("Configured Geo type: " + getConfiguredGeoType());
        geoData.loadGeoMaps();
    }

    public void setDoorState(int worldId, int instanceId, String name, boolean isOpened) {
        if (GeoDataConfig.GEO_ENABLE) {
            geoData.getMap(worldId).setDoorState(instanceId, name, isOpened);
        }
    }

    /**
     * @param worldId
     * @param x
     * @param y
     * @param z
     * @param instanceId
     * @return
     */
    public float getZAfterMoveBehind(int worldId, float x, float y, float z, int instanceId) {
//        if (GeoDataConfig.GEO_ENABLE) {
//            return getZ(worldId, x, y, z, 0, instanceId);
//        }
//        return getZ(worldId, x, y, z, 0.5f, instanceId);
        return getZ(worldId, x, y, z, 0, instanceId);
    }

    /**
     * @param object
     * @return
     */
    public float getZ(VisibleObject object) {
        //return geoData.getMap(object.getWorldId()).getZ(object.getX(), object.getY(), object.getZ(), object.getInstanceId());
        return GeoHttp.getFirstCollision(object.getWorldId(), object.getX(),  object.getY(), object.getZ());
    }

    /**
     * @param worldId
     * @param x
     * @param y
     * @param z
     * @param defaultUp
     * @param instanceId
     * @return
     */
    public float getZ(int worldId, float x, float y, float z, float defaultUp, int instanceId) {
//        float newZ = geoData.getMap(worldId).getZ(x, y, z, instanceId);
//        if (!GeoDataConfig.GEO_ENABLE) {
//            newZ += defaultUp;
//        }/*
//         else {
//         newZ += 0.5f;
//         }*/
//        return newZ;
        return GeoHttp.getFirstCollision(worldId, x, y, z);
    }

    /**
     * @param worldId
     * @param x
     * @param y
     * @return
     */
    public float getZ(int worldId, float x, float y) {
        return GeoHttp.getFirstZ(worldId, x, y);
    }

    public String getDoorName(int worldId, String meshFile, float x, float y, float z) {
        return geoData.getMap(worldId).getDoorName(worldId, meshFile, x, y, z);
    }

    /**
     * @param object
     * @param target
     * @return
     */
    public boolean canSee(VisibleObject object, VisibleObject target) {
        float limit = (float) (MathUtil.getDistance(object, target) - target.getObjectTemplate().getBoundRadius().getCollision());
        if (limit <= 0) {
            return true;
        }
        return GeoHttp.canSee(object.getWorldId(), object.getX(), object.getY(),object.getZ() + object.getObjectTemplate().getBoundRadius().getUpper() / 2,
                target.getX(), target.getY(),target.getZ() + target.getObjectTemplate().getBoundRadius().getUpper() / 2, limit);
    }

    public boolean isGeoOn() {
        return GeoDataConfig.GEO_ENABLE;
    }

    public Vector3f getClosestCollision(Creature object, float x, float y, float z, boolean changeDirection, byte intentions) {
        return geoData.getMap(object.getWorldId()).getClosestCollision(object.getX(), object.getY(), object.getZ(), x, y, z, changeDirection,
                object.isInFlyingState(), object.getInstanceId(), intentions);
    }

    public GeoType getConfiguredGeoType() {
        if (GeoDataConfig.GEO_ENABLE) {
            return GeoType.GEO_MESHES;
        }
        return GeoType.NO_GEO;
    }

    public static final GeoService getInstance() {
        return SingletonHolder.instance;
    }

    @SuppressWarnings("synthetic-access")
    private static final class SingletonHolder {

        protected static final GeoService instance = new GeoService();
    }
}
