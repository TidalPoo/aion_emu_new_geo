/*
 * This file is part of aion-engine <aion-engine.com>
 *
 * aion-engine is private software: you can redistribute it and or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Private Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with aion-engine.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.skillengine.model;

/**
 * @author MrPoke
 * @modified romanz
 */
public enum SkillMoveType {

    RESIST(0),
    DEFAULT(16),
    //PULL(18),
    STUMBLE(20),
    KNOCKBACK(28),
    CLOSEAERIAL(48),
    PULL(50),
    STAGGER(112),
    MOVEBEHIND(48),
    UNK(54);

    private int id;

    private SkillMoveType(int id) {
        this.id = id;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
}
