/*
 * AionLight project
 */
package com.aionemu.gameserver.services.custom;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 *
 * @author Alex
 */
public class NewCommandService {

    public enum newCommand {

        c1(0, "правила сервера"),
        c2(0, "правила форума"),
        с3(0, "гайд"),
        с4(1, "телепорт"),
        с5(6, "перезагрузка");
        private final int i;
        private final String command;

        newCommand(int i, String command) {
            this.i = i;
            this.command = command;
        }

        public int getI() {
            return i;
        }

        public String getCommand() {
            return command;
        }
    }

    public static boolean isCommand(Player player, String message) {
        for (newCommand c : newCommand.values()) {
            if (c.getCommand().equalsIgnoreCase(message) && c.getI() == player.getAccessLevel()) {
                startCommand(player, message);
                return true;
            }
        }
        return false;
    }

    private static void startCommand(Player player, String message) {
        if (message.equalsIgnoreCase("//" + newCommand.c1.getCommand())) {
            PacketSendUtility.sendMessage(player, "Информация временно отключена!");
        } else {
            PacketSendUtility.sendMessage(player, "Информация временно отключена!");
        }
    }
}
