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

import com.aionemu.gameserver.geoEngine.collision.CollisionIntention;
import com.aionemu.gameserver.geoEngine.math.Vector3f;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FORCED_MOVE;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.SkillMoveType;
import com.aionemu.gameserver.skillengine.model.SpellStatus;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.geo.GeoService;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author VladimirZ, @modified Cheatkiller
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SimpleRootEffect")
public class SimpleRootEffect extends EffectTemplate {

    @Override
    public void applyEffect(Effect effect) {
        effect.addToEffectedController();
    }

    @Override
    public void calculate(Effect effect) {
        super.calculate(effect, StatEnum.STAGGER_RESISTANCE, null);
    }

    @Override
    public void startEffect(final Effect effect) {
        final Creature effected = effect.getEffected();
        if (effected.getEffectController().isAbnormalPresentBySkillId(8224)) {
            return;
        }
        if (effected.getCastingSkill() != null) {
            effected.getController().cancelCurrentSkill();
        }
        byte heading = effect.getEffector().getHeading();
        effect.setSpellStatus(SpellStatus.NONE);
        effect.setSkillMoveType(SkillMoveType.KNOCKBACK);
        // effect.getEffected().getController().cancelCurrentSkill(); //TODO: Not sure about this
        effect.getEffected().getEffectController().setAbnormal(AbnormalState.KNOCKBACK.getId());
        effect.setAbnormal(AbnormalState.KNOCKBACK.getId());
        double radian = Math.toRadians(MathUtil.convertHeadingToDegree(heading));
        float x1 = (float) (Math.cos(radian) * 0.7f);
        float y1 = (float) (Math.sin(radian) * 0.7f);
        float z = effected.getZ();
        byte intentions = (byte) (CollisionIntention.PHYSICAL.getId() | CollisionIntention.DOOR.getId());
        Vector3f closestCollision = GeoService.getInstance().getClosestCollision(effected, effected.getX() + x1,
                effected.getY() + y1, effected.getZ() - 0.4f, false, intentions);
        x1 = closestCollision.x;
        y1 = closestCollision.y;
        z = closestCollision.z;
        World.getInstance().updatePosition(effected, x1, y1, z, heading, false);
        PacketSendUtility.broadcastPacketAndReceive(effected, new SM_FORCED_MOVE(effect.getEffector(), effected.getObjectId(), x1, y1, z));
    }

    @Override
    public void endEffect(Effect effect) {
        effect.getEffected().getEffectController().unsetAbnormal(AbnormalState.KNOCKBACK.getId());
    }
}