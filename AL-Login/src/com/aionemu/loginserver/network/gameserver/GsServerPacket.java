/**
 * This file is part of aion-emu <aion-emu.com>.
 *
 * aion-emu is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * aion-emu is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * aion-emu. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.loginserver.network.gameserver;

import com.aionemu.commons.network.packet.BaseServerPacket;
import java.nio.ByteBuffer;

/**
 * Base class for every LS -> GameServer Server Packet.
 *
 * @author -Nemesiss-
 */
public abstract class GsServerPacket extends BaseServerPacket {

    /**
     * Constructs a new server packet with specified id.
     *
     */
    protected GsServerPacket() {
        super(0);
    }

    /**
     * Write this packet data for given connection, to given buffer.
     *
     * @param con
     * @param buffer
     */
    public final void write(GsConnection con, ByteBuffer buffer) {
        setBuf(buffer);
        buf.putShort((short) 0);
        writeImpl(con);
        buf.flip();
        buf.putShort((short) buf.limit());
        buf.position(0);
    }

    /**
     * Write data that this packet represents to given byte buffer.
     *
     * @param con
     */
    protected abstract void writeImpl(GsConnection con);
}
