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
package com.aionemu.gameserver.model.templates.stats;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author IlBuono
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "petstats")
public class PetStatsTemplate {

    @XmlAttribute(name = "reaction")
    private String reaction;
    @XmlAttribute(name = "run_speed")
    private float runSpeed;
    @XmlAttribute(name = "walk_speed")
    private float walkSpeed;
    @XmlAttribute(name = "height")
    private float height;
    @XmlAttribute(name = "altitude")
    private float altitude;

    public String getReaction() {
        return reaction;
    }

    public float getRunSpeed() {
        return runSpeed;
    }

    public float getWalkSpeed() {
        return walkSpeed;
    }

    public float getHeight() {
        return height;
    }

    public float getAltitude() {
        return altitude;
    }
}
