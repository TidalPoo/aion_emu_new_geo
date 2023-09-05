/*
 * AionLight project
 */
package com.aionemu.gameserver.services;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.zone.ZoneType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.custom.ffa.FfaGroupService;
import com.aionemu.gameserver.services.custom.ffa.FfaLegionService;
import com.aionemu.gameserver.services.custom.ffa.FfaPlayers;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapType;

/**
 *
 * @author Alex
 */
public class FlyService {

    public static boolean canFly(Player player) {
        if (FfaGroupService.isInFFA(player) || FfaPlayers.isInFFA(player) || FfaLegionService.isInFFA(player)) {
            return true;
        }
        if (player.getAccessLevel() < AdminConfig.GM_FLIGHT_FREE && !player.isInsideZoneType(ZoneType.FLY)) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FLYING_FORBIDDEN_HERE);
            return false;
        }
        // If player is under NoFly Effect, show the retail message for it and return
        if (player.isUnderNoFly()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANT_FLY_NOW_DUE_TO_NOFLY);
            return false;
        }
        if (!player.isInsideZoneType(ZoneType.FLY) && player.getAccessLevel() == 0) {
            if (player.isInInstance()) {
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FLYING_FORBIDDEN_HERE);
                return false;
            }

            int questId = player.getRace() == Race.ELYOS ? 1007 : 2009;
            int level = WorldMapType.getWorld(player.getWorldId()).getFlyLevel();
            QuestState gs = player.getQuestStateList().getQuestState(questId);
            if (gs == null || gs.getStatus() != QuestStatus.COMPLETE) {
                PacketSendUtility.sendMessage(player, "Для открытия крыльев необходимо завершить квест [quest:" + questId + "]");
                return false;
            }
            /*if (level == 0) {
             PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FLYING_FORBIDDEN_HERE);
             return false;
             } else if (player.getLevel() < level && player.getMembership() == 0) {
             PacketSendUtility.sendMessage(player, "Для полета в этой локации необходимо иметь " + level + " уровень");
             return false;
             }*/
        }
        if (player.isUnderNoFPConsum()) {
            PacketSendUtility.sendMessage(player, "В этой локации не расходуется время полета");
        }
        return true;
    }

    public static boolean TrueFly(Player player) {
        int level = WorldMapType.getWorld(player.getWorldId()).getFlyLevel();
        if (player.isInInstance()) {
            return false;
        } else if (level == 0 || player.getLevel() < level) {
            return false;
        }
        return true;
    }
}
