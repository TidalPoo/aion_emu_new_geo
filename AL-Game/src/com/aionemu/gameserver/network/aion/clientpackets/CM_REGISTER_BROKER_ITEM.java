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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.BrokerService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author kosyak
 */
public class CM_REGISTER_BROKER_ITEM extends AionClientPacket {

    @SuppressWarnings("unused")
    private int brokerId;
    private int itemUniqueId;
    private long price;
    private int itemCount;

    public CM_REGISTER_BROKER_ITEM(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    @Override
    protected void readImpl() {
        this.brokerId = readD();
        this.itemUniqueId = readD();
        this.price = readQ();
        this.itemCount = readH();
    }

    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();

        if (player.isTrading() || price < 1 || itemCount < 1) {
            return;
        }

        if (player.getAccessLevel() != 0 && player.getAccessLevel() <= AdminConfig.GM_NO_BROKER) {
            PacketSendUtility.sendBrightYellowMessageOnCenter(player, "DEVELOPER: You can not use the access level " + AdminConfig.GM_NO_BROKER);
            return;
        }

        BrokerService.getInstance().registerItem(player, itemUniqueId, itemCount, price);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
