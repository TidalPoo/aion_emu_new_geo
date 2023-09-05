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

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.weddings.WeddingService;
import com.aionemu.gameserver.utils.ChatUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.audit.AuditLogger;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldType;

/**
 * Received when a name reports another name with /ReportAutoHunting
 *
 * @author Jego, Alex
 */
public class CM_REPORT_PLAYER extends AionClientPacket {

    private String name;

    /**
     * A name gets reported.
     *
     * @param opcode
     * @param state
     * @param restStates
     */
    public CM_REPORT_PLAYER(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    @Override
    protected void readImpl() {
        readB(1); // unknown byte.
        name = readS(); // the name of the reported person.
    }

    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        name = ChatUtil.getRealAdminName(name);
        if (name.contains("\ue020")) {
            name = WeddingService.getRealWeddingsName(name);
        }

        Player reportPlayer = World.getInstance().findPlayer(name);
        if (reportPlayer == null) {
            PacketSendUtility.sendMessage(player, name + " не онлайн");
            return;
        }
        if (reportPlayer == player) {
            PacketSendUtility.sendMessage(player, "Нельзя использовать на себя");
            return;
        }
        if (player.getAccuseLevel() > 1) {
            //1400085
            //<body>Вы не можете отправить жалобу об использовании программ,
            //автоматизирующих игровой процесс. </body>
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400085));
            return;
        }

        if (player.getRace() == Race.ELYOS && player.getWorldType() != WorldType.ELYSEA
                || player.getRace() == Race.ASMODIANS && player.getWorldType() != WorldType.ASMODAE) {
            //1400086
            //<body>Здесь нельзя подать жалобу на использование игроком программы
            //автоматизации игрового процесса.</body>
            //TODO FIX IT
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400086));
            return;
        }
        if (reportPlayer.getRace() != player.getRace()) {
            //<body>Прежде чем направить жалобу, пожалуйста, выберите персонажа своей расы.</body>
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400020));
            return;
        }
        if (reportPlayer.addAccountIdAccuese(player.getPlayerAccount().getId())) {
            //1390258
            //<body>На персонажа %0 поступила жалоба.
            //Количество жалоб на использование программ автоматизации игрового процесса,
            //которое вы можете отправить: %1 шт.</body>
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ACCUSE_SUBMIT(name, "2"));
        } else {
            //1400090
            //<body>Количество жалоб по использованию программы автоматизации игрового процесса
            //превысило число возможных, поэтому вы больше не можете подавать жалобы.</body>
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400090));
        }

        //1400069
        //<body>Поступили жалобы о том, что на данном аккаунте используются программы автоматизации
        //игрового процесса. К вам применен %num-й уровень ограничений.
        //Статус и время ограничения можно проверить с помощью команды '/ограничение'. </body>
        PacketSendUtility.sendPacket(reportPlayer, SM_SYSTEM_MESSAGE.STR_MSG_ACCUSE_UPGRADE_LEVEL(reportPlayer.getAccuseLevel()));

        //1400091
        //<body>Оставшееся количество жалоб по использованию программы
        //автоматизации игрового процесса составляет %0.</body>
        //STR_MSG_ACCUSE_COUNT_INFO
        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ACCUSE_COUNT_INFO("2"));
        AuditLogger.info(player, "Reports the player: " + name);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
