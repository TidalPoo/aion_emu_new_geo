package com.aionemu.gameserver.services.item;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.cardinal.AddItemType;
import com.aionemu.gameserver.cardinal.ItemLog;
import com.aionemu.gameserver.configs.main.LoggingConfig;
import com.aionemu.gameserver.dao.ItemStoneListDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.AionObject;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Equipment;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.ItemId;
import com.aionemu.gameserver.model.items.ManaStone;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.templates.item.ArmorType;
import com.aionemu.gameserver.model.templates.item.ItemCategory;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.quest.QuestItems;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemAddType;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemUpdateType;
import com.aionemu.gameserver.taskmanager.tasks.ExpireTimerTask;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import com.aionemu.gameserver.world.World;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author KID
 */
public class ItemService {

    private static final Logger log = LoggerFactory.getLogger("ITEM_LOG");

    public static final ItemUpdatePredicate DEFAULT_UPDATE_PREDICATE = new ItemUpdatePredicate(ItemAddType.ITEM_COLLECT, ItemUpdateType.INC_ITEM_COLLECT);

    public static void loadItemStones(Collection<Item> itemList) {
        if (itemList != null && itemList.size() > 0) {
            DAOManager.getDAO(ItemStoneListDAO.class).load(itemList);
        }
    }

    public static long addItem(Player player, int itemId, long count, AddItemType itemType, String text) {
        return addItem(player, itemId, count, DEFAULT_UPDATE_PREDICATE, itemType, text);
    }

    public static long addItem(Player player, int itemId, long count, ItemUpdatePredicate predicate, AddItemType itemType, String text) {
        return addItem(player, itemId, count, null, predicate, itemType, text);
    }

    /**
     * Add new item based on all sourceItem values
     *
     * @param player
     * @param sourceItem
     * @param itemType
     * @param text
     * @return
     */
    public static long addItem(Player player, Item sourceItem, AddItemType itemType, String text) {
        return addItem(player, sourceItem.getItemId(), sourceItem.getItemCount(), sourceItem, DEFAULT_UPDATE_PREDICATE, itemType, text);
    }

    public static long addItem(Player player, Item sourceItem, ItemUpdatePredicate predicate, AddItemType itemType, String text) {
        return addItem(player, sourceItem.getItemId(), sourceItem.getItemCount(), sourceItem, predicate, itemType, text);
    }

    public static long addItem(Player player, int itemId, long count, Item sourceItem, AddItemType itemType, String text) {
        return addItem(player, itemId, count, sourceItem, DEFAULT_UPDATE_PREDICATE, itemType, text);
    }

