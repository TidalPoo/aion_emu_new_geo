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
package com.aionemu.gameserver.model.gameobjects;

import com.aionemu.gameserver.controllers.NpcController;
import com.aionemu.gameserver.model.stats.container.HomingGameStats;
import com.aionemu.gameserver.model.stats.container.NpcLifeStats;
import com.aionemu.gameserver.model.templates.item.ItemAttackType;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import org.apache.commons.lang.StringUtils;

/**
 * @author ATracer
 */
public class Homing extends SummonedObject<Creature> {

    /**
     * Number of performed attacks
     */
    private int attackCount;

    private int skillId;

    /**
     * Skill id of this homing. 0 - usually attack, other - skills.
     */
    private int activeSkillId;

    /**
     * @param objId
     * @param controller
     * @param spawnTemplate
     * @param objectTemplate
     * @param level
     * @param skillId
     */
    public Homing(int objId, NpcController controller, SpawnTemplate spawnTemplate, NpcTemplate objectTemplate, byte level, int skillId) {
        super(objId, controller, spawnTemplate, objectTemplate, level);
        this.skillId = skillId;
    }

    @Override
    protected void setupStatContainers(byte level) {
        setGameStats(new HomingGameStats(this));
        setLifeStats(new NpcLifeStats(this));
    }

    /**
     * @param attackCount the attackCount to set
     */
    public void setAttackCount(int attackCount) {
        this.attackCount = attackCount;
    }

    /**
     * @return the attackCount
     */
    public int getAttackCount() {
        return attackCount;
    }

    /**
     * @return NpcObjectType.HOMING
     */
    @Override
    public NpcObjectType getNpcObjectType() {
        return NpcObjectType.HOMING;
    }

    @Override
    public String getMasterName() {
        return StringUtils.EMPTY;
    }

    @Override
    public ItemAttackType getAttackType() {
        if (getName().contains("fire")) {
            return ItemAttackType.MAGICAL_FIRE;
        } else if (getName().contains("stone")) {
            return ItemAttackType.MAGICAL_EARTH;
        } else if (getName().contains("water")) {
            return ItemAttackType.MAGICAL_WATER;
        } else if ((getName().contains("wind")) || (getName().contains("cyclone"))) {
            return ItemAttackType.MAGICAL_WIND;
        }
        return ItemAttackType.PHYSICAL;
    }

    public int getSkillId() {
        return skillId;
    }

    public int getActiveSkillId() {
        return activeSkillId;
    }

    public void setActiveSkillId(int activeSkillId) {
        this.activeSkillId = activeSkillId;
    }

}
