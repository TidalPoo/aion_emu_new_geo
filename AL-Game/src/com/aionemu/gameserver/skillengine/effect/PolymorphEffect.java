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

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CUSTOM_SETTINGS;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.knownlist.Visitor;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 * @modified Cheatkiller
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PolymorphEffect")
public class PolymorphEffect extends TransformEffect {

    @Override
    public void applyEffect(Effect effect) {
        super.applyEffect(effect);
        if (effect.getEffected() instanceof Player) {
            final Player player = (Player) effect.getEffected();
            player.getKnownList().doOnAllNpcs(new Visitor<Npc>() {
                @Override
                public void visit(Npc npc) {
                    PacketSendUtility.sendPacket(player, new SM_CUSTOM_SETTINGS(npc.getObjectId(), 0, npc.getType(player), 0));
                    //if (player.isGM()) {
                    //    PacketSendUtility.sendMessage(player, npc.getType(player) + "");
                    //}
                }
            });
        }
    }

    @Override
    public void startEffect(Effect effect) {
        if (model > 0) {
            Creature effected = effect.getEffected();
            NpcTemplate template = DataManager.NPC_DATA.getNpcTemplate(model);
            if (template != null) {
                effected.getTransformModel().setTribe(template.getTribe(), false);
            }
        }
        super.startEffect(effect);
    }

    @Override
    public void endEffect(Effect effect) {
        effect.getEffected().getTransformModel().setActive(false);
        if (effect.getEffected() instanceof Player) {
            final Player player = (Player) effect.getEffected();
            player.getKnownList().doOnAllNpcs(new Visitor<Npc>() {
                @Override
                public void visit(Npc npc) {
                    PacketSendUtility.sendPacket(player, new SM_CUSTOM_SETTINGS(npc.getObjectId(), 0, npc.getType(player), 0));
                    player.getTransformModel().setTribe(null, false);
                }
            });
        }
        super.endEffect(effect);
    }
}
