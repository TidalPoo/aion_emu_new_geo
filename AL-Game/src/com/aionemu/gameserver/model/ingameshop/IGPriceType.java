/*
 * AionLight project
 */
package com.aionemu.gameserver.model.ingameshop;

import com.aionemu.gameserver.configs.main.CustomConfig;

/**
 *
 * @author Alex
 */
public enum IGPriceType {

    TOLL(CustomConfig.COL),
    KINAH("Кинары"),
    ITEM_ID("item_id"),
    AP("Очки Бездны"),
    FREE("Бесплатно");
    String rusname;

    IGPriceType(String name) {
        this.rusname = name;
    }

    public String getRusname() {
        return rusname;
    }
}
