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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.QuestTemplate;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.ClassChangeService;
import com.aionemu.gameserver.services.QuestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author KKnD , orz, avol
 */
public class CM_DIALOG_SELECT extends AionClientPacket {

    /**
     * Target object id that client wants to TALK WITH or 0 if wants to unselect
     */
    private int targetObjectId;
    private int dialogId;
    private int extendedRewardIndex;
    @SuppressWarnings("unused")
    private int lastPage;
    private int questId;

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(CM_DIALOG_SELECT.class);

    /**
     * Constructs new instance of <tt>CM_CM_REQUEST_DIALOG </tt> packet
     *
     * @param opcode
     * @param state
     * @param restStates
     */
    public CM_DIALOG_SELECT(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        targetObjectId = readD();// empty
        dialogId = readH(); // total no of choice
        extendedRewardIndex = readH();
        lastPage = readH();
        questId = readD();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        final Player player = getConnection().getActivePlayer();
        if (player == null) {
            return;
        }
        QuestTemplate questTemplate = DataManager.QUEST_DATA.getQuestById(questId);
        QuestEnv env = new QuestEnv(null, player, questId, 0);
        if (player.isTrading()) {
            return;
        }

        if (targetObjectId == 0 || targetObjectId == player.getObjectId()) {
            if (questTemplate != null && !questTemplate.isCannotShare() && (dialogId == DialogAction.QUEST_ACCEPT_1.id() || dialogId == DialogAction.QUEST_ACCEPT_SIMPLE.id())) {
                QuestService.startQuest(env);
                return;
            }
            if (QuestEngine.getInstance().onDialog(new QuestEnv(null, player, questId, dialogId))) {
                return;
            }
            // FIXME client sends unk1=1, targetObjectId=0, dialogId=2 (trader) => we miss some packet to close window
            ClassChangeService.changeClassToSelection(player, dialogId);
            return;
        }

        VisibleObject obj = player.getKnownList().getObject(targetObjectId);
        if (obj == null) {
            return;
        }
        if (obj instanceof Creature) {
            Creature creature = (Creature) obj;
            creature.getController().onDialogSelect(dialogId, player, questId, extendedRewardIndex);
        }
        // log.info("id: "+targetObjectId+" dialogId: " + dialogId +" unk1: " + unk1 + " questId: "+questId);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
