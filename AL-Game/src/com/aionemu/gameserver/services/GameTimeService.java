/*
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.services;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_GAME_TIME;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.gametime.GameTimeManager;
import com.aionemu.gameserver.world.World;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ATracer
 */
public class GameTimeService {

    private static Logger log = LoggerFactory.getLogger(GameTimeService.class);

    public static final GameTimeService getInstance() {
        return SingletonHolder.instance;
    }

    private final static int GAMETIME_UPDATE = 15 * 60000;

    private GameTimeService() {
        /**
         * Update players with current game time
         */
        ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                log.info("Sending current game time to all players");
                Iterator<Player> iterator = World.getInstance().getPlayersIterator();
                while (iterator.hasNext()) {
                    Player next = iterator.next();
                    PacketSendUtility.sendPacket(next, new SM_GAME_TIME());
                }
                // Save game time.
                GameTimeManager.saveTime();
            }
        }, GAMETIME_UPDATE, GAMETIME_UPDATE);

        log.info("GameTimeService started. Update interval:" + GAMETIME_UPDATE);
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final GameTimeService instance = new GameTimeService();
    }
}
