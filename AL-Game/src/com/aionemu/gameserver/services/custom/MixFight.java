/*
 * AionLight project
 */
package com.aionemu.gameserver.services.custom;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Engine;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.cardinal.AddItemType;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.ingameshop.InGameShopEn;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.model.team2.group.events.ChangeGroupLeaderEvent;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemService;
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
public class MixFight {

    private static final Logger log = LoggerFactory.getLogger(MixFight.class);
    private static final WorldMapType FFA_INSTANCE_MAP = WorldMapType.TIAMARANTA_EYE_2;
    private static int instanceId = 99;

    public synchronized static WorldMapInstance initializeFfaGroup() {
        spawnPortals();
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

    public static void spawnPortals() {
        SpawnTemplate spawn2 = SpawnEngine.addNewSpawn(110010000, 831073, 1345, 1506, 569, (byte) 27, 0);
        spawn2.setMasterName("MixFight-3");
        SpawnEngine.spawnObject(spawn2, 1);
        AI2Engine.getInstance().setupAI("mix_fight", (Creature) spawn2.getVisibleObject());
        ((AbstractAI) ((Creature) spawn2.getVisibleObject()).getAi2()).setStateIfNot(AIState.IDLE);

        SpawnTemplate spawn3 = SpawnEngine.addNewSpawn(110010000, 831073, 1345, 1518, 569, (byte) 90, 0);
        spawn3.setMasterName("MixFight-6");
        SpawnEngine.spawnObject(spawn3, 1);
        AI2Engine.getInstance().setupAI("mix_fight", (Creature) spawn3.getVisibleObject());
        ((AbstractAI) ((Creature) spawn3.getVisibleObject()).getAi2()).setStateIfNot(AIState.IDLE);

        // ASMO
        SpawnTemplate spawn4 = SpawnEngine.addNewSpawn(120010000, 831073, 1659, 1395, 194, (byte) 43, 0);
        spawn4.setMasterName("MixFight-3");
        SpawnEngine.spawnObject(spawn4, 1);
        AI2Engine.getInstance().setupAI("mix_fight", (Creature) spawn4.getVisibleObject());
        ((AbstractAI) ((Creature) spawn4.getVisibleObject()).getAi2()).setStateIfNot(AIState.IDLE);

        SpawnTemplate spawn5 = SpawnEngine.addNewSpawn(120010000, 831073, 1658, 1406, 194, (byte) 17, 0);
        spawn5.setMasterName("MixFight-6");
        SpawnEngine.spawnObject(spawn5, 1);
        AI2Engine.getInstance().setupAI("mix_fight", (Creature) spawn5.getVisibleObject());
        ((AbstractAI) ((Creature) spawn5.getVisibleObject()).getAi2()).setStateIfNot(AIState.IDLE);

    }

    public static void onLogOut(Player player) {
        if (isInFFA(player)) {
            player.setNewName(null);
            player.setLegName(null);
            TeleportService2.teleportToCapital(player);
        }
    }
    private static final List<float[]> floats = new ArrayList<>(Arrays.asList(
            new float[]{210.30063f, 765.0387f, 1200.1624f},
            new float[]{356.7643f, 1165.4768f, 1191.7349f},
            new float[]{613.80646f, 911.0366f, 1207.7716f},
            new float[]{1032.5415f, 1247.0435f, 1191.5283f},
            new float[]{857.5528f, 942.6955f, 1207.7809f},
            new float[]{1304.5852f, 767.2749f, 1200.0845f},
            new float[]{1033.8379f, 290.326f, 1191.5283f},
            new float[]{861.2202f, 588.9755f, 1207.7754f},
            new float[]{364.02423f, 372.88916f, 1191.6683f},
            new float[]{609.1706f, 623.1897f, 1207.7852f}));
    private static float x, y, z;

    public static void generateFloat() {
        float[] rnd = floats.get(Rnd.get(0, floats.size() - 1));
        x = rnd[0];
        y = rnd[1];
        z = rnd[2];
    }

    public static void teleportToFfaGroup(Player player) {
        if (player.getPlayerGroup2() == null) {
            PacketSendUtility.sendBrightYellowMessageOnCenter(player, "Необходимо состоять в группе для телепорта в эту локацию!");
            return;
        }
        player.setNewName(player.getPlayerClass().getRusname());
        player.setLegName("aion");
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
        return player.getPosition().getWorldMapInstance().isMixFight();
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
