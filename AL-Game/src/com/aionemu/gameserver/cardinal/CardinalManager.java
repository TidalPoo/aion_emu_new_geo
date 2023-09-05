/*
 * SAO Project
 */
package com.aionemu.gameserver.cardinal;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.objects.filter.ObjectFilter;
import com.aionemu.gameserver.configs.administration.DeveloperConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.dao.MySQL5AionDAO;
import com.aionemu.gameserver.dao.RequestFriendListDAO;
import com.aionemu.gameserver.eventengine.events.EventManager;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.LetterType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.Trap;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.Request;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.gameobjects.state.RateTitle;
import com.aionemu.gameserver.model.ingameshop.InGameShopEn;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatRateFunction;
import com.aionemu.gameserver.model.stats.container.CreatureGameStats;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.model.templates.zone.ZoneType;
import com.aionemu.gameserver.network.BannedHDDManager;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DUEL;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ICON_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PETITION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PRIVATE_STORE_NAME;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.ClassChangeService;
import com.aionemu.gameserver.services.MembershipService;
import com.aionemu.gameserver.services.PunishmentService;
import com.aionemu.gameserver.services.abyss.AbyssRankingCache;
import com.aionemu.gameserver.services.custom.MessageEnterWorld;
import com.aionemu.gameserver.services.custom.RealiseConfigService;
import com.aionemu.gameserver.services.html.NewHTMLSettings;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.mail.SystemMailService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.utils.ColorEnum;
import com.aionemu.gameserver.utils.ColorUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.audit.AuditLogger;
import com.aionemu.gameserver.utils.audit.GMService;
import com.aionemu.gameserver.world.World;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Alex
 */
public class CardinalManager {

