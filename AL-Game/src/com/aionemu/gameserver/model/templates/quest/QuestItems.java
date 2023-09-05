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
package com.aionemu.gameserver.model.templates.quest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author MrPoke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestItems")
public class QuestItems {

    @XmlAttribute(name = "item_id")
    protected Integer itemId;
    @XmlAttribute
    protected long count = 1L;
    private int questId;

    /**
     * Constructor used by unmarshaller
     */
    public QuestItems() {
        this.count = 1L;
    }

    public QuestItems(int itemId, long count, int questId) {
        this.itemId = itemId;
        this.count = count;
        this.questId = questId;
    }

    /**
     * Gets the value of the itemId property.
     *
     * @return possible object is {@link Integer }
     */
    public Integer getItemId() {
        return itemId;
    }

    /**
     * Gets the value of the count property.
     *
     * @return possible object is {@link Integer }
     */
    public long getCount() {
        return count;
    }

    public int getQuestId() {
        return questId;
    }
}
