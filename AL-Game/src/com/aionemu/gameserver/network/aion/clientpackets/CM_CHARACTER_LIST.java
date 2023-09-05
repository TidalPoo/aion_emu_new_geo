/**
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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.cardinal.CardinalManager;
import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ACCOUNT_PROPERTIES;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CHARACTER_LIST;

/**
 * In this packets aion client is requesting character list.
 *
 * @author -Nemesiss-
 * @modified Alex
 */
public class CM_CHARACTER_LIST extends AionClientPacket {

    /**
     * PlayOk2 - we dont care...
     */
    private int playOk2;

    /**
     * Constructs new instance of <tt>CM_CHARACTER_LIST </tt> packet.
     *
     * @param opcode
     * @param state
     * @param restStates
     */
    public CM_CHARACTER_LIST(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        playOk2 = readD();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        AionConnection con = this.getConnection();
        Account account = con.getAccount();
        if (account.isIpDeveloper(con.getIP(), con.getMacAddress())) {
            account.setDeveloper(true);
        }
        sendPacket(new SM_CHARACTER_LIST(playOk2));
        sendPacket(new SM_ACCOUNT_PROPERTIES());
        CardinalManager.saveConnection(account.getId(), con.getMacAddress(), con.getHddSerial(), con.getIpv4list(), con.getIP());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
