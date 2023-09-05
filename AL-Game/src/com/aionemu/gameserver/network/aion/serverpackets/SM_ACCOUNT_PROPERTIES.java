/*
 * This file is part of aion-lightning <aion-lightning.org>.
 * 
 * aion-lightning is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * aion-lightning is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 *
 * @author pixfid
 * @modified Alex
 */
public class SM_ACCOUNT_PROPERTIES extends AionServerPacket {

    public SM_ACCOUNT_PROPERTIES() {
    }

    @Override
    protected void writeImpl(AionConnection con) {
        //AdminConfig.GM_PANEL
        Account account = con.getAccount();
        writeC(account.isDeveloper() || account.getAccessLevel() >= AdminConfig.GM_PANEL ? 1 : 0);
        writeH(0x00);//1
        writeC(0x00);//2
        writeD(0x00);//3
        writeH(0x04);//4
        writeH(0x04);//5
        writeC(0x00);//6
        writeH(0x10);//7
        writeD(0x00);//8
        writeH(0x00);//9
        writeD(0x00);//10
        writeH(0x04);//11
        writeH(0x00);//12
        /*
         ----------------------{0}---------------------
         accessLevel and gm panel
         ----------------------{1}---------------------
         unk
         ----------------------{12}--------------------
         0x00 - Особый пользователь - (полная подписка)
         0x01 - Фримиум - (TRIAL)
         0x02 - Мудрый пользователь - (Бесплатная)
         0x03 - Опытный пользователь - (Бронза)
         0x04 - Искусный пользователь - (Серебро)
         */
    }
}
