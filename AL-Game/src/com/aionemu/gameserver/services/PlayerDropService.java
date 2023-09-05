/*
 * AionLight project
 */
package com.aionemu.gameserver.services;

import com.aionemu.gameserver.ai2.AI2Engine;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.configs.main.DropConfig;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_UPDATE_PLAYER_APPEARANCE;
import com.aionemu.gameserver.services.drop.PlayerDrop;
import com.aionemu.gameserver.services.drop.PlayerDropItem;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javolution.util.FastMap;

/**
 *
 * @author Alex
 */
public class PlayerDropService {

    public static PlayerDropService getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {

        protected static final PlayerDropService instance = new PlayerDropService();
    }
    // private final Map ss = new HashMap();
    public int npcId = 700172;
    private Map<Integer, Set<PlayerDropItem>> current = new FastMap<Integer, Set<PlayerDropItem>>().shared();

    private PlayerDropItem r(Item item, int count) {
        //item.setItemCount(count);
        PlayerDropItem pd = new PlayerDropItem(new PlayerDrop(item));
        pd.getPd().setCount(count);
        //pd.getPd().getItem().setItemCount(count);
        return pd;
    }

    public void onDie(Player player) {
        Set<PlayerDropItem> droppedItems = new HashSet<>();
        SpawnTemplate spawn = SpawnEngine.addNewSpawn(player.getWorldId(), npcId, player.getX(), player.getY(), player.getZ(), player.getHeading(), 0);
        spawn.setMasterName("Труп " + player.getName());
        spawn.setOwnerID(player.getObjectId());
        SpawnEngine.spawnObject(spawn, player.getInstanceId());
        AI2Engine.getInstance().setupAI("chest_player", (Creature) spawn.getVisibleObject());
        ((AbstractAI) ((Creature) spawn.getVisibleObject()).getAi2()).setStateIfNot(AIState.IDLE);
        if (DropConfig.ENABLE_DROP_EQUIPED_ITEMS) {
            List<Item> i = player.getEquipment().getEquippedItems();
            Iterator<Item> ii = i.iterator();
            while (ii.hasNext()) {
                Item act = ii.next();
                player.getEquipment().unEquipItem(act.getObjectId(), act.getEquipmentSlot());
            }
        }
        PacketSendUtility.broadcastPacket(player, new SM_UPDATE_PLAYER_APPEARANCE(player.getObjectId(), player.getEquipment().getEquippedForApparence()), true);
        List<Item> items = player.getInventory().getItemsWithKinah();
        Iterator<Item> it = items.iterator();
        while (it.hasNext()) {
            Item act = it.next();
            if (canAddDrop(act)) {
                droppedItems.add(r(act, (int) act.getItemCount()));
                if (act.getItemTemplate().isKinah()) {
                    player.getInventory().decreaseKinah(act.getItemCount());
                } else {
                    player.getInventory().decreaseByItemId(act.getItemId(), act.getItemCount());
                }
            }
        }
        current.put(spawn.getOwnerID(), droppedItems);
        //droppedItems.clear();
        items.clear();
    }

    public boolean canAddDrop(Item act) {
        if (act.getItemTemplate().isArmor() && !DropConfig.ENABLE_PLAYER_DROP_ARMOR) {
            return false;
        } else if (act.getItemTemplate().isWeapon() && !DropConfig.ENABLE_PLAYER_DROP_WEAPON) {
            return false;
        } else if (act.getItemTemplate().isStigma() && !DropConfig.ENABLE_PLAYER_DROP_STIGMA) {
            return false;
        } else if (act.getItemTemplate().isKinah() && !DropConfig.ENABLE_PLAYER_DROP_KINAH) {
            return false;
        } else if (act.getItemCount() == 0) {
            return false;
        }
        return true;
    }

    public Set<PlayerDropItem> getPlayerDrop(int key) {
        return current.get(key);
    }

    public Map<Integer, Set<PlayerDropItem>> geDropRegistrationMap() {
        return current;
    }
}
