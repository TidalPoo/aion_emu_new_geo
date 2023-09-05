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
package com.aionemu.gameserver.model.templates.cron.actions.spawn;

import com.aionemu.gameserver.model.templates.cron.actions.CronActionType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javolution.util.FastList;

/**
 *
 * @author Steve
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CronSpawnModel")
public class CronSpawnModel extends CronActionType {

    @XmlAttribute(name = "npc_id")
    protected int npcId;
    @XmlAttribute(name = "map_id")
    protected int mapId;
    @XmlElement(name = "spot")
    protected FastList<CronSpawnPosition> spot;

    public int getNpcId() {
        return npcId;
    }

    public int getMapId() {
        return mapId;
    }

    public FastList<CronSpawnPosition> getPosition() {
        if (spot == null) {
            spot = FastList.newInstance();
        }
        return spot;
    }
}
