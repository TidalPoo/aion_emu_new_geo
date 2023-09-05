/*
 * AionLight project
 */
package com.aionemu.gameserver.services;

/**
 *
 * @author Alex
 */
public enum SiegeMapEnum {

    //крепости
    fortess1(1011, "Крепость святости"),
    fortess2(1131, "Западная крепость Сиэли"),
    fortess3(1132, "Восточная крепость Сиэли"),
    fortess4(1141, "Крепость серного дерева"),
    fortess5(1211, "Крепость древнего города Ру"),
    fortess6(1221, "Крепость Кротан"),
    fortess7(1231, "Крепость Ткисас"),
    fortess8(1241, "Крепость Ра-Мирэн"),//1
    fortess9(1251, "Крепость Астерия"),
    fortess10(2011, "Храм древнего дракона"),
    fortess11(2021, "Алтарь алчности"),
    fortess12(3011, "Запечатанная башня"),
    fortess13(3021, "Храм красной земли"),
    fortess14(5011, "Крепость Силлус"),
    fortess15(6011, "Крепость Базен"),
    fortess16(6021, "Крепость Парадес"),
    //истоки
    source1(4011, "Источник разлома"),
    source2(4021, "Источник гравитации"),
    source3(4031, "Источник гнева"),
    source4(4041, "Источник окаменения");

    private final int id;
    private final String rusname;

    public int getId() {
        return id;
    }

    public String getRusname() {
        return rusname;
    }

    SiegeMapEnum(int id, String rusname) {
        this.id = id;
        this.rusname = rusname;
    }

    public static SiegeMapEnum getMap(int siegeId) {
        for (SiegeMapEnum s : SiegeMapEnum.values()) {
            if (s.getId() == siegeId) {
                return s;
            }
        }
        return null;
    }
}
