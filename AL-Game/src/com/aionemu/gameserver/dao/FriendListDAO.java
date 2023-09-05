/*
 * This file is part of aion-lightning <aion-lightning.org>.
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

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.gameserver.model.gameobjects.player.FriendList;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author Ben
 */
public abstract class FriendListDAO implements DAO {

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public String getClassName() {
        return FriendListDAO.class.getName();
    }

    /**
     * Loads the friend list for the given player
     *
     * @param player Player to get friend list of
     * @return FriendList for player
     */
    public abstract FriendList load(final Player player);

    /**
     * Makes the given players friends
     * <ul>
     * <li>Note: Adds for both players</li>
     * </ul>
     *
     * @param player Player who is adding
     * @param friend Friend to add to the friend list
     * @return Success
     */
    public abstract boolean addFriends(final Player player, final Player friend);

    public abstract boolean addFriendOffline(final int playerObjId, final int friendObjId);

    /**
     * Deletes the friends from eachothers lists
     *
     * @param playerOid
     * @param friendOid
     * @return Success
     */
    public abstract boolean delFriends(final int playerOid, final int friendOid);

}
