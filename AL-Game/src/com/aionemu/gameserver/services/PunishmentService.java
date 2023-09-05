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

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.dao.PlayerPunishmentsDAO;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CAPTCHA;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUIT_RESPONSE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.chatserver.ChatServer;
import com.aionemu.gameserver.services.custom.Prison;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMapType;
import java.util.Calendar;
import java.util.concurrent.Future;

/**
 * @author lord_rex, Cura, nrg
 * @modified Alex added new banneds and fix gag.
 */
public class PunishmentService {

    /**
     * This method will handle unbanning a character
     *
     * @param playerId
     */
    public static void unbanChar(int playerId) {
        DAOManager.getDAO(PlayerPunishmentsDAO.class).unpunishPlayer(playerId, PunishmentType.CHARBAN);
    }

    /**
     * This method will handle banning a character
     *
     * @param playerId
     * @param dayCount
     * @param reason
     */
    public static void banChar(int playerId, int dayCount, String reason) {
        DAOManager.getDAO(PlayerPunishmentsDAO.class).punishPlayer(playerId, PunishmentType.CHARBAN, calculateDuration(dayCount), reason, 0);

        //if player is online - kick him
        Player player = World.getInstance().findPlayer(playerId);
        if (player != null) {
            player.getClientConnection().close(new SM_QUIT_RESPONSE(), false);
        }
    }

    /**
     * Calculates the timestamp when a given number of days is over
     *
     * @param dayCount
     * @return timeStamp
     */
    public static long calculateDuration(int dayCount) {
        if (dayCount == 0) {
            return Integer.MAX_VALUE; //int because client handles this with seconds timestamp in int
        }
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +dayCount);

