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
package com.aionemu.gameserver.skillengine.effect;

import com.aionemu.gameserver.controllers.observer.AttackShieldObserver;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.HealType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author kecimis
 */
public class ConvertHealEffect extends ShieldEffect {

    @XmlAttribute
    protected HealType type;
    @XmlAttribute(name = "hitpercent")
    protected boolean hitPercent;

    @Override
    public void startEffect(final Effect effect) {
        int skillLvl = effect.getSkillLevel();
        int valueWithDelta = value + delta * skillLvl;
        int hitValueWithDelta = hitvalue + hitdelta * skillLvl;

        AttackShieldObserver asObserver = new AttackShieldObserver(hitValueWithDelta, valueWithDelta, percent, hitPercent,
                effect, hitType, this.getType(), this.hitTypeProb, 0, 0, type, 0);

        effect.getEffected().getObserveController().addAttackCalcObserver(asObserver);
        effect.setAttackShieldObserver(asObserver, position);
        effect.getEffected().getEffectController().setUnderShield(true);
    }

    /**
     * shieldType 0: convertHeal 1: reflector 2: normal shield 8: protect
     *
     * @return
     */
    @Override
    public int getType() {
        return 0;
    }

}
