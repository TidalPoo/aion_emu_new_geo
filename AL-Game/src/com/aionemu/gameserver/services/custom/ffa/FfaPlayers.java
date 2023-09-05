/*
 * SAO Project
 */
package com.aionemu.gameserver.services.custom.ffa;

import com.aionemu.commons.services.CronService;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_COOLDOWN;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TRANSFORM;
import com.aionemu.gameserver.services.AccessLevelEnum;
import com.aionemu.gameserver.services.custom.NewSpawn;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.skillengine.model.SkillTargetSlot;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.skillengine.model.TransformType;
import com.aionemu.gameserver.spawnengine.StaticDoorSpawnManager;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.InstanceType;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMap;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldMapInstanceFactory;
import com.aionemu.gameserver.world.WorldMapType;
import com.aionemu.gameserver.world.knownlist.Visitor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alex
 */
public final class FfaPlayers {

    /*private static final WorldMapType PADMARASHKA_CAVE = WorldMapType.PADMARASHKA_CAVE;// marissa
     private static final WorldMapType DREDGION = WorldMapType.DREDGION;
     private static final WorldMapType TIAMAT_STRONGHOLD = WorldMapType.TIAMAT_STRONGHOLD;//fort tiamat
     private static final WorldMapType BESHMUNDIR_TEMPLE = WorldMapType.BESHMUNDIR_TEMPLE;//phasumandir
     private static final WorldMapType ELEMENTIS_FOREST = WorldMapType.ELEMENTIS_FOREST;//ratis
     private static final WorldMapType IDLDF5_Under_01 = WorldMapType.IDLDF5_Under_01;// yourmoongand*/
    private static final WorldMapType[] allMaps = {WorldMapType.IDLDF5_Under_01, WorldMapType.ELEMENTIS_FOREST, WorldMapType.BESHMUNDIR_TEMPLE, WorldMapType.TIAMAT_STRONGHOLD, WorldMapType.DREDGION, WorldMapType.PADMARASHKA_CAVE};
    private static final int instanceId = 90;
    private static boolean openFfaZone = true;
    private static long timer = 0;
    private static final Logger log = LoggerFactory.getLogger(FfaPlayers.class);
    private static WorldMapType location = null;
    private static final Map<String, FfaKillMap> players = new HashMap<>();
    private static boolean vote = false;

    public static String getTimeToRandomChangeLocation() {
        long time1 = timer + 7200000;
        long time2 = System.currentTimeMillis();
        return DurationFormatUtils.formatDuration(time1 - time2, "H ч m мин");
    }

    public static float[] rndLocationGenerate() {
        float[] rnd = null;
        switch (getLocation()) {
            case PADMARASHKA_CAVE:
                rnd = PADMARASHKA_CAVE[Rnd.get(0, PADMARASHKA_CAVE.length - 1)];
                break;
            case DREDGION:
                rnd = DREDGION[Rnd.get(0, DREDGION.length - 1)];
                break;
            case TIAMAT_STRONGHOLD:
                rnd = TIAMAT_STRONGHOLD[Rnd.get(0, TIAMAT_STRONGHOLD.length - 1)];
                break;
            case BESHMUNDIR_TEMPLE:
                rnd = BESHMUNDIR_TEMPLE[Rnd.get(0, BESHMUNDIR_TEMPLE.length - 1)];
                break;
            case ELEMENTIS_FOREST:
                rnd = ELEMENTIS_FOREST[Rnd.get(0, ELEMENTIS_FOREST.length - 1)];
                break;
            case IDLDF5_Under_01:
                rnd = IDLDF5_Under_01[Rnd.get(0, IDLDF5_Under_01.length - 1)];
                break;
        }
        return rnd;
    }

    public static void rndTpAnimation(Player player) {
        if (player.isInFfa()) {
            PacketSendUtility.sendMessage(player, "Телепортация в новую ффа локацию...");
        }
        float[] rnd = rndLocationGenerate();
        if (rnd != null) {
            TeleportService2.teleportTo(player, getFFAInstanceMap().getId(), getFFAInstanceId(), rnd[0], rnd[1], rnd[2], (byte) 0, TeleportAnimation.BEAM_ANIMATION);
        }
    }

