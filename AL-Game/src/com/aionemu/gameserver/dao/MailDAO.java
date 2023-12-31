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

import com.aionemu.gameserver.model.gameobjects.Letter;
import com.aionemu.gameserver.model.gameobjects.player.Mailbox;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import java.sql.Timestamp;

/**
 * @author kosyachok
 */
public abstract class MailDAO implements IDFactoryAwareDAO {

    @Override
    public String getClassName() {
        return MailDAO.class.getName();
    }

    public abstract boolean storeLetter(Timestamp time, Letter letter);

    public abstract Mailbox loadPlayerMailbox(Player player);

    public abstract void storeMailbox(Player player);

    public abstract boolean deleteLetter(int letterId);

    public abstract void updateOfflineMailCounter(PlayerCommonData recipientCommonData);

    public abstract boolean haveUnread(int playerId);

    //new
    public abstract void updateUsedIDs(final int id);
}
