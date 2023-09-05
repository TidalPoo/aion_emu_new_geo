/*
 * This file is part of [JS]Emulator <js-emu.ru>
 *
 *  [JS]Emulator is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  [JS]Emulator is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with [JS]Emulator.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.dataholders;

import com.aionemu.gameserver.model.templates.item.DecomposableItemInfo;
import com.aionemu.gameserver.model.templates.item.DecomposableType;
import com.aionemu.gameserver.model.templates.item.ExtractedItemsCollection;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author antness
 */
@XmlRootElement(name = "decomposable_items")
@XmlAccessorType(XmlAccessType.FIELD)
public class DecomposableItemsData {

    @XmlElement(name = "decomposable")
    private List<DecomposableItemInfo> decomposableItemsTemplates;
    private TIntObjectHashMap<List<ExtractedItemsCollection>> decomposableItemsInfo = new TIntObjectHashMap<>();
    private TIntObjectHashMap<DecomposableType> decomposableItemsType = new TIntObjectHashMap<>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        decomposableItemsInfo.clear();
        for (DecomposableItemInfo template : decomposableItemsTemplates) {
            decomposableItemsInfo.put(template.getItemId(), template.getItemsCollections());
            decomposableItemsType.put(template.getItemId(), template.getType());
        }
    }

    public int size() {
        return decomposableItemsInfo.size();
    }

    public List<ExtractedItemsCollection> getInfoByItemId(int itemId) {
        return decomposableItemsInfo.get(itemId);
    }

    public DecomposableType getItemByType(int itemId) {
        return decomposableItemsType.get(itemId);
    }
}
