/*
 * AionLight project
 */
package com.aionemu.gameserver.services.drop;

/**
 *
 * @author Alex
 */
public class PlayerDropItem {

    private final PlayerDrop pd;
    private int obId;

    public PlayerDropItem(PlayerDrop pd) {
        this.pd = pd;
    }

    public PlayerDrop getPd() {
        return pd;
    }

    public int getObId() {
        return obId;
    }

    public void setObId(int obId) {
        this.obId = obId;
    }
}
