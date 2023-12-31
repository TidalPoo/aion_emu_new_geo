/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.questEngine.handlers.template;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Altaress, Bobobear
 */
public class ItemOrders extends QuestHandler {

    private static final Logger log = LoggerFactory.getLogger(ItemOrders.class);

    private int startItemId;
    private final int talkNpc1;
    private final int talkNpc2;
    private final int endNpcId;

    public ItemOrders(int questId, int talkNpc1, int talkNpc2, int endNpcId) {
        super(questId);
        this.talkNpc1 = talkNpc1;
        this.talkNpc2 = talkNpc2;
        this.endNpcId = endNpcId;
    }

    @Override
    protected void onWorkItemsLoaded() {
        if (workItems == null) {
            log.warn("Q{} is not ItemOrders quest.", questId);
            return;
        }
        if (workItems.size() > 1) {
            log.warn("Q{} has more than 1 work item.", questId);
        }
        this.startItemId = workItems.get(0).getItemId();
    }

    @Override
    public void register() {
        qe.registerQuestNpc(endNpcId).addOnTalkEvent(questId);
        qe.registerQuestItem(startItemId, questId);
        if (talkNpc1 != 0) {
            qe.registerQuestNpc(talkNpc1).addOnTalkEvent(questId);
        }
        if (talkNpc2 != 0) {
            qe.registerQuestNpc(talkNpc2).addOnTalkEvent(questId);
        }
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);

        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        }
        if (targetId == 0) {
            if (env.getDialogId() == 1002) {
                QuestService.startQuest(env);
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
                return true;
            }
        } else if ((targetId == talkNpc1 && talkNpc1 != 0) || (targetId == talkNpc2 && talkNpc2 != 0)) {
            if (qs != null) {
                if (env.getDialog() == DialogAction.QUEST_SELECT) {
                    return sendQuestDialog(env, 1352);
                } else if (env.getDialog() == DialogAction.SETPRO1) {
                    qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                    updateQuestStatus(env);
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (targetId == endNpcId) {
            if (qs != null) {
                if (env.getDialog() == DialogAction.QUEST_SELECT && qs.getStatus() == QuestStatus.START) {
                    return sendQuestDialog(env, 2375);
                } else if (env.getDialogId() == 1009 && qs.getStatus() != QuestStatus.COMPLETE
                        && qs.getStatus() != QuestStatus.NONE) {
                    removeQuestItem(env, startItemId, 1, QuestStatus.COMPLETE);
                    qs.setQuestVar(1);
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                    return sendQuestEndDialog(env);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }

    @Override
    public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
        final Player player = env.getPlayer();
        final int id = item.getItemTemplate().getTemplateId();
        final int itemObjId = item.getObjectId();

        if (id != startItemId) {
            return HandlerResult.UNKNOWN;
        }
        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0,
                0), true);
        ThreadPoolManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0,
                        1, 0), true);
                sendQuestDialog(env, 4);
            }
        }, 3000);
        return HandlerResult.SUCCESS;
    }

    @Override
    public HashSet<Integer> getNpcIds() {
        if (constantSpawns == null) {
            constantSpawns = new HashSet<>();
            if (talkNpc1 != 0) {
                constantSpawns.add(talkNpc1);
            }
            if (talkNpc2 != 0) {
                constantSpawns.add(talkNpc2);
            }
            constantSpawns.add(endNpcId);
        }
        return constantSpawns;
    }

}
