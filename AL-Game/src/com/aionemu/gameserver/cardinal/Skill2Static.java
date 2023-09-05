/*
 * SAO Project
 */
package com.aionemu.gameserver.cardinal;

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TARGET_SELECTED;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TARGET_UPDATE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 *
 * @author Alex
 */
public class Skill2Static {

    public static void setTargetRemoveLoc(Player player) {
        VisibleObject target = player.getTarget();
        if (target != null && target != player) {
            player.setOldTarget(target);
            player.setTarget(null);
            PacketSendUtility.sendPacket(player, new SM_TARGET_SELECTED(player));
            PacketSendUtility.broadcastPacket(player, new SM_TARGET_UPDATE(player));
        }
        //сохранение координат для телепорта если упал в текстуры от Изгиба пространства и времени.
        player.saveZonePosition();
    }
}
