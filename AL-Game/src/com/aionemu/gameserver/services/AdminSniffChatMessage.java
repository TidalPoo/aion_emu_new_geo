package com.aionemu.gameserver.services;

import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import java.util.Collection;
import java.util.Map;
import javolution.util.FastMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alex
 */
public class AdminSniffChatMessage {

    private final Logger log = LoggerFactory.getLogger(AdminSniffChatMessage.class);

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final AdminSniffChatMessage instance = new AdminSniffChatMessage();
    }

    public static AdminSniffChatMessage getInstance() {
        return SingletonHolder.instance;
    }

    public AdminSniffChatMessage() {
        log.info("AdminChatMessage Initialize");
    }

    private final Map<Integer, AdminChatMessage> map = new FastMap<>();

    public void addMap(Player admin, Race race, ChatType ct) {
        map.put(admin.getObjectId(), new AdminChatMessage(admin, race, ct));
    }

    public Collection<AdminChatMessage> getChatMessage() {
        return map.values();
    }

    public AdminChatMessage getPlayer(int id) {
        return map.get(id);
    }

    public void clears(Player admin, boolean remove) {
        if (remove) {
            if (map.get(admin.getObjectId()) != null) {
                map.remove(admin.getObjectId());
            }
        } else {
            if (!map.isEmpty()) {
                map.clear();
            }
        }
    }

    public void sendMessage(Player player, ChatType type, String message) {
        if (map.isEmpty()) {
            return;
        }
        Race race = player.getRace();
        ChatType ct = player.getChatType();
        for (AdminChatMessage m : getChatMessage()) {
            if (m.getPlayers().isEmpty()) {
                if (m.getRace() == null || m.getRace() == race) {
                    if (m.getChatType() == null || m.getChatType() == type) {
                        PacketSendUtility.sendMessage(m.getAdmin(), "[" + type + "]" + player.getName() + ": " + message);
                    }
                }
            } else if (m.getPlayers().contains(player)) {
                if (ct == null || ct == type) {
                    PacketSendUtility.sendMessage(m.getAdmin(), "[" + type + "]" + player.getName() + ": " + message);
                }
            }
        }
    }
}
