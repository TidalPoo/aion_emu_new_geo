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
package com.aionemu.gameserver.model.templates.spawns;

import com.aionemu.gameserver.model.templates.spawns.basespawns.BaseSpawn;
import com.aionemu.gameserver.model.templates.spawns.riftspawns.RiftSpawn;
import com.aionemu.gameserver.model.templates.spawns.siegespawns.SiegeSpawn;
import com.aionemu.gameserver.model.templates.spawns.vortexspawns.VortexSpawn;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author xTz
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "SpawnMap")
public class SpawnMap {

    @XmlElement(name = "spawn")
    private List<Spawn> spawns;
    @XmlElement(name = "base_spawn")
    private List<BaseSpawn> baseSpawns;
    @XmlElement(name = "rift_spawn")
    private List<RiftSpawn> riftSpawns;
    @XmlElement(name = "siege_spawn")
    private List<SiegeSpawn> siegeSpawns;
    @XmlElement(name = "vortex_spawn")
    private List<VortexSpawn> vortexSpawns;
    @XmlAttribute(name = "map_id")
    private int mapId;

    public SpawnMap() {
    }

    public SpawnMap(int mapId) {
        this.mapId = mapId;
    }

    public int getMapId() {
        return mapId;
    }

    public List<Spawn> getSpawns() {
        if (spawns == null) {
            spawns = new ArrayList<>();
        }
        return spawns;
    }

    public void addSpawns(Spawn spawns) {
        getSpawns().add(spawns);
    }

    public void removeSpawns(Spawn spawns) {
        getSpawns().remove(spawns);
    }

    public List<BaseSpawn> getBaseSpawns() {
        if (baseSpawns == null) {
            baseSpawns = new ArrayList<>();
        }
        return baseSpawns;
    }

    public void addBaseSpawns(BaseSpawn spawns) {
        getBaseSpawns().add(spawns);
    }

    public List<RiftSpawn> getRiftSpawns() {
        if (riftSpawns == null) {
            riftSpawns = new ArrayList<>();
        }
        return riftSpawns;
    }

    public void addRiftSpawns(RiftSpawn spawns) {
        getRiftSpawns().add(spawns);
    }

    public List<SiegeSpawn> getSiegeSpawns() {
        if (siegeSpawns == null) {
            siegeSpawns = new ArrayList<>();
        }
        return siegeSpawns;
    }

    public void addSiegeSpawns(SiegeSpawn spawns) {
        getSiegeSpawns().add(spawns);
    }

    public List<VortexSpawn> getVortexSpawns() {
        if (vortexSpawns == null) {
            vortexSpawns = new ArrayList<>();
        }
        return vortexSpawns;
    }

    public void addVortexSpawns(VortexSpawn spawns) {
        getVortexSpawns().add(spawns);
    }

}
