/*
 * SAO Project
 */
package com.aionemu.gameserver.model.gameobjects.state;

/**
 *
 * @author Alex
 */
public enum RateTitle {

    Новичок(0, 0, 0, "Новичок", ""),
    Рядовой(1, 1, 1, "<Рядовой>", ""),
    Ефрейтор(2, 50, 1, "<<Ефрейтор>>", ""),
    МладшийCержант(3, 100, 1, "<<<Младший Cержант>>>", ""),
    Сержант(4, 200, 1, "<<<*Сержант*>>>", ""),
    СтаршийCержант(5, 250, 1, "<<<**Старший Cержант**>>>", ""),
    Старшина(6, 300, 1, "\\*\\Старшина/*/", ""),
    Прапорщик(7, 350, 1, "=*Прапорщик*=", ""),
    СтаршийПрапорщик(8, 400, 1, "=**Старший Прапорщик**=", ""),
    МладшийЛейтенант(9, 500, 1, "***Младший Лейтенант***", ""),
    Лейтенант(10, 700, 1, "***|Лейтенант|***", ""),
    СтаршийЛейтенант(11, 900, 1, "***||Старший Лейтенант||***", ""),
    Капитан(12, 1200, 1, "***&(Капитан)&***", ""),
    Майор(13, 1700, 1, "***\\Майор/***", ""),
    Подполковник(14, 2000, 1, "<*\\\\Подполковник//*>", ""),
    Полковник(15, 3000, 1, "<<***Полковник***>>", ""),
    ГенералМайор(16, 4000, 1, "Генерал-Майор", ""),
    ГенералЛейтенант(17, 5000, 1, "Генерал-Лейтенант", ""),
    ГенералПолковник(18, 6000, 1, "Генерал-Полковник", ""),
    ГенералАрмии(19, 7000, 1, "Генерал-Армии", ""),
    Маршал(20, 10000, 1, "<<|***|Маршал|***|>>", ""),
    Господь(21, 10001, 2, "Господь", "");

    public static RateTitle getForId(int rankTitle) {
        for (RateTitle rt : RateTitle.values()) {
            if (rt.getId() == rankTitle) {
                return rt;
            }
        }
        return RateTitle.Новичок;
    }
    private final int id;
    private final int kills;
    private final int rate;
    private final String rusTitle;
    private final String color;

    RateTitle(int id, int kills, int rate, String rusTitle, String color) {
        this.id = id;
        this.kills = kills;
        this.rate = rate;
        this.rusTitle = rusTitle;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public int getKills() {
        return kills;
    }

    public int getRate() {
        return rate;
    }

    public String getRusTitle() {
        return rusTitle;
    }

    public String getColor() {
        return color;
    }

    public static RateTitle titleByKill(int kills) {
        RateTitle title = RateTitle.Новичок;
        for (RateTitle rt : RateTitle.values()) {
            if (kills >= rt.getKills()) {
                title = rt;
            }
        }
        return title;
    }
}