    public static void rndTp(Player player) {
        float[] rnd = rndLocationGenerate();
        if (rnd != null) {
            TeleportService2.teleportTo(player, getFFAInstanceMap().getId(), getFFAInstanceId(), rnd[0], rnd[1], rnd[2]);
        }
    }

    private static void generateLocIfNo(WorldMapType loc) {

        List<WorldMapType> list = new ArrayList<>();
        for (WorldMapType w : allMaps) {
            if (w == loc) {
                continue;
            }
            list.add(w);
        }
        setLocation(Rnd.get(list));
        ffalocation(getLocation());
        list = null;
        //if (list != null) {
        //log.info("Is New FFA Location set " + getLocation().name());
        //}
    }

    public static void cron() {
        final int minute = CustomConfig.FFA_LOCATION_CRON_TIME;
        CronService.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (vote) {
                    return;
                }
                final WorldMapInstance endLocation = getFfaInstance();
                generateLocIfNo(getLocation());
                timer = System.currentTimeMillis();
                startCangeLoc(endLocation, getLocation());
                log.info("CronService: new generate ffa location " + minute + " min.");
            }
        }, "0 0/" + minute + " * * * ?");
    }

    private static void startCangeLoc(WorldMapInstance endLocation, WorldMapType location) {
        StringBuilder sb = new StringBuilder("---Статистика---\n");
        for (Map.Entry<String, FfaKillMap> s : players.entrySet()) {
            sb.append(s.getKey()).append(" убийств ").append(s.getValue().getKills()).append(" | смертей ").append(s.getValue().getDeath()).append("\n");
        }
        if (endLocation != null) {
            for (Player p : endLocation.getPlayersInside()) {
                PacketSendUtility.sendBrightYellowMessageOnCenter(p, "Смена FFA локации на " + getLocation().getRusname() + " произойдет через 30 секунд.");
                PacketSendUtility.sendMessage(p, sb.toString());
            }
        }

        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                Iterator<Player> it = endLocation.getPlayersInside().iterator();
                while (it.hasNext()) {
                    Player player = it.next();
                    rndTpAnimation(player);
                }
                InstanceService.destroyCustomInstance(endLocation);
                if (!players.isEmpty()) {
                    players.clear();
                }
            }
        }, 30 * 1000);
    }
    private static int yes;
    private static int no;
    private static int onlinePlayers;

    public static boolean isVote() {
        return vote;
    }

    public static void setVote(boolean vote) {
        FfaPlayers.vote = vote;
    }

    static void startVote(Player player, Player votePlayer, WorldMapType wmt) {
        final WorldMapInstance endLocation = getFfaInstance();
        RequestResponseHandler responseHandler = new RequestResponseHandler(player) {
            @Override
            public void acceptRequest(Creature p2, Player p) {
                PacketSendUtility.sendMessage(player, "Вы согласились сменить FFA локацию на " + wmt.getRusname());
                for (Player all : World.getInstance().getAllPlayers()) {
                    if (all != player) {
                        PacketSendUtility.sendMessage(all, "Игрок " + player.getName() + " соглашается сменить FFA локацию на " + wmt.getRusname());
                    }
                }
                yes++;
                int votes = (yes + no);
                if (votes == onlinePlayers && vote) {
                    vote = false;
                    setNewLocation(endLocation, wmt);
                    no = 0;
                    yes = 0;
                }
            }

            @Override
            public void denyRequest(Creature p2, Player p) {
                no++;
                int votes = (yes + no);
                PacketSendUtility.sendMessage(player, "Вы отказались сменить FFA локацию на " + wmt.getRusname());
                for (Player all : World.getInstance().getAllPlayers()) {
                    if (all != player) {
                        PacketSendUtility.sendMessage(all, "Игрок " + player.getName() + " отказывается сменить FFA локацию на " + wmt.getRusname());
                    }
                }
                if (votes == onlinePlayers && vote) {
                    vote = false;
                    no = 0;
                    yes = 0;
                }
            }
        };
        boolean requested = player.getResponseRequester().putRequest(902247, responseHandler);
        if (requested) {
            PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(902247, 0, 0, "Игрок " + votePlayer.getName() + " раса " + player.getRace() + " открыл голосование за смену ффа локации на " + wmt.getRusname() + " - Сейчас установлена: " + FfaPlayers.getLocation().getRusname() + ". Вы согласны?"));
        }
    }

    public static void startVote(Player player, WorldMapType loc) {
        onlinePlayers = World.getInstance().getAllPlayers().size();
        yes++;
        vote = true;
        for (Player p : World.getInstance().getAllPlayers()) {
            if (p != player) {
                startVote(p, player, loc);
            }
        }
    }

    public static void setNewLocation(WorldMapInstance endLocation, WorldMapType wmt) {
        StringBuilder sb = new StringBuilder("---Статистика---\n");
        for (Map.Entry<String, FfaKillMap> s : players.entrySet()) {
            sb.append(s.getKey()).append(" убийств ").append(s.getValue().getKills()).append(" | смертей ").append(s.getValue().getDeath());
        }

        for (Player p : endLocation.getPlayersInside()) {
            PacketSendUtility.sendBrightYellowMessageOnCenter(p, "Смена FFA локации на " + getLocation().getRusname() + " произойдет через 30 секунд.");
            PacketSendUtility.sendMessage(p, sb.toString());
        }

        if (wmt != null) {
            setLocation(wmt);
            ffalocation(getLocation());
        }

        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                Iterator<Player> it = endLocation.getPlayersInside().iterator();
                while (it.hasNext()) {
                    Player player = it.next();
                    rndTpAnimation(player);
                }
                InstanceService.destroyCustomInstance(endLocation);
                if (!players.isEmpty()) {
                    players.clear();
                }
            }
        }, 30 * 1000);
    }

    public static WorldMapType getLocation() {
        return location;
    }

    public static void setLocation(WorldMapType wmt) {
        location = wmt;
    }

    public synchronized static WorldMapInstance getInstance() {
        NewSpawn.ffaPortal(true);
        setLocation(Rnd.get(allMaps));
        timer = System.currentTimeMillis();
        //log.info("Initialize Random Generate FFA Location set " + getLocation().name());
        cron();
        WorldMapType loc = getLocation();
        WorldMap map = World.getInstance().getWorldMap(loc.getId());
        //log.info("Creating new FFAinstance:" + loc.name() + " id:" + instanceId);
        WorldMapInstance worldMapInstance = WorldMapInstanceFactory.createWorldMapInstance(map, instanceId, 0, InstanceType.FFA_PLAYER);
        map.addInstance(instanceId, worldMapInstance);
        FfaKillController.getInstance();
        StaticDoorSpawnManager.spawnTemplate(getLocation().getId(), instanceId);
        for (Map.Entry<Integer, StaticDoor> door : worldMapInstance.getDoors().entrySet()) {
            if (door != null) {
                door.getValue().setOpen(true);
            }
        }
        return worldMapInstance;
    }

    private static void ffalocation(WorldMapType loc) {
        WorldMap map = World.getInstance().getWorldMap(loc.getId());
        log.info("Creating new FFAinstance:" + loc.name() + " id:" + instanceId);
        WorldMapInstance worldMapInstance = WorldMapInstanceFactory.createWorldMapInstance(map, instanceId, 0, InstanceType.FFA_PLAYER);
        map.addInstance(instanceId, worldMapInstance);
        StaticDoorSpawnManager.spawnTemplate(loc.getId(), instanceId);
        for (Map.Entry<Integer, StaticDoor> door : worldMapInstance.getDoors().entrySet()) {
            if (door != null) {
                door.getValue().setOpen(true);
            }
        }
    }

    public static WorldMapInstance getFfaInstance() {
        return World.getInstance().getWorldMap(location.getId()).getWorldMapInstanceById(instanceId);
    }

    public static boolean isInFFA(Player player) {
        return player.getPosition().getWorldMapInstance().isFfaPlayerInstance();
    }

    public static void heal(Player player) {
        player.getLifeStats().increaseHp(SM_ATTACK_STATUS.TYPE.HP, player.getLifeStats().getMaxHp() + 1);
        player.getLifeStats().increaseMp(SM_ATTACK_STATUS.TYPE.MP, player.getLifeStats().getMaxMp() + 1);
    }

    private static void onStopMove(final Player player) {
        int seconds = 8;
        SkillEngine.getInstance().applyEffectDirectly(18191, player, player, 8000);
        player.getEffectController().setAbnormal(AbnormalState.CANNOT_MOVE.getId());
        if (!player.isProtectionActive()) {
            player.getController().startProtectionActiveTask();
        }
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (player.isProtectionActive()) {
                    player.getController().stopProtectionActiveTask();
                }
                player.getEffectController().unsetAbnormal(AbnormalState.CANNOT_MOVE.getId());
                player.getEffectController().updatePlayerEffectIcons();
            }
        }, seconds * 1000);//5 секунд
        player.getEffectController().updatePlayerEffectIcons();
    }

    public static void removeCooldowns(Player p) {
        List<Integer> delayIds = new ArrayList<>();
        if (p.getSkillCoolDowns() != null) {
            long currentTime = System.currentTimeMillis();
            for (Map.Entry<Integer, Long> en : p.getSkillCoolDowns().entrySet()) {
                delayIds.add(en.getKey());
            }

            for (int i = 0; i < delayIds.size(); i++) {
                int current = delayIds.get(i);
                ArrayList<Integer> skillIds = DataManager.SKILL_DATA.getSkillsForCooldownId(current);
                for (int id : skillIds) {
                    SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(id);
                    if (template.isDeityAvatar()) {
                        delayIds.remove(i);
                        break;
                    }
                }
            }

            for (Integer delayId : delayIds) {
                p.setSkillCoolDown(delayId, currentTime);
            }

            delayIds.clear();
            PacketSendUtility.sendPacket(p, new SM_SKILL_COOLDOWN(p.getSkillCoolDowns(), false));
        }
    }

    public static void onKill(Player killer, Player victim) {
        if (killer.isInFfa()) {
            increase(killer, victim);
            /*Iterator<Player> iter = getFfaInstance().getPlayersInside().iterator();
             while (iter.hasNext()) {
             Player player = iter.next();
             PacketSendUtility.sendMessage(player, killer.getName() + " убивает " + victim.getName() + " серийный убийца убивал " + players.get(killer.getName()).getKills() + " персонажей");
             }*/
        }
    }

    private static void increase(Player killer, Player victim) {
        if (players.containsKey(killer.getName())) {
            players.get(killer.getName()).increaseKills();
        } else {
            players.put(killer.getName(), new FfaKillMap(1, 0));
        }
        if (players.containsKey(victim.getName())) {
            players.get(victim.getName()).increaseDeath();
        } else {
            players.put(victim.getName(), new FfaKillMap(0, 1));
        }
        FfaKillList ffakill = FfaKillController.getInstance().getKillsForFfa(killer.getObjectId());
        FfaKillList ffakill2 = FfaKillController.getInstance().getKillsForFfa(victim.getObjectId());
        FfaDieList ffadie = FfaKillController.getInstance().getDieForFfa(victim.getObjectId());
        ffakill.addKillFor(victim.getName(), victim.getObjectId(), victim.getWorldId(), victim.getInstanceId());
        ffadie.addDieFor(killer.getName(), killer.getObjectId(), killer.getWorldId(), killer.getInstanceId());
        if (ffakill.getSuperKill() > 1) {
            PacketSendUtility.sendBrightYellowMessageOnCenter(killer, "[color:PROFE;128 0 128][color:SSION;128 0 128][color:AL X;128 0 128][color:" + ffakill.getSuperKill() + ";128 0 128]");
        }
        ffakill2.setSuperKill(0);
        /*for (KillList i : ffakill.getKillMap(victim.getObjectId()).getList()) {
         PacketSendUtility.sendMessage(killer, i.getUnk() + " " + (new Timestamp(i.getTime())) + " " + i.getWorldId() + " " + i.getInstanceId());
         }*/

        /*PacketSendUtility.sendMessage(killer, "===========|STATISTIC|==========");
         PacketSendUtility.sendMessage(killer, "You kills " + victim.getName() + " " + ffakill.getKillsFor(victim.getObjectId()) + " times");
         PacketSendUtility.sendMessage(killer, "================================");

         PacketSendUtility.sendMessage(victim, "===========|STATISTIC|==========");
         PacketSendUtility.sendMessage(victim, "You death " + killer.getName() + " " + ffadie.getDieFor(killer.getObjectId()) + " times");
         PacketSendUtility.sendMessage(victim, "================================");*/
    }

    private static void remove(Player player) {
        if (players.containsKey(player.getName())) {
            players.remove(player.getName());
        }
    }

    public static void onLogOut(Player player) {
        if (player.isInFfa()) {
            remove(player);
            TeleportService2.teleportToCapital(player);
        }
    }

    public static void onRevive(Player player) {
        if (!openFfaZone) {
            TeleportService2.teleportToCapital(player);
            remove(player);
            return;
        } else {
            rndTp(player);
        }
        player.getKnownList().doUpdate();
        player.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.DEBUFF);
        onStopMove(player);
    }

    public static void onEnterFFA(Player player) {
        if (isInFFA(player)) {
            player.setffa(true);
            player.getKnownList().clear();
            player.setNewName(player.getPlayerClass().getRusname());
            player.setLegName(AccessLevelEnum.ffaZone.getStatusName());
            player.getTransformModel().setModelId(getMorph(player));
            player.getTransformModel().setTransformType(TransformType.PC);
            PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
            PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, true));
            PacketSendUtility.broadcastPacketAndReceive(player, new SM_TRANSFORM(player, true));
            players.put(player.getName(), new FfaKillMap(0, 0));
            player.getKnownList().doUpdate();
            broadcastMessage(player, "Новый игрок входит в локацию. Да прольется кровь!", true);
            onStopMove(player);
        }
    }

    public static void onLeaveFFA(Player player) {
        if (player.isInFfa()) {
            player.setffa(false);
            player.setLegName(null);
            player.setNewName(null);
            player.getTransformModel().setModelId(0);
            PacketSendUtility.broadcastPacketAndReceive(player, new SM_TRANSFORM(player, true));
            //PacketSendUtility.broadcastPacketAndReceive(player, new SM_PLAYER_INFO(player, false));
            remove(player);
            broadcastMessage(player, "Игрок покидает локацию", false);

            FfaKillList kill = FfaKillController.getInstance().getKillsForFfa(player.getObjectId());
            if (kill.getAllKills() != 0) {
                PacketSendUtility.sendMessage(player, "<<<<<<KILL-LIST>>>>>>");
                for (Map.Entry<Integer, KillMap> kills : kill.getKillList().entrySet()) {
                    PacketSendUtility.sendMessage(player, "" + kills.getValue().getVictimName() + " " + kills.getValue().getKills());
                }
                PacketSendUtility.sendMessage(player, "         " + kill.getAllKills() + " size");
                PacketSendUtility.sendMessage(player, "----END KILL LIST----");
                kill.getKillList().clear();
                kill.setAllKills(0);
            }
            FfaDieList dies = FfaKillController.getInstance().getDieForFfa(player.getObjectId());
            if (dies.getAllDie() != 0) {
                PacketSendUtility.sendMessage(player, "<<<<<<DIE-LIST>>>>>>");
                for (Map.Entry<Integer, DieMap> die : dies.getDieList().entrySet()) {
                    PacketSendUtility.sendMessage(player, "" + die.getValue().getWinnerName() + " " + die.getValue().getDie());
                }
                PacketSendUtility.sendMessage(player, "         " + dies.getAllDie() + " size");
                PacketSendUtility.sendMessage(player, "----END DIE LIST----");
                dies.getDieList().clear();
                dies.setAllDie(0);
            }
        }
    }

    private static void broadcastMessage(Player killer, final String message, final boolean onCenter) {
        killer.getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player player) {
                if (killer != player) {
                    if (onCenter) {
                        PacketSendUtility.sendYellowMessageOnCenter(player, message);
                    } else {
                        PacketSendUtility.sendYellowMessage(player, message);
                    }
                }
            }
        });
    }

    public static void sendFFAStatus(String msg) {
        Iterator<Player> iter = World.getInstance().getPlayersIterator();
        while (iter.hasNext()) {
            Player player = iter.next();
            PacketSendUtility.sendBrightYellowMessageOnCenter(player, "[Ивент]: " + msg);
        }
    }

    private static final float[][] PADMARASHKA_CAVE = {{384f, 508f, 65f},
    {335f, 425f, 75f},
    {458f, 348f, 76f},
    {565f, 303f, 66f},
    {584f, 153f, 66f},
    {627f, 213f, 66f},
    {464f, 499f, 72f},
    {460f, 396f, 66f},
    {505f, 194f, 66f}};

    private static final float[][] DREDGION = {{412.19812f, 191.06874f, 431.2945f},
    {484.4189f, 307.51736f, 402.85144f},
    {555.176f, 196.05086f, 429.9018f},
    {652.36163f, 410.95947f, 412.23618f},
    {559.0787f, 471.64407f, 392.88226f},
    {558.97296f, 558.107f, 410.02615f},
    {569.4888f, 704.7233f, 402.22354f},
    {485.40942f, 827.30884f, 416.64697f},
    {484.77786f, 708.3705f, 387.38483f},
    {464.9114f, 584.20447f, 391.57822f},
    {485.095f, 411.23007f, 400.7358f},
    {310.78824f, 408.7082f, 412.1614f}};

    private static final float[] TIAMAT_STRONGHOLD[] = {{1582.4923f, 1068.5033f, 492.00992f},
    {1391.2054f, 1052.2179f, 491.3452f},
    {1244.2417f, 1068.5916f, 491.5408f},
    {1127.0762f, 1111.4333f, 497.3561f},
    {1114.5254f, 814.65295f, 495.04944f},
    {1044.5234f, 1022.0155f, 497.37585f},
    {750.1753f, 1067.9358f, 501.21805f},
    {840.1238f, 1047.2184f, 501.379f},
    {909.0546f, 1068.8324f, 497.75644f},
    {994.184f, 1318.4213f, 496.01215f}};

    private static final float[] BESHMUNDIR_TEMPLE[] = {{1122.7251f, 925.4847f, 283.49307f},
    {1062.4698f, 1059.7125f, 284.49088f},
    {1314.8486f, 1116.1454f, 283.49277f},
    {1315.0166f, 923.35846f, 283.49274f},
    {1231.6425f, 1006.03937f, 282.1778f}};

    private static final float[] ELEMENTIS_FOREST[] = {{142.2538f, 652.55664f, 238.88956f},
    {213.60968f, 515.70215f, 223.87022f},
    {352.39f, 623.424f, 211.53288f},
    {305.8411f, 722.0504f, 202.74686f},
    {306.24503f, 810.3228f, 210.36516f},
    {394.8509f, 884.05365f, 193.69347f},
    {525.89777f, 823.1109f, 136.10583f},
    {407.1539f, 651.22375f, 136.49695f},
    {429.1743f, 411.21585f, 178.66338f},
    {484.9439f, 495.52869f, 141.58832f},
    {594.0975f, 437.67493f, 122.125f},
    {735.6696f, 533.74585f, 134.82893f},
    {708.7106f, 745.5206f, 146.49377f},
    {635.55096f, 844.71063f, 144.64032f},
    {560.719f, 785.1516f, 141.68716f},
    {481.6927f, 695.5724f, 143.28314f}};

    private static final float[] IDLDF5_Under_01[] = {{686.27826f, 469.40836f, 599.75f},
    {614.21204f, 552.0039f, 590.625f},
    {576.6815f, 469.01898f, 620.001f},
    {486.05267f, 529.2116f, 597.375f},
    {617.2722f, 517.908f, 591.7947f},
    {661.5733f, 520.7454f, 594.261f},
    {633.98035f, 429.55423f, 606.752f},
    {437.32892f, 496.28687f, 604.887f}};

    public static int getMorph(Player player) {
        int[] FFA_MORPH_ELY = {202514, 202515, 202516, 202517, 202518, 202529, 202531, 202537, 202539, 202541, 202543, 202547, 202549, 202551, 202585};
        int[] FFA_MORPH_ASMO = {202519, 202520, 202521, 202522, 202523, 202530, 202532, 202538, 202540, 202542, 202544, 202548, 202550, 202552, 202586};
        return (player.getRace() == Race.ASMODIANS ? FFA_MORPH_ASMO[Rnd.get(FFA_MORPH_ASMO.length)] : FFA_MORPH_ELY[Rnd.get(FFA_MORPH_ELY.length)]);
    }

    /**
     * @return the ffaInstanceMap
     */
    public static WorldMapType getFFAInstanceMap() {
        return location;
    }

    public static int getFFAInstanceId() {
        return instanceId;
    }

    public static boolean isAvailable() {
        return openFfaZone;
    }

    public static WorldMapInstance getFfaPlayer() {
        return World.getInstance().getWorldMap(getFFAInstanceMap().getId()).getWorldMapInstanceById(getFFAInstanceId());
    }
}
