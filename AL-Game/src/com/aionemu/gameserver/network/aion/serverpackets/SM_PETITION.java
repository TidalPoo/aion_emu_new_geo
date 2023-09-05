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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.configs.main.PetitionConfig;
import com.aionemu.gameserver.model.Petition;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.PetitionService;

/**
 * @author zdead, Alex
 */
public class SM_PETITION extends AionServerPacket {

    private Petition petition;
    private int action = 1;
    private int on = 100;
    private int unk = 0x00;
    private int gmReply = 1;
    private String text;

    public SM_PETITION() {
        this.action = 0;
    }

    public SM_PETITION(int action) {
        this.action = action;//
    }

    public SM_PETITION(Petition petition) {
        this.action = 1;
        this.petition = petition;
    }

    public SM_PETITION(int action, String text) {
        this.action = action;
        this.text = text;
    }

    public SM_PETITION(Petition petition, int action, int on, int unk, int gmReply, String text) {
        this.petition = petition;
        this.action = action;
        this.on = on;
        this.unk = unk;
        this.gmReply = gmReply;
        this.text = text;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        Player player = con.getActivePlayer();
        writeC(action);
        switch (action) {
            case 0:
                writeD(0x00);
                writeD(0x00);
                writeD(0x00);
                writeD(0x00);
                writeH(0x00);
                writeC(0x00);
                break;
            //отправка петиции
            case 1:
                writeD(on); // unk (total online players ?)
                writeH(PetitionService.getInstance().getWaitingPlayers(con.getActivePlayer().getObjectId())); // Users
                writeS(Integer.toString(petition.getPetitionId())); // Ticket ID
                // writeH(unk);
                writeC(PetitionConfig.MAX_COUNT_OF_DAY); // Total Petitions
                writeC(PetitionService.getInstance().getRemainingCount(petition.getPlayerObjId())); // Remaining Petitions
                writeH(PetitionService.getInstance().calculateWaitTime(petition.getPlayerObjId())); // Estimated minutes
                writeD(gmReply);// before GM reply
                break;
            //окно гейм мастеру
            case 2:
                writeH(1);
                //writeS("sada");
                //writeS(text);
                writeD(1);
                break;
            //Ваша петиция на расмотрении
            case 3:
                writeC(1);
                writeS("sada");
                break;
            // Получен ответ от гейм мастера
            //когда гм ответил а игрокка нет в сети при входе игрока в игру отсылать это
            case 4:
                writeS("sada");
                writeD(player.getObjectId());


                writeD(1);
                writeC(1);
                //writeS(text);

                break;
        }
    }
}
