/*
 * AionLight project
 */
package com.aionemu.gameserver.services.custom;

import com.aionemu.commons.services.CronService;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Engine;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import static com.aionemu.gameserver.services.player.PlayerReviveService.revive;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.model.SkillTargetSlot;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.spawnengine.StaticDoorSpawnManager;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.InstanceType;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMap;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldMapInstanceFactory;
import com.aionemu.gameserver.world.WorldMapType;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alex
 */
public class PvPLocationService {

    private static final Logger log = LoggerFactory.getLogger(PvPLocationService.class);
    private static final WorldMapType ELTNEN = WorldMapType.ELTNEN;
    private static final int instanceId = 95;
    private static final WorldMapType TIAMARANTA = WorldMapType.TIAMARANTA;
    private static final int instanceId2 = 95;
    private static final WorldMapType SARPAN = WorldMapType.SARPAN;
    private static final int instanceId3 = 95;
    private static WorldMapType PVP_LOCATION;
    private static int instID = 95;

    private static long time = 0;

    public static void initialize() {
        eltnen();
        tiamaranta();
        sarpan();
        initializeRandomGeneratePvpLocation();
        cron();
        spawnPortalsToPvPLocation();
        spawnPvPZone();
    }

    public static void spawnPvPZone() {
        // пвп локация номер инста 95
        // ффа номер инста 90
        // NewSpawn.spawn(ид локации, номер инстанса, ид моба, 691.735f, 518.0524f, 108.95461f, (byte) 20, 300);
    }

    public synchronized static WorldMapInstance eltnen() {
        WorldMap map = World.getInstance().getWorldMap(ELTNEN.getId());
        WorldMapInstance worldMapInstance = WorldMapInstanceFactory.createWorldMapInstance(map, instanceId, 0, InstanceType.PVP_LOCATION);
        map.addInstance(instanceId, worldMapInstance);
        log.info("Create PvP instance to " + instanceId + " worldMap " + ELTNEN.name());
        StaticDoorSpawnManager.spawnTemplate(ELTNEN.getId(), instanceId);
        for (Map.Entry<Integer, StaticDoor> door : worldMapInstance.getDoors().entrySet()) {
            if (door != null) {
                door.getValue().setOpen(true);
            }
        }
        return worldMapInstance;
    }

    public synchronized static WorldMapInstance tiamaranta() {
        WorldMap map = World.getInstance().getWorldMap(TIAMARANTA.getId());
        WorldMapInstance worldMapInstance = WorldMapInstanceFactory.createWorldMapInstance(map, instanceId2, 0, InstanceType.PVP_LOCATION);
        map.addInstance(instanceId2, worldMapInstance);
        log.info("Create PvP instance to " + instanceId2 + " worldMap " + TIAMARANTA.name());
        StaticDoorSpawnManager.spawnTemplate(TIAMARANTA.getId(), instanceId2);
        for (Map.Entry<Integer, StaticDoor> door : worldMapInstance.getDoors().entrySet()) {
            if (door != null) {
                door.getValue().setOpen(true);
            }
        }
        return worldMapInstance;
    }

    public synchronized static WorldMapInstance sarpan() {
        WorldMap map = World.getInstance().getWorldMap(SARPAN.getId());
        WorldMapInstance worldMapInstance = WorldMapInstanceFactory.createWorldMapInstance(map, instanceId3, 0, InstanceType.PVP_LOCATION);
        map.addInstance(instanceId3, worldMapInstance);
        log.info("Create PvP instance to " + instanceId3 + " worldMap " + SARPAN.name());
        StaticDoorSpawnManager.spawnTemplate(SARPAN.getId(), instanceId3);
        for (Map.Entry<Integer, StaticDoor> door : worldMapInstance.getDoors().entrySet()) {
            if (door != null) {
                door.getValue().setOpen(true);
            }
        }
        return worldMapInstance;
    }

    public static boolean isPvPLocation(Player player) {
        return player.getPosition().getWorldMapInstance().isPvPLocation();
    }

