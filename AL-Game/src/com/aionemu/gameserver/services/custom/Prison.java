package com.aionemu.gameserver.services.custom;

import com.aionemu.commons.objects.filter.ObjectFilter;
import com.aionemu.gameserver.ai2.AI2Engine;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PRIVATE_STORE_NAME;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapType;
import java.util.List;
import java.util.Map;
import javolution.util.FastList;
import javolution.util.FastMap;

/**
 *
 * @author Alex
 */
public class Prison {

    private final List<Integer> ely = new FastList<>();
    private final List<Integer> asmo = new FastList<>();
    private final List<Npc> npcs = new FastList<>();
    private final Map<Integer, Integer> players = new FastMap<>();

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final Prison instance = new Prison();
    }

    public static Prison getInstance() {
        return SingletonHolder.instance;
    }

    //protected static final Prison instance = new Prison();
    public int genLoc(Race race) {
        int loc = -1;
        int p = race == Race.ELYOS ? prisonSanctum.length - 1 : prisonPandaemonium.length - 1;
        List<Integer> list = race == Race.ELYOS ? ely : asmo;
        for (int i = 0; i < p; i++) {
            if (!list.isEmpty() && list.contains(i)) {
                continue;
            }
            list.add(i);
            loc = i;
            break;
        }
        return loc;
    }

    public void stopPrison(Race race, int location) {
        if (location == -1) {
            return;
        }
        List<Integer> list = race == Race.ELYOS ? ely : asmo;
        if (!list.isEmpty() && list.contains(location)) {
            list.remove(new Integer(location));
        }
        float[] prison = race == Race.ELYOS ? prisonSanctum[location] : prisonPandaemonium[location];
        int worldId = race == Race.ELYOS ? WorldMapType.SANCTUM.getId() : WorldMapType.PANDAEMONIUM.getId();
        removeCube(worldId, prison[0], prison[1], prison[2]);
    }

    private void removeCube(int worldId, float x, float y, float z) {
        for (Npc n : npcs) {
            if (n.getWorldId() == worldId && n.getX() == x && n.getY() == y && n.getZ() == z) {
                players.remove(n.getObjectId());
                n.getController().onDelete();
                npcs.remove(n);
                break;
            }
        }
    }

    private void cube(Player player, int worldId, float x, float y, float z) {
        Npc npc = null;
        if (!npcs.isEmpty()) {
            for (Npc n : npcs) {
                if (n.getWorldId() == worldId && n.getX() == x && n.getY() == y && n.getZ() == z) {
                    return;
                }
            }
        }

        SpawnTemplate spawn = SpawnEngine.addNewSpawn(worldId, 802021, x, y, z, (byte) 0, 0);
        SpawnEngine.spawnObject(spawn, 1);
        AI2Engine.getInstance().setupAI("prison", (Creature) spawn.getVisibleObject());
        ((AbstractAI) ((Creature) spawn.getVisibleObject()).getAi2()).setStateIfNot(AIState.IDLE);
        npc = (Npc) spawn.getVisibleObject();
        npcs.add(npc);
        players.put(npc.getObjectId(), player.getObjectId());
    }

    public boolean isInPrison(Player player) {
        return players.containsValue(player.getObjectId());
    }

    public void toNicker(final Player player, String t, boolean remove) {
        if (remove) {
            t = "";
        }
        final String text = t;

        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.broadcastPacket(player, new SM_PRIVATE_STORE_NAME(player.getObjectId(), text), true, new ObjectFilter<Player>() {
                    @Override
                    public boolean acceptObject(Player object) {
                        return ((player.getRace().getRaceId() == object.getRace().getRaceId()));
                    }
                });
                PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_EMOTE2, 0, 0), true, new ObjectFilter<Player>() {
                    @Override
                    public boolean acceptObject(Player object) {
                        return ((player.getRace().getRaceId() == object.getRace().getRaceId()));
                    }
                });
                PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.SIT), true, new ObjectFilter<Player>() {
                    @Override
                    public boolean acceptObject(Player object) {
                        return ((player.getRace().getRaceId() == object.getRace().getRaceId()));
                    }
                });
                player.setNickerText(text);
            }
        }, 10 * 1000);
    }

    public void teleportToPrison(Player player, int location) {
        Race race = player.getRace();
        int len = race == Race.ELYOS ? prisonSanctum.length - 1 : prisonPandaemonium.length - 1;
        if (location > len) {
            player.setPrisonLocation(-1);
            TeleportService2.teleportToPrison(player);
            return;
        }

        /* if(player.isInPrison()){
         stopPrison(race, player.getPrisonLocation());
         }*/
        int worldId = race == Race.ELYOS ? WorldMapType.SANCTUM.getId() : WorldMapType.PANDAEMONIUM.getId();
        float[] prison = race == Race.ELYOS ? prisonSanctum[location] : prisonPandaemonium[location];
        TeleportService2.teleportTo(player, worldId, prison[0], prison[1], prison[2]);
        player.getEffectController().setAbnormal(AbnormalState.CANNOT_MOVE.getId());
        player.getEffectController().updatePlayerEffectIcons();
        cube(player, worldId, prison[0], prison[1], prison[2]);
        toNicker(player, "Лалка", false);
    }

    float[] prisonPandaemonium[] = {{1629.091f, 1371.0524f, 193.1271f},
    {1620.9938f, 1370.7485f, 193.1273f},
    {1613.686f, 1370.6765f, 193.12749f},
    {1607.1204f, 1370.538f, 193.12766f},
    {1601.9626f, 1370.8164f, 193.12791f},
    {1629.7286f, 1430.8055f, 193.12708f},
    {1620.0857f, 1430.8595f, 193.12732f},
    {1613.2169f, 1430.7262f, 193.1275f},
    {1606.789f, 1430.816f, 193.12766f},
    {1602.0641f, 1430.9443f, 193.12785f}};

    float[] prisonSanctum[] = {{1374.2712f, 1470.0902f, 569.0642f},
    {1374.2428f, 1474.2131f, 569.06085f},
    {1359.0571f, 1470.6322f, 569.1167f},
    {1360.5369f, 1474.7382f, 569.0816f},
    {1358.5499f, 1553.1891f, 569.11676f},
    {1359.1613f, 1550.9287f, 569.11676f},
    {1374.1108f, 1553.2197f, 569.08435f},
    {1373.8644f, 1549.7979f, 569.0783f}};

    public Integer getPlayer(int objectId) {
        if (players.get(objectId) == null) {
            return 0;
        }
        return players.get(objectId);
    }
}
