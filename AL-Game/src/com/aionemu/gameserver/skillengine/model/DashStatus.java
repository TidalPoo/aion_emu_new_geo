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
 * @author weiwei
 * @modified VladimirZ
 */
public enum DashStatus {

    NONE(0),
    RANDOMMOVELOC(1),
    DASH(2),
    BACKDASH(3),
    MOVEBEHIND(4);

    private int id;

    private DashStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
