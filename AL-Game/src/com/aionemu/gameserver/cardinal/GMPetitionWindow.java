/*
 * SAO Project
 */
package com.aionemu.gameserver.cardinal;

import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.audit.GMService;
import java.util.Map;

/**
 *
 * @author Alex
 */
public class GMPetitionWindow {

    public static void sendMessage(Player player, String message) {
        if (player.isGM()) {
            if (GMService.getInstance().getGMGhat(player).getPlayers().isEmpty()) {
                for (Player gm : GMService.getInstance().getGMs()) {
                    if (gm != player) {
                        PacketSendUtility.sendPacket(gm, new SM_SYSTEM_MESSAGE(1300564, player.getName(), message));
                    }
                }
            } else {
                for (Player players : GMService.getInstance().getGMGhat(player).getPlayers()) {
                    PacketSendUtility.sendPacket(players, new SM_SYSTEM_MESSAGE(1300564, player.getName(), message));
                }
                for (Map.Entry<Integer, GMPlayerChat> gms : GMService.getInstance().getAllPlayersPetition()) {
                    if (gms.getValue().getPlayers().contains(player)) {
                        Player gm = GMService.getInstance().getGM(gms.getKey());
                        PacketSendUtility.sendPacket(gm, new SM_SYSTEM_MESSAGE(1300564, player.getName(), message));
                    }
                }
            }
        } else {
            if (GMService.getInstance().getGMs().isEmpty()) {
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300564, GSConfig.SERVER_NAME, "Нет гмов онлайн!"));
                return;
            }
            if (GMService.getInstance().isSuch(player)) {
                GMService.getInstance().sendMessagePlayerForGM(player, message);
            } else {
                for (Player gm : GMService.getInstance().getGMs()) {
                    PacketSendUtility.sendPacket(gm, new SM_SYSTEM_MESSAGE(1300564, GSConfig.SERVER_NAME, player.getName() + " желает добавится в этот чат. Введите //gm add playerName для добавления игрока в ваш чат гейм-мастера"));
                }
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300564, GSConfig.SERVER_NAME, "Запрос на добавление вас в этот чат отправлен, ожидайте ответа гейм-мастера"));
            }
        }
    }
}
