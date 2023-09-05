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

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.DuelResult;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.model.summons.SummonMode;
import com.aionemu.gameserver.model.summons.UnsummonType;
import com.aionemu.gameserver.model.templates.zone.ZoneType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DUEL;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.summons.SummonsService;
import com.aionemu.gameserver.skillengine.model.SkillTargetSlot;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMapType;
import java.util.Iterator;
import java.util.concurrent.Future;
import javolution.util.FastMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Simple, Sphinx, xTz
 * @modified Alex
 */
public class DuelService {

    private static Logger log = LoggerFactory.getLogger(DuelService.class);
    private FastMap<Integer, Integer> duels;
    private FastMap<Integer, Future<?>> drawTasks;

    public static final DuelService getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * @param duels
     */
    private DuelService() {
        this.duels = new FastMap<Integer, Integer>().shared();
        this.drawTasks = new FastMap<Integer, Future<?>>().shared();
        log.info("DuelService started.");
    }

    /**
     * Send the duel request to the owner
     *
     * @param requester the player who requested the duel
     * @param responder the player who respond to duel request
     */
    public void onDuelRequest(Player requester, Player responder) {
        RequestResponseHandler rrh = new RequestResponseHandler(requester) {

            @Override
            public void denyRequest(Creature requester, Player responder) {
                rejectDuelRequest((Player) requester, responder);
            }

            @Override
            public void acceptRequest(Creature requester, Player responder) {
                Player player = (Player) requester;
                startDuel(player, responder);
                if (CustomConfig.ENABLE_START_DUEL_HEAL_PLAYERS) {
                    // requester
                    player.getLifeStats().increaseHp(SM_ATTACK_STATUS.TYPE.HP, player.getLifeStats().getMaxHp() + 1);
                    player.getLifeStats().increaseMp(SM_ATTACK_STATUS.TYPE.MP, player.getLifeStats().getMaxMp() + 1);
                    player.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.SPEC2);
                    PacketSendUtility.sendWhiteMessageOnCenter(player, "Your health and magic restored !");
                    // responder
                    responder.getLifeStats().increaseHp(SM_ATTACK_STATUS.TYPE.HP, responder.getLifeStats().getMaxHp() + 1);
                    responder.getLifeStats().increaseMp(SM_ATTACK_STATUS.TYPE.MP, responder.getLifeStats().getMaxMp() + 1);
                    responder.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.SPEC2);
                    PacketSendUtility.sendWhiteMessageOnCenter(responder, "Your health and magic restored !");

                }
            }
        };
        responder.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_DUEL_DO_YOU_ACCEPT_REQUEST, rrh);
        PacketSendUtility.sendPacket(responder, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_DUEL_DO_YOU_ACCEPT_REQUEST, 0, 0, requester.getName()));
        PacketSendUtility.sendPacket(responder, SM_SYSTEM_MESSAGE.STR_DUEL_REQUESTED(requester.getName()));
        PacketSendUtility.sendPacket(requester, SM_SYSTEM_MESSAGE.STR_DUEL_REQUEST_TO_PARTNER(responder.getName()));
    }

    /**
     * Asks confirmation for the duel request
     *
     * @param requester the player whose the duel was requested
     * @param responder the player whose the duel was responded
     */
    public void confirmDuelWith(Player requester, Player responder) {
        /**
         * Check if requester isn't already in a duel and responder is same race
         */
        /*if (requester.isEnemy(responder)) {
         return;
         }*/

        /**
         * Check if requester isn't already in a duel and responder is same race
         */
        if (requester.getWorldId() != WorldMapType.TIAMARANTA.getId() && requester.getWorldId() != WorldMapType.SARPAN.getId()
                && (requester.isInsideZoneType(ZoneType.PVP) || responder.isInsideZoneType(ZoneType.PVP))) {
            PacketSendUtility.sendPacket(requester, SM_SYSTEM_MESSAGE.STR_DUEL_PARTNER_INVALID(responder.getName()));
            return;
        }
        if (isDueling(requester.getObjectId()) || isDueling(responder.getObjectId())) {
            PacketSendUtility.sendPacket(requester, SM_SYSTEM_MESSAGE.STR_DUEL_HE_REJECT_DUEL(responder.getName()));
            return;
        }
        /*for (ZoneInstance zone : responder.getPosition().getMapRegion().getZones((Creature) responder)) {
         if (!zone.isOtherRaceDuelsAllowed() && !responder.getRace().equals(requester.getRace())
         || (!zone.isSameRaceDuelsAllowed() && responder.getRace().equals(requester.getRace()))) {
         PacketSendUtility.sendPacket(requester, SM_SYSTEM_MESSAGE.STR_MSG_DUEL_CANT_IN_THIS_ZONE);
         return;
         }
         }*/

        RequestResponseHandler rrh = new RequestResponseHandler(responder) {

            @Override
            public void denyRequest(Creature requester, Player responder) {
                onDuelRequest(responder, (Player) requester);
            }

            @Override
            public void acceptRequest(Creature requester, Player responder) {
                cancelDuelRequest(responder, (Player) requester);
            }
        };
        requester.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_DUEL_DO_YOU_WITHDRAW_REQUEST, rrh);
        PacketSendUtility.sendPacket(requester, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_DUEL_DO_YOU_WITHDRAW_REQUEST, 0, 0, responder.getName()));
    }

    /**
     * Rejects the duel request
     *
     * @param requester the duel requester
     * @param responder the duel responder
     */
    private void rejectDuelRequest(Player requester, Player responder) {
        log.debug("[Duel] Player " + responder.getName() + " rejected duel request from " + requester.getName());
        PacketSendUtility.sendPacket(requester, SM_SYSTEM_MESSAGE.STR_DUEL_HE_REJECT_DUEL(responder.getName()));
        PacketSendUtility.sendPacket(responder, SM_SYSTEM_MESSAGE.STR_DUEL_REJECT_DUEL(requester.getName()));
    }

    /**
     * Cancels the duel request
     *
     * @param target the duel target
     * @param requester
     */
    private void cancelDuelRequest(Player owner, Player target) {
        // log.debug("[Duel] Player " + owner.getName() + " cancelled his duel request with " + target.getName());
        PacketSendUtility.sendPacket(target, SM_SYSTEM_MESSAGE.STR_DUEL_REQUESTER_WITHDRAW_REQUEST(owner.getName()));
        PacketSendUtility.sendPacket(owner, SM_SYSTEM_MESSAGE.STR_DUEL_WITHDRAW_REQUEST(target.getName()));
    }

    /**
     * Starts the duel
     *
     * @param requester the player to start duel with
     * @param responder the other player
     */
    public void startDuel(Player requester, Player responder) {
        if (CustomConfig.ENABLE_DUEL_MESSAGE) {
            Iterator<Player> onlinePlayers = World.getInstance().getPlayersIterator();
            while (onlinePlayers.hasNext()) {
                Player all = onlinePlayers.next();
                if (all.getWorldId() == requester.getWorldId() && all.getRace() == requester.getRace()) {
                    PacketSendUtility.sendMessage(all, "\u0414\u0443\u044d\u043b\u044c: " + requester.getName() + " \u043f\u0440\u043e\u0442\u0438\u0432 " + responder.getName());
                }
            }
        }
        PacketSendUtility.sendPacket(requester, SM_DUEL.SM_DUEL_STARTED(responder.getObjectId()));
        PacketSendUtility.sendPacket(responder, SM_DUEL.SM_DUEL_STARTED(requester.getObjectId()));
        createDuel(requester.getObjectId(), responder.getObjectId());
        createTask(requester, responder);
    }

    /**
     * This method will make the selected player lose the duel
     *
     * @param player
     */
    public void loseDuel(Player player) {
        if (!isDueling(player.getObjectId())) {
            return;
        }
        int opponnentId = duels.get(player.getObjectId());

        // player.getAggroList().clear();
        Player opponent = World.getInstance().findPlayer(opponnentId);

        if (opponent != null) {
            /**
             * all debuffs are removed from winner, but buffs will remain Stop
             * casting or skill use
             */
            opponent.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.DEBUFF);
            opponent.getController().cancelCurrentSkill();
            // opponent.getAggroList().clear();

            /**
             * cancel attacking winner by summon
             */
            if (player.getSummon() != null) {
                //if (player.getSummon().getTarget().isTargeting(opponnentId))
                SummonsService.doMode(SummonMode.GUARD, player.getSummon(), UnsummonType.UNSPECIFIED);
            }

            /**
             * cancel attacking loser by summon
             */
            if (opponent.getSummon() != null) {
                //if (opponent.getSummon().getTarget().isTargeting(player.getObjectId()))
                SummonsService.doMode(SummonMode.GUARD, opponent.getSummon(), UnsummonType.UNSPECIFIED);
            }

            /**
             * cancel attacking winner by summoned object
             */
            if (player.getSummonedObj() != null) {
                player.getSummonedObj().getController().cancelCurrentSkill();
            }

            /**
             * cancel attacking loser by summoned object
             */
            if (opponent.getSummonedObj() != null) {
                opponent.getSummonedObj().getController().cancelCurrentSkill();
            }

            PacketSendUtility.sendPacket(opponent, SM_DUEL.SM_DUEL_RESULT(DuelResult.DUEL_WON, player.getName()));
            PacketSendUtility.sendPacket(player, SM_DUEL.SM_DUEL_RESULT(DuelResult.DUEL_LOST, opponent.getName()));
            if (CustomConfig.ENABLE_DUEL_MESSAGE) {
                Iterator<Player> onlinePlayers = World.getInstance().getPlayersIterator();
                while (onlinePlayers.hasNext()) {
                    Player all = onlinePlayers.next();
                    if (all.getWorldId() == player.getWorldId() && all.getRace() == player.getRace()) {
                        PacketSendUtility.sendMessage(all, "\u0414\u0443\u044d\u043b\u044c:" + opponent.getName() + " \u043f\u043e\u0431\u0435\u0434\u0438\u043b \u0432 \u043f\u043e\u0435\u0434\u0438\u043d\u043a\u0435 \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0430 " + player.getName());
                    }
                }
            }
            opponent.increaseDuelWin();
            player.increaseDuelLose();
            PacketSendUtility.sendMessage(player, "Господь: Не отчаивайся!");
            PacketSendUtility.sendMessage(opponent, "Дьявол: Продолжай в том же духе!");
        } else {
            log.warn("CHECKPOINT : duel opponent is already out of world");
        }

        removeDuel(player.getObjectId(), opponnentId);
    }

    public void loseArenaDuel(Player player) {
        if (!isDueling(player.getObjectId())) {
            return;
        }

        /**
         * all debuffs are removed from loser Stop casting or skill use
         */
        player.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.DEBUFF);
        player.getController().cancelCurrentSkill();

        int opponnentId = duels.get(player.getObjectId());
        Player opponent = World.getInstance().findPlayer(opponnentId);

        if (opponent != null) {
            /**
             * all debuffs are removed from winner, but buffs will remain Stop
             * casting or skill use
             */
            opponent.getEffectController().removeAbnormalEffectsByTargetSlot(SkillTargetSlot.DEBUFF);
            opponent.getController().cancelCurrentSkill();
        } else {
            log.warn("CHECKPOINT : duel opponent is already out of world");
        }

        removeDuel(player.getObjectId(), opponnentId);
    }

    private void createTask(final Player requester, final Player responder) {
        // Schedule for draw
        Future<?> task = ThreadPoolManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                if (isDueling(requester.getObjectId(), responder.getObjectId())) {
                    PacketSendUtility.sendPacket(requester, SM_DUEL.SM_DUEL_RESULT(DuelResult.DUEL_DRAW, requester.getName()));
                    PacketSendUtility.sendPacket(responder, SM_DUEL.SM_DUEL_RESULT(DuelResult.DUEL_DRAW, responder.getName()));
                    removeDuel(requester.getObjectId(), responder.getObjectId());
                    if (CustomConfig.ENABLE_DUEL_MESSAGE) {
                        Iterator<Player> onlinePlayers = World.getInstance().getPlayersIterator();
                        while (onlinePlayers.hasNext()) {
                            Player all = onlinePlayers.next();
                            if (all.getWorldId() == requester.getWorldId() && all.getRace() == requester.getRace()) {
                                PacketSendUtility.sendMessage(all, "\u0414\u0443\u044d\u043b\u044c: \u041f\u043e\u0435\u0434\u0438\u043d\u043e\u043a " + requester.getName() + " \u043f\u0440\u043e\u0442\u0438\u0432 " + responder.getName() + " \u0437\u0430\u0432\u0435\u0440\u0448\u0435\u043d \u0432 \u043d\u0438\u0447\u044c\u044e ");
                            }
                        }
                    }
                }
            }
        }, 303000); // 5 minutes + 3 seconds battle retail like

        drawTasks.put(requester.getObjectId(), task);
        drawTasks.put(responder.getObjectId(), task);

        //timer duel - 5 minutes + 3 seconds
        PacketSendUtility.sendPacket(requester, new SM_QUEST_ACTION(4, 303));
        PacketSendUtility.sendPacket(responder, new SM_QUEST_ACTION(4, 303));
    }

    /**
     * @param playerObjId
     * @return true of player is dueling
     */
    public boolean isDueling(int playerObjId) {
        return (duels.containsKey(playerObjId) && duels.containsValue(playerObjId));
    }

    /**
     * @param playerObjId
     * @param targetObjId
     * @return true of player is dueling
     */
    public boolean isDueling(int playerObjId, int targetObjId) {
        return duels.containsKey(playerObjId) && duels.get(playerObjId) == targetObjId;
    }

    /**
     * @param requesterObjId
     * @param responderObjId
     */
    public void createDuel(int requesterObjId, int responderObjId) {
        duels.put(requesterObjId, responderObjId);
        duels.put(responderObjId, requesterObjId);
    }

    /**
     * @param requesterObjId
     * @param responderObjId
     */
    private void removeDuel(int requesterObjId, int responderObjId) {
        duels.remove(requesterObjId);
        duels.remove(responderObjId);
        removeTask(requesterObjId);
        removeTask(responderObjId);
        //remove timer duel
        Player requester = World.getInstance().findPlayer(requesterObjId);
        Player responder = World.getInstance().findPlayer(responderObjId);
        PacketSendUtility.sendPacket(requester, new SM_QUEST_ACTION(4, (0)));
        PacketSendUtility.sendPacket(responder, new SM_QUEST_ACTION(4, (0)));
    }

    private void removeTask(int playerId) {
        Future<?> task = drawTasks.get(playerId);
        if (task != null && !task.isDone()) {
            task.cancel(true);
            drawTasks.remove(playerId);
        }
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final DuelService instance = new DuelService();
    }

}
