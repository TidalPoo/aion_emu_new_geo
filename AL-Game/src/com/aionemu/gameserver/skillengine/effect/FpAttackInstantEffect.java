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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.model.Effect;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Sippolo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FpAttackInstantEffect")
public class FpAttackInstantEffect extends EffectTemplate {

    @XmlAttribute
    protected boolean percent;

    @Override
    public void calculate(Effect effect) {
        // Only players have FP
        if (effect.getEffected() instanceof Player) {
            super.calculate(effect, null, null);
        }
    }

    @Override
    public void applyEffect(Effect effect) {
        // Restriction to players because lack of FP on other Creatures
        if (!(effect.getEffected() instanceof Player)) {
            return;
        }
        Player player = (Player) effect.getEffected();
        int maxFP = player.getLifeStats().getMaxFp();
        int newValue = value;
        // Support for values in percentage
        if (percent) {
            newValue = ((maxFP * value) / 100);
        }
        player.getLifeStats().reduceFp(newValue);
    }
}
