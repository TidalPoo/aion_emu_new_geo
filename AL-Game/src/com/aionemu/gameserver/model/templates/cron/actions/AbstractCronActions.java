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
package com.aionemu.gameserver.model.templates.cron.actions;

import com.aionemu.gameserver.model.templates.cron.actions.spawn.CronSpawnModel;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;
import javolution.util.FastList;

/**
 *
 * @author Steve
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractCronActions")
public class AbstractCronActions<T extends CronActionType> {

    @XmlElements({
        @XmlElement(name = "gp_bonus", type = CronGamePointBonus.class),
        @XmlElement(name = "item_bonus", type = CronItemBonus.class),
        @XmlElement(name = "spawn_model", type = CronSpawnModel.class),
        @XmlElement(name = "event_model", type = CronEvents.class)
    })
    protected FastList<T> cronActions;

    @XmlAttribute(name = "start_time")
    protected int startTime;

    public FastList<T> getCronActions() {
        if (cronActions == null) {
            cronActions = FastList.newInstance();
        }

        return cronActions;
    }

    public int getStartTime() {
        return startTime;
    }

    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (FastList.Node<T> n = cronActions.head(), end = cronActions.tail(); (n = n.getNext()) != end;) {
            n.getValue().setStartTime(getStartTime());
        }
    }
}
