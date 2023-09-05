/*
 * AionLight project
 */
package com.aionemu.gameserver.dao;

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 *
 * @author Alex
 */
public abstract class EventItemsDAO implements DAO {

    /**
     * @return unique identifier for EventItemsDAO
     */
    @Override
    public final String getClassName() {
        return EventItemsDAO.class.getName();
    }

    /**
     * @param player
     */
    public abstract void loadItems(Player player);

    /**
     * @param player
     */
    public abstract void storeItems(Player player);

    /**
     * @param itemId
     */
    public abstract void deleteItems(final int itemId);

}
