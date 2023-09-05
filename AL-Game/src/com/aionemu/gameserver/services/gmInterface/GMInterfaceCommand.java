/*
 * AionLight project
 */
package com.aionemu.gameserver.services.gmInterface;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.cardinal.AddItemType;
import com.aionemu.gameserver.configs.main.GeoDataConfig;
import com.aionemu.gameserver.dao.InGameShopDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureVisualState;
import com.aionemu.gameserver.model.gminterface.ItemTemp;
import com.aionemu.gameserver.model.gminterface.NpcTemp;
import com.aionemu.gameserver.model.ingameshop.IGPriceType;
import com.aionemu.gameserver.model.templates.item.ItemQuality;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MOVE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_COOLDOWN;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMap;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldMapType;
import com.aionemu.gameserver.world.geo.GeoService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Alex
 */
public class GMInterfaceCommand {

    public void addGameShop(int itemId, long count, long price) {
        IGPriceType price_type = IGPriceType.TOLL;
        int membership = 0;
        int price_item = 0;
        //int itemId = 0;
        int list = 1;
        byte category = 0, subCategory = 0, itemType = 0, gift = 0;
        String titleDescription = "";
        long toll;
        DAOManager.getDAO(InGameShopDAO.class).saveIngameShopItem(IDFactory.getInstance().nextId(), itemId, count, price,
                category, subCategory, list - 1, 1, itemType, gift, price_type, price_item, membership, titleDescription);
    }

