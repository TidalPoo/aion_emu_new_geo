package com.aionemu.gameserver.network;

import java.sql.Timestamp;

/**
 *
 * @author Alex
 */
public class NetworkBanEntry {

    private final String networkip;
    private String details;
    private Timestamp timeEnd;

    public NetworkBanEntry(String address, long newTime) {
        this.networkip = address;
        this.updateTime(newTime);
    }

    public NetworkBanEntry(String address, Timestamp time, String details) {
        this.networkip = address;
        this.timeEnd = time;
        this.details = details;
    }

    public final void setDetails(String details) {
        this.details = details;
    }

    public final void updateTime(long newTime) {
        this.timeEnd = new Timestamp(newTime);
    }

    public final String getNetworkIP() {
        return networkip;
    }

    public final Timestamp getTime() {
        return timeEnd;
    }

    public final boolean isActive() {
        return timeEnd != null && timeEnd.getTime() > System.currentTimeMillis();
    }

    public final boolean isActiveTill(long time) {
        return timeEnd != null && timeEnd.getTime() > time;
    }

    public final String getDetails() {
        return details;
    }
}
