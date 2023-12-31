/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.skillengine.model;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import javolution.util.FastMap;

/**
 * @author kecimis
 */
public class ChainSkills {

    private Map<String, ChainSkill> multiSkills = new FastMap<>();
    private ChainSkill chainSkill = new ChainSkill("", 0, 0);

    //private Logger log = LoggerFactory.getLogger(ChainSkills.class);
    public int getChainCount(Player player, SkillTemplate template, String category) {
        if (category == null) {
            return 0;
        }
        long nullTime = player.getSkillCoolDown(template.getCooldownId());
        if (this.multiSkills.get(category) != null) {
            if (System.currentTimeMillis() >= nullTime && this.multiSkills.get(category).getUseTime() <= nullTime) {
                this.multiSkills.get(category).setChainCount(0);
            }

            return this.multiSkills.get(category).getChainCount();
        }

        return 0;
    }

    public long getLastChainUseTime(String category) {
        if (this.multiSkills.get(category) != null) {
            return this.multiSkills.get(category).getUseTime();
        } else if (chainSkill.getCategory().equals(category)) {
            return this.chainSkill.getUseTime();
        } else {
            return 0;
        }
    }

    /**
     * returns true if next chain skill can still be casted, or time is over
     *
     * @param category
     * @param time
     * @return
     */
    public boolean chainSkillEnabled(String category, int time) {
        long useTime = 0;
        if (time == 0) {
            return true;
        }

        if (this.multiSkills.get(category) != null) {
            useTime = this.multiSkills.get(category).getUseTime();
        } else if (chainSkill.getCategory().equals(category)) {
            useTime = chainSkill.getUseTime();
        }

        return useTime + time >= System.currentTimeMillis();
    }

    public void addChainSkill(String category, boolean multiCast) {
        if (multiCast) {
            if (this.multiSkills.get(category) != null) {
                if (multiCast) {
                    this.multiSkills.get(category).increaseChainCount();
                }
                this.multiSkills.get(category).setUseTime(System.currentTimeMillis());
            } else {
                this.multiSkills.put(category, new ChainSkill(category, (multiCast ? 1 : 0), System.currentTimeMillis()));
            }
        } else {
            chainSkill.updateChainSkill(category);
        }
    }

    public Collection<ChainSkill> getChainSkills() {
        Collection<ChainSkill> collection = new ArrayList<>();
        collection.add(this.chainSkill);
        collection.addAll(this.multiSkills.values());

        return collection;
    }

}
