/*
 * AionLightProject
 */
package com.aionemu.gameserver.model.gameobjects.player;

/**
 *
 * @author DoYrdenDzirt
 */
public class TeleportTask {

    public int MapId, instanceId;
    public float x, y, z, h;

    public TeleportTask(int MapId, int instanceId, float x, float y, float z, float h) {
        this.MapId = MapId;
        this.instanceId = instanceId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.h = h;
    }
}
