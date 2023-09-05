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
 * @author ATracer
 * @modified Alex
 */
@XmlType(name = "quality")
@XmlEnum
public enum ItemQuality {

    JUNK(0, "Хлам"), // Junk - Gray
    COMMON(1, "Обычное"), // Common - White
    RARE(2, "Редкое"), // Superior - Green
    LEGEND(3, "Легендарное"), // Heroic - Blue
    UNIQUE(4, "Уникальное"), // Fabled - Yellow
    EPIC(5, "Героическое"), // Eternal - Orange
    MYTHIC(6, "Мифическое");	// Test - Purple

    private int qualityId;
    private String rusname;

    /**
     * Constructors
     */
    private ItemQuality(int qualityId, String rusname) {
        this.qualityId = qualityId;
        this.rusname = rusname;
    }

    /**
     * Accessors
     *
     * @return
     */
    public int getQualityId() {
        return qualityId;
    }

    public String getRusname() {
        return rusname;
    }

    public static ItemQuality getTypeIfId(int type) {
        for (ItemQuality iq : ItemQuality.values()) {
            if (iq.getQualityId() == type) {
                return iq;
            }
        }
        return null;
    }
}
