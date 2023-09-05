/*
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 * aion-lightning is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-lightning is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.services.teleport;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.main.SecurityConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.dataholders.PlayerInitialData;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.TribeClass;
import com.aionemu.gameserver.model.actions.PlayerMode;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.Pet;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.BindPointPosition;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.templates.flypath.FlyPathEntry;
import com.aionemu.gameserver.model.templates.portal.InstanceExit;
import com.aionemu.gameserver.model.templates.portal.PortalLoc;
import com.aionemu.gameserver.model.templates.portal.PortalPath;
import com.aionemu.gameserver.model.templates.portal.PortalScroll;
import com.aionemu.gameserver.model.templates.spawns.SpawnSearchResult;
import com.aionemu.gameserver.model.templates.spawns.SpawnSpotTemplate;
import com.aionemu.gameserver.model.templates.teleport.TelelocationTemplate;
import com.aionemu.gameserver.model.templates.teleport.TeleportLocation;
import com.aionemu.gameserver.model.templates.teleport.TeleportType;
import com.aionemu.gameserver.model.templates.teleport.TeleporterTemplate;
import com.aionemu.gameserver.model.templates.world.WorldMapTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_BIND_POINT_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CHANNEL_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DELETE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LEGION_UPDATE_MEMBER;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_SPAWN;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SERIAL_KILLER;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TELEPORT_LOC;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TELEPORT_MAP;
import com.aionemu.gameserver.restrictions.RestrictionsManager;
import com.aionemu.gameserver.services.DanceService;
import com.aionemu.gameserver.services.DuelService;
import com.aionemu.gameserver.services.PrivateStoreService;
import com.aionemu.gameserver.services.SerialKillerService;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.services.custom.Prison;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemUpdateType;
import com.aionemu.gameserver.services.trade.PricesService;
import com.aionemu.gameserver.utils.ChatUtil;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.audit.AuditLogger;
import com.aionemu.gameserver.utils.audit.GMService;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldMapType;
import com.aionemu.gameserver.world.WorldPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xTz
 * @modified Alex
 */
public class TeleportService2 {

    private static final Logger log = LoggerFactory.getLogger(TeleportService2.class);

    /**
     * Performs flight teleportation
     *
     * @param template
     * @param locId
     * @param player
     * @param npc
     * @param animation
     */
    public static void teleport(TeleporterTemplate template, int locId, Player player, Npc npc, TeleportAnimation animation) {
        TribeClass tribe = npc.getTribe();
        Race race = player.getRace();
        if (tribe.equals(TribeClass.FIELD_OBJECT_LIGHT) && race.equals(Race.ASMODIANS)
                || (tribe.equals(TribeClass.FIELD_OBJECT_DARK) && race.equals(Race.ELYOS))) {
            return;
        }

        if (template.getTeleLocIdData() == null) {
            log.info(String.format("Missing locId for this teleporter at teleporter_templates.xml with locId: %d", locId));
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_MOVE_TO_AIRPORT_NO_ROUTE);
            if (player.isGM()) {
                PacketSendUtility.sendMessage(player,
                        "Missing locId for this teleporter at teleporter_templates.xml with locId: " + locId);
            }
            return;
        }

