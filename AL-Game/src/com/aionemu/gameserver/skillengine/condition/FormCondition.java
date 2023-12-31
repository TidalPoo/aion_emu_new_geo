/*
 * This file is part of aion-engine <aion-engine.com>
 *
 * aion-engine is private software: you can redistribute it and or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Private Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with aion-engine.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.skillengine.condition;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.model.Skill;
import com.aionemu.gameserver.skillengine.model.TransformType;
import com.aionemu.gameserver.utils.PacketSendUtility;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FormCondition")
public class FormCondition extends Condition {

    @XmlAttribute(required = true)
    protected TransformType value;

    /* (non-Javadoc)
     * @see com.aionemu.gameserver.skillengine.condition.Condition#validate(com.aionemu.gameserver.skillengine.model.Skill)
     *
     * defines tranformtype in which player can cast given skill
     */
    @Override
    public boolean validate(Skill env) {
        if (env.getEffector() instanceof Player) {
            if (env.getEffector().getTransformModel().isActive() && env.getEffector().getTransformModel().getType() == value) {
                return true;
            } else {
                PacketSendUtility.sendPacket((Player) env.getEffector(), SM_SYSTEM_MESSAGE.STR_SKILL_CAN_NOT_CAST_IN_THIS_FORM);
                return false;
            }
        } else {
            return true;
        }
    }
}
