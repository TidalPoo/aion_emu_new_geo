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
package com.aionemu.gameserver.skillengine.model;

/**
 * @author kecimis
 */
public class ChainSkill {

    private String category;
    private int chainCount = 0;
    private long useTime;

    public ChainSkill(String category, int chainCount, long useTime) {
        this.category = category;
        this.chainCount = chainCount;
        this.useTime = useTime;
    }

    public void updateChainSkill(String category) {
        this.category = category;
        this.chainCount = 0;
        this.useTime = System.currentTimeMillis();
    }

    /**
     * @return the name
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param name the name to set
     */
    public void setCategory(String name) {
        this.category = name;
    }

    /**
     * @return the chainCount
     */
    public int getChainCount() {
        return chainCount;
    }

    /**
     * @param chainCount the chainCount to set
     */
    public void setChainCount(int chainCount) {
        this.chainCount = chainCount;
    }

    public void increaseChainCount() {
        this.chainCount++;
    }

    /**
     * @return the useTime
     */
    public long getUseTime() {
        return useTime;
    }

    /**
     * @param useTime the useTime to set
     */
    public void setUseTime(long useTime) {
        this.useTime = useTime;
    }
}
