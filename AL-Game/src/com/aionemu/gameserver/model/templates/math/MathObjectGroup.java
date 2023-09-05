/*
 * Copyright (C) 2013 Steve
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.templates.math;

import com.aionemu.gameserver.model.gameobjects.math.MathObjectReaction;
import com.aionemu.gameserver.model.gameobjects.math.MathObjectType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javolution.util.FastList;

/**
 *
 * @author Steve
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MathObjectGroup")
public class MathObjectGroup {

    @XmlAttribute(name = "npc_id")
    protected int npcId;
    @XmlAttribute
    protected MathObjectType type;
    @XmlAttribute
    protected MathObjectReaction reaction;
    @XmlAttribute(name = "skill_id")
    protected int skillId;
    @XmlAttribute(name = "min_radius")
    protected double minRadius;
    @XmlAttribute(name = "max_radius")
    protected double maxRadius;
    @XmlAttribute
    protected int duration;
    @XmlElement
    protected FastList<MathObjectPosition> mpos;

    public int getNpcId() {
        return npcId;
    }

    public MathObjectType getType() {
        return type;
    }

    public MathObjectReaction getReaction() {
        return reaction;
    }

    public int getSkillId() {
        return skillId;
    }

    public double getMixRadius() {
        return minRadius;
    }

    public double getMaxRadius() {
        return maxRadius;
    }

    public int getDuration() {
        return duration;
    }

    public FastList<MathObjectPosition> getPosition() {
        if (mpos == null) {
            mpos = FastList.newInstance();
        }
        return mpos;
    }
}
