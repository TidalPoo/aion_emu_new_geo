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
public class FfaDieList {

    private final Map<Integer, DieMap> dieList;
    private final String name;
    private int allDie;
    private final int ownerId;

    public int getOwnerId() {
        return ownerId;
    }

    public String getName() {
        return name;
    }

    public FfaDieList(int ownerId, String name) {
        dieList = new HashMap<>();
        this.ownerId = ownerId;
        this.name = name;
    }

    public int getDieFor(int winnerId) {
        DieMap killTimes = dieList.get(winnerId);
        if (killTimes == null) {
            return 0;
        }
        return killTimes.getDie();
    }

    public int getAllDie() {
        return dieList.size();
    }

    public Map<Integer, DieMap> getDieList() {
        return dieList;
    }

    public void setAllDie(int allDie) {
        this.allDie = allDie;
    }

    public void addDieFor(String name, int winnerId, int worldId, int instanceId) {
        DieMap dieTimes = dieList.get(winnerId);
        if (dieTimes == null) {
            dieTimes = new DieMap();
            dieTimes.setName(name);
            //dieTimes.setWorldIdAndInstanceId(worldId, instanceId);
            dieList.put(winnerId, dieTimes);
        }
        int un = dieTimes.getList().size();
        dieTimes.addPlayer(new DieList(++un, System.currentTimeMillis(), worldId, instanceId));
        //dieTimes.setWorldIdAndInstanceId(worldId, instanceId);
        dieTimes.incrementDie();
    }
}
