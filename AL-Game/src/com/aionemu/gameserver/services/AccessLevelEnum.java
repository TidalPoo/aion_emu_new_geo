package com.aionemu.gameserver.services;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.main.GSConfig;

/**
 * @author Alex
 */
public enum AccessLevelEnum {

    //Access Level
    AccessLevel0(0, "%s", "Игрок", new int[]{0}, "%s!"),
    AccessLevel1(1, AdminConfig.CUSTOMTAG_ACCESS1, "\ue042Помощник гейм-мастера\ue043", new int[]{174, 175}, "Приветствую тебя помощник " + GSConfig.SERVER_NAME + " %s ! Помните администрация сервера расчитывает на Вас!"),
    AccessLevel2(2, AdminConfig.CUSTOMTAG_ACCESS2, "\ue042Следящий за порядком\ue043", new int[]{174, 175, 1904, 1911}, "Приветствую тебя следящий за порядком %s ! Помните администрация сервера расчитывает на Вас!"),
    AccessLevel3(3, AdminConfig.CUSTOMTAG_ACCESS3, "\ue042Ивент ГМ\ue043", new int[]{174, 175, 1904, 1911}, "Приветствую тебя Ивент ГМ %s ! Помните администрация сервера расчитывает на Вас!"),
    AccessLevel4(4, AdminConfig.CUSTOMTAG_ACCESS4, "\ue042Главный ГМ\ue043", new int[]{174, 175, 1904, 1911}, "Приветствую тебя Главный ГМ %s ! Помните администрация сервера расчитывает на Вас!"),
    AccessLevel5(5, AdminConfig.CUSTOMTAG_ACCESS5, "\ue042Администратор\ue043", new int[]{174, 175, 1904, 1911, 3224, 3226, 3227, 3232, 3233, 3234, 3235, 3236, 3237, 3238, 3239, 3240, 3241}, "Приветствую тебя Администратор %s!"),
    AccessLevel6(6, AdminConfig.CUSTOMTAG_ACCESS6, "\ue042Зам. главного Администратора\ue043", new int[]{174, 175, 1904, 1911, 3224, 3226, 3227, 3232, 3233, 3234, 3235, 3236, 3237, 3238, 3239, 3240, 3241, 3558, 3576, 3581}, "Приветствую тебя Разработчик %s"),
    AccessLevel7(7, "DEV-AION", "\ue042Главный Администратор проекта\ue043", new int[]{174, 175, 1904, 1911, 3224, 3226, 3227, 3232, 3233, 3234, 3235, 3236, 3237, 3238, 3239, 3240, 3241, 3558, 3576, 3581}, "Приветствую тебя Главный Администратор %s"),
    AccessLevel8(8, "DEV-AION", "\ue042Главный Администратор проекта\ue043", new int[]{174, 175, 1904, 1911, 3224, 3226, 3227, 3232, 3233, 3234, 3235, 3236, 3237, 3238, 3239, 3240, 3241, 3558, 3576, 3581}, "Приветствую тебя Главный Администратор %s"),
    AccessLevel9(9, "DEV-AION", "\ue042Главный Администратор проекта\ue043", new int[]{174, 175, 1904, 1911, 3224, 3226, 3227, 3232, 3233, 3234, 3235, 3236, 3237, 3238, 3239, 3240, 3241, 3558, 3576, 3581}, "Приветствую тебя Главный Администратор %s"),
    AccessLevel10(10, "DEV-AION", "\ue042Главный Администратор проекта\ue043", new int[]{174, 175, 1904, 1911, 3224, 3226, 3227, 3232, 3233, 3234, 3235, 3236, 3237, 3238, 3239, 3240, 3241, 3558, 3576, 3581}, "Приветствую тебя Главный Администратор %s"),
    ffaZone("\ue042ServerAionOnline\ue043");
    private final int level;
    private final String nameLevel;
    private String status;
    private int[] skills;
    private String notice;

    AccessLevelEnum(String rusname) {
        this(0, null, rusname, new int[]{0}, null);
    }

    AccessLevelEnum(int id, String name, String status, int[] skills, String notice) {
        this.level = id;
        this.nameLevel = name;
        this.status = status;
        this.skills = skills;
        this.notice = notice;
    }

    public String getNotice(String name) {
        return String.format(notice, name);
    }

    public String getName() {
        return nameLevel;
    }

    public int getLevel() {
        return level;
    }

    public String getStatusName() {
        return status;
    }

    public int[] getSkills() {
        return skills;
    }

    public static AccessLevelEnum getAlType(int level) {
        switch (level) {
            case 0:
                return AccessLevelEnum.AccessLevel0;
            case 1:
                return AccessLevelEnum.AccessLevel1;
            case 2:
                return AccessLevelEnum.AccessLevel2;
            case 3:
                return AccessLevelEnum.AccessLevel3;
            case 4:
                return AccessLevelEnum.AccessLevel4;
            case 5:
                return AccessLevelEnum.AccessLevel5;
            case 6:
                return AccessLevelEnum.AccessLevel6;
            case 7:
                return AccessLevelEnum.AccessLevel7;
            default:
                return AccessLevelEnum.AccessLevel0;
        }
    }
}
