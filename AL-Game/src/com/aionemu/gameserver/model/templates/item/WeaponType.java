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
package com.aionemu.gameserver.model.templates.item;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer, Alex
 */
@XmlType(name = "weapon_type")
@XmlEnum
public enum WeaponType {

    DAGGER_1H(new int[]{30, 9}, 1, "Кинжал"),
    MACE_1H(new int[]{3, 10}, 1, "Булава"),
    SWORD_1H(new int[]{1, 8}, 1, "Меч"),
    TOOLHOE_1H(new int[]{}, 1, "TOOLHOE_1H"),
    GUN_1H(new int[]{83, 76}, 1, "Пистолет"),
    BOOK_2H(new int[]{64}, 2, "Гримуар"),
    ORB_2H(new int[]{64}, 2, "Орб"),
    POLEARM_2H(new int[]{16}, 2, "Копье"),
    STAFF_2H(new int[]{53}, 2, "Посох"),
    SWORD_2H(new int[]{15}, 2, "Двуручный меч"),
    TOOLPICK_2H(new int[]{}, 2, "TOOLPICK_2H"),
    TOOLROD_2H(new int[]{}, 2, ""),
    BOW(new int[]{17}, 2, "Лук"),
    CANNON_2H(new int[]{77}, 2, "Пушка"),
    HARP_2H(new int[]{92, 78}, 2, "Арфа"),
    GUN_2H(new int[]{}, 2, "Пистолет2"),
    KEYBLADE_2H(new int[]{}, 2, "KEYBLADE_2H"),
    KEYHAMMER_2H(new int[]{}, 2, "KEYHAMMER_2H");

    private final int[] requiredSkill;
    private final int slots;
    private final String rusname;

    private WeaponType(int[] requiredSkills, int slots, String rusname) {
        this.requiredSkill = requiredSkills;
        this.slots = slots;
        this.rusname = rusname;
    }

    public int[] getRequiredSkills() {
        return requiredSkill;
    }

    public int getRequiredSlots() {
        return slots;
    }

    /**
     * @return int
     */
    public int getMask() {
        return 1 << this.ordinal();
    }

    public String getRusname() {
        return rusname;
    }
}
