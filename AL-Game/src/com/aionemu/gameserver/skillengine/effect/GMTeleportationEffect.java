/**
 * SAO Project
 */
package com.aionemu.gameserver.skillengine.effect;

import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.TeleportTask;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Alex
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "gmteleporter")
public class GMTeleportationEffect extends EffectTemplate {
    //18798 телепорт в установленное место
    //18766 телепорт
    //19967 охуенный телепортация
    //16574 - пламя сделать за дп
    //16598 поедание плоти урон и хп
    //16581 опустошение
    //16575 ледник
    //16572 копье земли

    @Override
    public void applyEffect(Effect effect) {
        final Player player = (Player) effect.getEffector();
        if (player.getTeleportTask() != null) {
            final TeleportTask tp = player.getTeleportTask();
            ThreadPoolManager.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    TeleportService2.teleportTo(player, tp.MapId, tp.instanceId, tp.x, tp.y, tp.z, (byte) 0, TeleportAnimation.NO_ANIMATION);
                }
            }, 2100);
        }
    }
}
