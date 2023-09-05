/*
 * AionLight project
 */
package com.aionemu.gameserver.services.drop;

import com.aionemu.gameserver.model.gameobjects.Item;

/**
 *
 * @author Alex
 */
public class PlayerDrop {

    private int playerObjectId;
    private int itemId;
    private long count;
    private Item item;
    private int enchant;

    public PlayerDrop(Item item, long count) {
        this.item = item;
        this.count = count;
        // this.itemId = itemId;
        // this.playerObjectId = player;
    }

    public PlayerDrop(Item item) {
        this.item = item;
    }

    public int getPlayerId() {
        return playerObjectId;
    }

    public void setPlayerObjectId(int playerObjectId) {
        this.playerObjectId = playerObjectId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getItemId() {
        return itemId;
    }

    public long getCount() {
        return count;
    }

    public int getEnchant() {
        return enchant;
    }

    public void setEnchant(int enchant) {
        this.enchant = enchant;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
