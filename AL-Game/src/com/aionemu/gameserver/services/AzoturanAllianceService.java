/*
 * AionLight project
 */
package com.aionemu.gameserver.services;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.alliance.PlayerAlliance;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldMapType;
import java.util.List;
import javolution.util.FastList;

/**
 *
 * @author Alex
 */
public class AzoturanAllianceService {

    private static List<PlayerAlliance> pa = new FastList<>();

    private static int worldId = WorldMapType.AZOTURAN_FORTRESS.getId();
    private static float xE = 0, yE = 0, zE = 0;
    private static float xA = 0, yA = 0, zA = 0;
    private static int instanceId;

    public static void register(PlayerAlliance playerAlliance2, Player player) {
        if (pa.size() == 2) {
            PacketSendUtility.sendMessage(player, "Альянсы уже зарегестрированы, ожидайте окончания ивента");
            return;
        }
        if (pa.contains(playerAlliance2)) {
            PacketSendUtility.sendMessage(player, "Вы уже зарегестрированы на вход в Крепость Аджотуран");
            return;
        }
        for (PlayerAlliance pp : pa) {
            if (pp.getRace() == playerAlliance2.getRace()) {
                PacketSendUtility.sendMessage(player, "Альянс вашей расы уже зарегестрирован на вход в Крепость Аджотуран");
                return;
            }
        }
        pa.add(playerAlliance2);
        if (pa.size() == 2) {
            for (PlayerAlliance pp : pa) {
                if (pp.getRace() == Race.ELYOS) {
                    instanceId = getInstanceId(worldId, pp.getLeaderObject());
                    TeleportService2.teleportTo(pp.getLeaderObject(), worldId, instanceId, xE, yE, zE);
                    for (Player p : pp.getOnlineMembers()) {
                        TeleportService2.teleportTo(p, pp.getLeaderObject().getWorldId(), pp.getLeaderObject().getInstanceId(),
                                pp.getLeaderObject().getX(), pp.getLeaderObject().getY(), pp.getLeaderObject().getZ(), (byte) 0);
                    }
                } else {
                    TeleportService2.teleportTo(pp.getLeaderObject(), worldId, instanceId, xA, yA, zA);
                    for (Player p : pp.getOnlineMembers()) {
                        TeleportService2.teleportTo(p, pp.getLeaderObject().getWorldId(), pp.getLeaderObject().getInstanceId(),
                                pp.getLeaderObject().getX(), pp.getLeaderObject().getY(), pp.getLeaderObject().getZ(), (byte) 0);
                    }
                }
            }
        }
    }

    private static int getInstanceId(int worldId, Player player) {
        if (player.getWorldId() == worldId) {
            WorldMapInstance registeredInstance = InstanceService.getRegisteredInstance(worldId, player.getObjectId());
            if (registeredInstance != null) {
                return registeredInstance.getInstanceId();
            }
        }
        WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(worldId);
        InstanceService.registerPlayerWithInstance(newInstance, player);
        return newInstance.getInstanceId();
    }
}
