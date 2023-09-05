/*
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 * aion-lightning is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-lightning is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.model.templates.spawns.SpawnSearchResult;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SHOW_NPC_ON_MAP;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Lyahim
 * @modified Alex
 */
public class CM_OBJECT_SEARCH extends AionClientPacket {

    private int npcId;

    /**
     * Constructs new client packet instance.
     *
     * @param opcode
     * @param state
     * @param restStates
     */
    public CM_OBJECT_SEARCH(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);

    }

    /**
     * Nothing to do
     */
    @Override
    protected void readImpl() {
        this.npcId = readD();
    }

    /**
     * Logging
     */
    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        final SpawnSearchResult searchResult = DataManager.SPAWNS_DATA2.getFirstSpawnByNpcId(0, npcId);
        if (searchResult != null) {
            sendPacket(new SM_SHOW_NPC_ON_MAP(npcId, searchResult.getWorldId(), searchResult.getSpot().getX(),
                    searchResult.getSpot().getY(), searchResult.getSpot().getZ()));
            if (player.isGM()) {
                PacketSendUtility.sendMessage(player, "NpcID: " + npcId);
                RequestResponseHandler responseHandler = new RequestResponseHandler(player) {
                    @Override
                    public void acceptRequest(Creature p2, Player p) {
                        TeleportService2.teleportTo(p, searchResult.getWorldId(), searchResult.getSpot().getX(),
                                searchResult.getSpot().getY(), searchResult.getSpot().getZ(), (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                    }

                    @Override
                    public void denyRequest(Creature p2, Player p) {
                    }
                };
                boolean requested = player.getResponseRequester().putRequest(902247, responseHandler);
                if (requested) {
                    PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(902247, 0, 0, "Совершить телепорт к NpcID: " + npcId + " ?"));
                }
            }
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
