/*
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 * aion-lightning is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * aion-lightning is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * aion-lightning. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.BannedHDDManager;
import com.aionemu.gameserver.network.BannedMacManager;
import com.aionemu.gameserver.network.IPv4;
import com.aionemu.gameserver.network.NetworkBannedManager;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.world.World;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javolution.util.FastList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * In this packet client is sending Mac Address, Hard Disk Drive Serial Number
 *
 * @author -Nemesiss-, KID, Alex
 */
public class CM_MAC_ADDRESS extends AionClientPacket {

    private static final Logger log = LoggerFactory.getLogger(CM_MAC_ADDRESS.class);

    /**
     * Mac Addres send by client in the same format as: ipconfig /all [ie:
     * xx-xx-xx-xx-xx-xx]
     */
    private String macAddress;
    private final List<String> IPv4list = new FastList<>();
    private final List<String> IPv4listLocal = new FastList<>();
    private String hdd_serial;
    private String local_ip;
    private short counter;
    private int unk;

    /**
     * Constructs new instance of <tt>CM_MAC_ADDRESS </tt> packet
     *
     * @param opcode
     * @param state
     * @param restStates
     */
    public CM_MAC_ADDRESS(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        unk = readC();//unk always 0
        counter = (short) readH();//ip size traceroute
        for (short i = 0; i < counter; i++) {
            IPv4list.add(IPv4.getIP(readD()));// int32bit in byte[] and String[]
        }
        macAddress = readS();//mac address
        hdd_serial = readS();//HDD Serial number
        IPv4listLocal.add(IPv4.getIP(readD()));
        local_ip = IPv4listLocal.toString();// local ip address
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        if (BannedMacManager.getInstance().isBanned(macAddress)) {
            this.getConnection().closeNow();
            log.info("[MAC_AUDIT] " + macAddress + " (" + this.getConnection().getIP() + ") was kicked due to mac ban");
            return;
        }
        if (BannedHDDManager.getInstance().isBanned(hdd_serial)) {
            this.getConnection().closeNow();
            log.info("[HDD_AUDIT] " + hdd_serial + " (" + this.getConnection().getIP() + ") was kicked due to hdd ban");
            return;
        }

        if (NetworkBannedManager.getInstance().isBanned(getConnection().getIP())) {
            this.getConnection().closeNow();
            return;
        }

        String macReg = "^([A-F|0-9]{2}-){5}[A-F|0-9]{2}$";
        Pattern pattern = Pattern.compile(macReg);
        Matcher matcher = pattern.matcher(macAddress);
        if (!matcher.matches() || macAddress.isEmpty() || macAddress.contains("00-00-00-00")) {
            this.getConnection().closeNow();
            log.info("No valid Mac Address : " + macAddress);
            return;
        }
        if (AdminConfig.NO_OPEN_NEW_WINDOW && !getConnection().getIP().equals("127.0.0.1")) {
            for (Player player : World.getInstance().getAllPlayers()) {
                if (!player.isDeveloper() && !player.isGM()) {
                    if (player.getClientConnection().getIP().equals(getConnection().getIP()) && player.getClientConnection().getMacAddress().equals(macAddress) && player.getClientConnection().getHddSerial().equals(hdd_serial)) {
                        log.info("Logon attempt with two windows.\nhdd_serial: " + hdd_serial + "IP: " + getConnection().getIP());
                        this.getConnection().closeNow();
                        break;
                    }
                }
            }
        }
        log.info(counter + " IPv4: " + IPv4list.toString());
        log.info("Mac: " + macAddress);
        log.info("HDD SERIAL: " + hdd_serial);
        log.info("Local IP: " + local_ip);
        log.info("Connect IP: " + getConnection().getIP());
        this.getConnection().setMacAddress(macAddress);
        this.getConnection().setHDDSerial(hdd_serial);
        this.getConnection().setIPv4List(IPv4list.toString());
        this.getConnection().setLocalIP(local_ip);
        this.getConnection().setTracerouteIP(IPv4list.toString());
        IPv4list.clear();
        IPv4listLocal.clear();
        if (unk != 0) {
            log.info("UEEEEEEEEEEEEEE unk != 0 & unk == " + unk);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
