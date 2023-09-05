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

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author kecimis
 */
public class DelayedSkillEffect extends EffectTemplate {

    @XmlAttribute(name = "skill_id")
    protected int skillId;

    @Override
    public void applyEffect(Effect effect) {
        effect.addToEffectedController();
    }

    @Override
    public void endEffect(Effect effect) {
        // TODO figure out what value does
        //int valueWithDelta = value + delta * effect.getSkillLevel();

        /*(effect.getDispelCategory() == DispelCategoryType.ALL
         || effect.getDispelCategory() == DispelCategoryType.DEBUFF_PHYSICAL) &&*/
        if (effect.isRemove()) {
            return;
        }
        // apply effect
        SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(skillId);
        Effect e = new Effect(effect.getEffector(), effect.getEffected(), template, template.getLvl(), 0);
        e.initialize();
        e.applyEffect();
    }
}
