/*
 * This file is part of aion-lightning <aion-lightning.org>.
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

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import com.aionemu.gameserver.model.templates.materials.MaterialSkill;
import com.aionemu.gameserver.model.templates.materials.MaterialTemplate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"materialTemplates"})
@XmlRootElement(name = "material_templates")
public class MaterialData {

    @XmlElement(name = "material")
    protected List<MaterialTemplate> materialTemplates;

    @XmlTransient
    Map<Integer, MaterialTemplate> materialsById = new HashMap<>();

    @XmlTransient
    Set<Integer> skillIds = new HashSet<>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        if (materialTemplates == null) {
            return;
        }

        for (MaterialTemplate template : materialTemplates) {
            materialsById.put(template.getId(), template);
            if (template.getSkills() != null) {
                skillIds.addAll(extract(template.getSkills(), on(MaterialSkill.class).getId()));
            }
        }

        materialTemplates.clear();
        materialTemplates = null;
    }

    public MaterialTemplate getTemplate(int materialId) {
        return materialsById.get(materialId);
    }

    public boolean isMaterialSkill(int skillId) {
        return skillIds.contains(skillId);
    }

    public int size() {
        return materialsById.size();
    }

}
