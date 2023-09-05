/*
 * SAO Project
 */
package com.aionemu.gameserver.services.custom.ffa;

import java.util.List;
import javolution.util.FastList;

/**
 *
 * @author Alex
 */
public class KillMap {

    private int kills;
    private final FastList<KillList> list = new FastList<>();
    private String victimName;
    private int worldId;
    private int instanceId;

    public int getKills() {
        return kills;
    }

    public void incrementkills() {
        kills++;
    }

    public void decrimentkills() {
        kills--;
    }

    public void setName(String name) {
        victimName = name;
    }

    public String getVictimName() {
        return victimName;
    }

    public void setWorldIdAndInstanceId(int w, int i) {
        this.worldId = w;
        this.instanceId = i;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public int getWorldId() {
        return worldId;
    }

    public void addPlayer(KillList killList) {
        list.add(killList);
    }

    public List<KillList> getList() {
        return list;
    }
}
