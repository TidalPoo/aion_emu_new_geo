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

import com.aionemu.gameserver.skillengine.model.DispelCategoryType;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.SkillTargetSlot;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractDispelEffect")
public class AbstractDispelEffect extends EffectTemplate {

    @XmlAttribute
    protected int dpower;
    @XmlAttribute
    protected int power;
    @XmlAttribute(name = "dispel_level")
    protected int dispelLevel;

    @Override
    public void applyEffect(Effect effect) {
        //nothing to do, its overriden
    }

    public void applyEffect(Effect effect, DispelCategoryType type, SkillTargetSlot slot) {
        boolean isItemTriggered = (effect.getItemTemplate() != null);
        int count = value + delta * effect.getSkillLevel();
        int finalPower = power + dpower * effect.getSkillLevel();

        effect.getEffected().getEffectController()
                .removeEffectByDispelCat(type, slot, count, dispelLevel, finalPower, isItemTriggered);
    }
}
