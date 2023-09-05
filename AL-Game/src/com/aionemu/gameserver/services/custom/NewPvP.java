/*
 * AionLight project
 */
package com.aionemu.gameserver.services.custom;

import com.aionemu.gameserver.cardinal.AddItemType;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.itemset.ItemPart;
import com.aionemu.gameserver.model.templates.itemset.ItemSetTemplate;
import com.aionemu.gameserver.services.item.ItemService;

/**
 *
 * @author Alex
 */
public class NewPvP {

    public static void addItem(Player player) {
        int setId = 0;
        int[] itemsOne = null;
        int[] itemsTwo = null;
        switch (player.getPlayerClass()) {
            // глад
            case GLADIATOR:
                setId = 338;
                itemsOne = new int[]{101301018, 125002308, 121001163, 123001221, 190020149};
                itemsTwo = new int[]{100901081, 120001247, 122001406};
                break;
            // снайпер
            case GUNNER:
                setId = 347;
                itemsOne = new int[]{101800859, 121001164, 123001222, 125002306, 190020149};
                itemsTwo = new int[]{101900857, 120001248, 122001407};
                break;
            // бард
            case BARD:
                setId = 362;
                itemsOne = new int[]{102000891, 121001164, 123001222, 125002305, 190020149};
                itemsTwo = new int[]{120001248, 122001407};
                break;
            // страж
            case TEMPLAR:
                setId = 335;
                itemsOne = new int[]{115001476, 125002308, 121001163, 123001221, 190020149};
                itemsTwo = new int[]{122001406, 120001247, 100901081};
                break;
            // син
            case ASSASSIN:
                setId = 341;
                itemsOne = new int[]{100201207, 121001163, 123001221, 125002306, 190020149};
                itemsTwo = new int[]{100001360, 120001247, 122001406};
                break;
            // стрелок
            case RANGER:
                setId = 344;
                itemsOne = new int[]{121001163, 123001221, 125002306, 190020149};
                itemsTwo = new int[]{101701111, 120001247, 122001406};
                break;
            // волшебник
            case SORCERER:
                setId = 356;
                itemsOne = new int[]{100501050, 121001164, 123001222, 125002305, 190020149};
                itemsTwo = new int[]{100601106, 120001248, 122001407};
                break;
            case SPIRIT_MASTER:
                setId = 359;
                itemsOne = new int[]{100601106, 121001164, 123001222, 125002305, 190020149};
                itemsTwo = new int[]{100501050, 120001248, 122001407};
                break;
            case CLERIC:
                setId = 350;
                itemsOne = new int[]{115001476, 121001164, 125002307, 123001222, 190020149};
                itemsTwo = new int[]{101501070, 120001248, 122001407};
                break;
            case CHANTER:
                setId = 353;
                itemsOne = new int[]{115001476, 100101038, 121001163, 121001163, 123001221, 125002307, 190020149};
                itemsTwo = new int[]{101501070, 120001247, 122001406};
                break;
        }
        int[] items = new int[]{164002010, 164002012, 164000134, 164000076, 164000118, 164000122, 162000023, 162000124, 160010272, 160010100};
        if (setId != 0) {
            ItemSetTemplate itemSet = DataManager.ITEM_SET_DATA.getItemSetTemplate(setId);
            if (itemSet != null) {
                for (ItemPart setPart : itemSet.getItempart()) {
                    ItemService.addItem(player, setPart.getItemid(), 1, AddItemType.PVP, null);
                }
            }
        }
        for (int itemId : itemsOne) {
            ItemService.addItem(player, itemId, 1, AddItemType.PVP, null);
        }
        for (int itemId : itemsTwo) {
            ItemService.addItem(player, itemId, 2, AddItemType.PVP, null);
        }
        for (int itemId : items) {
            ItemService.addItem(player, itemId, 1, AddItemType.PVP, null);
        }
        player.getSkillList().addSkill(player, 30002, 499);
        player.getSkillList().addSkill(player, 30003, 499);

        player.getSkillList().addSkill(player, 40001, 550);
        player.getSkillList().addSkill(player, 40002, 550);
        player.getSkillList().addSkill(player, 40003, 550);
        player.getSkillList().addSkill(player, 40004, 550);
        player.getSkillList().addSkill(player, 40007, 550);
        player.getSkillList().addSkill(player, 40008, 550);
        player.getSkillList().addSkill(player, 40010, 550);
    }
}
