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
package com.aionemu.gameserver.model.templates.item.actions;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.templates.item.Acquisition;
import com.aionemu.gameserver.model.templates.item.ArmorType;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.utils.audit.AuditLogger;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Rolandas, Luzien
 */
public class ApExtractAction extends AbstractItemAction {

    @XmlAttribute
    protected UseTarget target;
    @XmlAttribute
    protected float rate;

    @Override
    public boolean canAct(Player player, Item parentItem, Item targetItem) {
        if (targetItem == null || !targetItem.canApExtract()) {
            return false;
        }
        if (parentItem.getItemTemplate().getLevel() < targetItem.getItemTemplate().getLevel()) {
            return false;
        }
        if (parentItem.getItemTemplate().getItemQuality() != targetItem.getItemTemplate().getItemQuality()) {
            return false;
        }

        // TODO: ApExtractTarget.OTHER, ApExtractTarget.ALL. Find out what should go there
        UseTarget type;
        switch (targetItem.getItemTemplate().getCategory()) {
            case SWORD:
            case DAGGER:
            case MACE:
            case ORB:
            case SPELLBOOK:
            case BOW:
            case GREATSWORD:
            case POLEARM:
            case STAFF:
            case SHIELD:
            case HARP:
            case GUN:
            case CANNON:
                type = UseTarget.WEAPON;
                break;
            case JACKET:
            case PANTS:
            case SHOES:
            case GLOVES:
            case SHOULDERS:
                type = UseTarget.ARMOR;
                break;
            case NECKLACE:
            case EARRINGS:
            case RINGS:
            case HELMET:
            case BELT:
                type = UseTarget.ACCESSORY;
                break;
            case NONE:
                if (targetItem.getItemTemplate().getArmorType() == ArmorType.WING) {
                    type = UseTarget.WING;
                    break;
                }
                return false;
            default:
                return false;
        }
        return (target == UseTarget.EQUIPMENT || target == type);
    }

    @Override
    public void act(Player player, Item parentItem, Item targetItem) {
        Acquisition acquisition = targetItem.getItemTemplate().getAcquisition();
        if (acquisition == null || acquisition.getRequiredAp() == 0) {
            return;
        }
        int ap = (int) (acquisition.getRequiredAp() * rate);
        Storage inventory = player.getInventory();

        if (inventory.delete(targetItem) != null) {
            if (inventory.decreaseByObjectId(parentItem.getObjectId(), 1)) {
                AbyssPointsService.addAp(player, ap);
            }
        } else {
            AuditLogger.info(player, "Possible extract item hack, do not remove item.");
        }
    }

    public UseTarget getTarget() {
        return target;
    }

    public float getRate() {
        return rate;
    }
}
