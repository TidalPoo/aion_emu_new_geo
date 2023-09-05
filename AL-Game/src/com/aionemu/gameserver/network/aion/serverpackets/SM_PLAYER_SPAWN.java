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

import com.aionemu.gameserver.cardinal.CardinalManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMapType;

/**
 * This packet is notify client what map should be loaded.
 *
 * @author -Nemesiss-
 */
public class SM_PLAYER_SPAWN extends AionServerPacket {

    /**
     * Player that is entering game.
     */
    private final Player player;

    /**
     * Constructor.
     *
     * @param player
     */
    public SM_PLAYER_SPAWN(Player player) {
        super();
        this.player = player;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con) {
        writeD(player.getWorldId()); // world + chnl
        writeD(player.getWorldId());
        writeD(0x00); // unk 0x13
        writeC(WorldMapType.getWorld(player.getWorldId()).isPersonal() ? 1 : 0);
        writeF(player.getX()); // x
        writeF(player.getY()); // y
        writeF(player.getZ()); // z
        writeC(player.getHeading()); // heading
        writeD(0);
        writeD(0); // TODO => can take some value but dunno what this info is atm
        // 1 - Azphels Curse, too much low level player killed //TODO
        // 2 - Victorys Pledge, boost attack 15, magic boost by 95, max hp by 440, healing boost by 30 //DONE
        // 3 - Victorys Pledge, boost attack 20, magic boost by 105, max hp by 520, healing boost by 30 //DONE
        // 4 - Victorys Pledge, boost attack 20, magic boost by 115, max hp by 600, healing boost by 30 //DONE
        // 5 - Victorys Pledge, boost attack 25, magic boost by 125, max hp by 680, healing boost by 50 //DONE
        // 6 - Victorys Pledge, boost attack 25, magic boost by 125, max hp by 680, healing boost by 50 //DONE
        // 7 - Boost Moral, boost pvp physical and magical defense by 90%, movement speed by 50%, magical resist by 9999, all altered state resist by 1000 //TODO
        // 8 - Boost Moral, boost pvp physical and magical defense by 90%, movement speed by 50%, magical resist by 9999, all altered state resist by 1000 //TODO
        // 9 - I never lose, boost pvp physical/magical attack/defense by 10% //TODO
        if (player.isUseAutoGroup()) {
            switch (player.getWorldId()) {
                case 300030000:
                    writeD(3);
                    break;
                case 320100000:
                    writeD(4);
                    break;
                case 300220000:
                case 300600000:
                case 300260000:
                case 300270000:
                case 300380000:
                case 300520000:
                    writeD(5);
                    break;
                case 300100000:
                case 300040000:
                case 300160000:
                case 300150000:
                case 300300000:
                    writeD(6);
                    break;
            }
        } else {
            writeD(0);
        }
        if (World.getInstance().getWorldMap(player.getWorldId()).getTemplate().getBeginnerTwinCount() > 0) {
            writeC(1); // Novice Server Enabled
        } else {
            writeC(0); // Novice Server Disabled
        }
        writeD(1); // 4.0
        CardinalManager.playerSpawn(player);
    }
}
