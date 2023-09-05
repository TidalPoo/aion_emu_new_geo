/*
 * AionLight project
 */
package com.aionemu.gameserver.model.templates.event;

/**
 *
 * @author Alex
 */
public class MaxCountOfDay {

    private int thisCount;

    public MaxCountOfDay(int thisCount) {
        this.thisCount = thisCount;
    }

    public int getThisCount() {
        return thisCount;
    }

    public void setThisCount(int thisCount) {
        this.thisCount = thisCount;
    }
}
