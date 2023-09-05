/*
 * AionLight project
 */
package com.aionemu.gameserver.services;

import com.aionemu.gameserver.configs.main.MembershipConfig;

/**
 *
 * @author Alex
 */
public enum MembershipEnum {

    MembershipPlayer(MembershipConfig.TAG_PLAYER, "Обычный", 0),
    MembershipPremium(MembershipConfig.TAG_PREMIUM, "\ue038ПРЕМИУМ\ue038", 1),
    MembershipVip(MembershipConfig.TAG_VIP, "\ue038VIP\ue038", 2),
    MembershipAdmin("(AION)", "Admin", 3);

    private String name;
    private int level;
    private String s;

    MembershipEnum(String name, String s, int level) {
        this.name = name;
        this.s = s;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public String getS() {
        return s;
    }

    public int getLevel() {
        return level;
    }

    public static MembershipEnum getAlType(int level) {
        switch (level) {
            case 0:
                return MembershipEnum.MembershipPlayer;
            case 1:
                return MembershipEnum.MembershipPremium;
            case 2:
                return MembershipEnum.MembershipVip;
            case 3:
                return MembershipEnum.MembershipAdmin;
        }
        return null;
    }
}
