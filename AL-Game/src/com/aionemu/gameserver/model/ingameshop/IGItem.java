/*
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 * aion-lightning is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-lightning is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.ingameshop;

import com.aionemu.gameserver.configs.main.InGameShopConfig;
import com.aionemu.gameserver.services.gmInterface.GMItnterfaseService;

/**
 * @author xTz
 * @modified Alex
 */
public class IGItem {

    private final int objectId;
    private final int itemId;
    private final long itemCount;
    private final long itemPriceCount;
    private final byte category;
    private final byte subCategory;
    private final int list;
    private int salesRanking;
    private final byte itemType;
    private final byte gift;
    private final String titleDescription;
    private final IGPriceType price_type;
    private final int priceItemId;
    private final String priceTypeRusname;
    private final int membership;

    public IGItem(int objectId, int itemId, long itemCount, long itemPrice, byte category, byte subCategory, int list, int salesRanking,
            byte itemType, byte gift, IGPriceType price_type, int price_item, int membership, String titleDescription) {
        this.objectId = objectId;
        this.itemId = itemId;
        this.itemCount = itemCount;
        this.itemPriceCount = itemPrice == 0 ? 1 : itemPrice;
        this.category = category;
        this.subCategory = subCategory;
        this.list = list;
        this.salesRanking = salesRanking;
        this.itemType = itemType;
        this.gift = gift;
        if (titleDescription.isEmpty()) {
            if (InGameShopConfig.SHOW_TYPE == 1) {
                this.titleDescription = GMItnterfaseService.getInstance().getItemByItemId(itemId).getRusname();
            } else {
                this.titleDescription = price_type.getRusname();
            }
        } else {
            this.titleDescription = titleDescription;
        }
        this.price_type = price_type;
        this.priceItemId = price_item;
        if (price_type == IGPriceType.ITEM_ID) {
            this.priceTypeRusname = GMItnterfaseService.getInstance().getItemByItemId(priceItemId).getRusname();
        } else {
            this.priceTypeRusname = price_type.getRusname();
        }
        this.membership = membership;
    }

    public IGPriceType getPriceType() {
        return price_type;
    }

    public int getPriceItemId() {
        return priceItemId;
    }

    public int getObjectId() {
        return objectId;
    }

    public int getItemId() {
        return itemId;
    }

    public long getItemCount() {
        return itemCount;
    }

    public long getItemPriceCount() {
        return itemPriceCount;
    }

    public byte getCategory() {
        return category;
    }

    public byte getSubCategory() {
        return subCategory;
    }

    public int getList() {
        return list;
    }

    public int getSalesRanking() {
        return salesRanking;
    }

    public byte getItemType() {
        return itemType;
    }

    public byte getGift() {
        return gift;
    }

    public String getTitleDescription() {
        return titleDescription;
    }

    public String getPriceTypeRusname() {
        return priceTypeRusname;
    }

    public void increaseSales() {
        salesRanking++;
    }

    public int getMembership() {
        return membership;
    }
}
