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

import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatShieldMasteryFunction;
import com.aionemu.gameserver.skillengine.model.Effect;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author VladimirZ
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShieldMasteryEffect")
public class ShieldMasteryEffect extends BufEffect {

    @Override
    public void startEffect(Effect effect) {

        List<IStatFunction> modifiers = getModifiers(effect);
        List<IStatFunction> masteryModifiers = new ArrayList<>(modifiers.size());
        for (IStatFunction modifier : modifiers) {
            masteryModifiers.add(new StatShieldMasteryFunction(modifier.getName(), modifier.getValue(), modifier.isBonus()));
        }
        if (masteryModifiers.size() > 0) {
            effect.getEffected().getGameStats().addEffect(effect, masteryModifiers);
        }
    }
}
