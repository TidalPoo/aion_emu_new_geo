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
import com.aionemu.gameserver.model.stats.container.NpcLifeStats;
import com.aionemu.gameserver.model.stats.container.TrapGameStats;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import org.apache.commons.lang.StringUtils;

/**
 * @author ATracer
 */
public class Trap extends SummonedObject<Creature> {

    /**
     * @param objId
     * @param controller
     * @param spawnTemplate
     * @param objectTemplate
     */
    public Trap(int objId, NpcController controller, SpawnTemplate spawnTemplate, NpcTemplate objectTemplate) {
        super(objId, controller, spawnTemplate, objectTemplate, objectTemplate.getLevel());
    }

    @Override
    protected void setupStatContainers(byte level) {
        setGameStats(new TrapGameStats(this));
        setLifeStats(new NpcLifeStats(this));
    }

    @Override
    public byte getLevel() {
        return (getCreator() == null ? 1 : getCreator().getLevel());
    }

    /**
     * @return NpcObjectType.TRAP
     */
    @Override
    public NpcObjectType getNpcObjectType() {
        return NpcObjectType.TRAP;
    }

    @Override
    public String getMasterName() {
        return StringUtils.EMPTY;
    }
}
