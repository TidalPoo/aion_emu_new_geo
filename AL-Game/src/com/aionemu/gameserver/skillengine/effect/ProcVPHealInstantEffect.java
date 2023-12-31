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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATUPDATE_EXP;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.PacketSendUtility;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author kecimis, source.com
 */
public class ProcVPHealInstantEffect extends EffectTemplate {

    @XmlAttribute(required = true)
    protected int value2;//cap
    @XmlAttribute
    protected boolean percent;

    @Override
    public void applyEffect(Effect effect) {
        if (effect.getEffected() instanceof Player) {
            Player player = (Player) effect.getEffected();
            PlayerCommonData pcd = player.getCommonData();

            long cap = pcd.getMaxReposteEnergy() * value2 / 100;

            if (pcd.getCurrentReposteEnergy() < cap) {
                int valueWithDelta = value + delta * effect.getSkillLevel();
                long addEnergy;
                if (percent) {
                    addEnergy = (long) (pcd.getMaxReposteEnergy() * valueWithDelta * 0.001);//recheck when more skills
                } else {
                    addEnergy = valueWithDelta;
                }

                pcd.addReposteEnergy(addEnergy);
                PacketSendUtility.sendPacket(player, new SM_STATUPDATE_EXP(
                        pcd.getExpShown(),
                        pcd.getExpRecoverable(),
                        pcd.getExpNeed(),
                        pcd.getCurrentReposteEnergy(),
                        pcd.getMaxReposteEnergy()));
            }
        }
    }

}
