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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.Gender;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.FriendList.Status;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_SEARCH;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.custom.ffa.FfaPlayers;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.audit.GMService;
import com.aionemu.gameserver.world.World;
import java.util.Iterator;
import javolution.util.FastList;

/**
 * Received when a player searches using the social search panel
 *
 * @author Ben
 * @modified Alex
 */
public class CM_PLAYER_SEARCH extends AionClientPacket {

    /**
     * The max number of players to return as results
     */
    public static final int MAX_RESULTS = 104; //3.0

    private String name;
    private int region;
    private int classMask;
    private int minLevel;
    private int maxLevel;
    private int lfgOnly;
    private int unk;

    public CM_PLAYER_SEARCH(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        name = readS(52);
        if (name != null) {
            name = CustomConfig.ENABLE_CONVERT_NAME ? Util.convertName(name) : name;
        }
        region = readD();
        classMask = readD();
        minLevel = readC();
        maxLevel = readC();
        lfgOnly = readC();
        unk = readC(); // 0x00 in search pane 0x30 in /who?
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        Player activePlayer = getConnection().getActivePlayer();
        Iterator<Player> it = World.getInstance().getPlayersIterator();
        FastList<Player> matches = new FastList<>(MAX_RESULTS);

        if (activePlayer.getLevel() < CustomConfig.LEVEL_TO_SEARCH && !activePlayer.isGM()) {
            sendPacket(SM_SYSTEM_MESSAGE.STR_CANT_WHO_LEVEL(String.valueOf(CustomConfig.LEVEL_TO_SEARCH)));
            return;
        }
        if (FfaPlayers.isInFFA(activePlayer) && !activePlayer.isDeveloper()) {
            PacketSendUtility.sendBrightYellowMessageOnCenter(activePlayer, "You can not use in FFA location!");
            return;
        }

        boolean gmSearch = false;
        boolean vip = false;
        boolean wedding = false;
        boolean genderGirl = false;
        boolean genderBoy = false;
        boolean custom = false;
        boolean success = (activePlayer.isVIP() || activePlayer.isGM() || activePlayer.isDeveloper());
        String n = name.toLowerCase();
        if (null != n && success) {
            switch (n) {
                case "gm":
                case "gamemaster":
                case "гмы":
                    gmSearch = true;
                    custom = true;
                    break;
                case "vip":
                    vip = true;
                    custom = true;
                    break;
                case "wedding":
                case "свадьба":
                    wedding = true;
                    custom = true;
                    break;
                case "девушки":
                case "девочки":
                case "girls":
                    genderGirl = true;
                    custom = true;
                    break;
                case "парни":
                case "boys":
                    genderBoy = true;
                    custom = true;
                    break;
            }
        }
        int race = 0;
        while (it.hasNext() && matches.size() < MAX_RESULTS) {
            Player player = it.next();
            /*if (!player.isSpawned()) {
             continue;
             } else*/
            if (gmSearch) {
                if (!player.isGM()) {
                    continue;
                }
            } else if (wedding) {
                if (!player.isMarried()) {
                    continue;
                }
            } else if (vip) {
                if (player.getMembership() < 2) {
                    continue;
                }
            } else if (genderGirl) {
                if (player.getGender() != Gender.FEMALE) {
                    continue;
                }
            } else if (genderBoy) {
                if (player.getGender() != Gender.MALE) {
                    continue;
                }
            }
            boolean ra = (player.getRace() != activePlayer.getRace());
            if (ra) {
                race++;
            }
            if (player.getFriendList().getStatus() == Status.OFFLINE && !activePlayer.isGM()) {
                continue;
            } else if (player.isGM() && !CustomConfig.SEARCH_GM_LIST && !activePlayer.isGM()) {
                continue;
            } else if (lfgOnly == 1 && !player.isLookingForGroup()) {
                continue;
            } else if (!custom && !name.isEmpty() && !player.getName().toLowerCase().contains(name.toLowerCase())) {
                continue;
            } else if (minLevel != 0xFF && player.getLevel() < minLevel) {
                continue;
            } else if (maxLevel != 0xFF && player.getLevel() > maxLevel) {
                continue;
            } else if (classMask > 0 && (player.getPlayerClass().getMask() & classMask) == 0) {
                continue;
            } else if (region > 0 && player.getActiveRegion() != null && player.getActiveRegion().getMapId() != region) {
                continue;
            } else if (ra && !CustomConfig.FACTIONS_SEARCH_MODE && !activePlayer.isGM()) {
                continue;
            }
            matches.add(player);
        }

        if (CustomConfig.SEARCH_GM_LIST) {
            matches = GMService.getInstance().getSortSearchList(matches, MAX_RESULTS);
        }
        sendPacket(new SM_PLAYER_SEARCH(matches, region));
        int gmSize = GMService.getInstance().getGMs().size();
        StringBuilder sb = new StringBuilder();
        if (race != 0) {
            if (activePlayer.getRace() == Race.ELYOS) {
                sb.append("Online asmodians ").append(race).append("\n");
            } else {
                sb.append("Online elyos ").append(race).append("\n");
            }
        }
        if (gmSize != 0) {
            sb.append("Game Master Online (").append(gmSize).append(") :\n");
            Iterator<Player> gm = GMService.getInstance().getGMs().iterator();
            while (gm.hasNext()) {
                Player gameMaster = gm.next();
                String masterName = gameMaster.getCustomLegionName();
                sb.append(masterName).append("  ").append(gameMaster.getName()).append("\n");
            }
        } else {
            sb.append("No gms online");
        }
        PacketSendUtility.sendBrightYellowMessageOnCenter(activePlayer, sb.toString());
        if (success) {
            PacketSendUtility.sendWhiteMessage(activePlayer, "Вам доступны новые возможности поиска,в поле ввода имени введите:\n"
                    + "[color:vip;0 255 0] - найдутся все персонажи имеющие VIP привилегии\n"
                    + "[color:девуш;0 255 0][color:ки;0 255 0],[color:девоч;0 255 0][color:ки;0 255 0],[color:girls;0 255 0] - отобразятся все персонажи женского пола\n"
                    + "[color:парни;0 255 0],[color:boys;0 255 0] - отобразятся все персонажи мужского пола\n"
                    + "[color:gm;0 255 0],[color:gamem;0 255 0][color:aster;0 255 0],[color:гмы;0 255 0] - отобразятся все гейм-мастера\n"
                    + "[color:сва;0 255 0][color:дьба;0 255 0],[color:wed;0 255 0][color:ding;0 255 0] - отобразятся персонажи состоящие в браке.");
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); 
    }
}
