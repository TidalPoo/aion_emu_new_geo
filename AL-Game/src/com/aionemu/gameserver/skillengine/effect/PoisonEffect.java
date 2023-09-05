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

import com.aionemu.gameserver.controllers.attack.AttackUtil;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.LOG;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.PacketSendUtility;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 * @author kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PoisonEffect")
public class PoisonEffect extends AbstractOverTimeEffect {

    @Override
    public void calculate(Effect effect) {
        super.calculate(effect, StatEnum.POISON_RESISTANCE, null);
    }

    @Override
    public void startEffect(Effect effect) {
        int valueWithDelta = value + delta * effect.getSkillLevel();
        int critAddDmg = this.critAddDmg2 + this.critAddDmg1 * effect.getSkillLevel();
        int finalDamage = AttackUtil.calculateMagicalOverTimeSkillResult(effect, valueWithDelta, element, this.position, false, this.critProbMod2, critAddDmg);
        effect.setReservedInt(position, finalDamage);
        if (effect.getEffector() instanceof Player) {
            PacketSendUtility.sendPacket((Player) effect.getEffector(), new SM_SYSTEM_MESSAGE(1301062, new DescriptionId(effect.getSkillTemplate().getNameId())));
        }
        super.startEffect(effect, AbnormalState.POISON);
    }

    @Override
    public void endEffect(Effect effect) {
        super.endEffect(effect, AbnormalState.POISON);
    }

    @Override
    public void onPeriodicAction(Effect effect) {
        Creature effected = effect.getEffected();
        Creature effector = effect.getEffector();
        effected.getController().onAttack(effector, effect.getSkillId(), TYPE.DAMAGE, effect.getReservedInt(position), false, LOG.POISON);
        effected.getObserveController().notifyDotAttackedObservers(effector, effect);
    }
}
