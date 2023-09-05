/*
 * SAO Project
 */
package com.aionemu.gameserver.dataholders;

import com.aionemu.gameserver.model.gminterface.SkillTemp;
import com.aionemu.gameserver.model.templates.gminterface.ResultSkillTemplate;
import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javolution.util.FastMap;

/**
 *
 * @author Alex
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "skill_temps")
public class SkillTempData {

    @XmlElement(name = "skill_temp")
    private List<ResultSkillTemplate> resultTemplate;
    @XmlTransient
    private FastMap<String, SkillTemp> skills = new FastMap<>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (ResultSkillTemplate template : resultTemplate) {
            skills.put(template.getName().toLowerCase(), new SkillTemp(template));
        }
    }

    public int size() {
        return skills.size();
    }

    public FastMap<String, SkillTemp> getNpcs() {
        return skills;
    }
}
