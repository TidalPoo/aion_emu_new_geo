/**
 * MODIF EVO Fichier de bannissement
 *
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 * aion-lightning is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * aion-lightning is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * aion-lightning. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.dao;

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.gameserver.model.Announcement;
import java.util.Set;

/**
 * DAO that manages Announcements
 *
 * @author Divinity
 */
public abstract class AnnouncementsDAO implements DAO {

    public abstract Set<Announcement> getAnnouncements();

    public abstract void addAnnouncement(final Announcement announce);

    public abstract boolean delAnnouncement(final int idAnnounce);

    /**
     * Returns class name that will be uses as unique identifier for all DAO
     * classes
     *
     * @return class name
     */
    @Override
    public final String getClassName() {
        return AnnouncementsDAO.class.getName();
    }
}
