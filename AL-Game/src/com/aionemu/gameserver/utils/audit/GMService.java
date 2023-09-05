/*
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.utils.audit;

import com.aionemu.gameserver.cardinal.GMPlayerChat;
import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.AccessLevelEnum;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * @author MrPoke
 * @modified Alex
 */
public class GMService {

    public static final GMService getInstance() {
        return SingletonHolder.instance;
    }

    private Map<Integer, Player> gms = new FastMap<>();
    private Map<Integer, GMPlayerChat> playersPetition = new FastMap<>();
    private boolean announceAny = false;
    private List<Byte> announceList;
    public boolean messageGM;

    public boolean isMessageGM() {
        return messageGM;
    }

    public void setGMMessage(boolean status) {
        this.messageGM = status;
    }

    private GMService() {
        announceList = new ArrayList<>();
        announceAny = AdminConfig.ANNOUNCE_LEVEL_LIST.equals("*");
        if (!announceAny) {
            try {
                for (String level : AdminConfig.ANNOUNCE_LEVEL_LIST.split(",")) {
                    announceList.add(Byte.parseByte(level));
                }
            } catch (NumberFormatException e) {
                announceAny = true;
            }
        }
    }

    public void broadcastMesageP(String message) {
        for (Player player : gms.values()) {
            PacketSendUtility.sendYellowMessage(player, message);
        }
    }

    public void addPlayer(Player g, Player player) {
        int gmObjId = g.getObjectId();
        if (!playersPetition.containsKey(gmObjId)) {
            playersPetition.put(gmObjId, new GMPlayerChat());
        }
        playersPetition.get(gmObjId).addPlayer(player);
        PacketSendUtility.sendPacket(g, new SM_SYSTEM_MESSAGE(1300564, GSConfig.SERVER_NAME, player.getName() + " присоединился к чату гейм-мастера"));
        PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300564, GSConfig.SERVER_NAME, g.getName() + " добавил вас к чату гейм-мастера"));
    }

    public GMPlayerChat getGMGhat(Player gm) {
        return playersPetition.get(gm.getObjectId());
    }

    public void sendMessagePlayerForGM(Player player, String message) {
        for (Map.Entry<Integer, GMPlayerChat> p : playersPetition.entrySet()) {
            if (p.getValue().getPlayers().contains(player)) {
                Player gm = gms.get(p.getKey());
                PacketSendUtility.sendPacket(gm, new SM_SYSTEM_MESSAGE(1300564, GSConfig.SERVER_NAME, player.getName() + " желает добавится в этот чат. Введите //gm add playerName для добавления игрока в ваш чат гейм-мастера"));
            }
        }
    }

    public Set<Map.Entry<Integer, GMPlayerChat>> getAllPlayersPetition() {
        return playersPetition.entrySet();
    }

    public void removePlayer(Player g, Player player) {
        int gmObjId = g.getObjectId();
        if (!playersPetition.containsKey(gmObjId)) {
            PacketSendUtility.sendPacket(g, new SM_SYSTEM_MESSAGE(1300564, GSConfig.SERVER_NAME, "Вы не добавляли этого игрока к чату гейм-мастера"));
            return;
        }
        if (getGMGhat(g).delPlayer(player)) {
            PacketSendUtility.sendPacket(g, new SM_SYSTEM_MESSAGE(1300564, GSConfig.SERVER_NAME, player.getName() + " покидает чат гейм-мастера"));
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300564, GSConfig.SERVER_NAME, g.getName() + " исключает вас из чата гейм-мастера"));
        }
    }

    public boolean isSuch(Player player) {
        for (Map.Entry<Integer, GMPlayerChat> p : playersPetition.entrySet()) {
            if (p.getValue().getPlayers().contains(player)) {
                return true;
            }
        }
        return false;
    }

    public Collection<Player> getPlayersPetition(Player gm) {
        return playersPetition.get(gm.getObjectId()).getPlayers();
    }

    public Collection<Player> getGMs() {
        return gms.values();
    }

    public Player getGM(int objId) {
        return gms.get(objId);
    }

    public void onPlayerLogin(Player player) {
        if (player.isGM()) {
            gms.put(player.getObjectId(), player);
            playersPetition.put(player.getObjectId(), new GMPlayerChat());
            if (announceAny) {
                broadcastMesage("GM: " + player.getName() + " logged in.");
            } else if (announceList.contains(player.getAccessLevel())) {
                broadcastMesage("GM: " + player.getName() + " logged in.");
            }
            // Special skill for gm
            int level = player.getAccessLevel();
            player.getSkillList().removeAllGameMasterSkills(level);
            AccessLevelEnum enums = AccessLevelEnum.getAlType(level);
            if (level >= AdminConfig.COMMAND_SPECIAL_SKILL) {
                for (int id : enums.getSkills()) {
                    player.getSkillList().addGameMasterSkill(player, id, 1);
                }
            }
            for (Player p : World.getInstance().getAllPlayers()) {
                PacketSendUtility.sendPacket(p, new SM_SYSTEM_MESSAGE(1300564, GSConfig.SERVER_NAME, enums.getStatusName() + " " + player.getName() + " входит в игру. Сейчас в онлайне " + gms.size() + " гм."));
            }
        }
    }

    public void onPlayerLogedOut(Player player) {
        AccessLevelEnum enums = AccessLevelEnum.getAlType(player.getAccessLevel());
        player.getSkillList().removeAllGameMasterSkills(player.getAccessLevel());
        gms.remove(player.getObjectId());
        playersPetition.remove(player.getObjectId());
        for (Player p : World.getInstance().getAllPlayers()) {
            PacketSendUtility.sendPacket(p, new SM_SYSTEM_MESSAGE(1300564, GSConfig.SERVER_NAME, enums.getStatusName() + " " + player.getName() + " выходит из игры. Сейчас в онлайне " + gms.size() + " гм."));
        }
    }

    public void broadcastMesage(String message) {
        for (Player player : gms.values()) {
            PacketSendUtility.sendYellowMessage(player, message);
        }
    }

    public void broadcastMesageToAccessLevel(String message, int level) {
        for (Player player : gms.values()) {
            if (player.getAccessLevel() == level) {
                PacketSendUtility.sendYellowMessage(player, message);
            }
        }
    }

    public FastList<Player> getSortSearchList(FastList<Player> matches, int MAX_RESULTS) {
        for (Player player : getGMs()) {
            if (announceAny) {
                matches = addMember(matches, player, MAX_RESULTS);
            } else if (announceList.contains(player.getAccessLevel())) {
                matches = addMember(matches, player, MAX_RESULTS);
            }
        }
        return matches;
    }

    public FastList<Player> addMember(FastList<Player> matches, Player player, int MAX_RESULTS) {
        if (matches.contains(player)) {
            matches.remove(player);
            matches.addFirst(player);
        } else {
            if (matches.size() >= MAX_RESULTS) {
                matches.removeLast();
                matches.addFirst(player);
            } else {
                matches.addFirst(player);
            }
        }
        return matches;

    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final GMService instance = new GMService();
    }
}