    public void toCommand(Player player, String command) {
        String[] params = command.split(" ");
        // npc and items
        switch (params[0]) {
            case "givetitle":
                int titleId = Integer.parseInt(params[1]);
                titleId += player.getRace().getRaceId() * 272;
                if (!player.getTitleList().contains(titleId)) {
                    player.getTitleList().addTitle(titleId, false, 0);
                }
                /*if (titleId == 263) {
                 PacketSendUtility.sendPacket(player, new SM_TITLE_INFO(player, false));
                 }*/
                break;
            case "wish":
                // npc
                if (params.length == 2) {
                    NpcTemp nt = GMItnterfaseService.getInstance().getResultNpc(params[1].toLowerCase());
                    if (nt == null) {
                        PacketSendUtility.sendWhiteMessage(player, "Не найден npc");
                        return;
                    }
                    SpawnTemplate spawn = SpawnEngine.addNewSpawn(player.getWorldId(), nt.getId(), player.getX(), player.getY(), player.getZ(), (byte) 0, 0);

                    if (spawn == null) {
                        PacketSendUtility.sendMessage(player, "There is no template with id " + nt.getId());
                        return;
                    }

                    VisibleObject visibleObject = SpawnEngine.spawnObject(spawn, player.getInstanceId());

                    if (visibleObject == null) {
                        PacketSendUtility.sendMessage(player, "Spawn id " + nt.getId() + " was not found!");
                    }
                } else if (params.length == 3) {
                    ItemTemp it;
                    //items link@
                    if (params[1].length() < 4) {
                        String item = params[2];
                        it = GMItnterfaseService.getInstance().getResultItem(item);
                        if (it == null) {
                            PacketSendUtility.sendWhiteMessage(player, "Не найден предмет 3 " + item);
                            return;
                        }
                        int itemId = it.getId();
                        int count = Integer.parseInt(params[1]);
                        if (DataManager.ITEM_DATA.getItemTemplate(itemId) == null) {
                            PacketSendUtility.sendMessage(player, "Item id is incorrect: " + itemId);
                            return;
                        }
                        ItemService.addItem(player, itemId, count, AddItemType.ADMINPANEL, "name: " + player.getName() + " al: " + player.getAccessLevel());
                    } else {
                        // items
                        String item = params[1];
                        it = GMItnterfaseService.getInstance().getResultItem(item);
                        long count = Long.parseLong(params[2]);
                        if (count == 0) {
                            count = 1;
                        }
                        if (it == null) {
                            PacketSendUtility.sendWhiteMessage(player, "Не найден предмет 1 " + item);
                            return;
                        }
                        if (DataManager.ITEM_DATA.getItemTemplate(it.getId()) == null) {
                            PacketSendUtility.sendMessage(player, "Item id is incorrect: " + it.getId() + " name: " + it.getName());
                            return;
                        }
                        ItemService.addItem(player, it.getId(), count, AddItemType.ADMINPANEL, "name: " + player.getName() + " al: " + player.getAccessLevel());
                    }
                }
                break;
            case "teleport_to_named":
                NpcTemp nt = GMItnterfaseService.getInstance().getResultNpc(params[1]);
                int npcId = nt.getId();
                TeleportService2.teleportToNpc(player, npcId);
                break;
            case "wishid":
                if (player.getTarget() != null && player.getTarget() instanceof Player) {
                    player = (Player) player.getTarget();
                }
                int itemId = Integer.parseInt(params[2]);
                long count = Long.parseLong(params[1]);
                ItemTemplate data = DataManager.ITEM_DATA.getItemTemplate(itemId);
                if (data == null) {
                    PacketSendUtility.sendMessage(player, "Item id is incorrect: " + itemId);
                    return;
                }
                if (!data.isStackable() && count > 20) {
                    count = 1;
                }
                ItemService.addItem(player, itemId, count, AddItemType.ADMINPANEL, "name: " + player.getName() + " al: " + player.getAccessLevel());
                break;
            case "invisible":
                if (player.getVisualState() >= 3) {
                    PacketSendUtility.sendMessage(player, "Ваш персонаж уже в инвизе!");
                    return;
                }
                //для бесконечного полета при слежке за игроком.
                if (!player.isInvul()) {
                    player.setInvul(true);
                }
                player.getEffectController().setAbnormal(AbnormalState.HIDE.getId());
                player.setVisualState(CreatureVisualState.INVISIBLE);
                PacketSendUtility.broadcastPacket(player, new SM_PLAYER_STATE(player), true);
                PacketSendUtility.sendMessage(player, "You are invisible.");
                break;
            case "visible":
                if (player.getVisualState() < 3) {
                    PacketSendUtility.sendMessage(player, "Ваш персонаж уже видимый!");
                    return;
                }
                player.getEffectController().unsetAbnormal(AbnormalState.HIDE.getId());
                player.unsetVisualState(CreatureVisualState.INVISIBLE);
                PacketSendUtility.broadcastPacket(player, new SM_PLAYER_STATE(player), true);
                PacketSendUtility.sendMessage(player, "You are visible.");
                break;
            case "remove_skill_delay_all":
                List<Integer> delayIds = new ArrayList<>();
                if (player.getSkillCoolDowns() != null) {
                    long currentTime = System.currentTimeMillis();
                    for (Map.Entry<Integer, Long> en : player.getSkillCoolDowns().entrySet()) {
                        delayIds.add(en.getKey());
                    }

                    for (Integer delayId : delayIds) {
                        player.setSkillCoolDown(delayId, currentTime);
                    }
                    delayIds.clear();
                    PacketSendUtility.sendPacket(player, new SM_SKILL_COOLDOWN(player.getSkillCoolDowns(), false));
                }
                break;
            case "teleportto":
                Player tplayer = World.getInstance().findPlayer(params[1]);
                if (tplayer == null) {
                    PacketSendUtility.sendMessage(player, params[1] + " не онлайн");
                    return;
                }

                TeleportService2.teleportTo(player, tplayer.getWorldId(), tplayer.getInstanceId(), tplayer.getX(), tplayer.getY(), tplayer.getZ(), (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                break;
            case "teleport":
                int worldId;
                int x,
                 y,
                 z;
                if (params.length == 4) {
                    worldId = player.getWorldId();
                    x = Integer.parseInt(params[1]);
                    y = Integer.parseInt(params[2]);
                    z = Integer.parseInt(params[3]);
                } else {
                    worldId = WorldMapType.getWorldClientName(params[1].toLowerCase()).getId();
                    x = Integer.parseInt(params[2]);
                    y = Integer.parseInt(params[3]);
                    z = Integer.parseInt(params[4]);
                }
                if (GeoDataConfig.GEO_ENABLE) {
                    int nz = (int) GeoService.getInstance().getZ(worldId, x, y);
                    if (z > nz) {
                        z = nz + 10;
                    }
                } else if (z < 100) {
                    z = 100;
                    //TODO fix if z > 1000 and if zone map minZ < 1000
                }
                if (worldId != 400010000) {
                    if (z > 1000) {
                        PacketSendUtility.sendMessage(player, "Z " + z + " недопустимо. Авто смена координаты на 1000");
                        z = 1000;
                    }
                } else {
                    if (z >= 10000) {
                        z = 2500;
                    } else {
                        z += 20;
                    }
                }
                if (worldId == 0) {
                    PacketSendUtility.sendMessage(player, "World id no such");
                    return;
                }
                //player.setInvul(true);
                if (worldId == player.getWorldId()) {
                    player.getMoveController().setNewDirection(x, y, z);
                    World.getInstance().updatePosition(player, x, y, z, (byte) 0);
                    PacketSendUtility.broadcastPacketAndReceive(player, new SM_MOVE(player));
                    //player.setInvul(false);
                } else {
                    goTo(player, worldId, x, y, z);
                    //player.setInvul(false);
                }
                break;
            case "delete_items":
                int type = Integer.parseInt(params[1]);
                List<Item> items = player.getInventory().getItems();
                Iterator<Item> it = items.iterator();
                while (it.hasNext()) {
                    Item act = it.next();
                    if (!act.getItemTemplate().isKinah()) {
                        if (act.getItemTemplate().getItemQuality().getQualityId() <= type) {
                            player.getInventory().decreaseByObjectId(act.getObjectId(), act.getItemCount());
                        }
                    }
                }
                PacketSendUtility.sendMessage(player, "Удалены предметы типа " + ItemQuality.getTypeIfId(type).getRusname() + " и хуже");
                break;
            case "addskill":
                // выучить умения разработчика
                String skillName = params[1];
                int id;
                switch (skillName) {
                    case "skill1":
                        id = 1904;
                        break;
                    case "skill2":
                        id = 1911;
                        break;
                    case "skill3":
                        id = 3558;
                        break;
                    case "skill4":
                        id = 3576;
                        break;
                    case "skill5":
                        id = 3581;
                        break;
                    default:
                        PacketSendUtility.sendMessage(player, skillName + " не найден");
                        return;
                }
                if (player.getSkillList().getSkillEntry(id) != null) {
                    PacketSendUtility.sendMessage(player, skillName + " уже изучено");
                    return;
                }
                if (id != 0) {
                    player.getSkillList().addGameMasterSkill(player, id, 1);
                }
                break;
            case "levelup": {
                int level = Integer.parseInt(params[1]);
                level += player.getLevel();
                // int level = player.getLevel() + levelup;
                player.getCommonData().setLevel(level);
                break;
            }
            case "leveldown": {
                int leveldown = Integer.parseInt(params[1]);
                int level = player.getLevel() - leveldown;
                player.getCommonData().setLevel(level);
                break;
            }
            case "changeclass":
                String className = params[1];
                if (PlayerClass.valueOf(className.toUpperCase()) != null) {
                    player.getCommonData().setPlayerClass(PlayerClass.valueOf(className.toUpperCase()));
                    player.getController().upgradePlayer();
                }
                break;
            case "partyrecall":
                if (player.getPlayerGroup2() == null) {
                    PacketSendUtility.sendMessage(player, "Вы не состоите в группе");
                    return;
                }
                for (Player pg : player.getPlayerGroup2().getOnlineMembers()) {
                    if (pg != player) {
                        TeleportService2.teleportTo(pg, player.getWorldId(), player.getInstanceId(), player.getX(), player.getY(), player.getZ(), (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                        PacketSendUtility.sendMessage(player, pg.getName() + " телепортируется к вам");
                    }
                }
                break;
            default:
                PacketSendUtility.sendWhiteMessage(player, "Нет реализации для команды " + command);
                break;
        }
    }

    private static void goTo(final Player player, int worldId, float x, float y, float z) {
        WorldMap destinationMap = World.getInstance().getWorldMap(worldId);
        TeleportAnimation anim = TeleportAnimation.BEAM_ANIMATION;
        if (player.getWorldId() == worldId) {
            anim = TeleportAnimation.NO_ANIMATION;
        }
        if (destinationMap.isInstanceType()) {
            TeleportService2.teleportTo(player, worldId, getInstanceId(worldId, player), x, y, z, (byte) 0, anim);
        } else {
            TeleportService2.teleportTo(player, worldId, x, y, z, (byte) 0, anim);
        }
        //PacketSendUtility.sendYellowMessageOnCenter(player, "\u0412\u044b \u043f\u0435\u0440\u0435\u043d\u043e\u0441\u0438\u0442\u0435\u0441\u044c \u0432 \u043b\u043e\u043a\u0430\u0446\u0438\u044e " + WorldMapType.getWorld(worldId).getRusname());
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

    public static GMInterfaceCommand getInstance() {
        return GMCommandHolder.INSTANCE;
    }

    private static class GMCommandHolder {

        private static final GMInterfaceCommand INSTANCE = new GMInterfaceCommand();
    }
}
