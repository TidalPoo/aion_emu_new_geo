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
package com.aionemu.gameserver.skillengine.properties;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.model.Skill;
import com.aionemu.gameserver.skillengine.properties.Properties.CastState;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.geo.GeoService;

/**
 * @author ATracer
 */
public class FirstTargetRangeProperty {

    /**
     * @param skill
     * @param properties
     * @param castState
     * @return
     */
    public static boolean set(Skill skill, Properties properties, CastState castState) {
        float firstTargetRange = properties.getFirstTargetRange();
        if (!skill.isFirstTargetRangeCheck()) {
            return true;
        }

        Creature effector = skill.getEffector();
        Creature firstTarget = skill.getFirstTarget();

        if (firstTarget == null) {
            return false;
        }
        /*if (firstTargetRange > 200) {
         return true;
         }*/

        // Add Weapon Range to distance
        if (properties.isAddWeaponRange()) {
            firstTargetRange += skill.getEffector().getGameStats().getAttackRange().getCurrent() / 1000f;
        }

        //on end cast check add revision distance value
        if (!castState.isCastStart()) {
            firstTargetRange += properties.getRevisionDistance();
        }

        if (firstTarget.getObjectId().equals(effector.getObjectId())) {
            return true;
        }

        if (!MathUtil.isInAttackRange(effector, firstTarget, firstTargetRange /*+ 2*/)) {
            if (effector instanceof Player) {
                //цель находится слишком далеко
                PacketSendUtility.sendPacket((Player) effector, SM_SYSTEM_MESSAGE.STR_ATTACK_TOO_FAR_FROM_TARGET);
            }
            return false;
        }

        /*if (((Creature) firstTarget).getLifeStats().isAlreadyDead() && !skill.getSkillTemplate().hasResurrectEffect()
         && !skill.checkNonTargetAOE()) {
         PacketSendUtility.sendPacket((Player) effector, SM_SYSTEM_MESSAGE.STR_SKILL_TARGET_IS_NOT_VALID);
         return false;
         }*/
        // TODO check for all targets too
        // Summon Group Member exception
        if (skill.getSkillTemplate().getSkillId() != 1606) {
            if (!GeoService.getInstance().canSee(effector, firstTarget)) {
                if (effector instanceof Player) {
                    //if (skill.getSkillTemplate().get)
                    //1300033 Невозможно атаковать из-за преграды.
                    PacketSendUtility.sendPacket((Player) effector, SM_SYSTEM_MESSAGE.STR_SKILL_OBSTACLE);
                }
                return false;
            }
        }
        return true;
    }

}
