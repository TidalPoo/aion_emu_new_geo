/**
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 * aion-lightning is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * aion-lightning is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * aion-lightning. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.cardinal.Language;
import com.aionemu.gameserver.network.WindowsVersion;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_VERSION_CHECK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author -Nemesiss-
 * @modified Alex
 */
public class CM_VERSION_CHECK extends AionClientPacket {

    private static final Logger log = LoggerFactory.getLogger(CM_VERSION_CHECK.class);

    /**
     * Aion Client version
     */
    private int version;
    @SuppressWarnings("unused")
    private int subversion;
    @SuppressWarnings("unused")
    private int windowsEncoding;
    @SuppressWarnings("unused")
    private int windowsVersion;
    @SuppressWarnings("unused")
    private int windowsSubVersion;
    @SuppressWarnings("unused")
    private int unk;

    /**
     * Constructs new instance of <tt>CM_VERSION_CHECK </tt> packet
     *
     * @param opcode
     * @param state
     * @param restStates
     */
    public CM_VERSION_CHECK(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        version = readH();
        subversion = readH();
        windowsEncoding = readD();
        windowsVersion = readD();
        windowsSubVersion = readD();
        unk = readC();//1 or 2
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        AionConnection con = this.getConnection();
        con.setWindows(WindowsVersion.getWindows(windowsVersion, windowsSubVersion));
        con.setWindowsEncoding(windowsEncoding);
        sendPacket(new SM_VERSION_CHECK(version));
        log.info("version:" + version + " subversion:" + subversion + " windowsEncoding: " + Language.getLanguageCode(windowsEncoding).name() + "(" + windowsEncoding + ") "
                + WindowsVersion.getWindows(windowsVersion, windowsSubVersion) + " unk: " + unk);

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