    /**
     * Add new item based on sourceItem values
     *
     * @param player
     * @param itemId
     * @param count
     * @param sourceItem
     * @param predicate
     * @param itemType
     * @param text
     * @return
     */
    public static long addItem(Player player, int itemId, long count, Item sourceItem, ItemUpdatePredicate predicate,
            AddItemType itemType, String text) {
        ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(itemId);
        if (count <= 0 && !itemTemplate.isKinah() || itemTemplate == null) {
            return 0;
        }
        Preconditions.checkNotNull(itemTemplate, "No item with id " + itemId);
        Preconditions.checkNotNull(predicate, "Predicate is not supplied");

        if (LoggingConfig.LOG_ITEM) {
            log.info("[ITEM] ID/Count"
                    + (LoggingConfig.ENABLE_ADVANCED_LOGGING ? "/Item Name - " + itemTemplate.getTemplateId() + "/" + count + "/"
                            + itemTemplate.getName() : " - " + itemTemplate.getTemplateId() + "/" + count) + " to player " + player.getName());
        }

        Storage inventory = player.getInventory();
        if (itemTemplate.isKinah()) {
            // quests do not add here
            inventory.increaseKinah(count);
            return 0;
        }

        if (itemTemplate.isStackable()) {
            count = addStackableItem(player, itemTemplate, count, predicate, itemType, text);
        } else {
            count = addNonStackableItem(player, itemTemplate, count, sourceItem, predicate, itemType, text);
        }

        if (inventory.isFull(itemTemplate.getExtraInventoryId()) && count > 0) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DICE_INVEN_ERROR);
        }
        return count;
    }

    /**
     * Add non-stackable item to inventory
     */
    private static long addNonStackableItem(Player player, ItemTemplate itemTemplate, long count, Item sourceItem, ItemUpdatePredicate predicate, AddItemType itemType, String text) {
        Storage inventory = player.getInventory();
        while (!inventory.isFull(itemTemplate.getExtraInventoryId()) && count > 0) {
            Item newItem = ItemFactory.newItem(itemTemplate.getTemplateId());
            newItem.setAddTime(new Timestamp(System.currentTimeMillis()));
            if (newItem.getExpireTime() != 0) {
                ExpireTimerTask.getInstance().addTask(newItem, player);
            }
            newItem.setAddItems(itemType.getType());
            newItem.setOwner(text);
            if (sourceItem != null) {
                copyItemInfo(sourceItem, newItem);
            }

            predicate.changeItem(newItem);
            inventory.add(newItem, predicate.getAddType());
            if (LoggingConfig.LOG_ITEMS) {
                player.addListItemLog(new ItemLog(player.getPlayerAccount().getId(), player.getObjectId(), player.getName(), player.getClientConnection().getIP(), player.getClientConnection().getMacAddress(), itemType.getType(), itemTemplate.getTemplateId(), count, text));
            }
            count--;
        }
        return count;
    }

    /**
     * Copy some item values like item stones and enchant level
     */
    private static void copyItemInfo(Item sourceItem, Item newItem) {
        newItem.setOptionalSocket(sourceItem.getOptionalSocket());
        newItem.setItemCreator(sourceItem.getItemCreator());
        if (sourceItem.hasManaStones()) {
            for (ManaStone manaStone : sourceItem.getItemStones()) {
                ItemSocketService.addManaStone(newItem, manaStone.getItemId());
            }
        }
        if (sourceItem.getGodStone() != null) {
            newItem.addGodStone(sourceItem.getGodStone().getItemId());
        }
        if (sourceItem.getEnchantLevel() > 0) {
            newItem.setEnchantLevel(sourceItem.getEnchantLevel());
        }
        if (sourceItem.isSoulBound()) {
            newItem.setSoulBound(true);
        }
        newItem.setBonusNumber(sourceItem.getBonusNumber());
        newItem.setRandomStats(sourceItem.getRandomStats());
        newItem.setRandomCount(sourceItem.getRandomCount());
        newItem.setIdianStone(sourceItem.getIdianStone());
        newItem.setItemColor(sourceItem.getItemColor());
        newItem.setItemSkinTemplate(sourceItem.getItemSkinTemplate());
        newItem.setAddItems(sourceItem.getAddItems());
        newItem.setOwner(sourceItem.getOwner());
    }

    /**
     * Add stackable item to inventory
     */
    private static long addStackableItem(Player player, ItemTemplate itemTemplate, long count, ItemUpdatePredicate predicate, AddItemType itemType, String owner) {
        Storage inventory = player.getInventory();
        Collection<Item> items = inventory.getItemsByItemId(itemTemplate.getTemplateId());
        for (Item item : items) {
            if (count == 0) {
                break;
            }
            count = inventory.increaseItemCount(item, count, predicate.getUpdateType(item, true));
        }

        // dirty & hacky check for arrows and shards...
        if (itemTemplate.getCategory() != ItemCategory.SHARD || itemTemplate.getArmorType() == ArmorType.ARROW) {
            Equipment equipement = player.getEquipment();
            items = equipement.getEquippedItemsByItemId(itemTemplate.getTemplateId());
            for (Item item : items) {
                if (count == 0) {
                    break;
                }
                count = equipement.increaseEquippedItemCount(item, count);
            }
        }

        while (!inventory.isFull(itemTemplate.getExtraInventoryId()) && count > 0) {
            Item newItem = ItemFactory.newItem(itemTemplate.getTemplateId(), count, itemType, owner);
            newItem.setAddTime(new Timestamp(System.currentTimeMillis()));
            count -= newItem.getItemCount();
            inventory.add(newItem, predicate.getAddType());
            if (LoggingConfig.LOG_ITEMS) {
                player.addListItemLog(new ItemLog(player.getPlayerAccount().getId(), player.getObjectId(), player.getName(), player.getClientConnection().getIP(), player.getClientConnection().getMacAddress(), itemType.getType(), itemTemplate.getTemplateId(), newItem.getItemCount(), owner));
            }
        }
        return count;
    }

    public static boolean addQuestItems(Player player, List<QuestItems> questItems) {
        return addQuestItems(player, questItems, DEFAULT_UPDATE_PREDICATE);
    }

    public static boolean addQuestItems(Player player, List<QuestItems> questItems, ItemUpdatePredicate predicate) {
        int slotReq = 0, specialSlot = 0;

        for (QuestItems qi : questItems) {
            if (qi.getItemId() != ItemId.KINAH.value() && qi.getCount() != 0) {
                ItemTemplate template = DataManager.ITEM_DATA.getItemTemplate(qi.getItemId());
                long stackCount = template.getMaxStackCount();
                long count = qi.getCount() / stackCount;
                if (qi.getCount() % stackCount != 0) {
                    count++;
                }
                if (template.getExtraInventoryId() > 0) {
                    specialSlot += count;
                } else {
                    slotReq += count;
                }
            }
        }
        Storage inventory = player.getInventory();
        if (slotReq > 0 && inventory.getFreeSlots() < slotReq) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DECOMPRESS_INVENTORY_IS_FULL);
            return false;
        }
        if (specialSlot > 0 && inventory.getSpecialCubeFreeSlots() < specialSlot) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DECOMPRESS_INVENTORY_IS_FULL);
            return false;
        }
        for (QuestItems qi : questItems) {
            addItem(player, qi.getItemId(), qi.getCount(), predicate, AddItemType.QUEST, "questid: " + qi.getQuestId());
        }
        return true;
    }

    public static void releaseItemId(Item item) {
        IDFactory.getInstance().releaseId(item.getObjectId());
    }

    public static void releaseItemIds(Collection<Item> items) {
        Collection<Integer> idIterator = Collections2.transform(items, AionObject.OBJECT_TO_ID_TRANSFORMER);
        IDFactory.getInstance().releaseIds(idIterator);
    }

    public static class ItemUpdatePredicate {

        private final ItemUpdateType itemUpdateType;
        private final ItemAddType itemAddType;

        public ItemUpdatePredicate(ItemAddType itemAddType, ItemUpdateType itemUpdateType) {
            this.itemUpdateType = itemUpdateType;
            this.itemAddType = itemAddType;
        }

        public ItemUpdatePredicate() {
            this(ItemAddType.ITEM_COLLECT, ItemUpdateType.INC_ITEM_COLLECT);
        }

        public ItemUpdateType getUpdateType(Item item, boolean isIncrease) {
            if (item.getItemTemplate().isKinah()) {
                return ItemUpdateType.getKinahUpdateTypeFromAddType(itemAddType, isIncrease);
            }
            return itemUpdateType;
        }

        public ItemAddType getAddType() {
            return itemAddType;
        }

        public boolean changeItem(Item item) {
            return true;
        }
    }

    public static boolean dropItemToInventory(int playerObjectId, int itemId) {
        return dropItemToInventory(World.getInstance().findPlayer(playerObjectId), itemId);
    }

    public static boolean dropItemToInventory(Player player, int itemId) {
        if (player == null || !player.isOnline()) {
            return false;
        }

        Storage storage = player.getInventory();
        if (storage.getFreeSlots() < 1) {
            List<Item> items = storage.getItemsByItemId(itemId);
            boolean hasFreeStack = false;
            for (Item item : items) {
                if (item.getPersistentState() == PersistentState.DELETED || item.getItemCount() < item.getItemTemplate().getMaxStackCount()) {
                    hasFreeStack = true;
                    break;
                }
            }
            if (!hasFreeStack) {
                return false;
            }
        }
        // TODO: check the exact type in retail
        return addItem(player, itemId, 1, new ItemUpdatePredicate() {

            @Override
            public boolean changeItem(Item item) {
                item.setAddItems(AddItemType.EVENT.getType());
                item.setItemCreator("\ue01FSAO\ue01F");
                return true;
            }
        }, AddItemType.EVENT, null) == 0;
    }

    public static boolean checkRandomTemplate(int randomItemId) {
        ItemTemplate template = DataManager.ITEM_DATA.getItemTemplate(randomItemId);
        return template != null;
    }
}
