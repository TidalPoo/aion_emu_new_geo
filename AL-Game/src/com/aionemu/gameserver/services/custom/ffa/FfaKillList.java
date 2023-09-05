/*
 * SAO Project
 */
package com.aionemu.gameserver.services.custom.ffa;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alex
 */
public class FfaKillList {

    private final Map<Integer, KillMap> killList;
    private int allKills;
    private final String name;
    private final int ownerId;
    private int superKill;

    public int getOwnerId() {
        return ownerId;
    }

    public String getName() {
        return name;
    }

    public KillMap getKillMap(int oId) {
        return killList.get(oId);
    }

    public int getAllKills() {
        return killList.size();
    }

    public void setAllKills(int allKills) {
        this.allKills = allKills;
    }

    public Map<Integer, KillMap> getKillList() {
        return killList;
    }

    public int getSuperKill() {
        return superKill;
    }

    public void increaseSuperKill() {
        this.superKill++;
    }

    public void setSuperKill(int superKill) {
        this.superKill = superKill;
    }

    public FfaKillList(int ownerId, String name) {
        killList = new HashMap<>();
        this.ownerId = ownerId;
        this.name = name;
    }

    public int getKillsFor(int victimId) {
        KillMap killTimes = killList.get(victimId);
        if (killTimes == null) {
            return 0;
        }
        return killTimes.getKills();
    }

    public void addKillFor(String name, int victimId, int worldId, int instanceId) {
        KillMap killTimes = killList.get(victimId);
        if (killTimes == null) {
            killTimes = new KillMap();
            killTimes.setName(name);
            killList.put(victimId, killTimes);
        }
        int un = killTimes.getList().size();
        killTimes.addPlayer(new KillList(++un, System.currentTimeMillis(), worldId, instanceId));
        //killTimes.setWorldIdAndInstanceId(worldId, instanceId);
        killTimes.incrementkills();
        increaseSuperKill();
    }
}
