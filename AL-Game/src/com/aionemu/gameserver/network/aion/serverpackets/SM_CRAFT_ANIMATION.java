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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Mr. Poke
 */
public class SM_CRAFT_ANIMATION extends AionServerPacket {

    private final int senderObjectId;
    private final int targetObjectId;
    private final int skillId;
    private final int action;

    /**
     * @param senderObjectId
     * @param targetObjectId
     * @param skillId
     * @param action
     */
    public SM_CRAFT_ANIMATION(int senderObjectId, int targetObjectId, int skillId, int action) {
        this.senderObjectId = senderObjectId;
        this.targetObjectId = targetObjectId;
        this.skillId = skillId;
        this.action = action;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con) {
        writeD(senderObjectId);
        writeD(targetObjectId);
        writeH(skillId);
        writeC(action);
    }
}
