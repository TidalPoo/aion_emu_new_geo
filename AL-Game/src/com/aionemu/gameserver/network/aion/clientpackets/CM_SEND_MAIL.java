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
import com.aionemu.gameserver.model.gameobjects.LetterType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.mail.MailService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Aion Gates, xTz
 * @modified Alex
 */
public class CM_SEND_MAIL extends AionClientPacket {

    private String recipientName;
    private String title;
    private String message;
    private int itemObjId;
    private int itemCount;
    private int kinahCount;
    private int idLetterType;

    public CM_SEND_MAIL(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    @Override
    protected void readImpl() {
        recipientName = readS();
        title = readS();
        message = readS();
        itemObjId = readD();
        itemCount = readD();
        readD();
        kinahCount = readD();
        readD();
        idLetterType = readC();
    }

    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        if (AdminConfig.GM_NO_SEND_MAIL && player.getAccessLevel() > 0 && player.getAccessLevel() <= AdminConfig.GM_NO_SEND_MAIL_LEVEL) {
            PacketSendUtility.sendBrightYellowMessageOnCenter(player, "DEVELOPER: Can use mail send!!");
            return;
        }

        if (player.isGM() || !player.isTrading() && kinahCount < 1000000000 && kinahCount > -1 && itemCount > -2) {
            MailService.getInstance().sendMail(player, recipientName, title, message, itemObjId, itemCount, kinahCount, LetterType.getLetterTypeById(idLetterType), false);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
