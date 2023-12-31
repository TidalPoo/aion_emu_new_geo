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
package com.aionemu.gameserver.model.gameobjects.player;

public enum ReviveType {

    /**
     * Revive to bindpoint
     */
    BIND_REVIVE(0),
    /**
     * Revive from rebirth effect
     */
    REBIRTH_REVIVE(1),
    /**
     * Self-Rez Stone
     */
    ITEM_SELF_REVIVE(2),
    /**
     * Revive from skill
     */
    SKILL_REVIVE(3),
    /**
     * Revive to Kisk
     */
    KISK_REVIVE(4),
    /**
     * Revive to Instance Start point
     */
    INSTANCE_REVIVE(6),
    /**
     * Revive to Obelisk
     */
    OBELISK_REVIVE(8);

    private int typeId;

    /**
     * Constructor.
     *
     * @param typeId
     */
    private ReviveType(int typeId) {
        this.typeId = typeId;
    }

    public int getReviveTypeId() {
        return typeId;
    }

    public static ReviveType getReviveTypeById(int id) {
        for (ReviveType rt : values()) {
            if (rt.typeId == id) {
                return rt;
            }
        }
        throw new IllegalArgumentException("Unsupported revive type: " + id);
    }
}
