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
package com.aionemu.gameserver.dataholders;

import com.aionemu.gameserver.model.templates.housing.Building;
import com.aionemu.gameserver.model.templates.housing.HousePart;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"houseParts"})
@XmlRootElement(name = "house_parts")
public class HousePartsData {

    @XmlElement(name = "house_part")
    protected List<HousePart> houseParts;

    @XmlTransient
    Map<String, List<HousePart>> partsByTags = new HashMap<>(5);

    @XmlTransient
    Map<Integer, HousePart> partsById = new HashMap<>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        if (houseParts == null) {
            return;
        }

        for (HousePart part : houseParts) {
            partsById.put(part.getId(), part);
            Iterator<String> iterator = part.getTags().iterator();
            while (iterator.hasNext()) {
                String tag = iterator.next();
                List<HousePart> parts = partsByTags.get(tag);
                if (parts == null) {
                    parts = new ArrayList<>();
                    partsByTags.put(tag, parts);
                }
                parts.add(part);
            }
        }

        houseParts.clear();
        houseParts = null;
    }

    public HousePart getPartById(int partId) {
        return partsById.get(partId);
    }

    public List<HousePart> getPartsForBuilding(Building building) {
        return partsByTags.get(building.getPartsMatchTag());
    }

    public int size() {
        return partsById.size();
    }
}