        TeleportLocation location = template.getTeleLocIdData().getTeleportLocation(locId);
        if (location == null) {
            log.info(String.format("Missing locId for this teleporter at teleporter_templates.xml with locId: %d", locId));
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_MOVE_TO_AIRPORT_NO_ROUTE);
            if (player.isGM()) {
                PacketSendUtility.sendMessage(player,
                        "Missing locId for this teleporter at teleporter_templates.xml with locId: " + locId);
            }
            return;
        }

        TelelocationTemplate locationTemplate = DataManager.TELELOCATION_DATA.getTelelocationTemplate(locId);
        if (locationTemplate == null) {
            log.info(String.format("Missing info at teleport_location.xml with locId: %d", locId));
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_MOVE_TO_AIRPORT_NO_ROUTE);
            if (player.isGM()) {
                PacketSendUtility.sendMessage(player, "Missing info at teleport_location.xml with locId: " + locId);
            }
            return;
        }

        // TODO: remove teleportation route if it's enemy fortress (1221, 1231, 1241)
        int id = SiegeService.getInstance().getFortressId(locId);
        if (id > 0 && !SiegeService.getInstance().getFortress(id).isCanTeleport(player)) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_MOVE_TO_AIRPORT_NO_ROUTE);
            PacketSendUtility.sendMessage(player, "Teleporter is dead"); // TODO retail chk
            return;
        }

        if (!checkKinahForTransportation(location, player)) {
            return;
        }

        if (location.getType().equals(TeleportType.FLIGHT)) {
            if (SecurityConfig.ENABLE_FLYPATH_VALIDATOR) {
                FlyPathEntry flypath = DataManager.FLY_PATH.getPathTemplate((short) location.getLocId());
                if (flypath == null) {
                    AuditLogger.info(player, "Try to use null flyPath #" + location.getLocId());
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_MOVE_TO_AIRPORT_NO_ROUTE);
                    return;
                }

                double dist = MathUtil.getDistance(player, flypath.getStartX(), flypath.getStartY(), flypath.getStartZ());
                if (dist > 7) {
                    AuditLogger.info(player, "Try to use flyPath #" + location.getLocId() + " but hes too far "
                            + dist);
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_MOVE_TO_AIRPORT_NO_ROUTE);
                    return;
                }

                if (player.getWorldId() != flypath.getStartWorldId()) {
                    AuditLogger.info(player, "Try to use flyPath #" + location.getLocId()
                            + " from not native start world " + player.getWorldId() + ". expected " + flypath.getStartWorldId());
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_MOVE_TO_AIRPORT_NO_ROUTE);
                    return;
                }

                player.setCurrentFlypath(flypath);
            }
            player.unsetPlayerMode(PlayerMode.RIDE);
            player.setState(CreatureState.FLIGHT_TELEPORT);
            player.unsetState(CreatureState.ACTIVE);
            player.setFlightTeleportId(location.getTeleportId());
            PacketSendUtility.broadcastPacket(player,
                    new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, location.getTeleportId(), 0), true);
        } else {
            int instanceId = 1;
            int mapId = locationTemplate.getMapId();
            if (player.getWorldId() == mapId) {
                instanceId = player.getInstanceId();
            }
            sendLoc(player, mapId, instanceId, locationTemplate.getX(), locationTemplate.getY(),
                    locationTemplate.getZ(), (byte) locationTemplate.getHeading(), animation);
        }
    }

    /**
     * Check kinah in inventory for teleportation
     *
     * @param location
     * @param player
     * @return
     */
    private static boolean checkKinahForTransportation(TeleportLocation location, Player player) {
        Storage inventory = player.getInventory();

        // TODO: Price vary depending on the influence ratio
        int basePrice = (location.getPrice());
        // TODO check for location.getPricePvp()

        long transportationPrice = PricesService.getPriceForService(basePrice, player.getRace());

        // If HiPassEffect is active, then all flight/teleport prices are 1 kinah
        if (player.getController().isHiPassInEffect()) {
            transportationPrice = 1;
        }

        if (!inventory.tryDecreaseKinah(transportationPrice, ItemUpdateType.DEC_KINAH_FLY)) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_NOT_ENOUGH_KINA(transportationPrice));
            return false;
        }
        return true;
    }

    private static void sendLoc(final Player player, final int mapId, final int instanceId, final float x,
            final float y, final float z, final byte h, final TeleportAnimation animation) {
        boolean isInstance = DataManager.WORLD_MAPS_DATA.getTemplate(mapId).isInstance();
        if (!animation.isNoAnimation()) {
            PacketSendUtility.broadcastPacket(player, new SM_DELETE(player, animation == TeleportAnimation.BEAM_ANIMATION ? 2 : 11), 50);
        }
        PacketSendUtility.sendPacket(player, new SM_TELEPORT_LOC(isInstance, instanceId, mapId, x, y, z, h, animation.getStartAnimationId()));
        player.unsetPlayerMode(PlayerMode.RIDE);
        ThreadPoolManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                if (player.getLifeStats().isAlreadyDead() || !player.isSpawned()) {
                    return;
                }
                changePosition(player, mapId, instanceId, x, y, z, h, animation);
            }
        }, 2200);
    }

    public static void teleportTo(Player player, WorldPosition pos) {
        if (player.getWorldId() == pos.getMapId()) {
            player.getPosition().setXYZH(pos.getX(), pos.getY(), pos.getZ(), pos.getHeading());
            Pet pet = player.getPet();
            if (pet != null) {
                World.getInstance().setPosition(pet, pos.getMapId(), player.getInstanceId(),
                        pos.getX(), pos.getY(), pos.getZ(), pos.getHeading());
            }
            PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
            PacketSendUtility.sendPacket(player, new SM_CHANNEL_INFO(player.getPosition()));
            player.setPortAnimation(4); // Beam exit animation
            PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
            player.getController().startProtectionActiveTask();
            PacketSendUtility.sendPacket(player, new SM_MOTION(player.getObjectId(), player.getMotions().getActiveMotions()));
            player.getEffectController().updatePlayerEffectIcons();
            player.getController().updateZone();
            if (pet != null) {
                World.getInstance().spawn(pet);
            }
            player.getKnownList().clear();
            player.updateKnownlist();

            SerialKillerService sks = SerialKillerService.getInstance();
            PacketSendUtility.sendPacket(player, new SM_SERIAL_KILLER(false, player.getSKInfo().getRank()));
            if (sks.isHandledWorld(player.getWorldId()) && !sks.isEnemyWorld(player)) {
                PacketSendUtility.sendPacket(player, new SM_SERIAL_KILLER(sks.getWorldKillers(player.getWorldId()).values()));
            }
        } else if (player.getLifeStats().isAlreadyDead()) {
            teleportDeadTo(player, pos.getMapId(), 1, pos.getX(), pos.getY(), pos.getZ(), pos.getHeading());
        } else {
            teleportTo(player, pos.getMapId(), pos.getX(), pos.getY(), pos.getZ(), pos.getHeading());
        }
    }

    public static void teleportDeadTo(Player player, int worldId, int instanceId, float x, float y, float z, byte heading) {
        player.getController().onLeaveWorld();
        World.getInstance().despawn(player);
        World.getInstance().setPosition(player, worldId, instanceId, x, y, z, heading);
        PacketSendUtility.sendPacket(player, new SM_CHANNEL_INFO(player.getPosition()));
        PacketSendUtility.sendPacket(player, new SM_PLAYER_SPAWN(player));
        player.setPortAnimation(3);
        PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));

        if (player.isLegionMember()) {
            PacketSendUtility.broadcastPacketToLegion(player.getLegion(), new SM_LEGION_UPDATE_MEMBER(player, 0, ""));
        }
    }

    public static boolean teleportTo(Player player, int worldId, float x, float y, float z) {
        return teleportTo(player, worldId, x, y, z, player.getHeading());
    }

    public static boolean teleportTo(Player player, int worldId, float x, float y, float z, byte h) {
        int instanceId = 1;
        if (player.getWorldId() == worldId) {
            instanceId = player.getInstanceId();
        }
        return teleportTo(player, worldId, instanceId, x, y, z, h, TeleportAnimation.NO_ANIMATION);
    }

    public static boolean teleportTo(Player player, int worldId, float x, float y, float z, byte h, TeleportAnimation animation) {
        int instanceId = 1;
        if (player.getWorldId() == worldId) {
            instanceId = player.getInstanceId();
        }
        return teleportTo(player, worldId, instanceId, x, y, z, h, animation);
    }

    public static boolean teleportTo(Player player, int worldId, int instanceId, float x, float y, float z, byte h) {
        return teleportTo(player, worldId, instanceId, x, y, z, h, TeleportAnimation.NO_ANIMATION);
    }

    public static void teleportToCapital(Player player) {
        switch (player.getRace()) {
            case ELYOS:
                TeleportService2.teleportTo(player, WorldMapType.SANCTUM.getId(), 1322, 1511, 568);
                break;
            case ASMODIANS:
                TeleportService2.teleportTo(player, WorldMapType.PANDAEMONIUM.getId(), 1679, 1400, 195);
                break;
        }
    }

    public static boolean teleportTo(Player player, int worldId, int instanceId, float x, float y, float z) {
        return teleportTo(player, worldId, instanceId, x, y, z, player.getHeading(), TeleportAnimation.NO_ANIMATION);
    }

    /**
     * @param player
     * @param worldId
     * @param instanceId
     * @param x
     * @param y
     * @param z
     * @param heading
     * @param animation
     * @return
     */
    public static boolean teleportTo(final Player player, final int worldId, final int instanceId,
            final float x, final float y, final float z, final byte heading, TeleportAnimation animation) {
        if (player.getLifeStats().isAlreadyDead()) {
            return false;
        } else if (DuelService.getInstance().isDueling(player.getObjectId())) {
            DuelService.getInstance().loseDuel(player);
        }

        player.setTeleport(true);

        if (animation.isNoAnimation()) {
            player.unsetPlayerMode(PlayerMode.RIDE);
            changePosition(player, worldId, instanceId, x, y, z, heading, animation);
        } else {
            sendLoc(player, worldId, instanceId, x, y, z, heading, animation);
        }
        return true;
    }

    /**
     * @param worldId
     * @param instanceId
     * @param x
     * @param y
     * @param z
     * @param heading
     */
    private static void changePosition(Player player, int worldId, int instanceId, float x, float y, float z, byte heading, TeleportAnimation animation) {
        if (player.hasStore()) {
            PrivateStoreService.closePrivateStore(player);
        }
        player.getController().cancelCurrentSkill();
        if (player.getWorldId() != worldId) {
            player.getController().onLeaveWorld();
        }

        player.getFlyController().endFly(true);
        World.getInstance().despawn(player);

        int currentWorldId = player.getWorldId();
        WorldPosition pos = World.getInstance().createPosition(worldId, x, y, z, heading, instanceId);
        player.setPosition(pos);
        boolean isInstance = DataManager.WORLD_MAPS_DATA.getTemplate(worldId).isInstance();

        Pet pet = player.getPet();
        if (pet != null) {
            World.getInstance().setPosition(pet, worldId, instanceId, x, y, z, heading);
        }

        /**
         * instant teleport when map is the same
         */
        player.setPortAnimation(animation.getEndAnimationId());
        player.getController().startProtectionActiveTask();
        if (currentWorldId == worldId) {
            PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
            PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
            PacketSendUtility.sendPacket(player, new SM_MOTION(player.getObjectId(), player.getMotions().getActiveMotions()));
            World.getInstance().spawn(player);
            player.getEffectController().updatePlayerEffectIcons();
            player.getController().updateZone();

            if (pet != null) {
                World.getInstance().spawn(pet);
            }
            player.setPortAnimation(0);
            if (!animation.isNoAnimation()) {
                ThreadPoolManager.getInstance().schedule(new Runnable() {

                    @Override
                    public void run() {
                        player.getKnownList().clear();
                        player.getKnownList().doUpdate();
                    }
                }, 3200);
            }
        } /**
         * teleport with full map reloading
         */
        else {
            PacketSendUtility.sendPacket(player, new SM_CHANNEL_INFO(player.getPosition()));
            PacketSendUtility.sendPacket(player, new SM_PLAYER_SPAWN(player));
        }
        if (player.isLegionMember()) {
            PacketSendUtility.broadcastPacketToLegion(player.getLegion(), new SM_LEGION_UPDATE_MEMBER(player, 0, ""));
        }

        SerialKillerService sks = SerialKillerService.getInstance();
        PacketSendUtility.sendPacket(player, new SM_SERIAL_KILLER(false, player.getSKInfo().getRank()));
        if (sks.isHandledWorld(player.getWorldId()) && !sks.isEnemyWorld(player)) {
            PacketSendUtility.sendPacket(player, new SM_SERIAL_KILLER(sks.getWorldKillers(player.getWorldId()).values()));
        }

        sendWorldSwitchMessage(player, currentWorldId, worldId, isInstance);
    }

    private static void sendWorldSwitchMessage(Player player, int oldWorld, int newWorld, boolean enteredInstance) {
        if (oldWorld != newWorld) {
            if (enteredInstance) {
                if (!WorldMapType.getWorld(newWorld).isPersonal()) {
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_DUNGEON_OPENED_FOR_SELF(newWorld));
                }
            } else {
                if (newWorld == 600080000) {
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401789));
                    DanceService.getInstance().onEnterZone(player);
                }
            }
        }
        if (player.isMarried() && player.getWedding().isOnline()) {
            Player partner = player.getWedding().getPartner();
            PacketSendUtility.sendMessage(partner, "Ваш партнер телепортируется в локацию " + (ChatUtil.position(WorldMapType.getWorld(player.getWorldId()).getRusname(), player.getPosition())));
        }
        if (AdminConfig.ENABLE_ANNOUNCE_TELEPORT_GM) {
            GMService.getInstance().broadcastMesage("Персонаж " + player.getName() + " телепортируется в " + (ChatUtil.position(WorldMapType.getWorld(player.getWorldId()).getRusname(), player.getPosition())));
        }
    }

    /**
     * @param player
     * @param targetObjectId
     * @param npcId
     */
    public static void showMap(Player player, int targetObjectId, int npcId) {
        if (player.isInFlyingState()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_AIRPORT_WHEN_FLYING);
            return;
        }

        Npc object = (Npc) World.getInstance().findVisibleObject(targetObjectId);
        if (player.isEnemyFrom(object)) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_MOVE_TO_AIRPORT_WRONG_NPC);// TODO retail
            // message
            return;
        }

        PacketSendUtility.sendPacket(player, new SM_TELEPORT_MAP(player, targetObjectId, getTeleporterTemplate(npcId)));
    }

    /**
     * @param npcId
     * @return the teleporterData
     */
    public static TeleporterTemplate getTeleporterTemplate(int npcId) {
        return DataManager.TELEPORTER_DATA.getTeleporterTemplateByNpcId(npcId);
    }

    /**
     * @param player
     * @param kisk
     */
    public static void moveToKiskLocation(Player player, WorldPosition kisk) {
        teleportTo(player, kisk.getMapId(), kisk.getX(), kisk.getY(), kisk.getZ(), kisk.getHeading());
    }

    public static void teleportToPrison(Player player) {
        int location = player.getPrisonLocation();
        if (location == -1) {
            int worldId = player.getRace() == Race.ELYOS ? WorldMapType.DE_PRISON.getId() : WorldMapType.DF_PRISON.getId();
            WorldMapInstance prison = InstanceService.getNextAvailableInstance(worldId);
            InstanceService.registerPlayerWithInstance(prison, player);
            teleportTo(player, worldId, prison.getInstanceId(), 275, 239, 49);
        } else {
            Prison.getInstance().teleportToPrison(player, location);
        }
    }

    public static void teleportToNpc(Player player, int npcId) {
        int worldId = player.getWorldId();
        SpawnSearchResult searchResult = DataManager.SPAWNS_DATA2.getFirstSpawnByNpcId(worldId, npcId);

        if (searchResult == null) {
            log.warn("No npc spawn found for : " + npcId);
            return;
        }

        SpawnSpotTemplate spot = searchResult.getSpot();
        WorldMapTemplate worldTemplate = DataManager.WORLD_MAPS_DATA.getTemplate(searchResult.getWorldId());
        WorldMapInstance newInstance = null;

        if (worldTemplate.isInstance()) {
            newInstance = InstanceService.getNextAvailableInstance(searchResult.getWorldId());
        }

        if (newInstance != null) {
            InstanceService.registerPlayerWithInstance(newInstance, player);
            teleportTo(player, searchResult.getWorldId(), newInstance.getInstanceId(), spot.getX(), spot.getY(), spot.getZ(), (byte) 0, TeleportAnimation.BEAM_ANIMATION);
        } else {
            teleportTo(player, searchResult.getWorldId(), spot.getX(), spot.getY(), spot.getZ(), (byte) 0, TeleportAnimation.BEAM_ANIMATION);
        }
    }

    /**
     * This method will send the set bind point packet
     *
     * @param player
     */
    public static void sendSetBindPoint(Player player) {
        int worldId;
        float x, y, z;
        if (player.getBindPoint() != null) {
            BindPointPosition bplist = player.getBindPoint();
            worldId = bplist.getMapId();
            x = bplist.getX();
            y = bplist.getY();
            z = bplist.getZ();
        } else {
            PlayerInitialData.LocationData locationData = DataManager.PLAYER_INITIAL_DATA.getSpawnLocation(player.getRace());
            worldId = locationData.getMapId();
            x = locationData.getX();
            y = locationData.getY();
            z = locationData.getZ();
        }
        PacketSendUtility.sendPacket(player, new SM_BIND_POINT_INFO(worldId, x, y, z, player));
    }

    /**
     * This method will move a player to their bind location
     *
     * @param player
     * @param useTeleport
     */
    public static void moveToBindLocation(Player player, boolean useTeleport) {
        float x, y, z;
        int worldId;
        byte h = 0;

        if (player.getBindPoint() != null) {
            BindPointPosition bplist = player.getBindPoint();
            worldId = bplist.getMapId();
            x = bplist.getX();
            y = bplist.getY();
            z = bplist.getZ();
            h = bplist.getHeading();
        } else {
            PlayerInitialData.LocationData locationData = DataManager.PLAYER_INITIAL_DATA.getSpawnLocation(player.getRace());
            worldId = locationData.getMapId();
            x = locationData.getX();
            y = locationData.getY();
            z = locationData.getZ();
        }

        InstanceService.onLeaveInstance(player);

        if (useTeleport) {
            teleportTo(player, worldId, x, y, z, h);
        } else {
            World.getInstance().setPosition(player, worldId, 1, x, y, z, h);
        }
    }

    /**
     * Move Player concerning object with specific conditions
     *
     * @param object
     * @param player
     * @param direction
     * @param distance
     * @return true or false
     */
    public static boolean moveToTargetWithDistance(VisibleObject object, Player player, int direction, int distance) {
        double radian = Math.toRadians(object.getHeading() * 3);
        float x0 = object.getX();
        float y0 = object.getY();
        float x1 = (float) (Math.cos(Math.PI * direction + radian) * distance);
        float y1 = (float) (Math.sin(Math.PI * direction + radian) * distance);
        return teleportTo(player, object.getWorldId(), x0 + x1, y0 + y1, object.getZ());
    }

    public static void moveToInstanceExit(Player player, int worldId, Race race) {
        player.getController().cancelCurrentSkill();
        InstanceExit instanceExit = getInstanceExit(worldId, race);
        if (instanceExit == null) {
            log.warn("No instance exit found for race: " + race + " " + worldId);
            moveToBindLocation(player, true);
            return;
        }
        if (InstanceService.isInstanceExist(instanceExit.getExitWorld(), 1)) {
            teleportTo(player, instanceExit.getExitWorld(), instanceExit.getX(), instanceExit.getY(), instanceExit.getZ(), instanceExit.getH());
        } else {
            moveToBindLocation(player, true);
        }
    }

    public static InstanceExit getInstanceExit(int worldId, Race race) {
        return DataManager.INSTANCE_EXIT_DATA.getInstanceExit(worldId, race);
    }

    /**
     * @param player
     * @param portalName
     * @param worldId
     */
    public static void useTeleportScroll(Player player, String portalName, int worldId) {
        PortalScroll template = DataManager.PORTAL2_DATA.getPortalScroll(portalName);
        if (template == null) {
            log.warn("No portal template found for : " + portalName + " " + worldId);
            return;
        }
        if (!RestrictionsManager.canTeleport(player)) {
            return;
        }

        Race playerRace = player.getRace();
        PortalPath portalPath = template.getPortalPath();
        if (portalPath == null) {
            log.warn("No portal scroll for " + playerRace + " on " + portalName + " " + worldId);
            return;
        }
        PortalLoc loc = DataManager.PORTAL_LOC_DATA.getPortalLoc(portalPath.getLocId());
        if (loc == null) {
            log.warn("No portal loc for locId" + portalPath.getLocId());
            return;
        }
        teleportTo(player, worldId, loc.getX(), loc.getY(), loc.getZ());
    }

    /**
     * @param player
     * @param channel
     */
    public static void changeChannel(Player player, int channel) {
        World.getInstance().despawn(player);
        World.getInstance().setPosition(player, player.getWorldId(), channel + 1, player.getX(), player.getY(),
                player.getZ(), player.getHeading());
        player.getController().startProtectionActiveTask();
        PacketSendUtility.sendPacket(player, new SM_CHANNEL_INFO(player.getPosition()));
        PacketSendUtility.sendPacket(player, new SM_PLAYER_SPAWN(player));
    }
}
