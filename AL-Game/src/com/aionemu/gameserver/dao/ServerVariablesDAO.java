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

/**
 * @author Ben
 */
public abstract class ServerVariablesDAO implements DAO {

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public final String getClassName() {
        return ServerVariablesDAO.class.getName();
    }

    /**
     * Loads the server variables stored in the database
     *
     * @return
     * @returns variable stored in database
     */
    public abstract int load(String var);

    /**
     * Stores the server variables
     *
     * @return
     */
    public abstract boolean store(String var, int value);

}
