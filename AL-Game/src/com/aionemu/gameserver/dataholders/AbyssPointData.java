package com.aionemu.gameserver.dataholders;

import com.aionemu.gameserver.model.templates.level_abyss_data.AbyssPointTemplate;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Alex
 */
@XmlRootElement(name = "abyss_point")
@XmlAccessorType(XmlAccessType.FIELD)
public class AbyssPointData {

    @XmlElement(name = "limit")
    private List<AbyssPointTemplate> levelList;

    private final TIntObjectHashMap<AbyssPointTemplate> abyssPointData = new TIntObjectHashMap<>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (AbyssPointTemplate level : levelList) {
            abyssPointData.put(level.getLevel(), level);
        }
    }

    public int size() {
        return abyssPointData.size();
    }

    public AbyssPointTemplate getTelelocationTemplate(int id) {
        return abyssPointData.get(id);
    }
}
