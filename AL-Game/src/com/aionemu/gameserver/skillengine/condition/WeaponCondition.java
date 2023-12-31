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
package com.aionemu.gameserver.skillengine.condition;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.templates.item.WeaponType;
import com.aionemu.gameserver.skillengine.model.Skill;
import com.aionemu.gameserver.skillengine.model.Skill.SkillMethod;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WeaponCondition")
public class WeaponCondition extends Condition {

    @XmlAttribute(name = "weapon")
    private List<WeaponType> weaponType;

    @Override
    public boolean validate(Skill env) {
        if (env.getSkillMethod() != SkillMethod.CAST) {
            return true;
        }

        return isValidWeapon(env.getEffector());
    }

    @Override
    public boolean validate(Stat2 stat, IStatFunction statFunction) {
        return isValidWeapon(stat.getOwner());
    }

    /**
     * @param creature
     * @return
     */
    private boolean isValidWeapon(Creature creature) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            return weaponType.contains(player.getEquipment().getMainHandWeaponType());
        }
        //for npcs we don't validate weapon, though in templates they are present
        return true;
    }

}
