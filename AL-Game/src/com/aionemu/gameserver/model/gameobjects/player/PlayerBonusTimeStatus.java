/*
 * Copyright (C) 2013 Steve
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.gameobjects.player;

/**
 *
 * @author Steve
 * @modified Alex
 */
public enum PlayerBonusTimeStatus {

    NORMAL(1, "Обычный"),
    NEW(2, "Привилегии нового пользователя"),
    COMEBACK(3, "Привилегии вернувшегося"),
    BONUS(COMEBACK.id | NEW.id, "Активные привилегии");
    private int id;
    private String rusname;

    private PlayerBonusTimeStatus(int id, String rusname) {
        this.id = id;
        this.rusname = rusname;
    }

    public int getId() {
        return id;
    }

    public boolean isBonus() {
        return (BONUS.id & getId()) == getId();
    }

    public String getRusname() {
        return rusname;
    }
}
