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

import com.aionemu.gameserver.model.siege.ArtifactLocation;
import com.aionemu.gameserver.model.siege.FortressLocation;
import com.aionemu.gameserver.model.siege.OutpostLocation;
import com.aionemu.gameserver.model.siege.SiegeLocation;
import com.aionemu.gameserver.model.siege.SourceLocation;
import com.aionemu.gameserver.model.templates.siegelocation.SiegeLocationTemplate;
import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javolution.util.FastMap;

/**
 * @author Sarynth, antness
 */
@XmlRootElement(name = "siege_locations")
@XmlAccessorType(XmlAccessType.FIELD)
public class SiegeLocationData {

    @XmlElement(name = "siege_location")
    private List<SiegeLocationTemplate> siegeLocationTemplates;
    /**
     * Map that contains skillId - SkillTemplate key-value pair
     */
    @XmlTransient
    private final FastMap<Integer, ArtifactLocation> artifactLocations = new FastMap<>();
    @XmlTransient
    private final FastMap<Integer, FortressLocation> fortressLocations = new FastMap<>();
    @XmlTransient
    private final FastMap<Integer, OutpostLocation> outpostLocations = new FastMap<>();
    @XmlTransient
    private final FastMap<Integer, SourceLocation> sourceLocations = new FastMap<>();
    @XmlTransient
    private final FastMap<Integer, SiegeLocation> siegeLocations = new FastMap<>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        artifactLocations.clear();
        fortressLocations.clear();
        outpostLocations.clear();
        sourceLocations.clear();
        siegeLocations.clear();
        for (SiegeLocationTemplate template : siegeLocationTemplates) {
            switch (template.getType()) {
                case FORTRESS:
                    FortressLocation fortress = new FortressLocation(template);
                    fortressLocations.put(template.getId(), fortress);
                    siegeLocations.put(template.getId(), fortress);
                    artifactLocations.put(template.getId(), new ArtifactLocation(template));
                    break;
                case ARTIFACT:
                    ArtifactLocation artifact = new ArtifactLocation(template);
                    artifactLocations.put(template.getId(), artifact);
                    siegeLocations.put(template.getId(), artifact);
                    break;
                case BOSSRAID_LIGHT:
                case BOSSRAID_DARK:
                    OutpostLocation protector = new OutpostLocation(template);
                    outpostLocations.put(template.getId(), protector);
                    siegeLocations.put(template.getId(), protector);
                    break;
                case SOURCE:
                    SourceLocation source = new SourceLocation(template);
                    sourceLocations.put(template.getId(), source);
                    siegeLocations.put(template.getId(), source);
                    break;
                default:
                    break;
            }
        }
    }

    public int size() {
        return siegeLocations.size();
    }

    public FastMap<Integer, ArtifactLocation> getArtifacts() {
        return artifactLocations;
    }

    public FastMap<Integer, FortressLocation> getFortress() {
        return fortressLocations;
    }

    public FastMap<Integer, OutpostLocation> getOutpost() {
        return outpostLocations;
    }

    public FastMap<Integer, SourceLocation> getSource() {
        return sourceLocations;
    }

    public FastMap<Integer, SiegeLocation> getSiegeLocations() {
        return siegeLocations;
    }

}
