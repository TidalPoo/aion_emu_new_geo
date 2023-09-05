/*
 * Novorussia Team 
 */
package com.aionemu.gameserver.model.instance.instancereward;

import com.aionemu.gameserver.model.instance.playerreward.VoidCubePlayerReward;

/**
 * @author Brera, Ritual
 */
public class VoidCubeReward extends InstanceReward<VoidCubePlayerReward> {

    private int points;
    private int npcKills;
    private int rank = 7;
    private int collections;
    private int scoreAP;
    private int sillus;
    private int ceramium;
    private int favorable;
    private boolean isRewarded = false;

    public VoidCubeReward(Integer mapId, int instanceId) {
        super(mapId, instanceId);
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public int getPoints() {
        return this.points;
    }

    public void addNpcKill() {
        this.npcKills++;
    }

    public int getNpcKills() {
        return this.npcKills;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return this.rank;
    }

    public void addGather() {
        this.collections++;
    }

    public int getGatherCollections() {
        return this.collections;
    }

    @Override
    public boolean isRewarded() {
        return this.isRewarded;
    }

    public void setRewarded() {
        this.isRewarded = true;
    }

    public int getScoreAP() {
        return scoreAP;
    }

    public int getSillus() {
        return sillus;
    }

    public int getCeramium() {
        return ceramium;
    }

    public int getFavorable() {
        return favorable;
    }

    public void setScoreAP(int ap) {
        this.scoreAP = ap;
    }

    public void setSillus(int sillus) {
        this.sillus = sillus;
    }

    public void setCeramium(int ceramium) {
        this.ceramium = ceramium;
    }

    public void setFavorable(int favorable) {
        this.favorable = favorable;
    }
}
