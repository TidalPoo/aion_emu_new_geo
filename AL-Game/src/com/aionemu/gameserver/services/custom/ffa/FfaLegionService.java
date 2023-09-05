/*
 * AionLight project
 */
package com.aionemu.gameserver.services.custom.ffa;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.model.team2.alliance.PlayerAlliance;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import static com.aionemu.gameserver.services.player.PlayerReviveService.revive;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.model.SkillTargetSlot;
import com.aionemu.gameserver.spawnengine.StaticDoorSpawnManager;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.InstanceType;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMap;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldMapInstanceFactory;
import com.aionemu.gameserver.world.WorldMapType;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alex
 */
public class FfaLegionService {

    private static final Logger log = LoggerFactory.getLogger(FfaLegionService.class);
    private static final WorldMapType FFA_INSTANCE_MAP = WorldMapType.BESHMUNDIR_TEMPLE;
    private static int instanceId = 99;

    public synchronized static WorldMapInstance initializeFfaLegion() {
        WorldMap map = World.getInstance().getWorldMap(FFA_INSTANCE_MAP.getId());
        WorldMapInstance worldMapInstance = WorldMapInstanceFactory.createWorldMapInstance(map, instanceId, 0, InstanceType.FFA_LEGION);
        map.addInstance(instanceId, worldMapInstance);
        log.info("Create FreeForAll Legion instance to " + instanceId + " worldMap " + FFA_INSTANCE_MAP.getId());
        StaticDoorSpawnManager.spawnTemplate(FFA_INSTANCE_MAP.getId(), instanceId);

        for (java.util.Map.Entry<Integer, StaticDoor> door : worldMapInstance.getDoors().entrySet()) {
            if (door != null) {
                door.getValue().setOpen(true);
            }
        }
        return worldMapInstance;
    }

    public static void onLogOut(Player player) {
        if (isInFFA(player)) {
            TeleportService2.teleportToCapital(player);
        }
    }

    private static final float[] coordinates[] = {{1400.4823f, 288.12668f, 246.90097f},
    {1384.5835f, 465.32826f, 243.24644f},
    {981.21155f, 134.60706f, 241.75218f},
    {1085.2325f, 614.4843f, 234.403f},
    {1247.3793f, 584.73804f, 247.08157f},
    {1229.99f, 1006.6456f, 282.1778f},
    {1512.949f, 1046.2384f, 273.44067f},
    {1478.2506f, 1427.6884f, 302.29547f},
    {1630.8588f, 1495.7483f, 329.94495f},
    {1500.9999f, 1587.5057f, 329.94492f}};

    public static void teleportToFfaLegion(Player player) {
        /*if (CustomConfig.NEW_PVP_MODE) {
         tpAlliance2(player);
         return;
         }*/
        Legion leg = player.getLegion();
        float[] rnd = coordinates[Rnd.get(0, coordinates.length - 1)];
        leg.setFfaStartPosition(rnd[0], rnd[1], rnd[2]);
        for (Player p : leg.getOnlineLegionMembers()) {
            // \u0412\u043e\u0441\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u0438\u0435 \u0445\u043f \u0438 \u043c\u043f
            p.getLifeStats().increaseHp(SM_ATTACK_STATUS.TYPE.HP, p.getLifeStats().getMaxHp() + 1);
            p.getLifeStats().increaseMp(SM_ATTACK_STATUS.TYPE.MP, p.getLifeStats().getMaxMp() + 1);
            TeleportService2.teleportTo(p, getFFAInstanceMap().getId(), getFFAInstanceId(), leg.getX(), leg.getY(), leg.getZ(), (byte) 0, TeleportAnimation.BEAM_ANIMATION);
        }
    }

    public static boolean isInFFA(Player player) {
        return player.getPosition().getWorldMapInstance().isFfaLegionInstance();
    }

    public static void onEnterFfa(Player player) {
        if (isInFFA(player)) {
            if (player.getLegion() != null) {
                player.setNewName(player.getLegionMember().getRank().getRusname());
            }
        }
    }

    public static void onLeaveFfa(Player player) {
        if (isInFFA(player)) {
            player.setNewName(null);
        }
    }

