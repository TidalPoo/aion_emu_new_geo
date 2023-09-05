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

import com.aionemu.gameserver.geoEngine.collision.CollisionIntention;
import com.aionemu.gameserver.geoEngine.math.Vector3f;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TARGET_UPDATE;
import com.aionemu.gameserver.skillengine.model.DashStatus;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.Skill;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.geo.GeoService;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Bio
 * @modified Alex - fix target teleport, teleport to textures, teleport if Z > 4
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RandomMoveLocEffect")
public class RandomMoveLocEffect extends EffectTemplate {

    @XmlAttribute(name = "distance")
    private float distance;
    @XmlAttribute(name = "direction")
    private float direction;

    @Override
    public void applyEffect(Effect effect) {
        final Player effector = (Player) effect.getEffector();

        // Deselect targets
        PacketSendUtility.sendPacket(effector, new SM_TARGET_UPDATE(effector));
        Skill skill = effect.getSkill();
        effector.getEffectController().setAbnormal(AbnormalState.CANNOT_MOVE.getId());
        effector.getEffectController().updatePlayerEffectIcons();
        World.getInstance().updatePosition(effector, skill.getX(), skill.getY(), skill.getZ(), skill.getH());
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                effector.getEffectController().unsetAbnormal(AbnormalState.CANNOT_MOVE.getId());
                effector.getEffectController().updatePlayerEffectIcons();
            }
        }, 200);
    }

    @Override
    public void calculate(Effect effect) {
        effect.addSucessEffect(this);
        effect.setDashStatus(DashStatus.RANDOMMOVELOC);
        final Player effector = (Player) effect.getEffector();
        //сохранение координат для телепорта если упал в текстуры от Изгиба пространства и времени.
        effector.saveZonePosition();
        Vector3f closestCollision = closestCollision(effector, direction);
        float direct = direction == 0 ? 1 : 0;
        int m = (int) (closestCollision.getZ() - effector.getZ());
        //fix 1 bag
        if (m >= 4) {
            closestCollision = closestCollision(effector, direct);
        }
        effect.getSkill().setTargetPosition(closestCollision.getX(), closestCollision.getY(), closestCollision.getZ(), effector.getHeading());
    }

    Vector3f closestCollision(Player effector, float direct) {
        // Move Effector backwards direction=1 or frontwards direction=0
        double radian = Math.toRadians(MathUtil.convertHeadingToDegree(effector.getHeading()));
        float x1 = (float) (Math.cos(Math.PI * direct + radian) * distance);
        float y1 = (float) (Math.sin(Math.PI * direct + radian) * distance);
        float targetZ = GeoService.getInstance().getZ(effector.getWorldId(), effector.getX() + x1, effector.getY() + y1, effector.getZ() + 1.5f, 0.2f, effector.getInstanceId());
        byte intentions = (byte) (CollisionIntention.PHYSICAL.getId() | CollisionIntention.DOOR.getId());
        return GeoService.getInstance().getClosestCollision(effector, effector.getX() + x1,
                effector.getY() + y1, targetZ, false, intentions);
    }
}
