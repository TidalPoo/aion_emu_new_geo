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
package com.aionemu.gameserver.model.templates.quest;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.questEngine.QuestEngine;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MrPoke
 */
public class QuestNpc {

    private static final Logger log = LoggerFactory.getLogger(QuestNpc.class);

    private final HashSet<Integer> onQuestStart;
    private final List<Integer> onKillEvent;
    private final List<Integer> onTalkEvent;
    private final List<Integer> onAttackEvent;
    private final List<Integer> onAddAggroListEvent;
    private final List<Integer> onAtDistanceEvent;
    private final int npcId;
    private final int questRange;
    private HashSet<Integer> allQuestIds;
    private boolean wasSpawned;

    public QuestNpc(int npcId, int questRange) {
        this.npcId = npcId;
        this.questRange = questRange;
        onQuestStart = new HashSet<>(0);
        onKillEvent = new ArrayList<>(0);
        onTalkEvent = new ArrayList<>(0);
        onAttackEvent = new ArrayList<>(0);
        onAddAggroListEvent = new ArrayList<>(0);
        onAtDistanceEvent = new ArrayList<>(0);
        allQuestIds = new HashSet<>(0);
    }

    public QuestNpc(int npcId) {
        this(npcId, 20);
    }

    private void registerCanAct(int questId, int npcId) {
        NpcTemplate template = DataManager.NPC_DATA.getNpcTemplate(npcId);
        if (template == null) {
            log.warn("[QuestEngine] No such NPC template for " + npcId + " in Q" + questId);
            return;
        }
        allQuestIds.add(questId);
        String aiName = DataManager.NPC_DATA.getNpcTemplate(npcId).getAi();
        if ("quest_use_item".equals(aiName)) {
            QuestEngine.getInstance().registerCanAct(questId, npcId);
        }
    }

    public void addOnQuestStart(int questId) {
        if (!onQuestStart.contains(questId)) {
            onQuestStart.add(questId);
            allQuestIds.add(questId);
        }
    }

    public HashSet<Integer> getOnQuestStart() {
        return onQuestStart;
    }

    public void addOnAttackEvent(int questId) {
        if (!onAttackEvent.contains(questId)) {
            onAttackEvent.add(questId);
            allQuestIds.add(questId);
        }
    }

    public List<Integer> getOnAttackEvent() {
        return onAttackEvent;
    }

    public void addOnKillEvent(int questId) {
        if (!onKillEvent.contains(questId)) {
            onKillEvent.add(questId);
            registerCanAct(questId, npcId);
        }
    }

    public List<Integer> getOnKillEvent() {
        return onKillEvent;
    }

    public void addOnTalkEvent(int questId) {
        if (!onTalkEvent.contains(questId)) {
            onTalkEvent.add(questId);
            registerCanAct(questId, npcId);
        }
    }

    public List<Integer> getOnTalkEvent() {
        return onTalkEvent;
    }

    public void addOnAddAggroListEvent(int questId) {
        if (!onAddAggroListEvent.contains(questId)) {
            onAddAggroListEvent.add(questId);
            registerCanAct(questId, npcId);
        }
    }

    public List<Integer> getOnAddAggroListEvent() {
        return onAddAggroListEvent;
    }

    public void addOnAtDistanceEvent(int questId) {
        if (!onAtDistanceEvent.contains(questId)) {
            onAtDistanceEvent.add(questId);
            registerCanAct(questId, npcId);
        }
    }

    public List<Integer> getOnDistanceEvent() {
        return onAtDistanceEvent;
    }

    public int getNpcId() {
        return npcId;
    }

    /**
     * The method returns quest ids which handlers must be asked for constant
     * spawn requirements. Is cleaned once SpawnEngine spawns them.
     *
     * @return
     */
    public HashSet<Integer> getNotCheckedQuestIds() {
        return allQuestIds;
    }

    public boolean isWasSpawned() {
        return wasSpawned;
    }

    public void setWasSpawned(boolean wasSpawned) {
        if (wasSpawned) {
            this.wasSpawned = wasSpawned;
            allQuestIds.clear();
        }
    }

    public int getQuestRange() {
        return questRange;
    }

}
