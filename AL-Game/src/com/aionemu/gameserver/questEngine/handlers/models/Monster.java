/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.questEngine.handlers.models;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author MrPoke
 * @reworked vlog, Bobobear
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Monster")
public class Monster {

    @XmlAttribute(name = "var", required = true)
    protected int var;

    @XmlAttribute(name = "start_var")
    protected Integer startVar;

    @XmlAttribute(name = "end_var", required = true)
    protected int endVar;
    @XmlAttribute(name = "end_var_reward")
    protected boolean rewardVar = false;
    @XmlAttribute(name = "npc_ids")
    protected List<Integer> npcIds;

    @XmlAttribute(name = "npc_seq")
    private Integer npcSequence;

    @XmlAttribute(name = "spawner_object")
    protected int spawnerObject;

    public int getVar() {
        return var;
    }

    public Integer getStartVar() {
        return startVar;
    }

    public int getEndVar() {
        return endVar;
    }

    public List<Integer> getNpcIds() {
        return npcIds;
    }

    public Integer getNpcSequence() {
        return npcSequence;
    }

    public int getSpawnerObject() {
        return spawnerObject;
    }

    public boolean getRewardVar() {
        return rewardVar;
    }
}
