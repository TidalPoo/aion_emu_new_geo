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
package com.aionemu.gameserver.dataholders;

import com.aionemu.gameserver.model.templates.math.MathObjectMaps;
import gnu.trove.map.hash.THashMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javolution.util.FastList;

/**
 *
 * @author Steve
 */
@XmlRootElement(name = "math_objects")
@XmlAccessorType(XmlAccessType.FIELD)
public class MathObjectData {

    @XmlElement(name = "math_map")
    protected FastList<MathObjectMaps> maps = FastList.newInstance();
    @XmlTransient
    private THashMap<Integer, MathObjectMaps> mathObjectMap = new THashMap<Integer, MathObjectMaps>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (MathObjectMaps map : maps) {
            mathObjectMap.put(map.getMapId(), map);
        }
    }

    public FastList<MathObjectMaps> getMaps() {
        return maps;
    }

    public MathObjectMaps getMap(int mapId) {
        return mathObjectMap.get(mapId);
    }

    public int size() {
        return maps.size();
    }
}
