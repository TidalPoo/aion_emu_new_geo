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
package com.aionemu.gameserver.model.gameobjects.state;

/**
 * @author Sweetkr
 * @modified Alex - added INVISIBLE
 */
public enum CreatureVisualState {

    VISIBLE(0), // Normal
    HIDE1(1), // Hide I
    HIDE2(2), // Hide II
    HIDE3(3), // Hide by Artifact?
    HIDE5(5), //No idea :D
    HIDE10(10), // Hide from Npc?
    HIDE13(13), // Hide from Npc?
    HIDE20(20), // Hide from Npc?
    BLINKING(64), // Blinking when entering to zone
    INVISIBLE(128); //Invisible

    private final int id;

    private CreatureVisualState(int id) {
        this.id = id;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
}
