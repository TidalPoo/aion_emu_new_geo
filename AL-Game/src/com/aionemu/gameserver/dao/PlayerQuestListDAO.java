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
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.QuestStateList;

/**
 * @author MrPoke
 * @modified vlog
 */
public abstract class PlayerQuestListDAO implements DAO {

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public String getClassName() {
        return PlayerQuestListDAO.class.getName();
    }

    /**
     * @param player
     * @param playerId
     * @return QuestStateList
     */
    public abstract QuestStateList load(final Player player);

    /**
     * @param player
     * @param Player
     * @param QuestStateList
     */
    public abstract void store(final Player player);
}
