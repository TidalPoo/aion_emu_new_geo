/*
 * Novorussia Team 
 */
package com.aionemu.gameserver.model.instance.instancereward;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.geometry.Point3D;
import com.aionemu.gameserver.model.instance.playerreward.EternalBastionGroupReward;
import com.aionemu.gameserver.model.instance.playerreward.EternalBastionWarPlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.InstancePlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.PvPArenaPlayerReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;
import java.util.Comparator;
import java.util.List;
import javolution.util.FastList;
import org.apache.commons.lang.mutable.MutableInt;

/**
 * @author Brera, Eloan
 */
public class EternalBastionWarReward extends InstanceReward<EternalBastionWarPlayerReward> {

    private int winnerPoints;
    private int looserPoints;
    @SuppressWarnings("unused")
    private int drawPoins;
    private int pvpKills;
    private MutableInt asmodiansPoints = new MutableInt(0);
    private MutableInt elyosPoins = new MutableInt(0);
    private Race race;
    private FastList<EternalBastionGroupReward> groups = new FastList<>();
    private Point3D asmodiansStartPosition;
    private Point3D elyosStartPosition;
    private final byte buffId;
    protected WorldMapInstance instance;
    private long instanceTime;
    private int bonusTime;

    public EternalBastionWarReward(Integer mapId, int instanceId, WorldMapInstance instance) {
        super(mapId, instanceId);
        this.instance = instance;
        winnerPoints = mapId == 301220000 ? 3000 : 4500;
        looserPoints = mapId == 301220000 ? 1500 : 2500;
        drawPoins = mapId == 301220000 ? 2250 : 3750;
        bonusTime = 12000;
        buffId = 11;
        setStartPositions();
    }

    public void addPvpKill() {
        pvpKills++;
    }

    public int getPvpKills() {
        return pvpKills;
    }

    public EternalBastionGroupReward getEternalBastionWarGroupReward(Integer object) {
        for (InstancePlayerReward reward : groups) {
            EternalBastionGroupReward eternalBastionGroupReward = (EternalBastionGroupReward) reward;
            if (eternalBastionGroupReward.containPlayer(object)) {
                return eternalBastionGroupReward;
            }
        }
        return null;
    }

    public FastList<Player> getPlayersInside(EternalBastionGroupReward group) {
        FastList<Player> players = new FastList<>();
        for (Player playerInside : instance.getPlayersInside()) {
            if (group.containPlayer(playerInside.getObjectId())) {
                players.add(playerInside);
            }
        }
        return players;
    }

    public void addEternalBastionWarGroup(EternalBastionGroupReward reward) {
        groups.add(reward);
    }

    public FastList<EternalBastionGroupReward> getGroups() {
        return groups;
    }

    public List<EternalBastionWarPlayerReward> sortPoints() {
        return sort(getInstanceRewards(), on(PvPArenaPlayerReward.class).getScorePoints(), new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 != null ? o2.compareTo(o1) : -o1.compareTo(o2);
            }
        });
    }

    private void setStartPositions() {
        Point3D a = new Point3D(570.468f, 166.897f, 432.28986f);
        Point3D b = new Point3D(400.741f, 166.713f, 432.290f);
        if (Rnd.get(2) == 0) {
            asmodiansStartPosition = a;
            elyosStartPosition = b;
        } else {
            asmodiansStartPosition = b;
            elyosStartPosition = a;
        }
    }

    public void portToPosition(Player player) {
        if (player.getRace() == Race.ASMODIANS) {
            TeleportService2.teleportTo(player, mapId, instanceId, asmodiansStartPosition.getX(), asmodiansStartPosition.getY(), asmodiansStartPosition.getZ());
        } else {
            TeleportService2.teleportTo(player, mapId, instanceId, elyosStartPosition.getX(), elyosStartPosition.getY(), elyosStartPosition.getZ());
        }
    }

    public MutableInt getPointsByRace(Race race) {
        switch (race) {
            case ELYOS:
                return elyosPoins;
            case ASMODIANS:
                return asmodiansPoints;
        }
        return null;
    }

    public void addPointsByRace(Race race, int points) {
        MutableInt racePoints = getPointsByRace(race);
        racePoints.add(points);
        if (racePoints.intValue() < 0) {
            racePoints.setValue(0);
        }
    }

    public int getLooserPoints() {
        return looserPoints;
    }

    public int getWinnerPoints() {
        return winnerPoints;
    }

    public void setWinningRace(Race race) {
        this.race = race;
    }

    public Race getWinningRace() {
        return race;
    }

    public Race getWinningRaceByScore() {
        return asmodiansPoints.compareTo(elyosPoins) > 0 ? Race.ASMODIANS : Race.ELYOS;
    }

    public int getNpcBonus(int npcId) {
        switch (npcId) {
            case 000000:
                return 0;
        }
        return 0;
    }

    @Override
    public void clear() {
        super.clear();
    }

    public void regPlayerReward(Integer object) {
        if (!containPlayer(object)) {
            addPlayerReward(new EternalBastionWarPlayerReward(object, bonusTime, buffId));
        }
    }

    @Override
    public void addPlayerReward(EternalBastionWarPlayerReward reward) {
        super.addPlayerReward(reward);
    }

    @Override
    public EternalBastionWarPlayerReward getPlayerReward(Integer object) {
        return (EternalBastionWarPlayerReward) super.getPlayerReward(object);
    }

    public void sendPacket(final int type, final Integer object) {
        instance.doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player player) {
                PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(type, getTime(), getInstanceReward(), object));
            }
        });
    }

    public int getTime() {
        long result = System.currentTimeMillis() - instanceTime;
        if (result < 20000) {
            return (int) (20000 - result);
        } else if (result < 1800000) { //20 Minutes.
            return (int) (1800000 - (result - 20000));
        }
        return 0;
    }

    public void setInstanceStartTime() {
        this.instanceTime = System.currentTimeMillis();
    }
}
