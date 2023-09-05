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
package com.aionemu.gameserver.questEngine.handlers.models;

import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.handlers.template.SkillUse;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javolution.util.FastMap;

/**
 * @author vlog, modified Bobobear
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SkillUseData")
public class SkillUseData extends XMLQuest {

    @XmlAttribute(name = "start_npc_id")
    protected int startNpc;
    @XmlAttribute(name = "end_npc_id")
    protected int endNpc;
    @XmlElement(name = "skill", required = true)
    protected List<QuestSkillData> skills;

    @Override
    public void register(QuestEngine questEngine) {
        FastMap<List<Integer>, QuestSkillData> questSkills = new FastMap<>();
        for (QuestSkillData qsd : skills) {
            questSkills.put(qsd.getSkillIds(), qsd);
        }
        SkillUse questTemplate = new SkillUse(id, startNpc, endNpc, questSkills);
        questEngine.addQuestHandler(questTemplate);
    }
}
