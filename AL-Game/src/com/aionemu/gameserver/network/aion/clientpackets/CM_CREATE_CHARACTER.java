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

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.administration.DeveloperConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.dao.InventoryDAO;
import com.aionemu.gameserver.model.Gender;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.model.account.PlayerAccountData;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerAppearance;
import com.aionemu.gameserver.model.gameobjects.player.PlayerBonusTimeStatus;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CREATE_CHARACTER;
import com.aionemu.gameserver.services.AccountService;
import com.aionemu.gameserver.services.NameRestrictionService;
import com.aionemu.gameserver.services.event.WellcomeToAtrea;
import com.aionemu.gameserver.services.player.PlayerService;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import java.sql.Timestamp;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * In this packets aion client is requesting creation of character.
 *
 * @author -Nemesiss-
 * @modified cura, Alex
 */
public class CM_CREATE_CHARACTER extends AionClientPacket {

    private final Logger log = LoggerFactory.getLogger(CM_CREATE_CHARACTER.class);
    /**
     * Character appearance
     */
    private PlayerAppearance playerAppearance;
    /**
     * Player base data
     */
    private PlayerCommonData playerCommonData;

    /**
     * Constructs new instance of <tt>CM_CREATE_CHARACTER </tt> packet
     *
     * @param opcode
     * @param state
     * @param restStates
     */
    public CM_CREATE_CHARACTER(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        readD(); // ignored for now
        readS(); // something + accointId

        playerCommonData = new PlayerCommonData(IDFactory.getInstance().nextId());
        String name = CustomConfig.ENABLE_CONVERT_NAME ? Util.convertName(readS()) : readS();
        playerCommonData.setName(name);
        readB(50 - (name.length() * 2)); // some shit? 2.5.x
        int level = DeveloperConfig.ENABLE_GM_PVP ? 65 : CustomConfig.START_LEVEL;
        playerCommonData.setLevel(level);
        playerCommonData.setGender(readD() == 0 ? Gender.MALE : Gender.FEMALE);
        playerCommonData.setRace(readD() == 0 ? Race.ELYOS : Race.ASMODIANS);
        playerCommonData.setPlayerClass(PlayerClass.getPlayerClassById((byte) readD()));
        int access_level = getConnection().getAccount().getAccessLevel();
        if (getConnection().getAccount().getMembership() >= MembershipConfig.STIGMA_SLOT_QUEST || access_level > 0) {
            playerCommonData.setAdvancedStigmaSlotSize(11);
        }
        playerAppearance = new PlayerAppearance();
        playerAppearance.setVoice(readD());
        playerAppearance.setSkinRGB(readD());
        playerAppearance.setHairRGB(readD());
        playerAppearance.setEyeRGB(readD());
        playerAppearance.setLipRGB(readD());
        playerAppearance.setFace(readC());
        playerAppearance.setHair(readC());
        playerAppearance.setDeco(readC());
        playerAppearance.setTattoo(readC());
        playerAppearance.setFaceContour(readC());
        playerAppearance.setExpression(readC());
        readC(); // always 4 o0 // 5 in 1.5.x
        playerAppearance.setJawLine(readC());
        playerAppearance.setForehead(readC());
        playerAppearance.setEyeHeight(readC());
        playerAppearance.setEyeSpace(readC());
        playerAppearance.setEyeWidth(readC());
        playerAppearance.setEyeSize(readC());
        playerAppearance.setEyeShape(readC());
        playerAppearance.setEyeAngle(readC());
        playerAppearance.setBrowHeight(readC());
        playerAppearance.setBrowAngle(readC());
        playerAppearance.setBrowShape(readC());
        playerAppearance.setNose(readC());
        playerAppearance.setNoseBridge(readC());
        playerAppearance.setNoseWidth(readC());
        playerAppearance.setNoseTip(readC());
        playerAppearance.setCheek(readC());
        playerAppearance.setLipHeight(readC());
        playerAppearance.setMouthSize(readC());
        playerAppearance.setLipSize(readC());
        playerAppearance.setSmile(readC());
        playerAppearance.setLipShape(readC());
        playerAppearance.setJawHeigh(readC());
        playerAppearance.setChinJut(readC());
        playerAppearance.setEarShape(readC());
        playerAppearance.setHeadSize(readC());
        playerAppearance.setNeck(readC());
        playerAppearance.setNeckLength(readC());
        playerAppearance.setShoulderSize(readC());
        playerAppearance.setTorso(readC());
        playerAppearance.setChest(readC()); // only woman
        playerAppearance.setWaist(readC());
        playerAppearance.setHips(readC());
        playerAppearance.setArmThickness(readC());
        playerAppearance.setHandSize(readC());
        playerAppearance.setLegThicnkess(readC());
        playerAppearance.setFootSize(readC());
        playerAppearance.setFacialRate(readC());
        readC(); // always 0
        playerAppearance.setArmLength(readC());
        playerAppearance.setLegLength(readC()); // wrong??
        playerAppearance.setShoulders(readC()); // 1.5.x May be ShoulderSize
        playerAppearance.setFaceShape(readC());
        readC();
        readC();
        readC();
        playerAppearance.setHeight(readF());
    }

