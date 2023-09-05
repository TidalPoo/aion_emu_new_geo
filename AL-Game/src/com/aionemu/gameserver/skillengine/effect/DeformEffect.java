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

import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.skillengine.model.Effect;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeformEffect")
public class DeformEffect extends TransformEffect {

    @Override
    public void applyEffect(Effect effect) {
        super.applyEffect(effect);
        effect.getEffected().getEffectController().setAbnormal(AbnormalState.DEFORM.getId());
        effect.setAbnormal(AbnormalState.DEFORM.getId());
    }

    @Override
    public void calculate(Effect effect) {
        super.calculate(effect, StatEnum.DEFORM_RESISTANCE, null);
    }

    @Override
    public void startEffect(Effect effect) {
        super.startEffect(effect);
    }

    @Override
    public void endEffect(Effect effect) {
        super.endEffect(effect);
        effect.getEffected().getEffectController().unsetAbnormal(AbnormalState.DEFORM.getId());
    }
}
