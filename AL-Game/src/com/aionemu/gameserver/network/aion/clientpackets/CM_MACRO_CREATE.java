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

import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MACRO_RESULT;
import com.aionemu.gameserver.services.player.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Request to create
 *
 * @author SoulKeeper
 */
public class CM_MACRO_CREATE extends AionClientPacket {

    /**
     * Logger
     */
    private static final Logger log = LoggerFactory.getLogger(CM_MACRO_CREATE.class);

    /**
     * Macro number. Fist is 1, second is 2. Starting from 1, not from 0
     */
    private int macroPosition;

    /**
     * XML that represents the macro
     */
    private String macroXML;

    /**
     * Constructs new client packet instance.
     *
     * @param opcode
     * @param restStates
     */
    public CM_MACRO_CREATE(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    /**
     * Read macro data
     */
    @Override
    protected void readImpl() {
        macroPosition = readC();
        macroXML = readS();
    }

    /**
     * Logging
     */
    @Override
    protected void runImpl() {
        log.debug(String.format("Created Macro #%d: %s", macroPosition, macroXML));

        PlayerService.addMacro(getConnection().getActivePlayer(), macroPosition, macroXML);

        sendPacket(SM_MACRO_RESULT.SM_MACRO_CREATED);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
