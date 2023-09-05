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
package com.aionemu.gameserver.taskmanager.fromdb.handler;

import com.aionemu.gameserver.ShutdownHook;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Divinity, nrg
 * @modified Alex
 */
public class RestartHandler extends TaskFromDBHandler {

    private static final Logger log = LoggerFactory.getLogger(RestartHandler.class);
    private int countDown;
    private int announceInterval;
    private int warnCountDown;

    @Override
    public boolean isValid() {
        if (params.length == 3) {
            try {
                // общее кол-во секунд до рестарта сервера
                countDown = Integer.parseInt(params[0]);
                // интервал показа сообщения о выключении сервера
                announceInterval = Integer.parseInt(params[1]);
                // предупреждать о рестарте обратный отчет
                warnCountDown = Integer.parseInt(params[2]);
                return true;
            } catch (NumberFormatException e) {
                log.warn("Invalid parameters for RestartHandler. Only valid integers allowed - not registered", e);
            }
        }
        log.warn("RestartHandler has more or less than 3 parameters - not registered");
        return false;
    }

    @Override
    public void trigger() {
        log.info("Task[" + taskId + "] launched : restarting the server !");

        World.getInstance().doOnAllPlayers(new Visitor<Player>() {

            @Override
            public void visit(final Player player) {
                PacketSendUtility.sendBrightYellowMessageOnCenter(player, "" + GSConfig.SERVER_NAME + ": \u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0430\u044f \u043f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0437\u043a\u0430 \u0441\u0435\u0440\u0432\u0435\u0440\u0430 \u043f\u0440\u043e\u0438\u0437\u043e\u0439\u0434\u0435\u0442 \u0447\u0435\u0440\u0435\u0437 " + warnCountDown
                        + " \u043c\u0438\u043d\u0443\u0442.");

                int timer = warnCountDown;
                if (timer > 30) {
                    ThreadPoolManager.getInstance().schedule(new Runnable() {
                        @Override
                        public void run() {
                            PacketSendUtility.sendBrightYellowMessageOnCenter(player, "" + GSConfig.SERVER_NAME + ": \u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0430\u044f \u043f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0437\u043a\u0430 \u0441\u0435\u0440\u0432\u0435\u0440\u0430 \u043f\u0440\u043e\u0438\u0437\u043e\u0439\u0434\u0435\u0442 \u0447\u0435\u0440\u0435\u0437 30 \u043c\u0438\u043d\u0443\u0442.");
                        }
                    }, (timer - 30) * 1000 * 60);
                }
                if (timer > 20) {
                    ThreadPoolManager.getInstance().schedule(new Runnable() {
                        @Override
                        public void run() {
                            PacketSendUtility.sendBrightYellowMessageOnCenter(player, "" + GSConfig.SERVER_NAME + ": \u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0430\u044f \u043f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0437\u043a\u0430 \u0441\u0435\u0440\u0432\u0435\u0440\u0430 \u043f\u0440\u043e\u0438\u0437\u043e\u0439\u0434\u0435\u0442 \u0447\u0435\u0440\u0435\u0437 20 \u043c\u0438\u043d\u0443\u0442.");
                        }
                    }, (timer - 20) * 1000 * 60);
                }
                if (timer > 10) {
                    ThreadPoolManager.getInstance().schedule(new Runnable() {
                        @Override
                        public void run() {
                            PacketSendUtility.sendBrightYellowMessageOnCenter(player, "" + GSConfig.SERVER_NAME + ": \u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0430\u044f \u043f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0437\u043a\u0430 \u0441\u0435\u0440\u0432\u0435\u0440\u0430 \u043f\u0440\u043e\u0438\u0437\u043e\u0439\u0434\u0435\u0442 \u0447\u0435\u0440\u0435\u0437 10 \u043c\u0438\u043d\u0443\u0442.");
                        }
                    }, (timer - 10) * 1000 * 60);
                }
                if (timer > 5) {
                    ThreadPoolManager.getInstance().schedule(new Runnable() {
                        @Override
                        public void run() {
                            PacketSendUtility.sendBrightYellowMessageOnCenter(player, "" + GSConfig.SERVER_NAME + ": \u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0430\u044f \u043f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0437\u043a\u0430 \u0441\u0435\u0440\u0432\u0435\u0440\u0430 \u043f\u0440\u043e\u0438\u0437\u043e\u0439\u0434\u0435\u0442 \u0447\u0435\u0440\u0435\u0437 5 \u043c\u0438\u043d\u0443\u0442. "
                                    + "\u041f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430 \u0441\u0432\u043e\u0435\u0432\u0440\u0435\u043c\u0435\u043d\u043d\u043e \u0437\u0430\u043a\u0440\u043e\u0439\u0442\u0435 \u043a\u043b\u0438\u0435\u043d\u0442.");
                        }
                    }, (timer - 5) * 1000 * 60);
                }
            }
        });

        if (!GSConfig.ENABLE_ACTIVE_ANTICHEAT) {
            ThreadPoolManager.getInstance().schedule(new Runnable() {

                @Override
                public void run() {
                    ShutdownHook.getInstance().doShutdown(countDown, announceInterval, ShutdownHook.ShutdownMode.RESTART);
                }
            }, warnCountDown * 1000);
        }
    }
}
