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
package com.aionemu.gameserver.model.templates.zone;

import com.aionemu.gameserver.model.geometry.Area;

/**
 * @author MrPoke
 *
 */
public class ZoneInfo {

    private Area area;
    private ZoneTemplate zoneTemplate;

    /**
     * @param area
     * @param zoneTemplate
     */
    public ZoneInfo(Area area, ZoneTemplate zoneTemplate) {
        this.area = area;
        this.zoneTemplate = zoneTemplate;
    }

    /**
     * @return the area
     */
    public Area getArea() {
        return area;
    }

    /**
     * @return the zoneTemplate
     */
    public ZoneTemplate getZoneTemplate() {
        return zoneTemplate;
    }
}
