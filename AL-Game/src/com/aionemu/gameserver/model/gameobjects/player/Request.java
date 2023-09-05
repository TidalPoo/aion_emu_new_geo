/**
 * SAO Project
 */
package com.aionemu.gameserver.model.gameobjects.player;

import com.aionemu.gameserver.model.PlayerClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alex
 */
public class Request {

    private static final Logger log = LoggerFactory.getLogger(Request.class);
    private PlayerCommonData pcd;

    public Request(PlayerCommonData pcd) {
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

    public int getLastOnlineTime() {
        if (pcd.getLastOnline() == null || isOnline()) {
            return 0;
        }

        return (int) (pcd.getLastOnline().getTime() / 1000); // Convert to int, unix time format (ms -> seconds)
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

    public int getRequestTime() {
        if (pcd.getRequestTime() == null) {
            return 0;
        }
        return (int) (pcd.getRequestTime().getTime() / 1000);
    }
}
