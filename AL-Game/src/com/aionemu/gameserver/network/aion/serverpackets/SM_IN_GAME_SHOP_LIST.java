/*
 * This file is part of gtemu.net <gtemu.net>.
 *
 * gtemu.net is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * gtemu.net is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with gtemu.net.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.configs.main.InGameShopConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.ingameshop.IGItem;
import com.aionemu.gameserver.model.ingameshop.InGameShopEn;
import com.aionemu.gameserver.model.templates.item.ArmorType;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.item.ItemUseLimits;
import com.aionemu.gameserver.model.templates.item.WeaponType;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.Collection;
import java.util.List;
import javolution.util.FastList;

/**
 * @author xTz, KID
 * @modified Alex
 */
public class SM_IN_GAME_SHOP_LIST extends AionServerPacket {

    private final Player player;
    private final int nrList;
    private final int salesRanking;
    private final TIntObjectHashMap<FastList<IGItem>> allItems = new TIntObjectHashMap<>();

    public SM_IN_GAME_SHOP_LIST(Player player, int nrList, int salesRanking) {
        this.player = player;
        this.nrList = nrList;
        this.salesRanking = salesRanking;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        List<IGItem> inAllItems;
        Collection<IGItem> items;
        byte category = player.inGameShop.getCategory();
        byte subCategory = player.inGameShop.getSubCategory();
        if (salesRanking == 1) {
            items = InGameShopEn.getInstance().getItems(category);
            int size = 0;
            int tabSize = 9;
            int f = 0;
            for (IGItem a : items) {
                if (subCategory != 2) {
                    if (a.getSubCategory() != subCategory) {
                        continue;
                    }
                }
                /* if (!isAvailableShow(player, a)) {
                 continue;
                 }*/
                if (size == tabSize) {
                    tabSize += 9;
                    f++;//page
                }
                FastList<IGItem> template = allItems.get(f);
                if (template == null) {
                    template = FastList.newInstance();
                    allItems.put(f, template);
                }

                template.add(a);
                size++;
            }

            inAllItems = allItems.get(nrList);
            writeD(salesRanking);
            writeD(nrList);
            writeD(size > 0 ? tabSize : 0);
            writeH(inAllItems == null ? 0 : inAllItems.size());
            if (inAllItems != null) {
                for (IGItem item : inAllItems) {
                    writeD(item.getObjectId());
                }
            }
        } else {
            FastList<Integer> salesRankingItems = InGameShopEn.getInstance().getTopSales(subCategory, category);
            writeD(salesRanking);
            writeD(nrList);
            writeD((InGameShopEn.getInstance().getMaxList(subCategory, category) + 1) * 9);
            writeH(salesRankingItems.size());
            for (int id : salesRankingItems) {
                writeD(id);
            }

            FastList.recycle(salesRankingItems);
        }
    }

    private boolean isAvailableShow(Player player, IGItem a) {
        if (InGameShopConfig.ALLOW_GIFTS) {
            return true;
        }
        if (InGameShopConfig.SHOP_MEMBERSHIP_TYPE == 1 && !player.havePermission((byte) a.getMembership())) {
            return false;
        }
        Race r = player.getRace();
        ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(a.getItemId());
        if (r != null && r != Race.PC_ALL && r != player.getRace()) {
            return false;
        }

        ItemUseLimits limits = itemTemplate.getUseLimits();
        if (limits != null && limits.getGenderPermitted() != null && limits.getGenderPermitted() != player.getGender()) {
            return false;
        }

        if (!itemTemplate.isClassSpecific(player.getPlayerClass())) {
            return false;
        }

        ArmorType at = itemTemplate.getArmorType();
        if (at != null) {
            int[] requiredSkillsArmor = at.getRequiredSkills();
            if (requiredSkillsArmor != null && !checkAvailableEquipSkills(player, requiredSkillsArmor)) {
                return false;
            }
        }
        WeaponType wt = itemTemplate.getWeaponType();
        if (wt != null) {
            int[] requiredSkillsWeapon = wt.getRequiredSkills();
            if (!checkAvailableEquipSkills(player, requiredSkillsWeapon)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param requiredSkills
     * @return
     */
    private boolean checkAvailableEquipSkills(Player owner, int[] requiredSkills) {
        boolean isSkillPresent = false;

        if (requiredSkills.length == 0) {
            return true;
        }

        for (int skill : requiredSkills) {
            if (owner.getSkillList().isSkillPresent(skill)) {
                isSkillPresent = true;
                break;
            }
        }
        return isSkillPresent;
    }

}
