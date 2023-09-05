/*
 * AionLight project
 */
package com.aionemu.gameserver.dataholders;

import com.aionemu.gameserver.model.gminterface.NpcTemp;
import com.aionemu.gameserver.model.templates.gminterface.ResultNpcTemplate;
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
@XmlRootElement(name = "npc_temps")
public class NpcTempData {

    @XmlElement(name = "npc_temp")
    private List<ResultNpcTemplate> resultTemplate;
    @XmlTransient
    private FastMap<String, NpcTemp> npcs = new FastMap<>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (ResultNpcTemplate template : resultTemplate) {
            npcs.put(template.getName().toLowerCase(), new NpcTemp(template));
        }
    }

    public int size() {
        return npcs.size();
    }

    public FastMap<String, NpcTemp> getNpcs() {
        return npcs;
    }
}
