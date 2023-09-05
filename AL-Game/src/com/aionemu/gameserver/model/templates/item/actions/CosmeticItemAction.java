/*
 * This file is part of aion-lightning <aion-lightning.com>
 *
 * aion-lightning is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-lightning is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-lightning. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.templates.item.actions;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerAppearanceDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerAppearance;
import com.aionemu.gameserver.model.templates.cosmeticitems.CosmeticItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import com.aionemu.gameserver.utils.PacketSendUtility;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author xTz
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CosmeticItemAction")
public class CosmeticItemAction extends AbstractItemAction {

    @XmlAttribute(name = "name")
    protected String cosmeticName;

    @Override
    public boolean canAct(Player player, Item parentItem, Item targetItem) {
        CosmeticItemTemplate template = DataManager.COSMETIC_ITEMS_DATA.getCosmeticItemsTemplate(cosmeticName);
        if (template == null) {
            return false;
        }
        if (!template.getRace().equals(player.getRace())) {
            return false;
        }
        if (!template.getGenderPermitted().equals("ALL")) {
            if (!player.getGender().toString().equals(template.getGenderPermitted())) {
                return false;
            }
        }
        return !player.getMoveController().isInMove();
    }

    @Override
    public void act(final Player player, Item parentItem, Item targetItem) {
        CosmeticItemTemplate template = DataManager.COSMETIC_ITEMS_DATA.getCosmeticItemsTemplate(cosmeticName);
        PlayerAppearance playerAppearance = player.getPlayerAppearance();
        String type = template.getType();
        int id = template.getId();
        switch (type) {
            case "hair_color":
                playerAppearance.setHairRGB(id);
                break;
            case "face_color":
                playerAppearance.setSkinRGB(id);
                break;
            case "lip_color":
                playerAppearance.setLipRGB(id);
                break;
            case "eye_color":
                playerAppearance.setEyeRGB(id);
                break;
            case "hair_type":
                playerAppearance.setHair(id);
                break;
            case "face_type":
                playerAppearance.setFace(id);
                break;
            case "voice_type":
                playerAppearance.setVoice(id);
                break;
            case "makeup_type":
                playerAppearance.setTattoo(id);
                break;
            case "tattoo_type":
                playerAppearance.setDeco(id);
                break;
            case "preset_name":
                CosmeticItemTemplate.Preset preset = template.getPreset();
                playerAppearance.setEyeRGB((preset.getEyeColor()));
                playerAppearance.setLipRGB((preset.getLipColor()));
                playerAppearance.setHairRGB((preset.getHairColor()));
                playerAppearance.setSkinRGB((preset.getEyeColor()));
                playerAppearance.setHair((preset.getHairType()));
                playerAppearance.setFace((preset.getFaceType()));
                playerAppearance.setHeight((preset.getScale()));
                break;
        }
        DAOManager.getDAO(PlayerAppearanceDAO.class).store(player);
        player.getInventory().delete(targetItem);
        PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
        player.clearKnownlist();
        player.updateKnownlist();
    }
}
