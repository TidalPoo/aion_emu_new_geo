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

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.controllers.observer.ActionObserver;
import com.aionemu.gameserver.controllers.observer.ObserverType;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.ProvokeTarget;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer modified by kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProvokerEffect")
public class ProvokerEffect extends ShieldEffect {

    @XmlAttribute(name = "provoke_target")
    protected ProvokeTarget provokeTarget;
    @XmlAttribute(name = "skill_id")
    protected int skillId;

    @Override
    public void applyEffect(Effect effect) {
        effect.addToEffectedController();
        if (effect.getEffector() instanceof Player) {
            PacketSendUtility.sendPacket((Player) effect.getEffector(), new SM_SYSTEM_MESSAGE(1301062, new DescriptionId(effect.getSkillTemplate().getNameId())));
        }
    }

    @Override
    public void startEffect(Effect effect) {
        ActionObserver observer = null;
        final Creature effector = effect.getEffector();
        final int prob2 = this.hitTypeProb;
        final int radius = this.radius;
        switch (this.hitType) {
            case NMLATK://ATTACK
                observer = new ActionObserver(ObserverType.ATTACK) {

                    @Override
                    public void attack(Creature creature) {
                        if (Rnd.get(0, 100) <= prob2) {
                            Creature target = getProvokeTarget(provokeTarget, effector, creature);
                            createProvokedEffect(effector, target);
                        }
                    }
                };
                break;
            case EVERYHIT://ATTACKED
                observer = new ActionObserver(ObserverType.ATTACKED) {

                    @Override
                    public void attacked(Creature creature) {
                        if (radius > 0) {
                            if (!MathUtil.isIn3dRange(effector, creature, radius)) {
                                return;
                            }
                        }
                        if (Rnd.get(0, 100) <= prob2) {
                            Creature target = getProvokeTarget(provokeTarget, effector, creature);
                            createProvokedEffect(effector, target);
                        }
                    }
                };
                break;
            //TODO MAHIT and PHHIT
        }

        if (observer == null) {
            return;
        }

        effect.setActionObserver(observer, position);
        effect.getEffected().getObserveController().addObserver(observer);
    }

    /**
     * @param effector
     * @param target
     */
    private void createProvokedEffect(final Creature effector, Creature target) {
        SkillEngine.getInstance().applyEffectDirectly(skillId, effector, target, 0);
    }

    /**
     * @param provokeTarget
     * @param effector
     * @param target
     * @return
     */
    private Creature getProvokeTarget(ProvokeTarget provokeTarget, Creature effector, Creature target) {
        switch (provokeTarget) {
            case ME:
                return effector;
            case OPPONENT:
                return target;
        }
        throw new IllegalArgumentException("Provoker target is invalid " + provokeTarget);
    }

    @Override
    public void endEffect(Effect effect) {
        ActionObserver observer = effect.getActionObserver(position);
        if (observer != null) {
            effect.getEffected().getObserveController().removeObserver(observer);
        }
    }
}
