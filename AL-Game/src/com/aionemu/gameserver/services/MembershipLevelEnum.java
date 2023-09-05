/*
 * AionLight project
 */
package com.aionemu.gameserver.services;

import com.aionemu.gameserver.configs.main.MembershipConfig;

/**
 *
 * @author Alex
 */
public enum MembershipLevelEnum {

    Member0(0, MembershipConfig.TAG_PLAYER, "Игрок", 0, 0),
    premium1(1, MembershipConfig.TAG_PREMIUM, "Премиум", MembershipConfig.MONEY_PREMIUM_7_DAY, MembershipConfig.MONEY_PREMIUM_30_DAY),
    vip1(2, MembershipConfig.TAG_VIP, "VIP", MembershipConfig.MONEY_VIP_7_DAY, MembershipConfig.MONEY_VIP_30_DAY);

    private int id;
    private String name;
    private String status;
    private final int donatPrice7;
    private final int donatPrice30;

    MembershipLevelEnum(int id, String name, String status, int donatPrice7, int donatPrice30) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.donatPrice7 = donatPrice7;
        this.donatPrice30 = donatPrice30;
    }

    public int getDonatPrice7() {
        return donatPrice7;
    }

    public int getDonatPrice30() {
        return donatPrice30;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatusName() {
        return status;
    }

    public static MembershipLevelEnum getMlType(int level) {
        switch (level) {
            case 0:
                return MembershipLevelEnum.Member0;
            case 1:
                return MembershipLevelEnum.premium1;
            case 2:
                return MembershipLevelEnum.vip1;
        }
        return null;
    }
}
