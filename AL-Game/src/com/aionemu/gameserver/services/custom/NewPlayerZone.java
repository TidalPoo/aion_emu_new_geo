package com.aionemu.gameserver.services.custom;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.custom.ffa.FfaGroupService;
import static com.aionemu.gameserver.services.player.PlayerReviveService.revive;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.model.SkillTargetSlot;
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
public class NewPlayerZone {

    private static final Logger log = LoggerFactory.getLogger(FfaGroupService.class);
    private static final WorldMapType FFA_INSTANCE_MAP = WorldMapType.DARK_POETA;
    private static int instanceId = 97;

    public synchronized static WorldMapInstance initializeFfaGroup() {
        WorldMap map = World.getInstance().getWorldMap(FFA_INSTANCE_MAP.getId());
        WorldMapInstance worldMapInstance = WorldMapInstanceFactory.createWorldMapInstance(map, instanceId, 0, InstanceType.NEW_PLAYER_ZONE);
        map.addInstance(instanceId, worldMapInstance);
        NewSpawn.playerZone();
        for (Map.Entry<Integer, StaticDoor> door : worldMapInstance.getDoors().entrySet()) {
            if (door != null) {
                door.getValue().setOpen(true);
            }
        }
        log.info("Create NewPlayerZone Group instance to " + instanceId + " worldMap " + FFA_INSTANCE_MAP.getId());
        return worldMapInstance;
    }

    public static void onLogOut(Player player) {
        if (isInZone(player)) {
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

    public static void teleportTo(Player player) {
        generateFloat();
        if (player.getPlayerGroup2() != null) {
            PlayerGroup pg = player.getPlayerGroup2();
            pg.setFfaStartPosition(x, y, z);
            for (Player p : pg.getOnlineMembers()) {
                if (p.isNewPlayer()) {
                    TeleportService2.teleportTo(p, getFFAInstanceMap().getId(), getFFAInstanceId(), pg.getX(), pg.getY(), pg.getZ(), (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                }
            }
        } else {
            TeleportService2.teleportTo(player, getFFAInstanceMap().getId(), getFFAInstanceId(), x, y, z, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
        }
    }

    public static boolean isInZone(Player player) {
        return player.getPosition().getWorldMapInstance().isNewPlayerInstance();
    }

    public static void FFARevive(Player player) {
        player.getKnownList().clear();
        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
        revive(player, 100, 100, false, 0);
        PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
        PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, true));
        PlayerGroup pg = player.getPlayerGroup2();
        if (pg == null || pg.getX() <= 0) {
            TeleportService2.teleportTo(player, getFFAInstanceMap().getId(), instanceId, x, y, z);
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
        if (isInZone(winner) && winner.getPlayerGroup2() != null) {
            PlayerGroup pg = winner.getPlayerGroup2();
            pg.increaseFfaKills();
            toKill(pg);
            Iterator<Player> iter = World.getInstance().getPlayersIterator();
            while (iter.hasNext()) {
                Player player = iter.next();
                if (isInZone(player)) {
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
        float kills = pg.getFfakills();
        float heal = (float) (kills < 10 ? (0.0 + kills) : (0. + kills));
        addBonusIsKills(heal, pg);
    }
}
