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
package com.aionemu.gameserver.services.instance;

import com.aionemu.gameserver.configs.main.AutoGroupConfig;
import com.aionemu.gameserver.model.autogroup.AutoGroupType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 *
 * @author xTz
 */
public class PvPArenaService {

    public static boolean isPvPArenaAvailable(Player player, AutoGroupType agt) {
        if (AutoGroupConfig.START_TIME_ENABLE && !checkTime(agt)) {
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401306, agt.getInstanceMapId()));
            return false;
        }
        if (!checkItem(player, agt)) {
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400219, agt.getInstanceMapId()));
            return false;
        }
        // todo check cool down
        return true;
    }

    public static boolean checkItem(Player player, AutoGroupType agt) {
        Storage inventory = player.getInventory();
        if (agt.isPvPFFAArena() || agt.isPvPSoloArena()) {
            return inventory.getItemCountByItemId(186000135) > 0;
        } else if (agt.isHarmonyArena()) {
            return inventory.getItemCountByItemId(186000184) > 0;
        } else if (agt.isGloryArena()) {
            return inventory.getItemCountByItemId(186000185) >= 3;
        }
        return true;
    }

    private static boolean checkTime(AutoGroupType agt) {
        if (agt.isPvPFFAArena() || agt.isPvPSoloArena()) {
            return isPvPArenaAvailable();
        } else if (agt.isHarmonyArena()) {
            return isHarmonyArenaAvailable();
        } else if (agt.isGloryArena()) {
            return isGloryArenaAvailable();
        }
        return true;
    }

    private static boolean isPvPArenaAvailable() {
        return true;
    }

    private static boolean isHarmonyArenaAvailable() {
        return true;
    }

    private static boolean isGloryArenaAvailable() {
        return true;
    }

}
