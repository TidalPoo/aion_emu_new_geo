/*
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 * aion-lightning is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-lightning is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.templates.panels;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author xTz
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SkillPanel")
public class SkillPanel {

    @XmlAttribute(name = "panel_id")
    protected byte id;
    @XmlAttribute(name = "panel_skills")
    protected List<Integer> skills;

    public int getPanelId() {
        return id;
    }

    public List<Integer> getSkills() {
        return skills;
    }

    public boolean canUseSkill(int skillId, int level) {
        return skills.stream().anyMatch((skill) -> ((skill >> 8) == skillId && (skill & 0xFF) == level));
    }
}
