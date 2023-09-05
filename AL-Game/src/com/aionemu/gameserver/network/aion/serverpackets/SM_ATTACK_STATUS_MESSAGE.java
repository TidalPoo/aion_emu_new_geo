package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 *
 * @author Alex
 */
public class SM_ATTACK_STATUS_MESSAGE extends AionServerPacket {

    private final int messageId;

    public SM_ATTACK_STATUS_MESSAGE(int messageId) {
        this.messageId = messageId;
    }

    /*
    1 - Атакующий или цель находятся в другой зоне.
    2 - Неверная цель
    3 - no message
    4 - Цель находится слишком далеко.
    5 - Невозможно атаковать из-за преграды.
    6 - Вы находитесь слишком близко к цели, чтобы её атаковать.
    */
    @Override
    protected void writeImpl(AionConnection con) {
        writeC(messageId);
        writeC(0);//unk
    }
}
