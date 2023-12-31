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
package com.aionemu.gameserver.skillengine.model;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;

/**
 * @author Cheatkiller
 */
public class ChargeSkill extends Skill {

    public ChargeSkill(SkillTemplate skillTemplate, Player effector, int skillLevel, Creature firstTarget, ItemTemplate itemTemplate) {
        super(skillTemplate, effector, skillLevel, firstTarget, null);
    }

    @Override
    public void calculateSkillDuration() {

    }

    @Override
    public boolean useSkill() {
        if (!canUseSkill()) {
            return false;
        }
        effector.getObserveController().notifySkilluseObservers(this);
        effector.setCasting(this);
        startCast();
        effector.getObserveController().attach(conditionChangeListener);
        endCast();
        return true;
    }
}
