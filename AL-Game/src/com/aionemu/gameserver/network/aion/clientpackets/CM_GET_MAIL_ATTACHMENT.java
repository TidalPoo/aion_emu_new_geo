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

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.ingameshop.InGameShopEn;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.mail.MailService;

/**
 * @author kosyachok
 */
public class CM_GET_MAIL_ATTACHMENT extends AionClientPacket {

    private int mailObjId;
    private int attachmentType;

    public CM_GET_MAIL_ATTACHMENT(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    @Override
    protected void readImpl() {
        mailObjId = readD();
        attachmentType = readC(); // 0 - item , 1 - kinah
    }

    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        MailService.getInstance().getAttachments(player, mailObjId, attachmentType);
        if (player.isGP()) {
            int col = CustomConfig.BONUS_TOLL;
            if (col != 0) {
                InGameShopEn.getInstance().addToll(player, col);
            }
            player.setGP(false);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
