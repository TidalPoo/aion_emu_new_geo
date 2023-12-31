/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.chatserver.model;

/**
 * @author ATracer
 */
public enum PlayerClass {

    WARRIOR(0),
    GLADIATOR(1), // fighter
    TEMPLAR(2), // knight
    SCOUT(3),
    ASSASSIN(4),
    RANGER(5),
    MAGE(6),
    SORCERER(7), // wizard
    SPIRIT_MASTER(8), // elementalist
    PRIEST(9),
    CLERIC(10),
    CHANTER(11),
    ENGINEER(12),
    RIDER(13),
    GUNNER(14),
    ARTIST(15),
    BARD(16),
    ALL(17);

    private byte classId;

    /**
     * @param classId
     */
    private PlayerClass(int classId) {
        this.classId = (byte) classId;
    }

    /**
     * @return classId
     */
    public byte getClassId() {
        return classId;
    }
}
