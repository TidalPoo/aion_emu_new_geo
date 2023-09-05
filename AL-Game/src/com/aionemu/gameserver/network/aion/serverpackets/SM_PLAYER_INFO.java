/**
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 * aion-lightning is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * aion-lightning is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * aion-lightning. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.actions.PlayerMode;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerAppearance;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.items.GodStone;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.model.team.legion.LegionEmblemType;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.custom.ffa.FfaPlayers;
import java.util.List;

/**
 * This packet is displaying visible players.
 *
 * @author -Nemesiss-, Avol, srx47 modified cura
 * @modified -Enomine- -Artur-, Alex
 */
public class SM_PLAYER_INFO extends AionServerPacket {

    /**
     * Visible player
     */
    private final Player player;
    private final boolean enemy;

    /**
     * Constructs new <tt>SM_PLAYER_INFO </tt> packet
     *
     * @param player actual player.
     * @param enemy
     */
    public SM_PLAYER_INFO(Player player, boolean enemy) {
        this.player = player;
        this.enemy = enemy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con) {
        Player activePlayer = con.getActivePlayer();
        if (activePlayer == null || player == null) {
            return;
        }
        PlayerCommonData pcd = player.getCommonData();
        final int raceId;
        if (player.getAdminNeutral() > 1 || activePlayer.getAdminNeutral() > 1) {
            raceId = activePlayer.getRace().getRaceId();
        } else if (activePlayer.isEnemy(player)) {
            raceId = (activePlayer.getRace().getRaceId() == 0 ? 1 : 0);
        } else {
            raceId = player.getRace().getRaceId();
        }

        final int genderId = pcd.getGender().getGenderId();
        final PlayerAppearance playerAppearance = player.getPlayerAppearance();

        writeF(player.getX());// x
        writeF(player.getY());// y
        writeF(player.getZ());// z
        writeD(player.getObjectId());
        /**
         * A3 female asmodian A2 male asmodian A1 female elyos A0 male elyos
         */
        writeD(pcd.getTemplateId());
        writeD(0);//new 4.3 NA
        /**
         * Transformed state - send transformed model id Regular state - send
         * player model id (from common data)
         */
        int model = player.getTransformModel().getModelId();
        writeD(model != 0 ? model : pcd.getTemplateId());
        writeC(0x01); // new 2.0 Packet --- probably pet info?
        writeD(player.getTransformModel().getType().getId());
        writeC(enemy ? 0x00 : 0x26);//wings

        writeC(raceId); // race
        writeC(pcd.getPlayerClass().getClassId());
        writeC(genderId); // sex
        writeH(player.getState());

        writeB(new byte[8]); // what this?

        writeC(player.getHeading());
        String name = player.getNewName();
        String nameFormat = player.getNameFormat();
        writeS(String.format(nameFormat, name)); // player name
        writeH(pcd.getTitleId()); // title id
        writeH(player.getCommonData().isHaveMentorFlag() ? 1 : 0);

        writeH(player.getCastingSkillId()); // status casting skill

        if (player.isLegionMember()) {
            writeD(player.getLegion().getLegionId());
            writeC(player.getLegion().getLegionEmblem().getEmblemId());
            writeC(player.getLegion().getLegionEmblem().getEmblemType().getValue());
            writeC(player.getLegion().getLegionEmblem().getEmblemType() == LegionEmblemType.DEFAULT ? 0x00 : 0xFF);
            writeC(player.getLegion().getLegionEmblem().getColor_r());
            writeC(player.getLegion().getLegionEmblem().getColor_g());
            writeC(player.getLegion().getLegionEmblem().getColor_b());
            writeS(player.getCustomLegionName());
        } else {
            if (!player.getCustomLegionName().isEmpty()) {
                writeD(0);
                writeC(0);
                writeC(0);
                writeC(0);
                writeC(0);
                writeC(0);
                writeC(0);
                writeS(player.getCustomLegionName());
            } else {
                writeB(new byte[12]);
            }
        }
        int maxHp = player.getLifeStats().getMaxHp();
        int currHp = player.getLifeStats().getCurrentHp();
        writeC(100 * currHp / maxHp);// %hp
        writeH(pcd.getDp());// current dp
        writeC(0x01);// unk (0x00)

        List<Item> items = player.getEquipment().getEquippedForApparence();
        short mask = 0;
        for (Item item : items) {
            if (item.getItemTemplate().isTwoHandWeapon()) {
                ItemSlot[] slots = ItemSlot.getSlotsFor(item.getEquipmentSlot());
                mask |= slots[0].getSlotIdMask();
            } else {
                mask |= item.getEquipmentSlot();
            }
        }
        writeH(mask); // Wrong !!! It's item count, but doesn't work
        for (Item item : items) {
            if (item.getEquipmentSlot() < Short.MAX_VALUE * 2) {
                writeD(item.getItemSkinTemplate().getTemplateId());
                GodStone godStone = item.getGodStone();
                writeD(godStone != null ? godStone.getItemId() : 0);
                writeD(item.getItemColor());
                writeH(item.getEnchantView());
            }
        }

        writeD(playerAppearance.getSkinRGB());
        writeD(playerAppearance.getHairRGB());
        writeD(playerAppearance.getEyeRGB());
        writeD(playerAppearance.getLipRGB());
        writeC(playerAppearance.getFace());
        writeC(playerAppearance.getHair());
        writeC(playerAppearance.getDeco());
        writeC(playerAppearance.getTattoo());
        writeC(playerAppearance.getFaceContour());
        writeC(playerAppearance.getExpression());

        writeC(0x06); // unk 0x05 0x06

        writeC(playerAppearance.getJawLine());
        writeC(playerAppearance.getForehead());

        writeC(playerAppearance.getEyeHeight());
        writeC(playerAppearance.getEyeSpace());
        writeC(playerAppearance.getEyeWidth());
        writeC(playerAppearance.getEyeSize());
        writeC(playerAppearance.getEyeShape());
        writeC(playerAppearance.getEyeAngle());

        writeC(playerAppearance.getBrowHeight());
        writeC(playerAppearance.getBrowAngle());
        writeC(playerAppearance.getBrowShape());

        writeC(playerAppearance.getNose());
        writeC(playerAppearance.getNoseBridge());
        writeC(playerAppearance.getNoseWidth());
        writeC(playerAppearance.getNoseTip());

        writeC(playerAppearance.getCheek());
        writeC(playerAppearance.getLipHeight());
        writeC(playerAppearance.getMouthSize());
        writeC(playerAppearance.getLipSize());
        writeC(playerAppearance.getSmile());
        writeC(playerAppearance.getLipShape());
        writeC(playerAppearance.getJawHeigh());
        writeC(playerAppearance.getChinJut());
        writeC(playerAppearance.getEarShape());
        writeC(playerAppearance.getHeadSize());
        // 1.5.x 0x00, shoulderSize, armLength, legLength (BYTE) after HeadSize

        writeC(playerAppearance.getNeck());
        writeC(playerAppearance.getNeckLength());
        writeC(playerAppearance.getShoulderSize());

        writeC(playerAppearance.getTorso());
        writeC(playerAppearance.getChest()); // only woman
        writeC(playerAppearance.getWaist());

        writeC(playerAppearance.getHips());
        writeC(playerAppearance.getArmThickness());
        writeC(playerAppearance.getHandSize());
        writeC(playerAppearance.getLegThicnkess());

        writeC(playerAppearance.getFootSize());
        writeC(playerAppearance.getFacialRate());

        writeC(0x01); // always 0
        writeC(playerAppearance.getArmLength());
        writeC(playerAppearance.getLegLength());
        writeC(playerAppearance.getShoulders());
        writeC(playerAppearance.getFaceShape());
        writeC(0x01); // always 0

        writeC(playerAppearance.getVoice());

        writeF(playerAppearance.getHeight());
        writeF(0.25f); // scale
        writeF(2.0f); // gravity or slide surface o_O
        writeF(player.getGameStats().getMovementSpeedFloat()); // move speed

        Stat2 attackSpeed = player.getGameStats().getAttackSpeed();
        writeH(attackSpeed.getBase());
        writeH(attackSpeed.getCurrent());
        //player.getPortAnimation() == 0 ? 3 : player.getPortAnimation()
        writeC(player.getPortAnimation() == 0 ? 3 : player.getPortAnimation());// port animation end location

        // new prison and ffa text
        writeS(player.hasStore() ? player.getStore().getStoreMessage() : player.getNickerText());// private store message

        /**
         * Movement
         */
        writeF(0);
        writeF(0);
        writeF(0);
        /*writeF(activePlayer.getX());// x
         writeF(activePlayer.getY());// y
         writeF(activePlayer.getZ());// z*/

        writeF(player.getX());// x
        writeF(player.getY());// y
        writeF(player.getZ());// z
        writeC(0); // move type player.getMoveController().getMovementMask() or 1

        if (player.isUsingFlyTeleport()) {
            // teleport master fly
            writeD(player.getFlightTeleportId());
            writeD(player.getFlightDistance());
        } else if (player.isInPlayerMode(PlayerMode.WINDSTREAM)) {
            // fly windstream
            writeD(player.windstreamPath.teleportId);
            writeD(player.windstreamPath.distance);
        }
        writeC(player.getVisualState()); // visualState
        writeS(player.getCommonData().getNote()); // note show in right down windows if target on player

        writeH(player.getLevel()); // [level]
        writeH(player.getPlayerSettings().getDisplay()); // unk - 0x04 - settings display
        writeH(player.getPlayerSettings().getDeny()); // unk - 0x00 - settings deny group, legion
        writeH(player.getAbyssRank().getRank().getId()); // abyss rank
        writeH(FfaPlayers.isInFFA(player) ? 1 : 0); // unk - 0x00
        writeD(player.getTarget() == null ? 0 : player.getTarget().getObjectId()); // target status
        writeC(player.getAccuseLevel() > 0 ? 1 : 0); // suspect id auto move
        writeD(player.getBonusTime().isBonus() ? 1 : 0); // bonus 1 - true, 0 - false
        writeC(player.isMentor() ? 1 : 0); // mentor group
        writeD(player.getHouseOwnerId()); // 3.0 status house yes or no
        writeD(player.getBonusTime().getStatus().getId()); // bonus buff id
    }
}
