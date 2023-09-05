/*
 * AionLight project
 */
package com.aionemu.gameserver.services.custom;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.cardinal.AddItemType;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.ingameshop.InGameShopEn;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.model.team2.group.events.ChangeGroupLeaderEvent;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alex
 */
public class MixFight6 {

    private static final Logger log = LoggerFactory.getLogger(MixFight6.class);
    private static final WorldMapType FFA_INSTANCE_MAP = WorldMapType.SILENTERA_CANYON;
    private static int instanceId = 98;

    public synchronized static WorldMapInstance initializeFfaGroup() {
        WorldMap map = World.getInstance().getWorldMap(FFA_INSTANCE_MAP.getId());
        WorldMapInstance worldMapInstance = WorldMapInstanceFactory.createWorldMapInstance(map, instanceId, 0, InstanceType.MIX_FIGHT);
        map.addInstance(instanceId, worldMapInstance);
        log.info("Create MixFight Group instance to " + instanceId + " worldMap " + FFA_INSTANCE_MAP.getId());
        StaticDoorSpawnManager.spawnTemplate(FFA_INSTANCE_MAP.getId(), instanceId);
        for (Map.Entry<Integer, StaticDoor> door : worldMapInstance.getDoors().entrySet()) {
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
    private static List<float[]> floats = new ArrayList<>(Arrays.asList(
            new float[]{507.62054f, 1169.7859f, 329.9292f},
            new float[]{838.04395f, 1335.3528f, 326.1314f},
            new float[]{670.0065f, 1083.8049f, 319.27673f},
            new float[]{857.7225f, 988.197f, 322.48846f},
            new float[]{1136.5586f, 897.6222f, 307.83646f},
            new float[]{1371.8607f, 767.4978f, 312.77734f},
            new float[]{1151.6492f, 676.4897f, 297.0944f},
            new float[]{978.33606f, 572.5204f, 309.21304f},
            new float[]{776.9674f, 674.72845f, 296.3461f},
            new float[]{561.9024f, 489.37445f, 310.0183f},
            new float[]{367.10443f, 466.05743f, 304.76096f},
            new float[]{341.82114f, 144.42845f, 303.2656f},
            new float[]{527.3656f, 390.59622f, 324.58313f},
            new float[]{897.28204f, 168.19408f, 340.42746f},
            new float[]{632.269f, 477.68637f, 322.9492f},
            new float[]{863.15936f, 543.6309f, 321.38196f}));
    private static float x, y, z;

    public static void generateFloat() {
        float[] rnd = floats.get(Rnd.get(0, floats.size() - 1));
        x = rnd[0];
        y = rnd[1];
        z = rnd[2];
    }

    public static void teleportToFfaGroup(Player player) {
        generateFloat();
        PlayerGroup pg = player.getPlayerGroup2();
        pg.setFfaStartPosition(x, y, z);
        for (Player p : pg.getOnlineMembers()) {
            // Восстановление хп и мп
            p.getLifeStats().increaseHp(SM_ATTACK_STATUS.TYPE.HP, p.getLifeStats().getMaxHp() + 1);
            p.getLifeStats().increaseMp(SM_ATTACK_STATUS.TYPE.MP, p.getLifeStats().getMaxMp() + 1);
            TeleportService2.teleportTo(p, getFFAInstanceMap().getId(), getFFAInstanceId(), pg.getX(), pg.getY(), pg.getZ(), (byte) 0, TeleportAnimation.BEAM_ANIMATION);
        }
    }

    public static boolean isInFFA(Player player) {
        return player.getPosition().getWorldMapInstance().isFfaGroupInstance();
    }

    private static void changeLeader(Player player) {
        if (player.getPlayerGroup2().isLeader(player)) {
            for (Player p : player.getPlayerGroup2().getOnlineMembers()) {
                if (!p.equals(player)) {
                    player.getPlayerGroup2().onEvent(new ChangeGroupLeaderEvent(p.getPlayerGroup2()));
                    return;
                }
            }
        }
    }

    public static void FFARevive(Player player) {
        player.getKnownList().clear();
        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
        revive(player, 100, 100, false, 0);
        PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
        PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, true));
        PlayerGroup pg = player.getPlayerGroup2();
        if (pg == null || pg.getX() <= 0) {
            TeleportService2.teleportToCapital(player);
        } else {
            TeleportService2.teleportTo(player, getFFAInstanceMap().getId(), instanceId, pg.getX(), pg.getY(), pg.getZ());
        }
        player.getKnownList().doUpdate();
        player.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.DEBUFF);
    }

    public static WorldMapType getFFAInstanceMap() {
        return FFA_INSTANCE_MAP;
    }

    public static int getFFAInstanceId() {
        return instanceId;
    }

    public static void initKill(Player winner, Player victim) {
        if (isInFFA(winner) && winner.getPlayerGroup2() != null) {
            PlayerGroup pg = winner.getPlayerGroup2();
            pg.increaseFfaKills();
            toKill(pg);
            Iterator<Player> iter = World.getInstance().getPlayersIterator();
            while (iter.hasNext()) {
                Player player = iter.next();
                if (isInFFA(player)) {
                    PacketSendUtility.sendMessage(player, "[FFA-GROUP] Группа лидера " + pg.getLeader().getName() + " совершает серию убийств из " + pg.getFfakills() + " персонажей, убивая " + victim.getName());
                }
            }
        }
    }

    private static void addBonusIsKills(float heal, PlayerGroup pg) {
        if (heal != 0) {
            for (Player p : pg.getOnlineMembers()) {
                int hp = (int) (p.getLifeStats().getMaxHp() * heal);
                int mp = (int) (p.getLifeStats().getMaxMp() * heal);
                p.getLifeStats().increaseHp(SM_ATTACK_STATUS.TYPE.HP, hp);
                p.getLifeStats().increaseHp(SM_ATTACK_STATUS.TYPE.MP, mp);
            }
        }
    }

    private static void toKill(PlayerGroup pg) {
        int kills = pg.getFfakills();
        if (CustomConfig.AION_MAGADAN) {
            if (kills == 2 || kills == 4 || kills == 6 || kills == 8 || kills == 10 || kills == 12 || kills == 14 || kills == 16 || kills == 18 || kills == 20 || kills == 22 || kills == 24 || kills == 26 || kills == 28 || kills == 30 || kills == 32 || kills == 34 || kills == 36 || kills == 38 || kills == 40) {
                for (Player p : pg.getOnlineMembers()) {
                    InGameShopEn.getInstance().addToll(p, 1);
                    if (CustomConfig.ITEM_FFA_GROUP != 0) {
                        ItemService.addItem(p, CustomConfig.ITEM_FFA_GROUP, CustomConfig.ITEM_FFA_GROUP_COUNT, AddItemType.MIXFIGHT, null);
                    }
                }
            }
        }
        float heal = (float) (kills < 10 ? (0.0 + kills) : (0. + kills));
        addBonusIsKills(heal, pg);
    }
}
