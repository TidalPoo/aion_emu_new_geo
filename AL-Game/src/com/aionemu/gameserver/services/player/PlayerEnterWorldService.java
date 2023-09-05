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
import com.aionemu.commons.database.dao.DAONotFoundException;
import com.aionemu.gameserver.cardinal.CardinalManager;
import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.main.AutoGroupConfig;
import com.aionemu.gameserver.configs.main.CraftConfig;
import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.configs.main.FastTrackConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.configs.main.HTMLConfig;
import com.aionemu.gameserver.configs.main.PeriodicSaveConfig;
import com.aionemu.gameserver.configs.main.SecurityConfig;
import com.aionemu.gameserver.dao.AbyssRankDAO;
import com.aionemu.gameserver.dao.InventoryDAO;
import com.aionemu.gameserver.dao.ItemStoneListDAO;
import com.aionemu.gameserver.dao.MySQL5AionDAO;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dao.PlayerPasskeyDAO;
import com.aionemu.gameserver.dao.PlayerPunishmentsDAO;
import com.aionemu.gameserver.dao.PlayerQuestListDAO;
import com.aionemu.gameserver.dao.PlayerSkillListDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.model.account.CharacterBanInfo;
import com.aionemu.gameserver.model.account.CharacterPasskey.ConnectType;
import com.aionemu.gameserver.model.account.PlayerAccountData;
import com.aionemu.gameserver.model.gameobjects.HouseObject;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.gameobjects.player.emotion.Emotion;
import com.aionemu.gameserver.model.gameobjects.player.motion.Motion;
import com.aionemu.gameserver.model.gameobjects.player.title.Title;
import com.aionemu.gameserver.model.gameobjects.state.CreatureSeeState;
import com.aionemu.gameserver.model.gameobjects.state.CreatureVisualState;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.items.storage.IStorage;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.items.storage.StorageType;
import com.aionemu.gameserver.model.skill.PlayerSkillEntry;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceService;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.model.templates.QuestTemplate;
import com.aionemu.gameserver.model.templates.quest.QuestExtraCategory;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ABYSS_RANK;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CHANNEL_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CHARACTER_SELECT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CUBE_UPDATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ENTER_WORLD_CHECK;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FAST_TRACK;
import com.aionemu.gameserver.network.aion.serverpackets.SM_GAME_TIME;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INVENTORY_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_COOLDOWN;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MACRO_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_SPAWN;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PRICES;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_COMPLETED_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUIT_RESPONSE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_RECIPE_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_COOLDOWN;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TITLE_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_UI_SETTINGS;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.AutoGroupService;
import com.aionemu.gameserver.services.BrokerService;
import com.aionemu.gameserver.services.DisputeLandService;
import com.aionemu.gameserver.services.FastTrackService;
import com.aionemu.gameserver.services.HousingService;
import com.aionemu.gameserver.services.KiskService;
import com.aionemu.gameserver.services.LegionService;
import com.aionemu.gameserver.services.PetitionService;
import com.aionemu.gameserver.services.PunishmentService;
import com.aionemu.gameserver.services.PunishmentService.PunishmentType;
import com.aionemu.gameserver.services.SerialKillerService;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.services.SkillLearnService;
import com.aionemu.gameserver.services.StigmaService;
import com.aionemu.gameserver.services.SurveyService;
import com.aionemu.gameserver.services.VortexService;
import com.aionemu.gameserver.services.abyss.AbyssSkillService;
import com.aionemu.gameserver.services.craft.RelinquishCraftStatus;
import com.aionemu.gameserver.services.event.EventService;
import com.aionemu.gameserver.services.html.HTMLService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.mail.MailService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.services.toypet.PetService;
import com.aionemu.gameserver.services.transfers.PlayerTransferService;
import com.aionemu.gameserver.services.weddings.WeddingService;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.taskmanager.tasks.ExpireTimerTask;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.audit.AuditLogger;
import com.aionemu.gameserver.utils.audit.GMService;
import com.aionemu.gameserver.utils.collections.ListSplitter;
import com.aionemu.gameserver.utils.rates.Rates;
import com.aionemu.gameserver.world.World;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javolution.util.FastList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ATracer
 * @modified Alex
 */
