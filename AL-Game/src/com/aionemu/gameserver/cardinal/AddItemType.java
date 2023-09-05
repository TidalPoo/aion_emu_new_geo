/*
 * SAO Project
 */
package com.aionemu.gameserver.cardinal;

/**
 *
 * @author Alex
 */
public enum AddItemType {

    FAKE(0),
    GAMEMASTER(1),
    BUY(2),
    DROP(3),
    TRADEINTRADE(4),
    MAIL(5),
    SHOP(6),
    EVENT(7),
    QUEST(8),
    HOUSE(9),
    ADMINPANEL(10),
    USEITEM(11),
    INSTANCEREWARD(12),
    AI2(13),
    ONLINEBONUS(14),
    CRAFT(15),
    FFA(16),
    MIXFIGHT(17),
    PVP(18),
    HTMLBONUS(19),
    CUSTOM(20),
    PETREWARD(21),
    TRANSFER(22),
    HTMLSURVEY(23),
    MANASTONE(24),
    PRIVATESTORE(25),
    REPURCHASE(26),
    GATHER(27),
    BROKER(28),
    EXCHANGE(29),
    STARTEQUIP(30);

    private final int type;

    AddItemType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static AddItemType forAllTypes(int type) {
        for (AddItemType itemType : AddItemType.values()) {
            if (itemType.getType() == type) {
                return itemType;
            }
        }
        return null;
    }
}
