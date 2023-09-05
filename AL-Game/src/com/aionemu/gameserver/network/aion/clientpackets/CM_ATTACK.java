/**
 * This file is part of aion-lightning <aion-lightning.smfnew.com>.
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

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author alexa026, Avol, ATracer, KID
 */
public class CM_ATTACK extends AionClientPacket {

    private static final Logger log = LoggerFactory.getLogger(CM_ATTACK.class);
    /**
     * Target object id that client wants to TALK WITH or 0 if wants to unselect
     */
    private int targetObjectId;
    // TODO: Question, are they really needed?
    @SuppressWarnings("unused")
    private int attackno;

    private int time;
    @SuppressWarnings("unused")
    private int type;

    public CM_ATTACK(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    @Override
    protected void readImpl() {
        targetObjectId = readD();// empty
        attackno = readC();// empty
        time = readH();// empty
        type = readC();// empty
    }

    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        if (player == null) {
            return;//удалить после фикса BodyGuardAI2
        }
        //VisibleObject target = player.getTarget();
        /*if (target == null) {
         return;
         }*/
        if (player.getLifeStats().isAlreadyDead()) {
            return;
        }

        if (player.isProtectionActive()) {
            player.getController().stopProtectionActiveTask();
        }

        VisibleObject obj = player.getKnownList().getObject(targetObjectId);
        if (obj != null && obj instanceof Creature) {
            player.getController().attackTarget((Creature) obj, time);
        } else {
            if (obj != null) {
                log.warn("Attacking unsupported target" + obj + " id " + obj.getObjectTemplate().getTemplateId());
            }
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
