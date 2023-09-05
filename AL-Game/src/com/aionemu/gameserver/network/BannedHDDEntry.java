package com.aionemu.gameserver.network;

import java.sql.Timestamp;

/**
 *
 * @author Alex
 */
public class BannedHDDEntry {

    private final String hdd_serial;
    private String details;
    private Timestamp timeEnd;

    public BannedHDDEntry(String address, long newTime) {
        this.hdd_serial = address;
        this.updateTime(newTime);
    }

    public BannedHDDEntry(String address, Timestamp time, String details) {
        this.hdd_serial = address;
        this.timeEnd = time;
        this.details = details;
    }

    public final void setDetails(String details) {
        this.details = details;
    }

    public final void updateTime(long newTime) {
        this.timeEnd = new Timestamp(newTime);
    }

    public final String getHDDSerial() {
        return hdd_serial;
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
