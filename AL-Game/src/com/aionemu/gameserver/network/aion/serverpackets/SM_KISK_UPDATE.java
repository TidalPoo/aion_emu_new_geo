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

import com.aionemu.gameserver.model.gameobjects.Kisk;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Sarynth 0xB0 for 1.5.1.10 and 1.5.1.15
 */
public class SM_KISK_UPDATE extends AionServerPacket {

    // useMask values determine who can bind to the kisk.
    // 1 ~ race
    // 2 ~ legion
    // 3 ~ solo
    // 4 ~ group
    // 5 ~ alliance
    // of course, we must programmatically check as well.
    private final int objId;
    private final int useMask;
    private final int currentMembers;
    private final int maxMembers;
    private final int remainingRessurects;
    private final int maxRessurects;
    private final int remainingLifetime;

    public SM_KISK_UPDATE(Kisk kisk) {
        this.objId = kisk.getObjectId();
        this.useMask = kisk.getUseMask();
        this.currentMembers = kisk.getCurrentMemberCount();
        this.maxMembers = kisk.getMaxMembers();
        this.remainingRessurects = kisk.getRemainingResurrects();
        this.maxRessurects = kisk.getMaxRessurects();
        this.remainingLifetime = kisk.getRemainingLifetime();
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeD(objId);
        writeD(useMask);
        writeD(currentMembers);
        writeD(maxMembers);
        writeD(remainingRessurects);
        writeD(maxRessurects);
        writeD(remainingLifetime);
    }
}
