/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.skillengine.effect;

import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.LOG;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.PacketSendUtility;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Sippolo
 * @modified Romanz
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MpAttackInstantEffect")
public class MpAttackInstantEffect extends EffectTemplate {

    @XmlAttribute
    protected boolean percent;

    @Override
    public void applyEffect(Effect effect) {
        int maxMP = effect.getEffected().getLifeStats().getMaxMp();
        int newValue = value;
        // Support for values in percentage
        if (percent) {
            newValue = ((maxMP * value) / 100);
        }
        effect.getEffected().getLifeStats().reduceMp(newValue);
        effect.getEffected().getController().onAttack(effect.getEffector(), effect.getSkillId(), TYPE.MP, -newValue, false, LOG.SPELLATK);
        if (effect.getEffector() instanceof Player) {
            PacketSendUtility.sendPacket((Player) effect.getEffector(), new SM_SYSTEM_MESSAGE(1301062, new DescriptionId(effect.getSkillTemplate().getNameId())));
        }
    }
}
