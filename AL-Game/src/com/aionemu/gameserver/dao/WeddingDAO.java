/*
 * SAO Project
 */
package com.aionemu.gameserver.dao;

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.weddings.Wedding;
import java.sql.Timestamp;

/**
 *
 * @author Alex
 */
public abstract class WeddingDAO implements DAO {

    public abstract void loadPartner(final Player player);

    public abstract Timestamp getLastOnlineTime(int playerId);

    public abstract int getWorldId(int playerId);

    public abstract boolean insertWedding(Wedding wedding);

    public abstract void update(Wedding wedding);

    public abstract void removeWedding(int playerId, int partnerId);

    public abstract void insertToLog(Wedding wedding);

    @Override
    public String getClassName() {
        return WeddingDAO.class.getName();
    }
}
