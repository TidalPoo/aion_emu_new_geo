/*
 * This file is part of aion-lightning <aion-lightning.smfnew.com>.
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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.templates.world.WeatherEntry;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author ATracer
 * @author Kwazar
 * @author Nemesiss :D:D
 */
public class SM_WEATHER extends AionServerPacket {

    private WeatherEntry[] weatherEntries;
    private int u = 0;
    private int lenght;
    private int code;

    public SM_WEATHER(WeatherEntry[] weatherEntries) {
        this.weatherEntries = weatherEntries;
    }

    public SM_WEATHER(WeatherEntry[] weatherEntries, int unk, int lenght, int code) {
        this.weatherEntries = weatherEntries;
        this.u = unk;
        this.lenght = lenght;
        this.code = code;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        writeC(u);// unk
        if (weatherEntries == null) {
            writeC(lenght);
            writeC(code);
        } else {
            writeC(weatherEntries.length);
            for (WeatherEntry entry : weatherEntries) {
                writeC(entry.getCode());
            }
        }
    }
}
