/*
 * This file is part of aion-lightning <aionu-unique.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.dao;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Equipment;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.items.storage.StorageType;
import java.util.Collections;
import java.util.List;
import javolution.util.FastList;

/**
 * @author ATracer
 */
public abstract class InventoryDAO implements IDFactoryAwareDAO {

    /**
     * @param playerId
     * @param storageType
     * @return IStorage
     */
    public abstract Storage loadStorage(int playerId, StorageType storageType);

    public abstract List<Item> loadStorageDirect(int playerId, StorageType storageType);

    /**
     * @param player
     * @return Equipment
     */
    public abstract Equipment loadEquipment(Player player);

    /**
     * @param playerId
     * @return
     */
    public abstract List<Item> loadEquipment(int playerId);

    public abstract boolean store(Player player);

    public abstract boolean store(Item item, Player player);

    public boolean store(Item item, int playerId) {
        return store(Collections.singletonList(item), playerId);
    }

    public abstract boolean store(List<Item> items, int playerId);

    /**
     * @param item
     * @return
     */
    public boolean store(Item item, Integer playerId, Integer accountId, Integer legionId) {
        FastList<Item> temp = FastList.newInstance();
        temp.add(item);
        return store(temp, playerId, accountId, legionId);
    }

    public abstract boolean store(List<Item> items, Integer playerId, Integer accountId, Integer legionId);

    /**
     * @param playerId
     * @return
     */
    public abstract boolean deletePlayerItems(int playerId);

    public abstract void deleteAccountWH(int accountId);

    @Override
    public String getClassName() {
        return InventoryDAO.class.getName();
    }
}
