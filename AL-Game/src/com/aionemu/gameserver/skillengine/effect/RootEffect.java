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

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.controllers.observer.ActionObserver;
import com.aionemu.gameserver.controllers.observer.ObserverType;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.PacketSendUtility;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RootEffect")
public class RootEffect extends EffectTemplate {

    @XmlAttribute
    protected int resistchance = 100;

    @Override
    public void applyEffect(Effect effect) {
        effect.addToEffectedController();
        if (effect.getEffector() instanceof Player) {
            PacketSendUtility.sendPacket((Player) effect.getEffector(), new SM_SYSTEM_MESSAGE(1301062, new DescriptionId(effect.getSkillTemplate().getNameId())));
        }
    }

    @Override
    public void calculate(Effect effect) {
        super.calculate(effect, StatEnum.ROOT_RESISTANCE, null);
    }

    @Override
    public void startEffect(final Effect effect) {
        final Creature effected = effect.getEffected();
        //effected.getMoveController().abortMove();
        effected.getEffectController().setAbnormal(AbnormalState.ROOT.getId());
        effect.setAbnormal(AbnormalState.ROOT.getId());
        //PacketSendUtility.broadcastPacketAndReceive(effected, new SM_TARGET_IMMOBILIZE(effected));

        ActionObserver observer = new ActionObserver(ObserverType.ATTACKED) {

            @Override
            public void attacked(Creature creature) {
                if (Rnd.get(0, 100) > resistchance) {
                    effected.getEffectController().removeEffect(effect.getSkillId());
                }
            }
        };
        effected.getObserveController().addObserver(observer);
        effect.setActionObserver(observer, position);

    }

    @Override
    public void endEffect(Effect effect) {
        effect.getEffected().getEffectController().unsetAbnormal(AbnormalState.ROOT.getId());
        ActionObserver observer = effect.getActionObserver(position);
        if (observer != null) {
            effect.getEffected().getObserveController().removeObserver(observer);
        }
    }
}
