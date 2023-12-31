/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.chatserver.service;

import com.aionemu.chatserver.configs.Config;
import com.aionemu.chatserver.network.gameserver.GsAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ATracer, KID
 */
public class GameServerService {

    private Logger log = LoggerFactory.getLogger(GameServerService.class);
    private static GameServerService instance = new GameServerService();

    public static GameServerService getInstance() {
        return instance;
    }

    public static byte GAMESERVER_ID;
    private boolean isOnline = false;

    /**
     * @param gameChannelHandler
     * @param gameServerId
     * @param defaultAddress
     * @param password
     * @return
     */
    public GsAuthResponse registerGameServer(byte gameServerId, byte[] defaultAddress, String password) {
        GAMESERVER_ID = gameServerId;
        if (isOnline) {
            return GsAuthResponse.ALREADY_REGISTERED;
        }

        return passwordConfigAuth(password);
    }

    /**
     * @return
     */
    private GsAuthResponse passwordConfigAuth(String password) {
        if (password.equals(Config.GAME_SERVER_PASSWORD)) {
            isOnline = true;
            return GsAuthResponse.AUTHED;
        }

        log.warn("Gameserver #" + GAMESERVER_ID + " has invalid password.");
        return GsAuthResponse.NOT_AUTHED;
    }

    public void setOffline() {
        log.info("Gameserver #" + GAMESERVER_ID + " is disconnected");
        isOnline = false;
    }
}
