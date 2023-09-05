/*
 * SAO Project
 */
package com.aionemu.gameserver.services.custom.ffa;

import com.aionemu.gameserver.world.World;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alex
 */
public class FfaKillController {

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final FfaKillController instance = new FfaKillController();
    }

    public static final FfaKillController getInstance() {
        return SingletonHolder.instance;
    }

    private final Map<Integer, FfaKillList> pvpKill;
    private final Map<Integer, FfaDieList> pvpDeath;
    private final Map<Integer, FfaKillList> pvpKillFfa;
    private final Map<Integer, FfaDieList> pvpDeathFfa;

    public Map<Integer, FfaKillList> getPvpKill() {
        return pvpKill;
    }

    public Map<Integer, FfaDieList> getPvpDeath() {
        return pvpDeath;
    }

    public FfaKillController() {
        pvpKill = new HashMap<>();
        pvpDeath = new HashMap<>();
        pvpKillFfa = new HashMap<>();
        pvpDeathFfa = new HashMap<>();
    }

    public FfaKillList getKillsFor(int winnerId) {
        if (pvpKill.get(winnerId) == null) {
            pvpKill.put(winnerId, new FfaKillList(winnerId, World.getInstance().findPlayer(winnerId).getName()));
        }
        return pvpKill.get(winnerId);
    }

    public FfaKillList getKillsForFfa(int winnerId) {
        if (pvpKillFfa.get(winnerId) == null) {
            pvpKillFfa.put(winnerId, new FfaKillList(winnerId, World.getInstance().findPlayer(winnerId).getName()));
        }
        return pvpKillFfa.get(winnerId);
    }

    public FfaDieList getDieForFfa(int victimId) {
        if (pvpDeathFfa.get(victimId) == null) {
            pvpDeathFfa.put(victimId, new FfaDieList(victimId, World.getInstance().findPlayer(victimId).getName()));
        }
        return pvpDeathFfa.get(victimId);
    }

    public FfaDieList getDieFor(int victimId) {
        if (pvpDeath.get(victimId) == null) {
            pvpDeath.put(victimId, new FfaDieList(victimId, World.getInstance().findPlayer(victimId).getName()));
        }
        return pvpDeath.get(victimId);
    }
}
