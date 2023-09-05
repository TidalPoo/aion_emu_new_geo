/*
 * This file is part of aion-engine <aion-engine.net>
 *
 * aion-engine is private software: you can redistribute it and or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Private Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with aion-engine.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.services.player;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SECURITY_TOKEN_REQUEST_STATUS;
import com.aionemu.gameserver.utils.PacketSendUtility;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xXMashUpXx
 *
 */
public class SecurityTokenService {

    private final Logger log = LoggerFactory.getLogger(SecurityTokenService.class);
    String token;

    public String MD5(String md5) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return token = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            log.warn("[SecurityToken] Error to generate token for player!");
        }
        return null;
    }

    public void generateToken(Player player) {
        if (player == null) {
            log.warn("[SecurityToken] Player don't exist O.o");// wtf????
            return;
        }

        if (!"".equals(player.getPlayerAccount().getSecurityToken())) {
            log.warn("[SecurityToken] Player with already exist token should'nt get another one!");
            return;
        }

        MD5(player.getName() + "GH58" + player.getRace().toString() + "8HHGZTU");

        player.getPlayerAccount().setSecurityToken(token);
        sendToken(player, player.getPlayerAccount().getSecurityToken());
    }

    public void sendToken(Player player, String token) {
        if (player == null) {
            return;
        }

        PacketSendUtility.sendPacket(player, new SM_SECURITY_TOKEN_REQUEST_STATUS(token));

    }

    public static final SecurityTokenService getInstance() {
        return SingletonHolder.instance;
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final SecurityTokenService instance = new SecurityTokenService();
    }
}
