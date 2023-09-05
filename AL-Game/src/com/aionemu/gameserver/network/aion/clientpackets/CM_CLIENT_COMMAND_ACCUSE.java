/*
 * AionLight project
 */
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 *
 * @author Alex
 */
public class CM_CLIENT_COMMAND_ACCUSE extends AionClientPacket {

    public CM_CLIENT_COMMAND_ACCUSE(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    @Override
    protected void readImpl() {
        // empty packet
    }

    @Override
    protected void runImpl() {
        Player p = getConnection().getActivePlayer();
        switch (p.getAccuseLevel()) {
            case 1:
                //1400071
                //<body>Сейчас на вас наложено ограничение 1-го уровня, которое будет снято через %0 мин.
                //Это никак не влияет на ход игры, но в случае, если жалобы на вас не прекратятся,
                //уровень ограничения поднимется, и вы будете получать меньше опыта и предметов.</body>
                PacketSendUtility.sendPacket(p, SM_SYSTEM_MESSAGE.STR_MSG_ACCUSE_INFO_1_LEVEL(""));
                break;
            case 2:
                //1400072
                //<body>Сейчас на вас наложено ограничение 2-го уровня, которое будет снижено до 1-го уровня через %0 мин.
                //Вы получаете меньше опыта, кинаров, очков Бездны, шанс успешного сбора предметов уменьшен.
                //Если жалобы на вас будут поступать и дальше, к вам будут применены более жесткие ограничения.</body>
                PacketSendUtility.sendPacket(p, SM_SYSTEM_MESSAGE.STR_MSG_ACCUSE_INFO_2_LEVEL(""));
                break;
            case 3:
                //1400073
                //<body>Сейчас на вас наложено ограничение 3-го уровня,
                //которое будет снижено до 2-го уровня через %0 мин.
                //Вы получаете значительно меньше опыта, кинаров, очков Бездны и не можете собирать трофеи.
                //Также вы не можете вступить в группу или альянс.
                //Если жалобы на вас будут поступать и дальше, к вам будут применены более жесткие ограничения.</body>
                PacketSendUtility.sendPacket(p, SM_SYSTEM_MESSAGE.STR_MSG_ACCUSE_INFO_3_LEVEL(""));
                break;
            case 4:
                //1400074
                //<body>Сейчас на вас наложено ограничение 4-го уровня,
                //которое будет снижено до 3-го уровня через %0 мин.
                //Вы не получаете опыт, кинары, очки Бездны и не можете собирать трофеи.
                //Также вы не можете вступить в группу или альянс.</body>
                PacketSendUtility.sendPacket(p, SM_SYSTEM_MESSAGE.STR_MSG_ACCUSE_INFO_4_LEVEL(""));
                break;
            default:
                //normal
                PacketSendUtility.sendPacket(p, new SM_SYSTEM_MESSAGE(1400076));
                break;
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
