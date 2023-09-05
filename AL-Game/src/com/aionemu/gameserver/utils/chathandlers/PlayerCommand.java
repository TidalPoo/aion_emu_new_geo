package com.aionemu.gameserver.utils.chathandlers;

import com.aionemu.gameserver.configs.main.LoggingConfig;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author synchro2
 */
public abstract class PlayerCommand extends ChatCommand {

    public PlayerCommand(String alias) {
        super(alias);
    }

    @Override
    public boolean checkLevel(Player player) {
        return player.havePermission(getLevel()) || player.isDeveloper();
    }

    @Override
    boolean process(Player player, String text) {
        if (!checkLevel(player)) {
            PacketSendUtility.sendMessage(player, "You not have permission for use this command.");
            return true;
        }

        if (!player.isDeveloper()) {
            if (LoggingConfig.LOG_GMAUDIT) {
                if (player.getTarget() != null && player.getTarget() instanceof Creature) {
                    Creature target = (Creature) player.getTarget();
                    log.info("[ADMIN COMMAND] > [Name: " + player.getName() + "][Target : " + target.getName() + "]: " + text);
                } else {
                    log.info("[ADMIN COMMAND] > [Name: " + player.getName() + "]: ." + text);
                }
            }
        }
        boolean success = false;
        if (text.length() == getAlias().length()) {
            success = this.run(player, EMPTY_PARAMS);
        } else {
            success = this.run(player, text.substring(getAlias().length() + 1).split(" "));
        }

        return success;
    }
}
