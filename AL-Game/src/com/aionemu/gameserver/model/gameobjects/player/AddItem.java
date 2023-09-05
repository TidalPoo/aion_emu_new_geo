/*
 * SAO Project
 */
package com.aionemu.gameserver.model.gameobjects.player;

/**
 *
 * @author Alex
 */
public class AddItem {

    private final Player player;
    private int itemType;
    private boolean gmcheckeritem;
    private boolean buyItems;

    public AddItem(Player player) {
        this.player = player;
    }

    public void setAddItems(int itemType) {
        this.itemType = itemType;
    }

    public void setBuyItems(boolean b) {
        this.buyItems = b;
    }

    public boolean isBuyItems() {
        return buyItems;
    }

    public void setGMAddItem(boolean b) {
        this.gmcheckeritem = b;
    }

    public boolean isGMAddItem() {
        return gmcheckeritem;
    }
}
