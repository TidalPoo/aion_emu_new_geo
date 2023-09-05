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

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author ATracer, nrg
 */
public class SM_SKILL_COOLDOWN extends AionServerPacket {

    private final Map<Integer, Long> cooldowns;
    private final boolean gmpanel;

    public SM_SKILL_COOLDOWN(Map<Integer, Long> cooldowns, boolean gmpanel) {
        this.cooldowns = cooldowns;
        this.gmpanel = gmpanel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con) {
        writeH(calculateSize());
        writeC(1); // unk 0 or 1
        long currentTime = System.currentTimeMillis();
        for (Map.Entry<Integer, Long> entry : cooldowns.entrySet()) {
            int left = (int) ((entry.getValue() - currentTime) / 1000);
            ArrayList<Integer> skillsWithCooldown = DataManager.SKILL_DATA.getSkillsForCooldownId(entry.getKey());
            for (Integer skillsWithCooldown1 : skillsWithCooldown) {
                int skillId = skillsWithCooldown1;
                writeH(skillId);
                writeD(left > 0 ? left : 0);
                writeD(DataManager.SKILL_DATA.getSkillTemplate(skillId).getCooldown());
            }
        }
    }

    private int calculateSize() {
        int size = 0;
        for (Map.Entry<Integer, Long> entry : cooldowns.entrySet()) {
            size += DataManager.SKILL_DATA.getSkillsForCooldownId(entry.getKey()).size();
        }
        return size;
    }

}
