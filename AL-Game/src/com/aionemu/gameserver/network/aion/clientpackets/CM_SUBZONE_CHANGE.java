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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.configs.main.WorldConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.zone.ZoneClassName;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import java.util.List;

/**
 * @author Rolandas
 */
public class CM_SUBZONE_CHANGE extends AionClientPacket {

    private int unk;

    public CM_SUBZONE_CHANGE(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    @Override
    protected void readImpl() {
        // Always 1, maybe for neutral zones 0 ?
        unk = readC();
    }

    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        player.revalidateZones();
        if (player.isDeveloper() || player.isGM()) {
            PacketSendUtility.sendMessage(player, "Zone intType " + unk);
        }
        if (player.getAccessLevel() >= 3 && WorldConfig.ENABLE_SHOW_ZONEENTER) {
            List<ZoneInstance> zones = player.getPosition().getMapRegion().getZones(player);
            int foundZones = 0;
            for (ZoneInstance zone : zones) {
                if (zone.getZoneTemplate().getZoneType() == ZoneClassName.DUMMY
                        || zone.getZoneTemplate().getZoneType() == ZoneClassName.WEATHER) {
                    continue;
                }
                foundZones++;
                PacketSendUtility.sendMessage(player, "Passed zone: unk=" + unk + "; " + zone.getZoneTemplate().getZoneType()
                        + " " + zone.getAreaTemplate().getZoneName().name());
            }
            if (foundZones == 0) {
                PacketSendUtility.sendMessage(player, "Passed unknown zone, unk=" + unk);
            }
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
