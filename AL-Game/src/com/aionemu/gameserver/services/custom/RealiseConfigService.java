/*
 * AionLight project
 */
package com.aionemu.gameserver.services.custom;

import com.aionemu.gameserver.cardinal.AddItemType;
import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.StorageType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CUBE_UPDATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_UPDATE_PLAYER_APPEARANCE;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.ClassChangeService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 *
 * @author Alex
 */
public class RealiseConfigService {

    public static void onEnableConfigurationToEnterWorld(Player player) {
        // set level 65 if enable config parameter
        int level = GSConfig.PLAYER_MAX_LEVEL;
        if (CustomConfig.ENABLE_MAX_LEVEL && player.getLevel() < level) {
            player.getCommonData().setLevel(level);
        }

        // Fully extend the cube
        int cube = CustomConfig.BASIC_CUBE_SIZE_LIMIT;
        if (CustomConfig.ENABLE_CUBE_MAX_LIMIT && player.getNpcExpands() < cube) {
            for (int i = 0; i < cube; i++) {
                player.getCommonData().setNpcExpands(player.getNpcExpands() + 1);
                player.setCubeLimit();
                PacketSendUtility.sendPacket(player, SM_CUBE_UPDATE.cubeSize(StorageType.CUBE, player, false));
            }
            PacketSendUtility.sendMessage(player, "\u0412\u0430\u0448 \u043a\u0443\u0431 \u043c\u0430\u043a\u0441\u0438\u043c\u0430\u043b\u044c\u043d\u043e \u0440\u0430\u0441\u0448\u0438\u0440\u0435\u043d!");
        }
        int questComplete = player.getRace() == Race.ELYOS ? 1929 : 2900;
        if (player.havePermission(MembershipConfig.STIGMA_SLOT_QUEST)) {
            if (player.getQuestStateList().getQuestState(questComplete) != null && player.getQuestStateList().getQuestState(questComplete).getStatus() != QuestStatus.COMPLETE) {
                ClassChangeService.completeQuest(player, questComplete);
            } else if (player.getCommonData().getAdvancedStigmaSlotSize() < 11) {
                player.getCommonData().setAdvancedStigmaSlotSize(11);
                PacketSendUtility.sendPacket(player, SM_CUBE_UPDATE.stigmaSlots(player.getCommonData().getAdvancedStigmaSlotSize(), false));
            }
        }
        if (AdminConfig.ENABLE_GM_NO_EQUIP_ITEM) {
            if (player.getAccessLevel() > 0 && player.getAccessLevel() < 7) {
                int itemId = 110900339;
                if (player.getEquipment().getEquippedItemsByItemId(itemId) == null) {
                    for (Item item : player.getEquipment().getEquippedItems()) {
                        player.getEquipment().unEquipItem(item.getObjectId(), item.getEquipmentSlot());
                    }
                }
                if (player.getInventory().getFirstItemByItemId(itemId) == null) {
                    ItemService.addItem(player, itemId, 1, AddItemType.GAMEMASTER, GSConfig.SERVER_NAME);
                }
                Item it = player.getInventory().getFirstItemByItemId(itemId);
                if (it != null && player.getEquipment().getEquippedItemsByItemId(itemId) == null) {
                    player.getEquipment().equipItem(it.getObjectId(), it.getItemTemplate().getItemSlot());
                    PacketSendUtility.broadcastPacket(player, new SM_UPDATE_PLAYER_APPEARANCE(player.getObjectId(),
                            player.getEquipment().getEquippedForApparence()), true);
                }
                player.setBanItems(true);
            }
        }
    }
}
