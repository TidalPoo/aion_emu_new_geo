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
package com.aionemu.gameserver.model.summons;

import com.aionemu.gameserver.model.gameobjects.Creature;

/**
 * @author Rolandas
 */
public class SkillOrder {

    private final int skillId;
    private final int skillLvl;
    private final Creature target;
    private final boolean release;

    public SkillOrder(int skillId, int skillLvl, boolean release, Creature target) {
        this.skillId = skillId;
        this.target = target;
        this.release = release;
        this.skillLvl = skillLvl;
    }

    public int getSkillId() {
        return skillId;
    }

    public Creature getTarget() {
        return target;
    }

    public boolean isRelease() {
        return release;
    }

    public int getSkillLevel() {
        return skillLvl;
    }
}