    public static void onlineBonus(Player player) {
        int itemId = player.getRace() == Race.ASMODIANS ? CustomConfig.ITEM_ELY_ONLINE : CustomConfig.ITEM_ASMO_ONLINE;
        int itemCount = CustomConfig.ITEM_COUNT_ONLINE;
        if (CustomConfig.EXPRESS_BONUS) {
            if (player.getMailbox().size() < 100) {
                SystemMailService.getInstance().sendMail(GSConfig.SERVER_NAME, player.getName(),
                        "Получение бонуса за онлайн!",
                        "Поздравляем вы получили бонус за игру на нашем сервере!Чтобы получить гейм-поинты нажмите \"забрать все\"! Приятной игры " + player.getName() + "!",
                        itemId, itemCount, 0, LetterType.EXPRESS);
            } else {
                PacketSendUtility.sendMessage(player, "Ваш почтовый ящик забит, бонус за онлайн автоматически ложится вам в инвентарь.");
                if (itemId != 0) {
                    ItemService.addItem(player, itemId, itemCount, AddItemType.ONLINEBONUS, null);
                }
                addToll(player);
            }
        } else {
            //PacketSendUtility.sendMessage(player, "Бонус за онлайн! :)");
            if (itemId != 0) {
                ItemService.addItem(player, itemId, itemCount, AddItemType.ONLINEBONUS, null);
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400836, "[item:" + itemId + "]"));
            }
            addToll(player);
        }
        String s = CustomConfig.VOTE;
        if (s != null) {
            PacketSendUtility.sendMessage(player, "Не забудьте проголосовать за сервер " + GSConfig.SERVER_NAME + " на aion.mmotop.ru. Приятной игры " + player.getName() + "!");
        }
    }

    static void addToll(Player player) {
        if (CustomConfig.BONUS_TOLL != 0) {
            InGameShopEn.getInstance().addToll(player, CustomConfig.BONUS_TOLL);
        }
    }

    public static void rankTitleControl(final Player player) {
        int kills = player.getAbyssRank().getAllKill();
        if (kills > 0) {
            RateTitle title = RateTitle.titleByKill(kills);
            if (title.getId() != player.getCommonData().getRankTitle()) {
                player.setNickerText(title.getRusTitle());
                player.getCommonData().setRankTitle(title.getId());
                PacketSendUtility.broadcastPacket(player, new SM_PRIVATE_STORE_NAME(player.getObjectId(), title.getRusTitle()), true,
                        new ObjectFilter<Player>() {
                            @Override
                            public boolean acceptObject(Player object) {
                                return true;
                            }
                        });
                announceOnRankTitle(player);
            }
        }
    }

    public static void announceOnRankTitle(Player player) {
        Iterator<Player> iter = World.getInstance().getPlayersIterator();
        while (iter.hasNext()) {
            Player p = iter.next();
            PacketSendUtility.sendBrightYellowMessageOnCenter(p, player.getName() + " получает звание " + player.getNickerText() + " убийств " + player.getAbyssRank().getAllKill());
        }
    }

    public static void onStopMove(final Player player, int seconds, final boolean protectionActive) {
        if (seconds == 0) {
            seconds = 8;
        }
        SkillEngine.getInstance().applyEffectDirectly(18191, player, player, 8000);
        player.getEffectController().setAbnormal(AbnormalState.CANNOT_MOVE.getId());
        if (protectionActive && !player.isProtectionActive()) {
            player.getController().startProtectionActiveTask();
        }
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (protectionActive && player.isProtectionActive()) {
                    player.getController().stopProtectionActiveTask();
                }
                player.getEffectController().unsetAbnormal(AbnormalState.CANNOT_MOVE.getId());
                player.getEffectController().updatePlayerEffectIcons();
            }
        }, seconds * 1000);//5 секунд
        player.getEffectController().updatePlayerEffectIcons();
    }

    public static void rankTitleControlVictim(Player player) {
        //todo
        /*int die = player.getCommonData().getDie();
         if (die > 0) {
         String title = RateTitle.titleByKill(die);
         player.setNickerText(title);
         }*/
    }

    public static void startUseAnimationTeleport(final Player player, float x, float y, float z) {
    }

    public static void addFlyTimePercent(Player player, int percent) {
        CreatureGameStats<? extends Creature> cgs = player.getGameStats();
        List<IStatFunction> modifiers2 = new ArrayList<>();
        StatOwner bonus = null;
        modifiers2.add(new StatRateFunction(StatEnum.FLY_TIME, percent, true).withConditions(null));
        cgs.addEffect(bonus, modifiers2);
    }

    public static void pvpSerivce(Player winner, Player victim, int killers) {
        if (!victim.getClientConnection().isDev()) {
            String ip1 = winner.getClientConnection().getIP();
            String mac1 = winner.getClientConnection().getMacAddress();
            String hdd1 = winner.getClientConnection().getHddSerial();
            String ip2 = victim.getClientConnection().getIP();
            String mac2 = victim.getClientConnection().getMacAddress();
            String hdd2 = victim.getClientConnection().getHddSerial();
            if (hdd1.equalsIgnoreCase(hdd2)) {
                if (killers < 2) {
                    PacketSendUtility.sendMessage(winner, GSConfig.SERVER_NAME + ": за перелив вы получите пожизненный бан по железу.");
                } else {
                    BannedHDDManager.getInstance().banAddress(hdd1, System.currentTimeMillis() + 60 * 24 * 365 * 10L * 60 * 1000,
                            "author=SAOProject, AP");
                }
            }
            if (mac1 != null && mac2 != null) {
                if (ip1.equalsIgnoreCase(ip2) && mac1.equalsIgnoreCase(mac2)) {
                    AuditLogger.info(winner, "Possible Power Leveling : " + winner.getName() + " with " + victim.getName() + "; same ip=" + ip1 + " and mac=" + mac1 + ".");
                } else if (mac1.equalsIgnoreCase(mac2)) {
                    AuditLogger.info(winner, "Possible Power Leveling : " + winner.getName() + " with " + victim.getName() + "; same mac=" + mac1 + ".");
                }
            }
            //if (!winner.isGM()) {
            //    rankTitleControl(winner);
            //     rankTitleControlVictim(victim);
            // }
        }
        //todo if no ffa
        if (victim.isMarried() && victim.getWedding().isOnline()) {
            Player partner = victim.getWedding().getPartner();
            PacketSendUtility.sendMessage(partner, winner.getName() + " killed your partner!!!");
        }
    }

    public static void targetSelect(Player player, VisibleObject obj) {
        if (obj instanceof Player) {
            Player target = (Player) obj;
            if (player != obj && !player.canSee(target)) {
                AuditLogger.info(player, "Possible radar hacker detected, targeting on invisible Player name: "
                        + target.getName() + " objectId: " + target.getObjectId() + " by");
                if (CustomConfig.ENABLE_AUTO_BAN_RADAR) {
                    PunishmentService.banChar(player.getObjectId(), 30, "Использование радара");
                    PacketSendUtility.sendMessage(target, ColorUtil.convertTextToColor("WARNING", ColorEnum.getRGB("красный")) + " " + player.getName() + " видит вас!");
                }
            }
        } else if (obj instanceof Trap) {
            Trap target = (Trap) obj;
            boolean isSameTeamTrap = false;
            if (target.getMaster() instanceof Player) {
                isSameTeamTrap = ((Player) target.getMaster()).isInSameTeam(player);
            }
            if (player != obj && !player.canSee(target) && !isSameTeamTrap) {
                AuditLogger.info(player, "Possible radar hacker detected, targeting on invisible Trap name: "
                        + target.getName() + " objectId: " + target.getObjectId() + " by");
                if (CustomConfig.ENABLE_AUTO_BAN_RADAR) {
                    PunishmentService.banChar(player.getObjectId(), 30, "Использование радара");
                }
            }

        } else if (obj instanceof Creature) {
            Creature target = (Creature) obj;
            if (player != obj && !player.canSee(target)) {
                AuditLogger.info(player, "Possible radar hacker detected, targeting on invisible Npc name: "
                        + target.getName() + " objectId: " + target.getObjectId() + " by");
                if (CustomConfig.ENABLE_AUTO_BAN_RADAR) {
                    PunishmentService.banChar(player.getObjectId(), 30, "Использование радара");
                }
            }
        }
    }

    public static void onEnterWorld(final Player player) {
        // new buff 'Privileges Safety'
        applySecurityBuff(player);
        if (player.getCommonData().isPowershard()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_WEAPON_BOOST_BOOST_MODE_STARTED);
            player.setState(CreatureState.POWERSHARD);
            PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_EMOTE2, 0, 0), true);
            PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.POWERSHARD_ON), true);
        }

        // Announce for enable events
        EventManager.getInstance().onEnterWorldAnnounce(player);
        RealiseConfigService.onEnableConfigurationToEnterWorld(player);
        if (player.getBonusTime().getStatus().isBonus()) {
            //904558
            switch (player.getBonusTime().getStatus()) {
                case NEW:
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(904556, "10"));
                    break;
                case COMEBACK:
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(904558, "10"));
                    break;
            }
        }
        ClassChangeService.showClassChangeDialog(player);
        // Display information about elapsed Privileges
        MembershipService.getMembershipExpireTo(player);
        // New HTML
        NewHTMLSettings.onHtml(player);
        player.setEnterWorldCount(player.getEnterWorldCount() + 1);
        MessageEnterWorld.sendMessage(player);
        //if (!player.isGM() && !player.isDeveloper()) {
        //    ps(player);
        // }
        sendMessage(player);
        /*if (DeveloperConfig.ENABLE_GM_PVP) {
         player.setAdminTeleportation(true);
         }*/
        int list = player.getRequestFriendList().getSize();
        if (list > 0) {
            //Пришло заявок на добавление в друзья: %0 шт. Ответить на них можно в окне "Друзья" во вкладке "Запросы на добавление в друзья".
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401672, list));
        }

        if (player.getRequestSenderList().getSize() > 0) {
            for (Request sender : player.getRequestSenderList()) {
                if (sender.getRequestTime() < System.currentTimeMillis() / 1000) {
                    //%0: закончилось время запроса на дружбу (7 дн.).
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401499, sender.getName()));
                    player.getRequestSenderList().delFriend(sender.getOid());
                    if (sender.isOnline()) {
                        sender.getPlayer().getRequestFriendList().delRequest(player.getObjectId());
                    }
                    DAOManager.getDAO(RequestFriendListDAO.class).delRequests(sender.getOid(), player.getObjectId());
                }
            }
        }

        if (DeveloperConfig.ENABLE_GM_PVP) {
            boolean check = false;
            if (!player.getPlayerClass().isStartingClass()) {
                if (player.getMembership() == 2) {
                    for (int i = 0; i < SkillsEnterWorld.skillVip.length; i++) {
                        if (player.getSkillList().getSkillEntry(SkillsEnterWorld.skillVip[i]) == null) {
                            check = true;
                            player.getSkillList().addSkill(player, SkillsEnterWorld.skillVip[i], SkillsEnterWorld.skillVipLevel[i]);
                        }
                    }
                    if (check) {
                        for (int skillId : SkillsEnterWorld.skillPlayer) {
                            player.getSkillList().removeSkill(skillId);
                        }
                    }
                } else {
                    for (int i = 0; i < SkillsEnterWorld.skillPlayer.length; i++) {
                        if (player.getSkillList().getSkillEntry(SkillsEnterWorld.skillPlayer[i]) == null) {
                            check = true;
                            player.getSkillList().addSkill(player, SkillsEnterWorld.skillPlayer[i], SkillsEnterWorld.skillPlayerLevel[i]);
                        }
                    }
                    if (check) {
                        for (int skillId : SkillsEnterWorld.skillVip) {
                            player.getSkillList().removeSkill(skillId);
                        }
                    }
                }
            }
        }
        String legName = player.getRace() == Race.ELYOS ? AbyssRankingCache.getInstance().getElyLeg() : AbyssRankingCache.getInstance().getAsmoLeg();
        if (legName != null) {
            PacketSendUtility.sendMessage(player, "Обьявление: Лидирующий легион: " + ColorUtil.convertText(legName, "0 255 0"));
        }
    }

    public static void ps(Player player) {
        //new
        int titleId = player.getCommonData().getRankTitle();
        if (titleId == 0 && (player.isGM() || player.isDeveloper())) {
            titleId = 20;
        }
        RateTitle title = RateTitle.getForId(titleId);
        player.setNickerText(title.getRusTitle());
        PacketSendUtility.broadcastPacket(player, new SM_PRIVATE_STORE_NAME(player.getObjectId(), title.getRusTitle()), true, (Player object) -> true);
    }

    public static void sendMessage(final Player player) {
        ThreadPoolManager.getInstance().schedule(() -> {
            String SAOProject = "[color:SAO;0 128 0] [color:Proje;0 128 0][color:ct;0 128 0]";
            PacketSendUtility.sendPacket(player, new SM_PETITION(2));
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300564, SAOProject, "Здравствуйте " + player.getName() + ", перед вами чат гейм-мастера. Для того чтобы добавится к этому чату оставьте любое сообщение и ожидайте ответа гейм-мастера"));
            int gms = GMService.getInstance().getGMs().size();
            if (!GMService.getInstance().getGMs().isEmpty()) {
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300564, SAOProject, "Сейчас в онлайне " + gms + " гм."));
            } else {
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300564, SAOProject, "Нет гмов онлайн!"));
            }
            /*if (!player.isGM()) {
             PacketSendUtility.sendPacket(player, new SM_PETITION(0));
             }*/
            PacketSendUtility.sendBrightYellowMessageOnCenter(player,
                    "Обьявление: [color:Добро;0 255 0] [color:пожал;0 255 0][color:овать;0 255 0] " + ColorUtil.convertText(player.getName(), "255 0 0"));
        }, 8 * 1000);
    }

    public static void applySecurityBuff(Player player) {
        if (player.havePermission((byte) 1) || player.isGM() || player.isDeveloper()) {
            PacketSendUtility.sendPacket(player, new SM_ICON_INFO(2, true));
            addFlyTimePercent(player, 30);
            int fp = (int) (30 / 100f * player.getGameStats().getFlyTime().getBase());
            player.getLifeStats().setCurrentFp((player.getLifeStats().getCurrentFp() + fp));
            //SkillEngine.getInstance().applyEffectDirectly(3233, player, player, 0);
            PacketSendUtility.sendMessage(player, "\"Привилегии безопасности\" - Время полета +30%");
        }
    }

    public static void targetInfo(Player player, VisibleObject obj) {
        String info;
        if (obj instanceof Player && ((Player) obj).isGM()) {
            Player target = (Player) obj;
            info = "PlayerObjectId: " + target.getObjectId() + " PlayerName: " + target.getName();
            if (!player.equals(obj)) {
                PacketSendUtility.sendMessage(player, info);
                PacketSendUtility.sendMessage((Player) obj, player.getName() + " навел на вас таргет");
            }
        } else if (obj instanceof Npc && player.isGM()) {
            Npc target = (Npc) obj;
            info = "NpcId: " + target.getNpcId() + " | AI: " + target.getAi2().getName() + " | HP: " + target.getGameStats().getMaxHp().getCurrent() + "/" + target.getLifeStats().getCurrentHp();
            PacketSendUtility.sendMessage(player, info);
        }
    }

    public static void onAttackToTarget(Player player, Player target) {
        if (!player.isInsideZoneType(ZoneType.FLY)) {
            return;
        }

        RequestResponseHandler responseHandler = new RequestResponseHandler(player) {
            @Override
            public void acceptRequest(Creature p2, Player p) {
                if (player.getAttacker() != null) {
                    PacketSendUtility.sendYellowMessageOnCenter(player, "Вы уже начали атаку персонажа " + player.getAttacker().getName());
                    return;
                }
                PacketSendUtility.sendYellowMessageOnCenter(target, "ГОТОВЬТЕСЬ К АТАКЕ!!!");
                player.setAttacker(target);
                target.setAttacker(player);
                PacketSendUtility.sendPacket(player, SM_DUEL.SM_DUEL_STARTED(target.getObjectId()));
                PacketSendUtility.sendPacket(target, SM_DUEL.SM_DUEL_STARTED(player.getObjectId()));
            }

            @Override
            public void denyRequest(Creature p2, Player p) {
                PacketSendUtility.sendYellowMessageOnCenter(player, "Ну лаааадна :(((");
            }
        };
        boolean requested = player.getResponseRequester().putRequest(902247, responseHandler);
        if (requested) {
            PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(902247, 0, 0, "Желаете атаковать игрока " + target.getName() + " ? Вы не получите очков бездны за атаку персонажа своей расы."));
        }
    }

    public static void npcSpawnDuplicateController(Object sp) {
        Npc spawn = (Npc) sp;
        int counter = 0;
        for (Npc npc : World.getInstance().getNpcs()) {
            if (spawn.getNpcId() == npc.getNpcId() && npc.getPosition().getX() == spawn.getX() && npc.getPosition().getY() == spawn.getY() && npc.getPosition().getZ() == npc.getZ()) {
                if (counter > 1) {
                    npc.getController().onDelete();
                }
                counter++;
            }
        }
    }

    public static void playerSpawn(Player player) {
        if (player.isTeleport()) {
            player.setTeleport(false);
            /*if (!RestrictionsManager.canTeleport(player)) {
             //warning
             }*/
        } else {
            if (player.isEnterWorld()) {
                player.enterWorld();
                onEnterWorld(player);
            } else {
                player.setLogged(true);
                for (Player gm : GMService.getInstance().getGMs()) {
                    PacketSendUtility.sendMessage(gm, GSConfig.SERVER_NAME + ": Подозрение " + player.getName() + " в авто-телепортации в локацию. Требуется проверка. Временное логирование установлено.");
                }
            }
        }
    }

    public static void saveConnection(int accountId, String macAddress, String hdd_serial, String ipv4List, String ip) {
        if (ip.equals("127.0.0.1")) {
            return;
        }
        MySQL5AionDAO save = MySQL5AionDAO.getInstance();
        save.insertIP(accountId, ip);
        save.insertMac(accountId, macAddress);
        save.insertHdd(accountId, hdd_serial);
        save.insertTraceroute(accountId, ipv4List);
    }

    public static void checkSkills(Player player, long lastOnlineL) {
        if (player.getEffectController().hasAbnormalEffect(11885) && ((System.currentTimeMillis() - lastOnlineL) / 1000) > 300) {
            player.getEffectController().removeEffect(11885);
            player.getEffectController().removeEffect(11912);
        }

        if (player.getEffectController().hasAbnormalEffect(11886) && ((System.currentTimeMillis() - lastOnlineL) / 1000) > 300) {
            player.getEffectController().removeEffect(11886);
            player.getEffectController().removeEffect(11913);
        }

        if (player.getEffectController().hasAbnormalEffect(11887) && ((System.currentTimeMillis() - lastOnlineL) / 1000) > 300) {
            player.getEffectController().removeEffect(11887);
            player.getEffectController().removeEffect(11914);
        }

        if (player.getEffectController().hasAbnormalEffect(11888) && ((System.currentTimeMillis() - lastOnlineL) / 1000) > 300) {
            player.getEffectController().removeEffect(11888);
            player.getEffectController().removeEffect(11915);
        }

        if (player.getEffectController().hasAbnormalEffect(11889) && ((System.currentTimeMillis() - lastOnlineL) / 1000) > 300) {
            player.getEffectController().removeEffect(11889);
            player.getEffectController().removeEffect(11916);
        }

        if (player.getEffectController().hasAbnormalEffect(11890) && ((System.currentTimeMillis() - lastOnlineL) / 1000) > 300) {
            player.getEffectController().removeEffect(11890);
        }

        if (player.getEffectController().hasAbnormalEffect(11890) && ((System.currentTimeMillis() - lastOnlineL) / 1000) > 300) {
            player.getEffectController().removeEffect(11890);
            player.getEffectController().removeEffect(11907);
        }

        if (player.getEffectController().hasAbnormalEffect(11891) && ((System.currentTimeMillis() - lastOnlineL) / 1000) > 300) {
            player.getEffectController().removeEffect(11891);
            player.getEffectController().removeEffect(11908);
        }

        if (player.getEffectController().hasAbnormalEffect(11892) && ((System.currentTimeMillis() - lastOnlineL) / 1000) > 300) {
            player.getEffectController().removeEffect(11892);
            player.getEffectController().removeEffect(11909);
        }

        if (player.getEffectController().hasAbnormalEffect(11893) && ((System.currentTimeMillis() - lastOnlineL) / 1000) > 300) {
            player.getEffectController().removeEffect(11893);
            player.getEffectController().removeEffect(11910);
        }

        if (player.getEffectController().hasAbnormalEffect(11894) && ((System.currentTimeMillis() - lastOnlineL) / 1000) > 300) {
            player.getEffectController().removeEffect(11894);
            player.getEffectController().removeEffect(11911);
        }
    }
}
