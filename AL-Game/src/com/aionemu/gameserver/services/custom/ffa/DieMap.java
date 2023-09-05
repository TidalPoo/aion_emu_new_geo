/*
 * SAO Project
 */
package com.aionemu.gameserver.services.custom.ffa;

import java.util.List;
import javolution.util.FastList;

/**
 *
 * @author Alex
 */
public class DieMap {

    private int die;
    private String name;
    private int instanceId;
    private int worldId;
    private final FastList<DieList> list = new FastList<>();

    public int getDie() {
        return die;
    }

    public String getWinnerName() {
        return name;
    }

    public void incrementDie() {
        die++;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWorldIdAndInstanceId(int w, int i) {
        this.worldId = w;
        this.instanceId = i;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public int getWorldId() {
        return worldId;
    }

    public void addPlayer(DieList dieList) {
        list.add(dieList);
    }

    public List<DieList> getList() {
        return list;
    }
}
