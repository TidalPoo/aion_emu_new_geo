/*
 * AionLight project
 */
package com.aionemu.gameserver.dataholders;

import com.aionemu.gameserver.model.gminterface.ItemTemp;
import com.aionemu.gameserver.model.templates.gminterface.ResultItemTemplate;
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
@XmlRootElement(name = "item_temps")
public class ItemTempData {

    @XmlElement(name = "item_temp")
    private List<ResultItemTemplate> resultTemplate;
    @XmlTransient
    private FastMap<String, ItemTemp> items = new FastMap<>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (ResultItemTemplate template : resultTemplate) {
            items.put(template.getName(), new ItemTemp(template));
        }
    }

    public int size() {
        return items.size();
    }

    public FastMap<String, ItemTemp> getItems() {
        return items;
    }
}