@SuppressWarnings("unused")
public final class PlayerEnterWorldService {

    private static String serverAnnounce;
    private static String serverAnnounce2;
    private static final Logger log = LoggerFactory.getLogger("GAMECONNECTION_LOG");
    private static final Set<Integer> pendingEnterWorld = new HashSet<>();

    static {
        //23.02.2014
        if (GSConfig.SERVER_MOTD_DISPLAYREV) {
            serverAnnounce2 = "Главный администратор и разработчик Sun";
        }
    }

    /**
     * @param objectId
     * @param client
     */
    public static final void startEnterWorld(final int objectId, final AionConnection client) {
        // check if char is banned
        PlayerAccountData playerAccData = client.getAccount().getPlayerAccountData(objectId);
        if (playerAccData == null) {
            log.warn("playerAccData == null " + objectId);
            if (client != null) {
                client.closeNow();
            }
            return;
        }
        if (playerAccData.getPlayerCommonData() == null) {
            log.warn("playerAccData.getPlayerCommonData() == null " + objectId);
            if (client != null) {
                client.closeNow();
            }
            return;
        }
        Timestamp lastOnline = playerAccData.getPlayerCommonData().getLastOnline();
        Player edit = playerAccData.getPlayerCommonData().getPlayer();
        if (lastOnline != null && client.getAccount().getAccessLevel() < AdminConfig.GM_LEVEL && edit != null && !edit.isInEditMode()) {
            if (System.currentTimeMillis() - lastOnline.getTime() < (GSConfig.CHARACTER_REENTRY_TIME * 1000)) {
                client.sendPacket(new SM_ENTER_WORLD_CHECK((byte) 6)); // 20 sec time
                return;
            }
        }
        CharacterBanInfo cbi = client.getAccount().getPlayerAccountData(objectId).getCharBanInfo();
        if (cbi != null) {
            if (cbi.getEnd() > System.currentTimeMillis() / 1000) {
                client.close(new SM_QUIT_RESPONSE(), false);
                return;
            } else {
                DAOManager.getDAO(PlayerPunishmentsDAO.class).unpunishPlayer(objectId, PunishmentType.CHARBAN);
            }
        }

        // passkey check
        if (SecurityConfig.PASSKEY_ENABLE && !client.getAccount().getCharacterPasskey().isPass()) {
            showPasskey(objectId, client);
        } else {
            validateAndEnterWorld(objectId, client);
        }
    }

    /**
     * @param objectId
     * @param client
     */
    private static final void showPasskey(final int objectId, final AionConnection client) {
        client.getAccount().getCharacterPasskey().setConnectType(ConnectType.ENTER);
        client.getAccount().getCharacterPasskey().setObjectId(objectId);
        boolean isExistPasskey = DAOManager.getDAO(PlayerPasskeyDAO.class).existCheckPlayerPasskey(client.getAccount().getId());

        if (!isExistPasskey) {
            client.sendPacket(new SM_CHARACTER_SELECT(0));
        } else {
            client.sendPacket(new SM_CHARACTER_SELECT(1));
        }
    }

