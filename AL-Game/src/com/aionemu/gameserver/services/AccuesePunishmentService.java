/*
 * AionLight project
 */
package com.aionemu.gameserver.services;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import java.sql.Timestamp;

/**
 *
 * @author Alex
 */
public class AccuesePunishmentService {

    public static void sendToDataBase(Player player, int accountId) {
        Timestamp time = new Timestamp(System.currentTimeMillis());

        //  MySQL5AionDAO.getInstance().updateAccuese(accountId, playerId, null);
    }
}