    /**
     * Actually does the dirty job
     */
    @Override
    protected void runImpl() {
        AionConnection client = getConnection();

        Account account = client.getAccount();

        /* Some reasons why player can' be created */
        if (client.getActivePlayer() != null) {
            return;
        }
        if (AdminConfig.ENGINEER_AND_ARTIST_OFF) {
            if (playerCommonData.getPlayerClass() == PlayerClass.ENGINEER || playerCommonData.getPlayerClass() == PlayerClass.ARTIST) {
                client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.FAILED_TO_CREATE_THE_CHARACTER));
                IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
                return;
            }
        }
        if (account.getMembership() >= MembershipConfig.CHARACTER_ADDITIONAL_ENABLE) {
            if (MembershipConfig.CHARACTER_ADDITIONAL_COUNT <= account.size()) {
                client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.RESPONSE_SERVER_LIMIT_EXCEEDED));
                IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
                return;
            }
        } else if (GSConfig.CHARACTER_LIMIT_COUNT <= account.size()) {
            client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.RESPONSE_SERVER_LIMIT_EXCEEDED));
            IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
            return;
        }
        if (!PlayerService.isFreeName(playerCommonData.getName())) {
            if (GSConfig.CHARACTER_CREATION_MODE == 2) {
                client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.RESPONSE_NAME_RESERVED));
            } else {
                client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.RESPONSE_NAME_ALREADY_USED));
            }
            IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
            return;
        }
        if (PlayerService.isOldName(playerCommonData.getName())) {
            client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.RESPONSE_NAME_ALREADY_USED));
            IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
            return;
        }
        if (!NameRestrictionService.isValidName(playerCommonData.getName())) {
            client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.RESPONSE_INVALID_NAME));
            IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
            return;
        }
        if (NameRestrictionService.isForbiddenWord(playerCommonData.getName())) {
            client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.RESPONSE_FORBIDDEN_CHAR_NAME));
            IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
            return;
        }
        if (!playerCommonData.getPlayerClass().isStartingClass()) {
            client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.FAILED_TO_CREATE_THE_CHARACTER));
            IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
            return;
        }
        if (GSConfig.CHARACTER_CREATION_MODE == 0) {
            for (PlayerAccountData data : account.getSortedAccountsList()) {
                if (data.getPlayerCommonData().getRace() != playerCommonData.getRace()) {
                    client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.FAILED_TO_CREATE_THE_CHARACTER));
                    IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
                    return;
                }
            }
        }
        AccountService.removeDeletedCharacters(account);
        if (WellcomeToAtrea.novice_inventory) {
            playerCommonData.setBonusType(PlayerBonusTimeStatus.NEW);
        }
        Player player = PlayerService.newPlayer(playerCommonData, playerAppearance, account);
        if (!PlayerService.storeNewPlayer(player, account.getName(), account.getId())) {
            client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.RESPONSE_DB_ERROR));
            IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
        } else {
            List<Item> equipment = DAOManager.getDAO(InventoryDAO.class).loadEquipment(player.getObjectId());
            PlayerAccountData accPlData = new PlayerAccountData(playerCommonData, null, playerAppearance, equipment, null);
            accPlData.setCreationDate(new Timestamp(System.currentTimeMillis()));
            PlayerService.storeCreationTime(player.getObjectId(), accPlData.getCreationDate());
            playerCommonData.setCreationDate(accPlData.getCreationDate());
            /*if (CustomConfig.NEW_PVP_MODE) {
             account.setPlayerMember(true);
             playerCommonData.setMembershipLevel(2);
             playerCommonData.setMembershipLevelExpire(new Timestamp(System.currentTimeMillis() + 3L * 24 * 60 * 60 * 1000));
             }*/
            account.addPlayerAccountData(accPlData);
            client.sendPacket(new SM_CREATE_CHARACTER(accPlData, SM_CREATE_CHARACTER.RESPONSE_OK));
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