    public static void teleportToPvpLocation(Player player) {
        /* if (isPvPLocation(player)) {
         PacketSendUtility.sendMessage(player, "You will be teleported to a new location pvp. Please Wait...");
         }*/
        switch (getLocation()) {
            case SARPAN:
                if (player.getRace() == Race.ELYOS) {
                    TeleportService2.teleportTo(player, 600020000, instanceId3, 2347, 1475, 534, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                } else {
                    TeleportService2.teleportTo(player, 600020000, instanceId3, 1337, 2234, 567, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                }
                break;
            case TIAMARANTA:
                if (player.getRace() == Race.ASMODIANS) {
                    TeleportService2.teleportTo(player, 600030000, instanceId2, 1262, 1217, 249, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                } else {
                    TeleportService2.teleportTo(player, 600030000, instanceId2, 1945, 1817, 248, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                }
                break;
            case ELTNEN:
                if (player.getRace() == Race.ASMODIANS) {
                    TeleportService2.teleportTo(player, 210020000, instanceId, 1814, 1450, 410, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                } else {
                    TeleportService2.teleportTo(player, 210020000, instanceId, 1489, 2286, 347, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                }
                break;
        }
    }

    public static WorldMapType getLocation() {
        return PVP_LOCATION;
    }

    public static void setLocation(WorldMapType wmt) {
        PvPLocationService.PVP_LOCATION = wmt;
        switch (PVP_LOCATION) {
            case SARPAN:
                instID = instanceId3;
                break;
            case TIAMARANTA:
                instID = instanceId2;
                break;
            case ELTNEN:
                instID = instanceId;
                break;
        }
    }

    public static void initializeRandomGeneratePvpLocation() {
        WorldMapType[] list = {SARPAN, TIAMARANTA, ELTNEN};
        setLocation(Rnd.get(list));
        time = System.currentTimeMillis();
        log.info("initialize Random Generate Pvp Location set " + getLocation().name());
    }

    public static void teleportAllPlayersFromPvPLocation() {
        Iterator<Player> it = World.getInstance().getPlayersIterator();
        while (it.hasNext()) {
            Player player = it.next();
            PacketSendUtility.sendYellowMessageOnCenter(player, "\u041f\u0412\u041f \u043b\u043e\u043a\u0430\u0446\u0438\u044f \u0441\u043c\u0435\u043d\u0438\u043b\u0430\u0441\u044c \u043d\u0430 " + PVP_LOCATION.getRusname());
            if (isPvPLocation(player)) {
                // AbyssPointsService.addAp(player, 5000);
                teleportToPvpLocation(player);
            }
        }

    }

    public static void generateLocIfNo(WorldMapType location) {
        WorldMapType[] list = null;
        switch (location) {
            case SARPAN:
                list = new WorldMapType[]{TIAMARANTA, ELTNEN};
                break;
            case TIAMARANTA:
                list = new WorldMapType[]{SARPAN, ELTNEN};
                break;
            case ELTNEN:
                list = new WorldMapType[]{SARPAN, TIAMARANTA};
                break;
        }
        if (list != null) {
            setLocation(Rnd.get(list));
            log.info("Is New PvP Location set " + getLocation().name());
        } else {
            log.info("ERROR: PVP LOCATION GENERATE == null!!!");
        }
    }

    public static void cron() {
        final int minute = CustomConfig.PVP_LOCATION_CRON_TIME;
        CronService.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                generateLocIfNo(getLocation());
                time = System.currentTimeMillis();
                teleportAllPlayersFromPvPLocation();
                log.info("CronService: new generate pvp location in every time " + minute + " minute sucessfull.");
            }
        }, "0 0/" + minute + " * * * ?");
    }

    public static void spawnPortalsToPvPLocation() {
        SpawnTemplate spawnEly = SpawnEngine.addNewSpawn(700010000, 831073, 1214, 1910, 95, (byte) 32, 0);
        spawnEly.setMasterName("PVP-LOCATION");
        SpawnEngine.spawnObject(spawnEly, 1);
        AI2Engine.getInstance().setupAI("pvp_portal", (Creature) spawnEly.getVisibleObject());
        ((AbstractAI) ((Creature) spawnEly.getVisibleObject()).getAi2()).setStateIfNot(AIState.IDLE);

        SpawnTemplate spawnAsmo = SpawnEngine.addNewSpawn(710010000, 831073, 1067, 1451, 95, (byte) 32, 0);
        spawnAsmo.setMasterName("PVP-LOCATION");
        SpawnEngine.spawnObject(spawnAsmo, 1);
        AI2Engine.getInstance().setupAI("pvp_portal", (Creature) spawnAsmo.getVisibleObject());
        ((AbstractAI) ((Creature) spawnAsmo.getVisibleObject()).getAi2()).setStateIfNot(AIState.IDLE);
    }

    public static WorldMapInstance getPvPLocation() {
        return World.getInstance().getWorldMap(PVP_LOCATION.getId()).getWorldMapInstanceById(instID);
    }

    public static String getTimeToRandomChangeLocation() {
        long time1 = time + 7200000;
        long time2 = System.currentTimeMillis();
        return DurationFormatUtils.formatDuration(time1 - time2, "H \u0447 m \u043c\u0438\u043d");
    }

    public static long getTime() {
        return time;
    }

    public static void onRevive(Player player) {
        player.getKnownList().clear();
        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
        revive(player, 100, 100, false, 0);
        PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
        PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, true));
        teleportToPvpLocation(player);
        player.getKnownList().doUpdate();
        player.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.DEBUFF);
    }

    public static void onKill(Player player, Player victim) {
        if (isPvPLocation(player)) {
            player.increasePvPLocationKills();
            victim.setPvPLocationKills(0);
        }
    }

    public static void onLeavePvPLocation(Player player, boolean isLeaveWorld) {
        if (isPvPLocation(player)) {
            player.setPvPLocationKills(0);
            if (isLeaveWorld) {
                TeleportService2.teleportToCapital(player);
            }
        }
    }

    public static void sendMessage(String message) {
        Iterator<Player> it = World.getInstance().getPlayersIterator();
        while (it.hasNext()) {
            Player p = it.next();
            if (isPvPLocation(p)) {
                PacketSendUtility.sendYellowMessageOnCenter(p, "[**PVP**] " + message);
            }
        }
    }
}
