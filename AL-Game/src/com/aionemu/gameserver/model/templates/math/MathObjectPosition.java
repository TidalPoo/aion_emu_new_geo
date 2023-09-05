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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Steve
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MathObjectPosition")
public class MathObjectPosition {

    @XmlAttribute(name = "x")
    protected float x;
    @XmlAttribute(name = "y")
    protected float y;
    @XmlAttribute(name = "z")
    protected float z;
    @XmlAttribute(name = "h")
    protected byte h;
    @XmlAttribute(name = "rw")
    protected int rw;
    @XmlAttribute(name = "static_id")
    protected int staticId;
    @XmlAttribute(name = "npc_id")
    protected int npcId;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public byte getH() {
        return h;
    }

    public int getRandomWalk() {
        return rw;
    }

    public int getStaticId() {
        return staticId;
    }
}
