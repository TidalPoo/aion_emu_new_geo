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
package com.aionemu.gameserver.services.player;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.AutoGroupConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.dao.EventItemsDAO;
import com.aionemu.gameserver.dao.HouseObjectCooldownsDAO;
import com.aionemu.gameserver.dao.ItemCooldownsDAO;
import com.aionemu.gameserver.dao.ItemLogDAO;
import com.aionemu.gameserver.dao.PlayerCooldownsDAO;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dao.PlayerEffectsDAO;
import com.aionemu.gameserver.dao.PlayerLifeStatsDAO;
import com.aionemu.gameserver.eventengine.events.EventManager;
import com.aionemu.gameserver.model.account.PlayerAccountData;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.StorageType;
import com.aionemu.gameserver.model.summons.SummonMode;
import com.aionemu.gameserver.model.summons.UnsummonType;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceService;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.network.aion.clientpackets.CM_QUIT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DELETE;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.AutoGroupService;
import com.aionemu.gameserver.services.BrokerService;
import com.aionemu.gameserver.services.ChatService;
import com.aionemu.gameserver.services.DuelService;
import com.aionemu.gameserver.services.ExchangeService;
import com.aionemu.gameserver.services.FindGroupService;
import com.aionemu.gameserver.services.KiskService;
import com.aionemu.gameserver.services.LegionService;
import com.aionemu.gameserver.services.PunishmentService;
import com.aionemu.gameserver.services.RepurchaseService;
import com.aionemu.gameserver.services.SerialKillerService;
import com.aionemu.gameserver.services.WorldBuffService;
import com.aionemu.gameserver.services.custom.MixFight;
import com.aionemu.gameserver.services.custom.Prison;
import com.aionemu.gameserver.services.custom.PvPLocationService;
import com.aionemu.gameserver.services.custom.ffa.FfaGroupService;
import com.aionemu.gameserver.services.custom.ffa.FfaLegionService;
import com.aionemu.gameserver.services.custom.ffa.FfaPlayers;
import com.aionemu.gameserver.services.drop.DropService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.summons.SummonsService;
import com.aionemu.gameserver.services.toypet.PetSpawnService;
import com.aionemu.gameserver.services.weddings.WeddingService;
import com.aionemu.gameserver.taskmanager.tasks.ExpireTimerTask;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.audit.GMService;
import java.sql.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ATracer
 * @modified Alex
 */
public class PlayerLeaveWorldService {

    private static final Logger log = LoggerFactory.getLogger(PlayerLeaveWorldService.class);

