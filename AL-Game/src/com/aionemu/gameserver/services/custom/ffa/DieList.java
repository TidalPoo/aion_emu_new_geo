/*
 * SAO Project by Alex
 */
package com.aionemu.gameserver.services.custom.ffa;

/**
 *
 * @author Alex
 */
public class DieList {

    private final int unk;
    private final long time;
    private final int worldId;
    private final int instanceId;

    public DieList(int unk, long time, int worldId, int instanceId) {
        this.unk = unk;
        this.time = time;
        this.worldId = worldId;
        this.instanceId = instanceId;
    }

    public int getUnk() {
        return unk;
    }

    public long getTime() {
        return time;
    }

    public int getWorldId() {
        return worldId;
    }

    public int getInstanceId() {
        return instanceId;
    }
}
