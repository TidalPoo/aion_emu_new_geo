/*
 * This file is part of aion-unique <aion-unique.com>.
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

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatAddFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatRateFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatSetFunction;
import com.aionemu.gameserver.model.stats.container.CreatureGameStats;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.aionemu.gameserver.skillengine.change.Change;
import com.aionemu.gameserver.skillengine.condition.Conditions;
import com.aionemu.gameserver.skillengine.model.Effect;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BufEffect")
public abstract class BufEffect extends EffectTemplate {

    @XmlAttribute
    protected boolean maxstat;

    private static final Logger loger = LoggerFactory.getLogger(BufEffect.class);

    @Override
    public void applyEffect(Effect effect) {
        effect.addToEffectedController();
    }

    /**
     * Will be called from effect controller when effect ends
     */
    @Override
    public void endEffect(Effect effect) {
        Creature effected = effect.getEffected();
        effected.getGameStats().endEffect(effect);
    }

    /**
     * Will be called from effect controller when effect starts
     */
    @Override
    public void startEffect(Effect effect) {
        if (change == null) {
            return;
        }

        Creature effected = effect.getEffected();
        CreatureGameStats<? extends Creature> cgs = effected.getGameStats();

        List<IStatFunction> modifiers2 = getModifiers(effect);

        if (modifiers2.size() > 0) {
            cgs.addEffect(effect, modifiers2);
        }

        if (maxstat) {
            effected.getLifeStats().increaseHp(TYPE.HP, effected.getGameStats().getMaxHp().getCurrent());
            effected.getLifeStats().increaseMp(TYPE.HEAL_MP, effected.getGameStats().getMaxMp().getCurrent());
        }
    }

    /**
     * @param effect
     * @return
     */
    protected List<IStatFunction> getModifiers(Effect effect) {
        int skillId = effect.getSkillId();
        int skillLvl = effect.getSkillLevel();

        List<IStatFunction> mods = new ArrayList<>();

        for (Change changeItem : change) {
            if (changeItem.getStat() == null) {
                loger.warn("Skill stat has wrong name for skillid: " + skillId);
                continue;
            }

            int valueWithDelta = changeItem.getValue() + changeItem.getDelta() * skillLvl;

            Conditions conditions = changeItem.getConditions();
            switch (changeItem.getFunc()) {
                case ADD:
                    mods.add(new StatAddFunction(changeItem.getStat(), valueWithDelta, true).withConditions(conditions));
                    break;
                case PERCENT:
                    mods.add(new StatRateFunction(changeItem.getStat(), valueWithDelta, true).withConditions(conditions));
                    break;
                case REPLACE:
                    mods.add(new StatSetFunction(changeItem.getStat(), valueWithDelta).withConditions(conditions));
                    break;
            }
        }
        return mods;
    }

    @Override
    public void onPeriodicAction(Effect effect) {
        // TODO Auto-generated method stub
    }
}
