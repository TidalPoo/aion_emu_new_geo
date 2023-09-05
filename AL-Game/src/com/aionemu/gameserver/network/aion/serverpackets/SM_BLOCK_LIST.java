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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.player.BlockList;
import com.aionemu.gameserver.model.gameobjects.player.BlockedPlayer;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * Packet responsible for telling a player his block list
 *
 * @author Ben, Alex
 */
public class SM_BLOCK_LIST extends AionServerPacket {

    private final BlockList blockList;
    private final boolean gmpanel;

    public SM_BLOCK_LIST(BlockList blockList, boolean gmpanel) {
        this.blockList = blockList;
        this.gmpanel = gmpanel;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        BlockList list = blockList;
        writeH((0 - list.getSize()));
        writeC(gmpanel ? 1 : 0); // gm panel
        for (BlockedPlayer p : list) {
            writeS(p.getName());
            writeS(p.getReason());
        }
    }
}
