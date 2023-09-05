/*
 * AionLight project
 */
package com.aionemu.gameserver.services.wars;

import java.sql.Timestamp;

/**
 *
 * @author Alex
 */
public class LegionWars {

    private int legionId;
    private int legionWarId;
    private String legatOne;
    private String legatTwo;
    private Timestamp startWarTime;
    private Timestamp endWarTime;
    private String winner;
    private int war_kill_all;
    private int war_kill_day;
    private Timestamp war_kill_time;

    public LegionWars(int legionId, int legionWarId, String legatOne, String legatTwo, Timestamp startWarTime, Timestamp endWarTime, String winner, int war_kill_all, int war_kill_day, Timestamp war_kill_time) {
        this.legionId = legionId;
        this.legionWarId = legionWarId;
        this.legatOne = legatOne;
        this.legatTwo = legatTwo;
        this.startWarTime = startWarTime;
        this.endWarTime = endWarTime;
        this.winner = winner;
        this.war_kill_all = war_kill_all;
        this.war_kill_day = war_kill_day;
        this.war_kill_time = war_kill_time;
    }

    public int getLegionId() {
        return legionId;
    }

    public void setLegionId(int legionId) {
        this.legionId = legionId;
    }

    public int getLegionWarId() {
        return legionWarId;
    }

    public void setLegionWarId(int legionWarId) {
        this.legionWarId = legionWarId;
    }

    public String getLegatOne() {
        return legatOne;
    }

    public void setLegatOne(String legatOne) {
        this.legatOne = legatOne;
    }

    public String getLegatTwo() {
        return legatTwo;
    }

    public void setLegatTwo(String legatTwo) {
        this.legatTwo = legatTwo;
    }

    public Timestamp getStartWarTime() {
        return startWarTime;
    }

    public void setStartWarTime(Timestamp startWarTime) {
        this.startWarTime = startWarTime;
    }

    public Timestamp getEndWarTime() {
        return endWarTime;
    }

    public void setEndWarTime(Timestamp endWarTime) {
        this.endWarTime = endWarTime;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public int getWar_kill_all() {
        return war_kill_all;
    }

    public void setWar_kill_all(int war_kill_all) {
        this.war_kill_all = war_kill_all;
    }

    public int getWar_kill_day() {
        return war_kill_day;
    }

    public void setWar_kill_day(int war_kill_day) {
        this.war_kill_day = war_kill_day;
    }

    public Timestamp getWar_kill_time() {
        return war_kill_time;
    }

    public void setWar_kill_time(Timestamp war_kill_time) {
        this.war_kill_time = war_kill_time;
    }
}