        return ((cal.getTimeInMillis() - System.currentTimeMillis()) / 1000);
    }

    /**
     * This method will handle moving or removing a player from prison
     *
     * @param player
     * @param state
     * @param delayInMinutes
     * @param reason
     */
    public static void setIsInPrison(Player player, boolean state, long delayInMinutes, String reason) {
        stopPrisonTask(player, false);
        if (state) {
            long prisonTimer = player.getPrisonTimer();
            if (delayInMinutes > 0) {
                prisonTimer = delayInMinutes * 60000L;
                schedulePrisonTask(player, prisonTimer);
                PacketSendUtility.sendMessage(player, "Вы отправлены в тюрьму на " + delayInMinutes
                        + " минут.\n Если вы вышли из игры счетчик времени останавливается, и запускается при следующем входе в игру.");
            }
            if (GSConfig.ENABLE_CHAT_SERVER) {
                ChatServer.getInstance().sendPlayerLogout(player);
            }
            player.setStartPrison(System.currentTimeMillis());
            player.setPrisonReason(reason);
            if (!Prison.getInstance().isInPrison(player)) {
                TeleportService2.teleportToPrison(player);
            }
            DAOManager.getDAO(PlayerPunishmentsDAO.class).punishPlayer(player, PunishmentType.PRISON, reason, player.getPrisonLocation());
        } else {
            PacketSendUtility.sendMessage(player, "Вы вышли из тюрьмы.");
            if (GSConfig.ENABLE_CHAT_SERVER) {
                PacketSendUtility.sendMessage(player, "Чат будет доступен после релога!");
            }
            player.setPrisonTimer(0);
            if (player.getPrisonLocation() != -1) {
                player.getEffectController().unsetAbnormal(AbnormalState.CANNOT_MOVE.getId());
                player.getEffectController().updatePlayerEffectIcons();
                Prison.getInstance().stopPrison(player.getRace(), player.getPrisonLocation());
                Prison.getInstance().toNicker(player, null, true);
            } else {
                InstanceService.destroyCustomInstance(player.getPosition().getWorldMapInstance());
            }
            TeleportService2.moveToBindLocation(player, true);
            DAOManager.getDAO(PlayerPunishmentsDAO.class).unpunishPlayer(player.getObjectId(), PunishmentType.PRISON);
        }
    }

    /**
     * This method will stop the prison task
     *
     * @param player
     * @param save
     */
    public static void stopPrisonTask(Player player, boolean save) {
        Future<?> prisonTask = player.getController().getTask(TaskId.PRISON);
        if (prisonTask != null) {
            if (save) {
                long delay = player.getPrisonTimer();
                if (delay < 0) {
                    delay = 0;
                }
                player.setPrisonTimer(delay);
            }
            player.getController().cancelTask(TaskId.PRISON);
        }
    }

    /**
     * This method will update the prison status
     *
     * @param player
     */
    public static void updatePrisonStatus(final Player player) {
        if (player.isInPrison()) {
            long prisonTimer = player.getPrisonTimer();
            if (prisonTimer > 0) {
                schedulePrisonTask(player, prisonTimer);
                int timeInPrison = (int) (prisonTimer / 60000);

                if (timeInPrison <= 0) {
                    timeInPrison = 1;
                }

                PacketSendUtility.sendMessage(player, "Вам осталось отсидеть " + timeInPrison + " минут"
                        + (timeInPrison > 1 ? "ы" : "") + ".");
                player.setStartPrison(System.currentTimeMillis());
            }

            if (player.getPrisonLocation() != -1) {
                TeleportService2.teleportToPrison(player);
            }
            boolean noDefaultLocation = player.getWorldId() != WorldMapType.DF_PRISON.getId() && player.getWorldId() != WorldMapType.DE_PRISON.getId();
            boolean noCustomLocation = player.getWorldId() != WorldMapType.SANCTUM.getId() && player.getWorldId() != WorldMapType.PANDAEMONIUM.getId();
            if ((noDefaultLocation && player.getPrisonLocation() == -1) || player.getPrisonLocation() != -1 && noCustomLocation) {
                PacketSendUtility.sendMessage(player, "Вы будете телепортированы в тюрьму через одну минуту!");
                ThreadPoolManager.getInstance().schedule(new Runnable() {

                    @Override
                    public void run() {
                        TeleportService2.teleportToPrison(player);
                    }
                }, 60000);
            }
        }
    }

    /**
     * This method will schedule a prison task
     *
     * @param player
     * @param prisonTimer
     */
    private static void schedulePrisonTask(final Player player, long prisonTimer) {
        player.setPrisonTimer(prisonTimer);
        player.getController().addTask(TaskId.PRISON, ThreadPoolManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                setIsInPrison(player, false, 0, "");
            }
        }, prisonTimer));
    }

    /**
     * This method will handle can or cant gathering
     *
     * @param player
     * @param captchaCount
     * @param state
     * @param delay
     * @author Cura
     */
    public static void setIsNotGatherable(Player player, int captchaCount, boolean state, long delay) {
        stopGatherableTask(player, false);

        if (state) {
            if (captchaCount < 3) {
                PacketSendUtility.sendPacket(player, new SM_CAPTCHA(captchaCount + 1, player.getCaptchaImage()));
            } else {
                player.setCaptchaWord(null);
                player.setCaptchaImage(null);
            }

            player.setGatherableTimer(delay);
            player.setStopGatherable(System.currentTimeMillis());
            scheduleGatherableTask(player, delay);
            DAOManager.getDAO(PlayerPunishmentsDAO.class).punishPlayer(player, PunishmentType.GATHER, "Possible gatherbot", 0);
        } else {
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400269));
            player.setCaptchaWord(null);
            player.setCaptchaImage(null);
            player.setGatherableTimer(0);
            player.setStopGatherable(0);
            DAOManager.getDAO(PlayerPunishmentsDAO.class).unpunishPlayer(player.getObjectId(), PunishmentType.GATHER);
        }
    }

    /**
     * This method will stop the gathering task
     *
     * @param player
     * @param save
     * @author Cura
     */
    public static void stopGatherableTask(Player player, boolean save) {
        Future<?> gatherableTask = player.getController().getTask(TaskId.GATHERABLE);

        if (gatherableTask != null) {
            if (save) {
                long delay = player.getGatherableTimer();
                if (delay < 0) {
                    delay = 0;
                }
                player.setGatherableTimer(delay);
            }
            player.getController().cancelTask(TaskId.GATHERABLE);
        }
    }

    /**
     * This method will update the gathering status
     *
     * @param player
     * @author Cura
     */
    public static void updateGatherableStatus(Player player) {
        if (player.isNotGatherable()) {
            long gatherableTimer = player.getGatherableTimer();

            if (gatherableTimer > 0) {
                scheduleGatherableTask(player, gatherableTimer);
                player.setStopGatherable(System.currentTimeMillis());
            }
        }
    }

    /**
     * This method will schedule a gathering task
     *
     * @param player
     * @param gatherableTimer
     * @author Cura
     */
    private static void scheduleGatherableTask(final Player player, long gatherableTimer) {
        player.setGatherableTimer(gatherableTimer);
        player.getController().addTask(TaskId.GATHERABLE, ThreadPoolManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                setIsNotGatherable(player, 0, false, 0);
            }
        }, gatherableTimer));
    }

    /**
     *
     * @param player
     * @param state
     * @param delayInMinutes
     * @param reason
     */
    public static void setGAG(Player player, boolean state, long delayInMinutes, String reason) {
        stopGAGTask(player, false);
        if (state) {
            long gagTimer = player.getGAGTimer();
            if (delayInMinutes > 0) {
                gagTimer = delayInMinutes * 60000L;
                scheduleGAGTask(player, gagTimer);
            }
            if (delayInMinutes == 2) {
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FLOODING);
            }
            player.setStartGAG(System.currentTimeMillis());
            player.setChatBanReason(reason);
            DAOManager.getDAO(PlayerPunishmentsDAO.class).punishPlayer(player, PunishmentType.GAG, reason, 0);
        } else {
            player.setGAGTimer(0);
            DAOManager.getDAO(PlayerPunishmentsDAO.class).unpunishPlayer(player.getObjectId(), PunishmentType.GAG);
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CAN_CHAT_NOW);
        }
    }

    /**
     *
     * @param player
     * @param save
     */
    public static void stopGAGTask(Player player, boolean save) {
        Future<?> prisonTask = player.getController().getTask(TaskId.GAG);
        if (prisonTask != null) {
            if (save) {
                long delay = player.getGAGTimer();
                if (delay < 0) {
                    delay = 0;
                }
                player.setGAGTimer(delay);
            }
            player.getController().cancelTask(TaskId.GAG);
        }
    }

    private static void scheduleGAGTask(final Player player, long gagTimer) {
        player.setGAGTimer(gagTimer);
        player.getController().addTask(TaskId.GAG, ThreadPoolManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                setGAG(player, false, 0, "");
            }
        }, gagTimer));
    }

    /**
     *
     * @param player
     */
    public static void updateGAGStatus(final Player player) {
        long gagTimer = player.getGAGTimer();
        if (gagTimer > 0) {
            scheduleGAGTask(player, gagTimer);
            int timeInGag = (int) (gagTimer / 60000);

            if (timeInGag <= 0) {
                timeInGag = 1;
            }
            player.setStartGAG(System.currentTimeMillis());
            if (GSConfig.ENABLE_CHAT_SERVER) {
                ChatServer.getInstance().sendPlayerGagPacket(player.getObjectId(), gagTimer);
            }
        }
    }

    /**
     *
     * @param player
     * @param state
     * @param delayInMinutes
     * @param reason
     */
    public static void setBanPrivateShop(Player player, boolean state, long delayInMinutes, String reason) {
        stopPrivateShopTask(player, false);
        if (state) {
            long pshopTimer = player.getPShopTimer();
            if (delayInMinutes > 0) {
                pshopTimer = delayInMinutes * 60000L;
                schedulePShopTask(player, pshopTimer);
            }
            if (delayInMinutes == 2) {
                // PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FLOODING);
            }
            player.setStartPShop(System.currentTimeMillis());
            player.setPrivateStoreBanReason(reason);
            DAOManager.getDAO(PlayerPunishmentsDAO.class).punishPlayer(player, PunishmentType.PRIVATESHOP, reason, 0);
        } else {
            player.setPShopTimer(0);
            DAOManager.getDAO(PlayerPunishmentsDAO.class).unpunishPlayer(player.getObjectId(), PunishmentType.PRIVATESHOP);
            //PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CAN_CHAT_NOW);
        }
    }

    /**
     *
     * @param player
     * @param save
     */
    public static void stopPrivateShopTask(Player player, boolean save) {
        Future<?> prisonTask = player.getController().getTask(TaskId.PRIVATESHOP);
        if (prisonTask != null) {
            if (save) {
                long delay = player.getPShopTimer();
                if (delay < 0) {
                    delay = 0;
                }
                player.setPShopTimer(delay);
            }
            player.getController().cancelTask(TaskId.PRIVATESHOP);
        }
    }

    private static void schedulePShopTask(final Player player, long pshopTimer) {
        player.setPShopTimer(pshopTimer);
        player.getController().addTask(TaskId.PRIVATESHOP, ThreadPoolManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                setBanPrivateShop(player, false, 0, "");
            }
        }, pshopTimer));
    }

    /**
     *
     * @param player
     */
    public static void updatePShopStatus(final Player player) {
        long pShopTimer = player.getPShopTimer();
        if (pShopTimer > 0) {
            schedulePShopTask(player, pShopTimer);
            int timeInB = (int) (pShopTimer / 60000);

            if (timeInB <= 0) {
                timeInB = 1;
            }
            player.setStartPShop(System.currentTimeMillis());
        }
    }

    /**
     *
     * @param player
     * @param state
     * @param delayInMinutes
     * @param reason
     */
    public static void setBanInGameShop(Player player, boolean state, long delayInMinutes, String reason) {
        stopGameShopTask(player, false);
        if (state) {
            long gshopTimer = player.getGShopTimer();
            if (delayInMinutes > 0) {
                gshopTimer = delayInMinutes * 60000L;
                scheduleGameShopTask(player, gshopTimer);
            }
            if (delayInMinutes == 2) {
                // PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FLOODING);
            }
            player.setStartGShop(System.currentTimeMillis());
            player.setGameShopBanReason(reason);
            DAOManager.getDAO(PlayerPunishmentsDAO.class).punishPlayer(player, PunishmentType.INGAMESHOP, reason, 0);
        } else {
            player.setGShopTimer(0);
            DAOManager.getDAO(PlayerPunishmentsDAO.class).unpunishPlayer(player.getObjectId(), PunishmentType.INGAMESHOP);
            //PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CAN_CHAT_NOW);
        }
    }

    /**
     *
     * @param player
     * @param save
     */
    public static void stopGameShopTask(Player player, boolean save) {
        Future<?> gameShopTask = player.getController().getTask(TaskId.INGAMESHOP);
        if (gameShopTask != null) {
            if (save) {
                long delay = player.getGShopTimer();
                if (delay < 0) {
                    delay = 0;
                }
                player.setGShopTimer(delay);
            }
            player.getController().cancelTask(TaskId.INGAMESHOP);
        }
    }

    private static void scheduleGameShopTask(final Player player, long gshopTimer) {
        player.setGShopTimer(gshopTimer);
        player.getController().addTask(TaskId.INGAMESHOP, ThreadPoolManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                setBanInGameShop(player, false, 0, "");
            }
        }, gshopTimer));
    }

    /**
     *
     * @param player
     */
    public static void updateGShopStatus(final Player player) {
        long gShopTimer = player.getGShopTimer();
        if (gShopTimer > 0) {
            scheduleGameShopTask(player, gShopTimer);
            int timeInB = (int) (gShopTimer / 60000);

            if (timeInB <= 0) {
                timeInB = 1;
            }
            player.setStartGShop(System.currentTimeMillis());
        }
    }

    /**
     *
     * @param player
     * @param state
     * @param delayInMinutes
     * @param reason
     */
    public static void setTeleportBan(Player player, boolean state, long delayInMinutes, String reason) {
        stopTeleportTask(player, false);
        if (state) {
            long teleportTimer = player.getTeleportTimer();
            if (delayInMinutes > 0) {
                teleportTimer = delayInMinutes * 60000L;
                scheduleTeleportTask(player, teleportTimer);
            }
            if (delayInMinutes == 2) {
                // PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FLOODING);
            }
            player.setStartTeleport(System.currentTimeMillis());
            player.setTeleportBanReason(reason);
            DAOManager.getDAO(PlayerPunishmentsDAO.class).punishPlayer(player, PunishmentType.TELEPORT, reason, 0);
        } else {
            player.setTeleportTimer(0);
            DAOManager.getDAO(PlayerPunishmentsDAO.class).unpunishPlayer(player.getObjectId(), PunishmentType.TELEPORT);
            //PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CAN_CHAT_NOW);
        }
    }

    /**
     *
     * @param player
     * @param save
     */
    public static void stopTeleportTask(Player player, boolean save) {
        Future<?> teleportTask = player.getController().getTask(TaskId.TELEPORT);
        if (teleportTask != null) {
            if (save) {
                long delay = player.getTeleportTimer();
                if (delay < 0) {
                    delay = 0;
                }
                player.setTeleportTimer(delay);
            }
            player.getController().cancelTask(TaskId.TELEPORT);
        }
    }

    private static void scheduleTeleportTask(final Player player, long teleportTimer) {
        player.setTeleportTimer(teleportTimer);
        player.getController().addTask(TaskId.TELEPORT, ThreadPoolManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                setTeleportBan(player, false, 0, "");
            }
        }, teleportTimer));
    }

    /**
     *
     * @param player
     */
    public static void updateTeleportStatus(final Player player) {
        long teleportTimer = player.getGShopTimer();
        if (teleportTimer > 0) {
            scheduleTeleportTask(player, teleportTimer);
            int timeInB = (int) (teleportTimer / 60000);

            if (timeInB <= 0) {
                timeInB = 1;
            }
            player.setStartTeleport(System.currentTimeMillis());
        }
    }

    /**
     *
     * @param player
     * @param state
     * @param delayInMinutes
     * @param reason
     */
    public static void setMoveBan(Player player, boolean state, long delayInMinutes, String reason) {
        stopMoveTask(player, false);
        if (state) {
            long moveTimer = player.getMoveTimer();
            if (delayInMinutes > 0) {
                moveTimer = delayInMinutes * 60000L;
                scheduleMoveTask(player, moveTimer);
            }
            if (delayInMinutes == 2) {
                // PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FLOODING);
            }
            player.setStartBanMove(System.currentTimeMillis());
            player.setMoveBanReason(reason);
            DAOManager.getDAO(PlayerPunishmentsDAO.class).punishPlayer(player, PunishmentType.MOVE, reason, 0);
            player.getEffectController().setAbnormal(AbnormalState.CANNOT_MOVE.getId());
            player.getEffectController().updatePlayerEffectIcons();
        } else {
            player.setTeleportTimer(0);
            DAOManager.getDAO(PlayerPunishmentsDAO.class).unpunishPlayer(player.getObjectId(), PunishmentType.MOVE);
            //PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CAN_CHAT_NOW);
            player.getEffectController().unsetAbnormal(AbnormalState.CANNOT_MOVE.getId());
            player.getEffectController().updatePlayerEffectIcons();
        }
    }

    /**
     *
     * @param player
     * @param save
     */
    public static void stopMoveTask(Player player, boolean save) {
        Future<?> moveTask = player.getController().getTask(TaskId.MOVE);
        if (moveTask != null) {
            if (save) {
                long delay = player.getMoveTimer();
                if (delay < 0) {
                    delay = 0;
                }
                player.setMoveTimer(delay);
            }
            player.getController().cancelTask(TaskId.MOVE);
        }
    }

    private static void scheduleMoveTask(final Player player, long moveTimer) {
        player.setMoveTimer(moveTimer);
        player.getController().addTask(TaskId.MOVE, ThreadPoolManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                setMoveBan(player, false, 0, "");
            }
        }, moveTimer));
    }

    /**
     *
     * @param player
     */
    public static void updateMoveStatus(final Player player) {
        long moveTimer = player.getMoveTimer();
        if (moveTimer > 0) {
            scheduleMoveTask(player, moveTimer);
            int timeInB = (int) (moveTimer / 60000);

            if (timeInB <= 0) {
                timeInB = 1;
            }
            player.setStartBanMove(System.currentTimeMillis());
            player.getEffectController().setAbnormal(AbnormalState.CANNOT_MOVE.getId());
            player.getEffectController().updatePlayerEffectIcons();
        }
    }

    /**
     * PunishmentType
     *
     * @author Cura, Alex
     */
    public enum PunishmentType {

        PRISON,
        GATHER,
        CHARBAN,
        GAG,
        PRIVATESHOP,
        INGAMESHOP,
        TELEPORT,
        MOVE;
    }
}
