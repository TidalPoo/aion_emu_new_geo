/**
 * SAO Project
 */
package com.aionemu.gameserver.model.gameobjects.player;

import com.aionemu.gameserver.model.PlayerClass;

/**
 * @author Alex
 */
public class RequestFriend {

    private PlayerCommonData pcd;

    public RequestFriend(PlayerCommonData pcd) {
        this.pcd = pcd;
    }

    public void setPCD(PlayerCommonData pcd) {
        this.pcd = pcd;
    }

    public String getName() {
        return pcd.getName();
    }

    public int getLevel() {
        return pcd.getLevel();
    }

    public PlayerClass getPlayerClass() {
        return pcd.getPlayerClass();
    }

    public int getOid() {
        return pcd.getPlayerObjId();
    }

    public Player getPlayer() {
        return pcd.getPlayer();
    }

    public boolean isOnline() {
        return pcd.isOnline();
    }

    public String getMessage() {
        return pcd.getMessage();
    }

    public int getRequestTime() {
        if (pcd.getRequestTime() == null) {
            return 0;
        }
        return (int) (pcd.getRequestTime().getTime() / 1000);
    }

    public PlayerCommonData getPCD() {
        return pcd;
    }
}
