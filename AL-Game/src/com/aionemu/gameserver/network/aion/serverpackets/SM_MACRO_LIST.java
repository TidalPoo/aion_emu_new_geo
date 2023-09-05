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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import java.util.Map;

/**
 * Packet with macro list.
 *
 * @author -Nemesiss-
 * @modified Alex
 */
public class SM_MACRO_LIST extends AionServerPacket {

    private final Player player;

    /**
     * Constructs new <tt>SM_MACRO_LIST </tt> packet
     *
     * @param player
     */
    public SM_MACRO_LIST(Player player) {
        this.player = player;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con) {
        writeD(player.getObjectId());// player id
        writeC(0x01);
        final int size = player.getMacroList().getSize();
        writeH(-size);
        if (size > 0) {
            for (Map.Entry<Integer, String> entry : player.getMacroList().entrySet()) {
                writeC(entry.getKey());// order
                writeS(entry.getValue());// xml
            }
        }
    }
}