    /**
     * @param objectId
     * @param client
     */
    private static final void validateAndEnterWorld(final int objectId, final AionConnection client) {
        synchronized (pendingEnterWorld) {
            if (pendingEnterWorld.contains(objectId)) {
                log.warn("Skipping enter world " + objectId);
                return;
            }
            pendingEnterWorld.add(objectId);
        }
        int delay = 0;
        // double checked enter world
        if (World.getInstance().findPlayer(objectId) != null) {
            delay = 15000;
            log.warn("Postponed enter world " + objectId);
        }
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Player player = World.getInstance().findPlayer(objectId);
                    if (player != null) {
                        AuditLogger.info(player, "Duplicate player in world");
                        client.close(new SM_QUIT_RESPONSE(), false);
                        return;
                    }

                    enterWorld(client, objectId);
                } catch (Throwable ex) {
                    log.error("Error during enter world " + objectId, ex);
                } finally {
                    synchronized (pendingEnterWorld) {
                        pendingEnterWorld.remove(objectId);
                    }
                }
            }

        }, delay);
    }

    /**
     * @param client
     * @param objectId
     */
    public static final void enterWorld(AionConnection client, int objectId) {
        Account account = client.getAccount();
        PlayerAccountData playerAccData = client.getAccount().getPlayerAccountData(objectId);

        if (playerAccData == null) {
            // Somebody wanted to login on character that is not at his account
            return;
        }
        Player player = PlayerService.getPlayer(objectId, account);

        if (player != null && client.setActivePlayer(player)) {
            player.setClientConnection(client);

            log.info("[MAC_AUDIT] Player " + player.getName() + " (account " + account.getName() + ") has entered world with "
                    + client.getMacAddress() + " MAC.");
            log.info("[HDD_AUDIT] Player " + player.getName() + " (account " + account.getName() + ") has entered world with "
                    + client.getHddSerial() + " HDD.");
            World.getInstance().storeObject(player);

            StigmaService.onPlayerLogin(player);

            /**
             * Energy of Repose must be calculated before sending SM_STATS_INFO
             */
            if (playerAccData.getPlayerCommonData().getLastOnline() != null) {
                long lastOnline = playerAccData.getPlayerCommonData().getLastOnline().getTime();
                PlayerCommonData pcd = player.getCommonData();
                long secondsOffline = (System.currentTimeMillis() / 1000) - lastOnline / 1000;
                if (pcd.isReadyForSalvationPoints()) {
                    // 10 mins offline = 0 salvation points.
                    if (secondsOffline > 10 * 60) {
                        player.getCommonData().resetSalvationPoints();
                    }
                }
                if (pcd.isReadyForReposteEnergy()) {
                    pcd.updateMaxReposte();
                    // more than 4 hours offline = start counting Reposte Energy addition.
                    if (secondsOffline > 4 * 3600) {
                        double hours = secondsOffline / 3600d;
                        long maxRespose = player.getCommonData().getMaxReposteEnergy();
                        if (hours > 24) {
                            hours = 24;
                        }
                        // 24 hours offline = 100% Reposte Energy
                        long addResposeEnergy = (long) ((hours / 24) * maxRespose);

                        // Additional Energy of Repose bonus
                        if (player.getHouseOwnerId() / 10000 * 10000 == player.getWorldId()) {
                            switch (player.getActiveHouse().getHouseType()) {
                                case STUDIO:
                                    addResposeEnergy *= 1.05f;
                                    break;
                                case MANSION:
                                    addResposeEnergy *= 1.08f;
                                    break;
                                case ESTATE:
                                    addResposeEnergy *= 1.15f;
                                    break;
                                case PALACE:
                                    addResposeEnergy *= 1.50f;
                                    break;
                                default:
                                    addResposeEnergy *= 1.10f;
                                    break;
                            }
                        }
                        //Add max energy, if player a developer or a GM or a Premium Account
                        boolean ThePlayerHasThePrivilege = (player.isDeveloper() /*|| player.getMembership() > 0*/ || player.isGM());
                        pcd.addReposteEnergy(addResposeEnergy > maxRespose || ThePlayerHasThePrivilege ? maxRespose : addResposeEnergy);
                    }
                }

                if (((System.currentTimeMillis() / 1000) - lastOnline) > 300) {
                    player.getCommonData().setDp(0);
                }
            }
            long lastOnlineL = playerAccData.getPlayerCommonData().getLastOnline().getTime();

            CardinalManager.checkSkills(player, lastOnlineL);
            InstanceService.onPlayerLogin(player);

            //client.sendPacket(new SM_UNK_3_5_1());
            //new
            client.sendPacket(new SM_FAST_TRACK());
            SkillLearnService.addMissingSkills(player);
            if (!player.getSkillList().isSkillPresent(3512)) {
                player.getSkillList().addSkill(player, 3512, 129);
            }
            if (!player.getSkillList().isSkillPresent(40009) && player.getLevel() > 10) {
                player.getSkillList().addSkill(player, 40009, 1);
            }
            // Update player skills first!!!
            AbyssSkillService.onEnterWorld(player);
            // TODO: check the split size
            client.sendPacket(new SM_SKILL_LIST(player, player.getSkillList().getBasicSkills(), false));
            for (PlayerSkillEntry stigmaSkill : player.getSkillList().getStigmaSkills()) {
                client.sendPacket(new SM_SKILL_LIST(player, stigmaSkill, false));
            }

            if (player.getSkillCoolDowns() != null) {
                client.sendPacket(new SM_SKILL_COOLDOWN(player.getSkillCoolDowns(), false));
            }

            if (player.getItemCoolDowns() != null) {
                client.sendPacket(new SM_ITEM_COOLDOWN(player.getItemCoolDowns()));
            }
            player.initializeLevels();
            // New Weddings
            WeddingService.getInstance().onEnterWorld(player);
            player.setNewName(null);
            player.setLegName(null);
            player.setPlayerSearch(null);
            FastList<QuestState> questList = FastList.newInstance();
            FastList<QuestState> completeQuestList = FastList.newInstance();
            for (QuestState qs : player.getQuestStateList().getAllQuestState()) {
                QuestTemplate questTemplate = DataManager.QUEST_DATA.getQuestById(qs.getQuestId());
                if (questTemplate.getExtraCategory() != QuestExtraCategory.NONE) {
                    continue;
                }
                if (qs.getStatus() == QuestStatus.NONE && qs.getCompleteCount() == 0) {
                    continue;
                }
                if (qs.getStatus() != QuestStatus.COMPLETE && qs.getStatus() != QuestStatus.NONE) {
                    questList.add(qs);
                }
                if (qs.getCompleteCount() > 0) {
                    completeQuestList.add(qs);
                }
            }
            client.sendPacket(new SM_QUEST_COMPLETED_LIST(completeQuestList, false));
            client.sendPacket(new SM_QUEST_LIST(questList, false));
            client.sendPacket(new SM_TITLE_INFO(player.getCommonData().getTitleId(), false));
            client.sendPacket(new SM_TITLE_INFO(6, player.getCommonData().getBonusTitleId(), false));
            client.sendPacket(new SM_MOTION(player.getMotions().getMotions().values()));
            client.sendPacket(new SM_ENTER_WORLD_CHECK());

            byte[] uiSettings = player.getPlayerSettings().getUiSettings();
            byte[] shortcuts = player.getPlayerSettings().getShortcuts();
            byte[] houseBuddies = player.getPlayerSettings().getHouseBuddies();

            if (uiSettings != null) {
                client.sendPacket(new SM_UI_SETTINGS(uiSettings, 0));
            }

            if (shortcuts != null) {
                client.sendPacket(new SM_UI_SETTINGS(shortcuts, 1));
            }

            if (houseBuddies != null) {
                client.sendPacket(new SM_UI_SETTINGS(houseBuddies, 2));
            }

            sendItemInfos(client, player);
            if (FastTrackConfig.FASTTRACK_ENABLE) {
                FastTrackService.getInstance().checkAuthorizationRequest(player);
            }
            playerLoggedIn(player);

            client.sendPacket(new SM_INSTANCE_INFO(player, false, player.getCurrentTeam()));

            client.sendPacket(new SM_CHANNEL_INFO(player.getPosition()));

            KiskService.getInstance().onLogin(player);
            TeleportService2.sendSetBindPoint(player);

            // SM_WEATHER miss on login (but he 'live' in CM_LEVEL_READY.. need invistigate)
            client.sendPacket(new SM_GAME_TIME());

            SerialKillerService.getInstance().onLogin(player);

            if (player.isLegionMember()) {
                LegionService.getInstance().onLogin(player);
            }

            client.sendPacket(new SM_TITLE_INFO(player, false));
            client.sendPacket(new SM_EMOTION_LIST((byte) 0, player.getEmotions().getEmotions()));

            // SM_INFLUENCE_RATIO, SM_SIEGE_LOCATION_INFO, SM_RIFT_ANNOUNCE (Balaurea), SM_RIFT_ANNOUNCE (Tiamaranta)
            SiegeService.getInstance().onPlayerLogin(player);

            client.sendPacket(new SM_PRICES());
            DisputeLandService.getInstance().onLogin(player);
            client.sendPacket(new SM_ABYSS_RANK(player.getAbyssRank(), false));

            player.setRates(Rates.getRatesFor(player.getMembership()));
            PacketSendUtility.sendBrightYellowMessage(player, serverAnnounce);
            player.enterWorld();
            if (player.isGM()) {
                PacketSendUtility.sendBrightYellowMessage(player, serverAnnounce2);
                if (AdminConfig.INVULNERABLE_GM_CONNECTION || AdminConfig.INVISIBLE_GM_CONNECTION
                        || AdminConfig.ENEMITY_MODE_GM_CONNECTION.equalsIgnoreCase("Neutral")
                        || AdminConfig.ENEMITY_MODE_GM_CONNECTION.equalsIgnoreCase("Enemy") || AdminConfig.VISION_GM_CONNECTION
                        || AdminConfig.WHISPER_GM_CONNECTION) {
                    PacketSendUtility.sendMessage(player, "=============================");
                    if (AdminConfig.INVULNERABLE_GM_CONNECTION) {
                        player.setInvul(true);
                        PacketSendUtility.sendMessage(player, ">> Режим неуязвимости : ВКЛ <<");
                    }
                    if (AdminConfig.INVISIBLE_GM_CONNECTION) {
                        player.getEffectController().setAbnormal(AbnormalState.HIDE.getId());
                        player.setVisualState(CreatureVisualState.HIDE20);
                        PacketSendUtility.broadcastPacket(player, new SM_PLAYER_STATE(player), true);
                        PacketSendUtility.sendMessage(player, ">> Режим невидимости : ВКЛ <<");
                    }
                    if (AdminConfig.ENEMITY_MODE_GM_CONNECTION.equalsIgnoreCase("Neutral")) {
                        player.setAdminNeutral(3);
                        player.setAdminEnmity(0);
                        PacketSendUtility.sendMessage(player, ">> Нейтральный режим : ВКЛ <<");
                    }
                    if (AdminConfig.ENEMITY_MODE_GM_CONNECTION.equalsIgnoreCase("Enemy")) {
                        player.setAdminNeutral(0);
                        player.setAdminEnmity(3);
                        PacketSendUtility.sendMessage(player, ">> Режим <Все враги> : ВКЛ <<");
                    }
                    if (AdminConfig.VISION_GM_CONNECTION) {
                        player.setSeeState(CreatureSeeState.SEARCH10);
                        PacketSendUtility.broadcastPacket(player, new SM_PLAYER_STATE(player), true);
                        PacketSendUtility.sendMessage(player, ">> Вы видите невидимые цели <<");
                    }
                    if (AdminConfig.WHISPER_GM_CONNECTION) {
                        player.setUnWispable();
                        PacketSendUtility.sendMessage(player, ">> Личные сообщения : ВЫКЛ <<");
                    }
                    PacketSendUtility.sendMessage(player, "=============================");
                }
            }
            // Alliance Packet after SetBindPoint
            PlayerAllianceService.onPlayerLogin(player);

            if (player.isInPrison()) {
                PunishmentService.updatePrisonStatus(player);
            }

            if (player.isGagged()) {
                PunishmentService.updateGAGStatus(player);
            }

            if (player.isBannedPStore()) {
                PunishmentService.updatePShopStatus(player);
            }

            if (player.isBannedGameShop()) {
                PunishmentService.updateGShopStatus(player);
            }
            if (player.isMoveBanned()) {
                PunishmentService.updateMoveStatus(player);
            }

            if (player.isNotGatherable()) {
                PunishmentService.updateGatherableStatus(player);
            }

            PlayerGroupService.onPlayerLogin(player);
            PetService.getInstance().onPlayerLogin(player);

            // ----------------------------- Retail sequence -----------------------------
            MailService.getInstance().onPlayerLogin(player);
            HousingService.getInstance().onPlayerLogin(player);
            BrokerService.getInstance().onPlayerLogin(player);
            client.sendPacket(new SM_MACRO_LIST(player));// new fixed macroList, this NO delete!
            client.sendPacket(new SM_RECIPE_LIST(player.getRecipeList().getRecipeList()));
            // ----------------------------- Retail sequence -----------------------------

            PetitionService.getInstance().onPlayerLogin(player);
            if (AutoGroupConfig.AUTO_GROUP_ENABLE) {
                AutoGroupService.getInstance().onPlayerLogin(player);
            }

            //Homeward Bound Skill fix
            if (player.getActiveHouse() != null) {
                if (player.getSkillList().getSkillEntry(2670) == null
                        && player.getRace() == Race.ELYOS) {
                    player.getSkillList().addSkill(player, 2670, 1);
                } else if (player.getSkillList().getSkillEntry(2671) == null
                        && player.getRace() == Race.ASMODIANS) {
                    player.getSkillList().addSkill(player, 2671, 1);
                }
            }
            GMService.getInstance().onPlayerLogin(player);
            /**
             * Trigger restore services on login.
             */
            // set full dp if you GM and if enable config parameter
            if (AdminConfig.ENABLE_GM_DP && player.isGM()) {
                player.getCommonData().setDp(player.getGameStats().getMaxDp().getBase());
            }
            player.getLifeStats().updateCurrentStats();
            player.getNpcFactions().sendDailyQuest();
            for (StorageType st : StorageType.values()) {
                if (st == StorageType.LEGION_WAREHOUSE) {
                    continue;
                }
                IStorage storage = player.getStorage(st.getId());
                if (storage != null) {
                    for (Item item : storage.getItemsWithKinah()) {
                        if (item.getExpireTime() > 0) {
                            ExpireTimerTask.getInstance().addTask(item, player);
                        }
                        if (item.getSealTime() != null) {
                            if (item.getSeal() == 1 && item.getSealTime().getTime() < System.currentTimeMillis()) {
                                item.setSeal(0);
                                item.setSealTime(null);
                                //%0: печать снята.
                                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400808, new DescriptionId(item.getNameId())));
                            }

                            //1400807
                            //%0%: идет снятие печати. До снятия печати осталось: %DURATIONDAY1
                            long day = (item.getSealTime().getTime() - System.currentTimeMillis()) / 1000 / 60 / 60 / 24;
                            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400807, new DescriptionId(item.getNameId()), day));
                        }
                    }
                }
            }

            for (Item item : player.getEquipment().getEquippedItems()) {
                if (item.getExpireTime() > 0) {
                    ExpireTimerTask.getInstance().addTask(item, player);
                }
                if (item.getSealTime() != null) {
                    if (item.getSeal() == 1 && item.getSealTime().getTime() < System.currentTimeMillis()) {
                        item.setSeal(0);
                        item.setSealTime(null);
                        //%0: печать снята.
                        PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400808, new DescriptionId(item.getNameId())));
                    }

                    //1400807
                    //%0%: идет снятие печати. До снятия печати осталось: %DURATIONDAY1
                    long day = (item.getSealTime().getTime() - System.currentTimeMillis()) / 1000 / 60 / 60 / 24;
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400807, new DescriptionId(item.getNameId()), day));
                }
            }

            player.getEquipment().checkRankLimitItems(); // Remove items after offline changed rank

            for (Motion motion : player.getMotions().getMotions().values()) {
                if (motion.getExpireTime() != 0) {
                    ExpireTimerTask.getInstance().addTask(motion, player);
                }
            }

            for (Emotion emotion : player.getEmotions().getEmotions()) {
                if (emotion.getExpireTime() != 0) {
                    ExpireTimerTask.getInstance().addTask(emotion, player);
                }
            }

            for (Title title : player.getTitleList().getTitles()) {
                if (title.getExpireTime() != 0) {
                    ExpireTimerTask.getInstance().addTask(title, player);
                }
            }

            if (player.getHouseRegistry() != null) {
                for (HouseObject<?> obj : player.getHouseRegistry().getObjects()) {
                    if (obj.getPersistentState() == PersistentState.DELETED) {
                        continue;
                    }
                    if (obj.getObjectTemplate().getUseDays() > 0) {
                        ExpireTimerTask.getInstance().addTask(obj, player);
                    }
                }
            }
            // scheduler periodic update
            player.getController().addTask(TaskId.PLAYER_UPDATE, ThreadPoolManager.getInstance().scheduleAtFixedRate(new GeneralUpdateTask(player.getObjectId()),
                    PeriodicSaveConfig.PLAYER_GENERAL * 1000, PeriodicSaveConfig.PLAYER_GENERAL * 1000));
            player.getController().addTask(TaskId.INVENTORY_UPDATE, ThreadPoolManager.getInstance().scheduleAtFixedRate(new ItemUpdateTask(player.getObjectId()),
                    PeriodicSaveConfig.PLAYER_ITEMS * 1000, PeriodicSaveConfig.PLAYER_ITEMS * 1000));
            SurveyService.getInstance().showAvailable(player);

            if (EventsConfig.ENABLE_EVENT_SERVICE) {
                EventService.getInstance().onPlayerLogin(player);
            }

            if (CraftConfig.DELETE_EXCESS_CRAFT_ENABLE) {
                RelinquishCraftStatus.removeExcessCraftStatus(player, false);
            }

            PlayerTransferService.getInstance().onEnterWorld(player);

            if (HTMLConfig.ENABLE_GUIDES) {
                HTMLService.onPlayerLogin(player);
            }
            MySQL5AionDAO.getInstance().loadAccuese(player);
            // Without player spawn initialization can't access to his mapRegion for chk below
            World.getInstance().preSpawn(player);
            player.getController().validateLoginZone();
            VortexService.getInstance().validateLoginZone(player);
            client.sendPacket(new SM_PLAYER_SPAWN(player));
        } else {
            log.info("[DEBUG] enter world" + objectId + ", Player: " + player);
        }
    }

    /**
     * @param client
     * @param player
     */
    // TODO! this method code is really odd [Nemesiss]
    private static void sendItemInfos(AionConnection client, Player player) {
        // Cubesize limit set in inventory.
        int questExpands = player.getQuestExpands();
        int npcExpands = player.getNpcExpands();
        player.setCubeLimit();
        player.setWarehouseLimit();
        // items
        Storage inventory = player.getInventory();
        List<Item> allItems = new ArrayList<>();
        if (inventory.getKinah() == 0) {
            inventory.increaseKinah(0); // create an empty object with value 0
        }
        allItems.add(inventory.getKinahItem()); // always included even with 0 count, and first in the packet !
        allItems.addAll(player.getEquipment().getEquippedItems());
        allItems.addAll(inventory.getItems());

        ListSplitter<Item> splitter = new ListSplitter<>(allItems, 10);
        while (!splitter.isLast()) {
            client.sendPacket(new SM_INVENTORY_INFO(splitter.isFirst(), splitter.getNext(), npcExpands, questExpands, player, false, false));
        }

        client.sendPacket(new SM_INVENTORY_INFO(false, new ArrayList<>(0), npcExpands, questExpands, player, false, false));
        client.sendPacket(new SM_STATS_INFO(player));
        client.sendPacket(SM_CUBE_UPDATE.stigmaSlots(player.getCommonData().getAdvancedStigmaSlotSize(), false));
    }

    /**
     * @param player
     */
    private static void playerLoggedIn(Player player) {
        log.info("Player logged in: " + player.getName() + " Account: " + player.getClientConnection().getAccount().getName());
        player.setBonusTime(player.getCommonData().getBonusTime());
        player.setBonusTimeStatus();
        player.getCommonData().setOnline(true);
        DAOManager.getDAO(PlayerDAO.class).onlinePlayer(player, true);
        player.onLoggedIn();
        player.setOnlineTime();
    }
}

class GeneralUpdateTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(GeneralUpdateTask.class);
    private final int playerId;

    GeneralUpdateTask(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public void run() {
        Player player = World.getInstance().findPlayer(playerId);
        if (player != null) {
            try {
                DAOManager.getDAO(AbyssRankDAO.class).storeAbyssRank(player);
                DAOManager.getDAO(PlayerSkillListDAO.class).storeSkills(player);
                DAOManager.getDAO(PlayerQuestListDAO.class).store(player);
                DAOManager.getDAO(PlayerDAO.class).storePlayer(player);
                for (House house : player.getHouses()) {
                    house.save();
                }
            } catch (DAONotFoundException ex) {
                log.error("Exception during periodic saving of player " + player.getName(), ex);
            }
        }

    }

}

class ItemUpdateTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ItemUpdateTask.class);
    private final int playerId;

    ItemUpdateTask(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public void run() {
        Player player = World.getInstance().findPlayer(playerId);
        if (player != null) {
            try {
                DAOManager.getDAO(InventoryDAO.class).store(player);
                DAOManager.getDAO(ItemStoneListDAO.class).save(player);
            } catch (DAONotFoundException ex) {
                log.error("Exception during periodic saving of player items " + player.getName(), ex);
            }
        }
    }
}
