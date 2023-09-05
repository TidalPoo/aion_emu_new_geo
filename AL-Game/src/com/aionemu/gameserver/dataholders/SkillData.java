/*
 * This file is part of aion-lightning <aion-lightning.smfnew.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.dataholders;

import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ATracer
 */
@XmlRootElement(name = "skill_data")
@XmlAccessorType(XmlAccessType.FIELD)
public class SkillData {

    @XmlElement(name = "skill_template")
    private List<SkillTemplate> skillTemplates;
    private HashMap<Integer, ArrayList<Integer>> cooldownGroups;
    /**
     * Map that contains skillId - SkillTemplate key-value pair
     */
    private TIntObjectHashMap<SkillTemplate> skillData = new TIntObjectHashMap<>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        skillData.clear();
        for (SkillTemplate skillTempalte : skillTemplates) {
            skillData.put(skillTempalte.getSkillId(), skillTempalte);
        }
    }

    /**
     * @param skillId
     * @return SkillTemplate
     */
    public SkillTemplate getSkillTemplate(int skillId) {
        return skillData.get(skillId);
    }

    /**
     * @return skillData.size()
     */
    public int size() {
        return skillData.size();
    }

    /**
     * @return the skillTemplates
     */
    public List<SkillTemplate> getSkillTemplates() {
        return skillTemplates;
    }

    /**
     * @param skillTemplates the skillTemplates to set
     */
    public void setSkillTemplates(List<SkillTemplate> skillTemplates) {
        this.skillTemplates = skillTemplates;
        afterUnmarshal(null, null);
    }

    /**
     * This method creates a HashMap with all skills assigned to their
     * representative cooldownIds
     */
    public void initializeCooldownGroups() {
        cooldownGroups = new HashMap<>();
        for (SkillTemplate skillTemplate : skillTemplates) {
            int cooldownId = skillTemplate.getCooldownId();
            if (!cooldownGroups.containsKey(cooldownId)) {
                cooldownGroups.put(cooldownId, new ArrayList<>());
            }
            cooldownGroups.get(cooldownId).add(skillTemplate.getSkillId());
        }
    }

    /**
     * This method is used to get all skills assigned to a specific cooldownId
     *
     * @param cooldownId
     * @return ArrayList<Integer> including all skills for asked cooldownId
     */
    public ArrayList<Integer> getSkillsForCooldownId(int cooldownId) {
        if (cooldownGroups == null) {
            initializeCooldownGroups();
        }
        return cooldownGroups.get(cooldownId);
    }
}
