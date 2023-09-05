/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.chatserver.network.aion.serverpackets;

import com.aionemu.chatserver.model.channel.Channel;
import com.aionemu.chatserver.network.aion.AbstractServerPacket;
import com.aionemu.chatserver.network.netty.handler.ClientChannelHandler;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author ATracer
 */
public class SM_CHANNEL_RESPONSE extends AbstractServerPacket {

    private Channel channel;
    private final int channelIndex;

    public SM_CHANNEL_RESPONSE(Channel channel, int channelIndex) {
        super(0x11);
        this.channel = channel;
        this.channelIndex = channelIndex;
    }

    @Override
    protected void writeImpl(ClientChannelHandler cHandler, ChannelBuffer buf) {
        writeC(buf, getOpCode());
        writeC(buf, 0x40);
        writeH(buf, channelIndex);
        writeH(buf, 0x00);
        writeH(buf, 0x00);
        writeD(buf, channel.getChannelId());
        // writeC(buf, 0x19);
        // writeC(buf, 0x00);
        // writeC(buf, 0x80); //part of id
    }

}