    public static void FFARevive(Player player) {
        player.getKnownList().clear();
        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
        revive(player, 100, 100, false, 0);
        PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
        PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, true));
        /* if (CustomConfig.NEW_PVP_MODE) {
         PlayerAlliance all = player.getPlayerAlliance2();
         if (all == null || all.getX() <= 0) {
         TeleportService2.teleportToCapital(player);
         } else {
         TeleportService2.teleportTo(player, getFFAInstanceMap().getId(), instanceId, all.getX(), all.getY(), all.getZ());
         }
         } else {*/
        Legion leg = player.getLegion();
        if (leg == null || leg.getX() <= 0) {
            TeleportService2.teleportToCapital(player);
        } else {
            TeleportService2.teleportTo(player, getFFAInstanceMap().getId(), instanceId, leg.getX(), leg.getY(), leg.getZ());
        }
        player.getKnownList().doUpdate();
        player.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.DEBUFF);
    }

    public static void initKill(Player winner, Player victim) {
        /*if (CustomConfig.NEW_PVP_MODE) {
         if (isInFFA(winner) && winner.getPlayerAlliance2() != null) {
         PlayerAlliance all = winner.getPlayerAlliance2();
         all.increaseFfaAllianceKills();
         toKillA(all);
         Iterator<Player> iter = World.getInstance().getPlayersIterator();
         while (iter.hasNext()) {
         Player player = iter.next();
         if (isInFFA(player)) {
         PacketSendUtility.sendMessage(player, "[FFA-LEGION] \u041b\u0435\u0433\u0438\u043e\u043d " + winner.getLegion().getLegionName() + " \u0441\u043e\u0432\u0435\u0440\u0448\u0430\u0435\u0442 \u0441\u0435\u0440\u0438\u044e \u0443\u0431\u0438\u0439\u0441\u0442\u0432 " + all.getAllianceFfaKills() + " \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0435\u0439, \u0443\u0431\u0438\u0432\u0430\u044f " + victim.getName());
         }
         }
         }
         } else {*/
        if (isInFFA(winner) && winner.getLegion() != null) {
            Legion leg = winner.getLegion();
            leg.increaseFfaLegionKills();
            toKill(leg);
            Iterator<Player> iter = World.getInstance().getPlayersIterator();
            while (iter.hasNext()) {
                Player player = iter.next();
                if (isInFFA(player)) {
                    PacketSendUtility.sendMessage(player, "[FFA-LEGION] \u041b\u0435\u0433\u0438\u043e\u043d " + leg.getLegionName() + " \u0441\u043e\u0432\u0435\u0440\u0448\u0430\u0435\u0442 \u0441\u0435\u0440\u0438\u044e \u0443\u0431\u0438\u0439\u0441\u0442\u0432 " + leg.getLegionFfaKills() + " \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0435\u0439, \u0443\u0431\u0438\u0432\u0430\u044f " + victim.getName());
                }
            }
        }
    }

    private static void addBonusIsKills(float heal, Legion leg) {
        if (heal != 0) {
            for (Player p : leg.getOnlineLegionMembers()) {
                int hp = (int) (p.getLifeStats().getMaxHp() * heal);
                int mp = (int) (p.getLifeStats().getMaxMp() * heal);
                p.getLifeStats().increaseHp(SM_ATTACK_STATUS.TYPE.HP, hp);
                p.getLifeStats().increaseHp(SM_ATTACK_STATUS.TYPE.MP, mp);
            }
        }
    }

    private static void addBonusIsKills(float heal, PlayerAlliance all) {
        if (heal != 0) {
            for (Player p : all.getOnlineMembers()) {
                int hp = (int) (p.getLifeStats().getMaxHp() * heal);
                int mp = (int) (p.getLifeStats().getMaxMp() * heal);
                p.getLifeStats().increaseHp(SM_ATTACK_STATUS.TYPE.HP, hp);
                p.getLifeStats().increaseHp(SM_ATTACK_STATUS.TYPE.MP, mp);
            }
        }
    }

    private static void toKill(Legion leg) {
        float kills = leg.getLegionFfaKills();
        float heal = (float) (kills < 10 ? (0.0 + kills) : (0. + kills));
        addBonusIsKills(heal, leg);
    }

    private static void toKillA(PlayerAlliance all) {
        float kills = all.getAllianceFfaKills();
        float heal = (float) (kills < 10 ? (0.0 + kills) : (0. + kills));
        addBonusIsKills(heal, all);
    }

    public static WorldMapType getFFAInstanceMap() {
        return FFA_INSTANCE_MAP;
    }

    public static int getFFAInstanceId() {
        return instanceId;
    }

    private static void tpAlliance2(Player player) {
        if (player.getPlayerAlliance2() == null) {
            return;
        }
        PlayerAlliance all = player.getPlayerAlliance2();
        float[] rnd = coordinates[Rnd.get(0, coordinates.length - 1)];
        all.setFfaStartPosition(rnd[0], rnd[1], rnd[2]);
        for (Player p : all.getOnlineMembers()) {
            p.getLifeStats().increaseHp(SM_ATTACK_STATUS.TYPE.HP, p.getLifeStats().getMaxHp() + 1);
            p.getLifeStats().increaseMp(SM_ATTACK_STATUS.TYPE.MP, p.getLifeStats().getMaxMp() + 1);
            TeleportService2.teleportTo(p, getFFAInstanceMap().getId(), getFFAInstanceId(), all.getX(), all.getY(), all.getZ(), (byte) 0, TeleportAnimation.BEAM_ANIMATION);
        }
    }

    public static WorldMapInstance getFfaLegion() {
        return World.getInstance().getWorldMap(getFFAInstanceMap().getId()).getWorldMapInstanceById(getFFAInstanceId());
    }
}
