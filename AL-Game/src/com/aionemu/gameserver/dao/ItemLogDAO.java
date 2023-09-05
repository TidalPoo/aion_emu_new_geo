/*
 * SAO Project
 */
package com.aionemu.gameserver.dao;

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 *
 * @author Alex
 */
public abstract class ItemLogDAO implements DAO {

    public abstract boolean insertLog(Player player);

    @Override
    public String getClassName() {
        return ItemLogDAO.class.getName();
    }

}
