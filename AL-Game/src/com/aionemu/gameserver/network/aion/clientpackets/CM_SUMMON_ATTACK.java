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

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ATracer
 */
public class CM_SUMMON_ATTACK extends AionClientPacket {

    private static final Logger log = LoggerFactory.getLogger(CM_SUMMON_ATTACK.class);

    private int summonObjId;
    private int targetObjId;
    @SuppressWarnings("unused")
    private int unk1;

    private int time;
    @SuppressWarnings("unused")
    private int unk3;

    public CM_SUMMON_ATTACK(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    @Override
    protected void readImpl() {
        summonObjId = readD();
        targetObjId = readD();
        unk1 = readC();
        time = readH();
        unk3 = readC();
    }

    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();

        Summon summon = player.getSummon();
        if (summon == null) {
            log.warn("summon attack without active summon on " + player.getName() + ".");
            return;
        }

        if (summon.getObjectId() != summonObjId) {
            log.warn("summon attack from a different summon instance on " + player.getName() + ".");
            return;
        }

        VisibleObject obj = summon.getKnownList().getObject(targetObjId);
        if (obj != null && obj instanceof Creature) {
            summon.getController().attackTarget((Creature) obj, time);
        } //else {
        //log.warn("summon attack on a wrong target on " + player.getName());
        // }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
