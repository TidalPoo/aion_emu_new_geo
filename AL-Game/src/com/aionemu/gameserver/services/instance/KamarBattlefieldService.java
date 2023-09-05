/*
 * This file is part of [JS]Emulator <js-emu.ru>
 *
 * [JS]Emulator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * [JS]Emulator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with [JS]Emulator. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.services.instance;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.configs.main.AutoGroupConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_AUTO_GROUP;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.AutoGroupService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import java.util.Iterator;
import javolution.util.FastList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sharlatan
 */
public class KamarBattlefieldService {

    private static final Logger log = LoggerFactory.getLogger(KamarBattlefieldService.class);
    private boolean registerAvailable;
    private final FastList<Integer> playersWithCooldown = FastList.newInstance();
    public static final byte minLevel = 60, capLevel = 66;
    public static final int maskId = 107;

    public void start() {
        String[] times = AutoGroupConfig.KAMAR_TIMES.split("\\|");
        for (String cron : times) {
            CronService.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    startKamarRegistration();
                }
            }, cron);
            log.info("Scheduled Kamar Battlefield based on cron expression: " + cron + " Duration: " + AutoGroupConfig.KAMAR_TIMER + " in minutes");
        }
    }

    private void startUregisterKamarTask() {
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                registerAvailable = false;
                playersWithCooldown.clear();
                AutoGroupService.getInstance().unRegisterInstance(maskId);
                Iterator<Player> iter = World.getInstance().getPlayersIterator();
                while (iter.hasNext()) {
                    Player player = iter.next();
                    if (player.getLevel() > minLevel) {
                        int instanceMaskId = getInstanceMaskId(player);
                        if (instanceMaskId > 0) {
                            PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, SM_AUTO_GROUP.wnd_EntryIcon, true));
                        }
                    }
                }
            }
        }, AutoGroupConfig.KAMAR_TIMER * 60 * 1000);
    }

    private void startKamarRegistration() {
        this.registerAvailable = true;
        startUregisterKamarTask();
        Iterator<Player> iter = World.getInstance().getPlayersIterator();
        while (iter.hasNext()) {
            Player player = iter.next();
            if (player.getLevel() > minLevel && player.getLevel() < capLevel) {
                int instanceMaskId = getInstanceMaskId(player);
                if (instanceMaskId > 0) {
                    PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, SM_AUTO_GROUP.wnd_EntryIcon));
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_OPEN_IDKAMAR);
                }
            }
        }
    }

    public boolean isKamarAvailable() {
        return this.registerAvailable;
    }

    public byte getInstanceMaskId(Player player) {
        int level = player.getLevel();
        if (level < minLevel || level >= capLevel) {
            return 0;
        }

        return maskId;
    }

    public void addCoolDown(Player player) {
        this.playersWithCooldown.add(player.getObjectId());
    }

    public boolean hasCoolDown(Player player) {
        return this.playersWithCooldown.contains(player.getObjectId());
    }

    public void showWindow(Player player, byte instanceMaskId) {
        if (getInstanceMaskId(player) != instanceMaskId) {
            return;
        }
        if (!this.playersWithCooldown.contains(player.getObjectId())) {
            PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId));
        }
    }

    public boolean isKamarAvialable() {
        return this.registerAvailable;
    }

    private static class SingletonHolder {

        protected static final KamarBattlefieldService instance = new KamarBattlefieldService();
    }

    public static KamarBattlefieldService getInstance() {
        return SingletonHolder.instance;
    }
}
