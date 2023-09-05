/*
 * AionLight project
 */
package com.aionemu.gameserver.services.craft;

/**
 *
 * @author Alex
 */
public enum CraftSkillEnum {

    craft1(30001, 49, "Сбор", true),
    craft2(30002, 499, "Энергокинез", true),
    craft3(30003, 499, "Эфирокинез", false),
    craft4(40001, 549, "Кулинария", false),
    craft5(40002, 549, "Оружейное дело", false),
    craft6(40003, 549, "Кузнечное дело", false),
    craft7(40004, 549, "Портяжное дело", false),
    craft8(40005, 99, "Кожевенное дело", false),
    craft9(40006, 99, "Столярное дело", false),
    craft10(40007, 549, "Алхимия", false),
    craft11(40008, 549, "Ювелирное дело", false),
    craft12(40009, 1, "Преобразование материалов", false),
    craft13(40010, 549, "Изготовление мебели", false);

    private final int id;
    private final int level;
    private final String rusname;
    private final boolean added;

    CraftSkillEnum(int id, int level, String rusname, boolean added) {
        this.id = id;
        this.level = level;
        this.rusname = rusname;
        this.added = added;
    }

    public int getId() {
        return id;
    }

    public String getRusname() {
        return rusname;
    }

    public boolean isAdded() {
        return added;
    }

    public int getLevel() {
        return level;
    }
}