    /**
     * @param player
     * @param delay
     */
    public static final void startLeaveWorldDelay(final Player player, int delay) {
        // force stop movement of player
        player.getController().stopMoving();

        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                startLeaveWorld(player);
            }

        }, delay);
    }

    /**
     * This method is called when player leaves the game, which includes just
     * two cases: either player goes back to char selection screen or it's
     * leaving the game [closing client].<br> <br> <b><font color='red'>NOTICE:
     * </font> This method is called only from {@link GameConnection} and
     * {@link CM_QUIT} and must not be called from anywhere else</b>
     *
     * @param player
     */
    public static final void startLeaveWorld(Player player) {
        log.info("Player logged out: " + player.getName() + " Account: "
                + (player.getClientConnection() != null ? player.getClientConnection().getAccount().getName() : "disconnected"));
        FindGroupService.getInstance().removeFindGroup(player.getRace(), 0x00, player.getObjectId());
        FindGroupService.getInstance().removeFindGroup(player.getRace(), 0x04, player.getObjectId());
        player.onLoggedOut();
        if (player.isGM()) {
            GMService.getInstance().onPlayerLogedOut(player);
        }
        //new
        EventManager.getInstance().unregisterPlayer(player);
        FfaPlayers.onLogOut(player);
        FfaGroupService.onLogOut(player);
        FfaLegionService.onLogOut(player);
        MixFight.onLogOut(player);
        PvPLocationService.onLeavePvPLocation(player, true);
        BrokerService.getInstance().removePlayerCache(player);
        ExchangeService.getInstance().cancelExchange(player);
        RepurchaseService.getInstance().removeRepurchaseItems(player);
        if (AutoGroupConfig.AUTO_GROUP_ENABLE) {
            AutoGroupService.getInstance().onPlayerLogOut(player);
        }
        SerialKillerService.getInstance().onLogout(player);
        InstanceService.onLogOut(player);
        KiskService.getInstance().onLogout(player);
        WorldBuffService.getInstance().onLogOut(player);
        player.getMoveController().abortMove();

        if (player.isLooting()) {
            DropService.getInstance().closeDropList(player, player.getLootingNpcOid());
        }

        // Update prison timer
        if (player.isInPrison()) {
            long prisonTimer = System.currentTimeMillis() - player.getStartPrison();
            prisonTimer = player.getPrisonTimer() - prisonTimer;
            player.setPrisonTimer(prisonTimer);
            Prison.getInstance().stopPrison(player.getRace(), player.getPrisonLocation());
            log.debug("Update prison timer to " + prisonTimer / 1000 + " seconds !");
        }
        // Update gag timer
        if (player.isGagged()) {
            long gagTimer = System.currentTimeMillis() - player.getStartGAG();
            gagTimer = player.getGAGTimer() - gagTimer;
            player.setGAGTimer(gagTimer);
            log.debug("Update gag timer to " + gagTimer / 1000 + " seconds !");
        }

        // Update ban private shop timer
        if (player.isBannedPStore()) {
            long banPStoreTimer = System.currentTimeMillis() - player.getStartPShop();
            banPStoreTimer = player.getPShopTimer() - banPStoreTimer;
            player.setPShopTimer(banPStoreTimer);
            log.debug("Update ban private store timer to " + banPStoreTimer / 1000 + " seconds !");
        }

        // Update ban game shop timer
        if (player.isBannedGameShop()) {
            long banGameShopTimer = System.currentTimeMillis() - player.getStartGShop();
            banGameShopTimer = player.getGShopTimer() - banGameShopTimer;
            player.setGShopTimer(banGameShopTimer);
            log.debug("Update ban game shop timer to " + banGameShopTimer / 1000 + " seconds !");
        }
        // Update ban teleport timer
        if (player.isTeleportBanned()) {
            long teleportTimer = System.currentTimeMillis() - player.getStartTeleport();
            teleportTimer = player.getTeleportTimer() - teleportTimer;
            player.setTeleportTimer(teleportTimer);
            log.debug("Update ban teleport timer to " + teleportTimer / 1000 + " seconds !");
        }

        // Update ban move timer
        if (player.isMoveBanned()) {
            long moveTimer = System.currentTimeMillis() - player.getStartBanMove();
            moveTimer = player.getMoveTimer() - moveTimer;
            player.setMoveTimer(moveTimer);
            log.debug("Update ban move timer to " + moveTimer / 1000 + " seconds !");
        }
        // store current effects
        DAOManager.getDAO(PlayerEffectsDAO.class).storePlayerEffects(player);
        DAOManager.getDAO(PlayerCooldownsDAO.class).storePlayerCooldowns(player);
        DAOManager.getDAO(ItemCooldownsDAO.class).storeItemCooldowns(player);
        DAOManager.getDAO(HouseObjectCooldownsDAO.class).storeHouseObjectCooldowns(player);
        DAOManager.getDAO(PlayerLifeStatsDAO.class).updatePlayerLifeStat(player);
        DAOManager.getDAO(EventItemsDAO.class).storeItems(player);
        DAOManager.getDAO(ItemLogDAO.class).insertLog(player);

        PlayerGroupService.onPlayerLogout(player);
        PlayerAllianceService.onPlayerLogout(player);
        // fix legion warehouse exploits
        LegionService.getInstance().LegionWhUpdate(player);
        player.getEffectController().removeAllEffects(true);
        player.getLifeStats().cancelAllTasks();

        if (player.getLifeStats().isAlreadyDead()) {
            if (player.isInInstance()) {
                PlayerReviveService.instanceRevive(player);
            } else {
                PlayerReviveService.bindRevive(player);
            }
        } else if (DuelService.getInstance().isDueling(player.getObjectId())) {
            DuelService.getInstance().loseDuel(player);
        }
        Summon summon = player.getSummon();
        if (summon != null) {
            SummonsService.doMode(SummonMode.RELEASE, summon, UnsummonType.LOGOUT);
        }
        PetSpawnService.dismissPet(player, true);

        if (player.getPostman() != null) {
            player.getPostman().getController().onDelete();
        }
        player.setPostman(null);

        PunishmentService.stopPrisonTask(player, true);
        PunishmentService.stopGatherableTask(player, true);
        PunishmentService.stopGAGTask(player, true);
        PunishmentService.stopPrivateShopTask(player, true);
        PunishmentService.stopGameShopTask(player, true);
        PunishmentService.stopTeleportTask(player, true);
        PunishmentService.stopMoveTask(player, true);

        if (player.isLegionMember()) {
            LegionService.getInstance().onLogout(player);
        }

        QuestEngine.getInstance().onLogOut(new QuestEnv(null, player, 0, 0));

        player.getController().delete();
        player.getCommonData().setOnline(false);
        player.getCommonData().setLastOnline(new Timestamp(System.currentTimeMillis()));
        //TODO long
        player.getCommonData().addOnlineTime((int) player.getOnlineTime() / 60);
        player.setClientConnection(null);

        DAOManager.getDAO(PlayerDAO.class).onlinePlayer(player, false);

        if (GSConfig.ENABLE_CHAT_SERVER) {
            ChatService.onPlayerLogout(player);
        }

        PlayerService.storePlayer(player);

        ExpireTimerTask.getInstance().removePlayer(player);
        if (player.getCraftingTask() != null) {
            player.getCraftingTask().stop(true);
        }
        player.getEquipment().setOwner(null);
        player.getInventory().setOwner(null);
        player.getWarehouse().setOwner(null);
        player.getStorage(StorageType.ACCOUNT_WAREHOUSE.getId()).setOwner(null);
        WeddingService.getInstance().dofinal(player);
        // broadcast player delete beam
        PacketSendUtility.broadcastPacket(player, new SM_DELETE(player, 2), 50);
        // обновление шмоток надетых на персе в окне выбора персонажа
        PlayerAccountData pad = player.getPlayerAccount().getPlayerAccountData(player.getObjectId());
        pad.setEquipment(player.getEquipment().getEquippedItems());
        //player.getPlayerAccount().getListAccount().set(1, pad);
        player.getPlayerAccount().setOnlineTimeChecker();
    }

    /**
     * @param player
     */
    public static void tryLeaveWorld(Player player) {
        player.getMoveController().abortMove();
        if (player.getController().isInShutdownProgress()) {
            PlayerLeaveWorldService.startLeaveWorld(player);
        } // prevent ctrl+alt+del / close window exploit
        else {
            int delay = 15;
            PlayerLeaveWorldService.startLeaveWorldDelay(player, (delay * 1000));
        }
    }
}
