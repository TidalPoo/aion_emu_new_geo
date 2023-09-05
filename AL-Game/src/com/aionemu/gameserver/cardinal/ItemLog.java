/*
 * SAO Project
 */
package com.aionemu.gameserver.cardinal;

/**
 *
 * @author Alex
 */
public class ItemLog {

    private final int itemType;
    private final int itemId;
    private final long itemCount;
    private final String info;
    private final int accountId;
    private final Integer playerId;
    private final String name;
    private final String ip;
    private final String mac;

    public ItemLog(int accountId, int playerId, String name, String ip, String mac, int itemType, int itemId, long itemCount, String info) {
        this.mac = mac;
        this.itemType = itemType;
        this.itemId = itemId;
        this.itemCount = itemCount;
        this.info = info;
        this.accountId = accountId;
        this.playerId = playerId;
        this.name = name;
        this.ip = ip;
    }

    public int getAccountId() {
        return accountId;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public String getMac() {
        return mac;
    }

    public int getItemType() {
        return itemType;
    }

    public int getItemId() {
        return itemId;
    }

    public long getItemCount() {
        return itemCount;
    }

    public String getInfo() {
        return info;
    }
}
