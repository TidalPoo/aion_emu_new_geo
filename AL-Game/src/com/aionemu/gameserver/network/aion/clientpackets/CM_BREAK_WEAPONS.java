/**
 * This file is part of aion-lightning <aion-lightning.smfnew.com>.
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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.ArmsfusionService;

/**
 * @author zdead
 */
public class CM_BREAK_WEAPONS extends AionClientPacket {

    public CM_BREAK_WEAPONS(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    private int weaponToBreakUniqueId;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        readD();
        weaponToBreakUniqueId = readD();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        ArmsfusionService.breakWeapons(getConnection().getActivePlayer(), weaponToBreakUniqueId);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
