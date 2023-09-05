/**
 * This file is part of aion-lightning <aion-lightning.com>.
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

import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.configs.network.IPConfig;
import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.templates.event.EventTemplate;
import com.aionemu.gameserver.network.NetworkController;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.ChatService;
import java.util.List;

/**
 * @author -Nemesiss- CC fix
 * @modified Dision, Alex
 */
public class SM_VERSION_CHECK extends AionServerPacket {

    /**
     * Aion Client version
     */
    private final int version;
    /**
     * Number of characters can be created
     */
    private int characterLimitCount;

    /**
     * Related to the character creation mode
     */
    private final int characterFactionsMode;
    private final int characterCreateMode;

    /**
     * @param version
     */
    public SM_VERSION_CHECK(int version) {
        this.version = version;

        if (MembershipConfig.CHARACTER_ADDITIONAL_ENABLE != 10 && MembershipConfig.CHARACTER_ADDITIONAL_COUNT > GSConfig.CHARACTER_LIMIT_COUNT) {
            characterLimitCount = MembershipConfig.CHARACTER_ADDITIONAL_COUNT;
        } else {
            characterLimitCount = GSConfig.CHARACTER_LIMIT_COUNT;
        }

        characterLimitCount *= NetworkController.getInstance().getServerCount();

        if (GSConfig.CHARACTER_CREATION_MODE < 0 || GSConfig.CHARACTER_CREATION_MODE > 2) {
            characterFactionsMode = 0;
        } else {
            characterFactionsMode = GSConfig.CHARACTER_CREATION_MODE;
        }

        if (GSConfig.CHARACTER_FACTION_LIMITATION_MODE < 0 || GSConfig.CHARACTER_FACTION_LIMITATION_MODE > 3) {
            characterCreateMode = 0;
        } else {
            characterCreateMode = GSConfig.CHARACTER_FACTION_LIMITATION_MODE * 0x04;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con) {
        //aion 3.0 = 194
        //aion 3.5 = 196
        //aion 4.0 = 201
        if (version < 201) {
            //Send wrong client version
            writeC(0x02);
            return;
        }

        writeC(0x00);
        writeC(NetworkConfig.GAMESERVER_ID);
        writeD(130612);// start year month day
        writeD(130423);// start year month day
        writeD(0x00);// spacing
        writeD(130610);// year month day
        writeD(0x507575B5);// start server time in mili
        writeC(0x00);// unk
        writeC(con.getCountryCode());// country code;
        writeC(0x00);// unk

        int serverMode = (characterLimitCount * 0x10) | characterFactionsMode;

        if (GSConfig.ENABLE_RATIO_LIMITATION) {
            if (GameServer.getCountFor(Race.ELYOS) + GameServer.getCountFor(Race.ASMODIANS) > GSConfig.RATIO_HIGH_PLAYER_COUNT_DISABLING) {
                writeC(serverMode | 0x0C);
            } else if (GameServer.getRatiosFor(Race.ELYOS) > GSConfig.RATIO_MIN_VALUE) {
                writeC(serverMode | 0x04);
            } else if (GameServer.getRatiosFor(Race.ASMODIANS) > GSConfig.RATIO_MIN_VALUE) {
                writeC(serverMode | 0x08);
            } else {
                writeC(serverMode);
            }
        } else {
            writeC(serverMode | characterCreateMode);
        }

        writeD((int) (System.currentTimeMillis() / 1000));
        writeH(350);//4.3
        writeH(1281);//4.3
        writeH(2575);//4.3
        writeH(257);//4.3
        writeH(322);//4.3
        writeH(1);//4.3
        writeC(GSConfig.CHARACTER_REENTRY_TIME);// 20sec
        List<EventTemplate> allEvents = DataManager.EVENT_DATA.getAllEvents();
        int event_id = EventsConfig.ENABLE_DECOR;
        if (event_id == 0) {
            for (EventTemplate et : allEvents) {
                if (et.isActive() && et.isStarted() && et.getDecor() != 0) {
                    event_id += et.getDecor();
                }
            }
        }
        writeC(event_id);//decor
        writeD(0);//4.3
        writeD(0XFFFFF1F0);//4.3
        writeC(4);//4.3
        writeC(120);//4.3
        writeH(25233);//4.3
        writeC(2);// 4.3
        writeC(0);//4.3
        writeD(0);// 4.3
        writeH(0x01);//its loop size
        //for... chat servers?
        {
            writeC(0x00);//spacer
            // if the correct ip is not sent it will not work
            writeB(IPConfig.getDefaultAddress());
            writeH(ChatService.getPort());
        }
    }
}
