/*
 * AionLight project
 */
package com.aionemu.gameserver.services.custom.ffa;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.model.team2.group.events.ChangeGroupLeaderEvent;
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
public class FfaClassService {

    private static final Logger log = LoggerFactory.getLogger(FfaGroupService.class);
    private static final WorldMapType FFA_INSTANCE_MAP = WorldMapType.DARK_POETA;
    private static final int instanceId = 100;

    public synchronized static WorldMapInstance initializeFfaClass() {
        WorldMap map = World.getInstance().getWorldMap(FFA_INSTANCE_MAP.getId());
        WorldMapInstance worldMapInstance = WorldMapInstanceFactory.createWorldMapInstance(map, instanceId, 0, InstanceType.FFA_GROUP);
        map.addInstance(instanceId, worldMapInstance);
        log.info("Create FreeForAll Group instance to " + instanceId + " worldMap " + FFA_INSTANCE_MAP.getId());

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
            new float[]{1212.8611f, 411.29224f, 140.00958f},
            new float[]{569.25684f, 259.43506f, 108.503204f},
            new float[]{228.6041f, 490.70282f, 111.69373f},
            new float[]{436.18582f, 1044.006f, 119.75f},
            new float[]{244.60194f, 894.63354f, 155.9748f},
            new float[]{675.3121f, 766.02026f, 117.58219f},
            new float[]{217.79488f, 1264.6267f, 211.09865f},
            new float[]{781.419f, 418.33197f, 124.54201f},
            new float[]{611.7149f, 532.65045f, 129.0924f},
            new float[]{480.95682f, 733.17035f, 121.00825f}));
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
            // \u0412\u043e\u0441\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u0438\u0435 \u0445\u043f \u0438 \u043c\u043f
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
                    PacketSendUtility.sendMessage(player, "[FFA-GROUP] \u0413\u0440\u0443\u043f\u043f\u0430 \u043b\u0438\u0434\u0435\u0440\u0430 " + pg.getLeader().getName() + " \u0441\u043e\u0432\u0435\u0440\u0448\u0430\u0435\u0442 \u0441\u0435\u0440\u0438\u044e \u0443\u0431\u0438\u0439\u0441\u0442\u0432 \u0438\u0437 " + pg.getFfakills() + " \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0435\u0439, \u0443\u0431\u0438\u0432\u0430\u044f " + victim.getName());
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
        float heal = (float) (kills < 10 ? (0.0 + kills) : (0. + kills));
        addBonusIsKills(heal, pg);
    }

    public static WorldMapInstance getFfaGroup() {
        return World.getInstance().getWorldMap(getFFAInstanceMap().getId()).getWorldMapInstanceById(getFFAInstanceId());
    }
}
