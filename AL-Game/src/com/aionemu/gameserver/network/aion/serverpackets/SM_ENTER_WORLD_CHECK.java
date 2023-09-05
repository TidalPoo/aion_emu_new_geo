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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * dunno wtf this packet is doing. Not sure about id/name
 *
 * @author -Nemesiss-
 */
public class SM_ENTER_WORLD_CHECK extends AionServerPacket {

    private byte msg = 0x00;

    public SM_ENTER_WORLD_CHECK(byte msg) {
        this.msg = msg;
    }

    public SM_ENTER_WORLD_CHECK() {
        //TODO
    }

    // от 1 до 7
    //1 выбранный вами персонаж уже используеться на данном сервере.
    //2 ---
    //3 и 7 на данном аккаунте есть персонажи принадлежащие к разным расам но существующие на одном сервере
    //4 вы не можете войти в игру до тех пор пока ваш персонаж зарезервирован.
    //5 вы создали слишком много персонажей , для продолжения игры удалите одного из них.Если вы поменяли сервер, зайдите на старый сервер и после этого попробуйте зайти на новый
    //6 данный персонаж ожидает переподключения макс 20 сек.
    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con) {
        writeC(0);
        writeC(0);
        writeC(0);
    }
}
