/**
 * SAO Project
 */
package com.aionemu.gameserver.dao;

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestFriendList;
import com.aionemu.gameserver.model.gameobjects.player.RequestList;

/**
 * @author Alex
 */
public abstract class RequestFriendListDAO implements DAO {

    @Override
    public String getClassName() {
        return RequestFriendListDAO.class.getName();
    }

    public abstract RequestFriendList load(final Player player);

    public abstract boolean addRequests(final int objId, final Player request);

    public abstract boolean delRequests(final int playerOid, final int requestOid);

    public abstract RequestList loadSender(final Player player);
}
