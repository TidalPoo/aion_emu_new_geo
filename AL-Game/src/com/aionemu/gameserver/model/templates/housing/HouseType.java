/*
 * This file is part of aion-lightning <aion-lightning.com>.
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
package com.aionemu.gameserver.model.templates.housing;

/**
 * @author Rolandas
 */
public enum HouseType {

    ESTATE(0, 3, "a", "Элитный дом"),
    MANSION(1, 2, "b", "Хороший дом"),
    HOUSE(2, 1, "c", "Обычный дом"),
    STUDIO(3, 0, "d", "Квартира"),
    PALACE(4, 4, "s", "Резиденция");
    private String rusname;

    private HouseType(int index, int id, String abbrev, String rusname) {
        this.abbrev = abbrev;
        this.limitTypeIndex = index;
        this.id = id;
        this.rusname = rusname;
    }

    private String abbrev;
    private int limitTypeIndex;
    private int id;

    public String getRusname() {
        return rusname;
    }

    public String getAbbreviation() {
        return abbrev;
    }

    public int getLimitTypeIndex() {
        return limitTypeIndex;
    }

    public int getId() {
        return id;
    }

    public String value() {
        return name();
    }

    public static HouseType fromValue(String value) {
        return valueOf(value);
    }
}
