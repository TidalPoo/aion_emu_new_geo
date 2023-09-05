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
import com.aionemu.gameserver.model.templates.item.LeftHandSlot;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.model.Skill;
import com.aionemu.gameserver.utils.PacketSendUtility;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Cheatkiller
 * @modifier DeathMagnestic
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LeftHandCondition")
public class LeftHandCondition extends Condition {

    @XmlAttribute(name = "type")
    private LeftHandSlot type;

    @Override
    public boolean validate(Skill env) {
        if (env.getEffector() instanceof Player) {
            Player player = (Player) env.getEffector();
            switch (type) {
                case DUAL: {
                    // TODO
                    if (player.getEquipment().getOffHandWeapon() != null
                            && player.getEquipment().getOffHandWeapon().getItemTemplate().isWeapon()) {
                        return true;
                    } else {
                        if (player.getEquipment().isUsedTwoHandWeapon()) {
                            return true;
                        }
                        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_NEED_DUAL_WEAPON);
                        return false;
                    }
                }
                case SHIELD: {
                    if (player.getEquipment().isShieldEquipped()) {
                        return true;
                    } else {
                        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_NEED_SHIELD);
                        return false;
                    }
                }
            }
        }
        return false;
    }
}
