/*
 * This file is part of aion-engine <aion-engine.com>
 *
 * aion-engine is private software: you can redistribute it and or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Private Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with aion-engine.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.skillengine.effect;

import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.PacketSendUtility;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetTeleportEffect")
public class TargetTeleportEffect extends EffectTemplate {

    @XmlAttribute(name = "same_map")
    protected boolean isSameMap;

    @XmlAttribute
    protected int distance;

    @Override
    public void applyEffect(Effect effect) {
        Player player = (Player) effect.getEffector();
        if (isSameMap) {
            float x = effect.getX();
            float y = effect.getY();
            float z = effect.getZ();
            if (x != 0 && y != 0) {
                PacketSendUtility.sendBrightYellowMessageOnCenter(player, "Заебись! Пишем начисто нахуй!");
                //todo update position
                TeleportService2.teleportTo(player, player.getWorldId(), player.getInstanceId(), x, y, z, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
            }
        }
        // TODO Should be handled manually for each effect with isSameMap = false
        // if in same map, should be teleported at the distance in front of NPC
    }

}
