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
 *  along with aion-lightning  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.utils.stats.enums;

/**
 * @author ATracer
 */
public enum AGILITY {

    WARRIOR(100),
    GLADIATOR(100),
    TEMPLAR(110),
    SCOUT(100),
    ASSASSIN(100),
    RANGER(100),
    MAGE(95),
    SORCERER(100),
    SPIRIT_MASTER(100),
    PRIEST(100),
    CLERIC(90),
    CHANTER(90),
    ENGINEER(100),
    RIDER(100),
    GUNNER(100),
    ARTIST(100),
    BARD(100);

    private int value;

    private AGILITY(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
