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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.audit.GMService;
import java.util.Arrays;

/**
 * @author Rolandas, Alex
 */
public class CM_UNK extends AionClientPacket {

    int size;
    int unk5;
    int unk6;
    int opt2;
    int option;
    byte unk1;
    byte unk2;
    byte unk3;
    byte unk4;
    int someId;
    int sequence;
    byte[] data;
    private int unk01;
    private int unk02;
    private int unk03;
    private int unk04;
    private int unk05;
    private int unk06;
    private int unk07;
    private int unk08;
    private int unk09;
    private int unk10;
    private int unk11;
    private int unk12;
    private int memory;
    private int unk14;
    private int unk15;
    private int unk16;
    private int unk17;
    private int unk18;
    private int unk19;
    private String AionBinConnection;

    public CM_UNK(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }
    /* TODO information is log.txt
     Hardware information:
     OEM ID: 0
     Number of processors: 4
     Page size: 4096
     Processor type: 586
     Minimum application address: 10000
     Maximum application address: fffeffff
     Active processor mask: 15

     OS information:
     MajorVersion: 6
     MinorVersion: 2
     BuildNumber: 9200
     PlatformId: 2
     */

    @Override
    protected void readImpl() {
        size = readD();
        unk1 = (byte) readC();
        unk2 = (byte) readC();
        unk3 = (byte) readC();
        unk4 = (byte) readC();
        size = readD();//size
        unk5 = readD();
        someId = readD();
        unk6 = readD();
        sequence = readD();
        option = readD();//5
        //data = readB(size - 36);
        switch (option) {
            case 0:
                unk01 = readH();
                unk02 = readH();
                break;
            case 1:
                opt2 = readD();
                unk03 = readH();
                unk04 = readH();
                unk05 = readH();
                unk06 = readH();
                switch (opt2) {
                    case 1:
                        break;
                    default:
                        unk07 = readD();
                        unk08 = readD();
                        unk09 = readH();
                        unk10 = readH();
                    //break;
                }
                break;
            case 2:
            case 3:
                break;
            case 4:
                break;
            case 5:
                unk11 = readD();
                unk12 = readH();
                memory = readH();//4096 ОЗУ
                unk14 = readD();//156
                unk15 = readD();//1
                unk16 = readD();//1
                unk17 = readD();//30000
                unk18 = readD();//268435457
                unk19 = readD();//1*/
                AionBinConnection = readS();//aion.bin - connection, if no aion.bin = banned ?
                break;
            default:
                break;
        }
    }

    @Override
    protected void runImpl() {
        //Отсылается раз в 30 минут.
        Player player = getConnection().getActivePlayer();
        if (player == null) {
            return;
        }
        if (player.isGM() || player.isDeveloper()) {
            PacketSendUtility.sendMessage(player, "Info:\n"
                    + size + "\n"
                    + unk5 + "\n"
                    + unk6 + "\n"//-n
                    + opt2 + "\n"//0
                    + option + "\n"
                    + unk1 + "\n"
                    + unk2 + "\n"
                    + unk3 + "\n"
                    + unk4 + "\n"
                    + someId + "\n"
                    + sequence + "\n"
                    + Arrays.toString(data) + "\n"
                    + unk01 + "\n"
                    + unk02 + "\n"
                    + unk03 + "\n"
                    + unk04 + "\n"
                    + unk05 + "\n"
                    + unk06 + "\n"
                    + unk07 + "\n"
                    + unk08 + "\n"
                    + unk09 + "\n"
                    + unk10 + "\n"
                    + unk11 + "\n"
                    + unk12 + "\n"
                    + memory + "\n"
                    + unk14 + "\n"
                    + unk15 + "\n"
                    + unk16 + "\n"
                    + unk17 + "\n"
                    + unk18 + "\n"
                    + unk19 + "\n"
                    + AionBinConnection + "\n");
        }
        this.getConnection().setMemoryPC(memory);
        this.getConnection().setAionBin(AionBinConnection);
        if (!AionBinConnection.equalsIgnoreCase("AION.bin")) {
            for (Player gm : GMService.getInstance().getGMs()) {
                PacketSendUtility.sendMessage(gm, "WARNING name: " + player.getName());
            }
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
