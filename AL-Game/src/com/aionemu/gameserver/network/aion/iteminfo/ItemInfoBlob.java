/*
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.iteminfo;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatFunction;
import com.aionemu.gameserver.model.templates.item.ArmorType;
import com.aionemu.gameserver.model.templates.item.EquipType;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.PacketWriteHelper;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Entry item info packet data (contains blob entries with detailed info).
 *
 * @author -Nemesiss-
 * @modified Rolandas - complete rewrite, no trees (blob size must be known in
 * advance!), just sequences
 */
public class ItemInfoBlob extends PacketWriteHelper {

    protected final Player player;
    protected final Item item;

    private final List<ItemBlobEntry> itemBlobEntries = new ArrayList<>();

    public ItemInfoBlob(Player player, Item item) {
        this.player = player;
        this.item = item;
    }

    @Override
    public void writeMe(ByteBuffer buf) {
        writeH(buf, size());
        itemBlobEntries.stream().forEach((ent) -> {
            ent.writeMe(buf);
        });
    }

    public void addBlobEntry(ItemBlobType type) {
        ItemBlobEntry ent = type.newBlobEntry();
        ent.setOwner(player, item, null);
        itemBlobEntries.add(ent);
    }

    public void addBonusBlobEntry(IStatFunction modifier) {
        ItemBlobEntry ent = ItemBlobType.STAT_BONUSES.newBlobEntry();
        ent.setOwner(player, item, modifier);
        itemBlobEntries.add(ent);
    }

    public static ItemBlobEntry newBlobEntry(ItemBlobType type, Player player, Item item) {
        if (type == ItemBlobType.STAT_BONUSES) {
            throw new UnsupportedOperationException();
        }
        ItemBlobEntry ent = type.newBlobEntry();
        ent.setOwner(player, item, null);
        return ent;
    }

    public static ItemInfoBlob getFullBlob(Player player, Item item) {
        ItemInfoBlob blob = new ItemInfoBlob(player, item);

        ItemTemplate itemTemplate = item.getItemTemplate();

        if (itemTemplate.getWeaponType() != null) {
            blob.addBlobEntry(ItemBlobType.COMPOSITE_ITEM);
        }

        if (item.getEquipmentType() != EquipType.NONE) {
            // EQUIPPED SLOT
            blob.addBlobEntry(ItemBlobType.EQUIPPED_SLOT);

            // SLOT INFO
            if (itemTemplate.getArmorType() != null && itemTemplate.getArmorType() != ArmorType.NO_ARMOR) {
                switch (itemTemplate.getArmorType()) {
                    case WING:
                        blob.addBlobEntry(ItemBlobType.SLOTS_WING);
                        break;
                    case SHIELD:
                        blob.addBlobEntry(ItemBlobType.SLOTS_SHIELD);
                        break;
                    default:
                        blob.addBlobEntry(ItemBlobType.SLOTS_ARMOR);
                        break;
                }
            } else if (itemTemplate.isWeapon()) {
                blob.addBlobEntry(ItemBlobType.SLOTS_WEAPON);
            } else if (item.getEquipmentType() == EquipType.ARMOR) {
                blob.addBlobEntry(ItemBlobType.SLOTS_ACCESSORY); // power shards, helmets, earrings, rings, belts
            }
            // MANA STONES
            blob.addBlobEntry(ItemBlobType.MANA_SOCKETS);

            //blob.addBlobEntry(ItemBlobType);
            if (item.getConditioningInfo() != null) {
                blob.addBlobEntry(ItemBlobType.CONDITIONING_INFO);
            }

            // All items with only General
            if (blob.getBlobEntries().size() > 0) {
                blob.addBlobEntry(ItemBlobType.PREMIUM_OPTION);
                if (itemTemplate.isCanPolish()) {
                    blob.addBlobEntry(ItemBlobType.POLISH_INFO);
                }
            }

            List<StatFunction> allModifiers = itemTemplate.getModifiers();
            if (allModifiers != null) {
                allModifiers.stream().filter((modifier) -> (modifier.isBonus() && !modifier.hasConditions())).forEach((modifier) -> {
                    blob.addBonusBlobEntry(modifier);
                });
            }
        } else if (itemTemplate.getTemplateId() == 141000001) {
            blob.addBlobEntry(ItemBlobType.STIGMA_SHARD);
        }

        // GENERAL INFO
        blob.addBlobEntry(ItemBlobType.GENERAL_INFO);

        return blob;
    }

    public List<ItemBlobEntry> getBlobEntries() {
        return itemBlobEntries;
    }

    public int size() {
        int totalSize = 0;
        totalSize = itemBlobEntries.stream().map((ent) -> ent.getSize() + 1).reduce(totalSize, Integer::sum); // 1 C for blob id
        return totalSize;
    }

    public enum ItemBlobType {

        GENERAL_INFO(0x00) {
                    @Override
                    ItemBlobEntry newBlobEntry() {
                        return new GeneralInfoBlobEntry();
                    }
                },
        SLOTS_WEAPON(0x01) {
                    @Override
                    ItemBlobEntry newBlobEntry() {
                        return new WeaponInfoBlobEntry();
                    }
                },
        SLOTS_ARMOR(0x02) {
                    @Override
                    ItemBlobEntry newBlobEntry() {
                        return new ArmorInfoBlobEntry();
                    }
                },
        SLOTS_SHIELD(0x03) {
                    @Override
                    ItemBlobEntry newBlobEntry() {
                        return new ShieldInfoBlobEntry();
                    }
                },
        SLOTS_ACCESSORY(0x04) {
                    @Override
                    ItemBlobEntry newBlobEntry() {
                        return new AccessoryInfoBlobEntry();
                    }
                },
        SLOTS_ARROW(0x05) {
                    @Override
                    ItemBlobEntry newBlobEntry() {
                        return new ArrowInfoBlobEntry();
                    }
                },
        EQUIPPED_SLOT(0x06) {
                    @Override
                    ItemBlobEntry newBlobEntry() {
                        return new EquippedSlotBlobEntry();
                    }
                },
        // Removed from 3.5
        STIGMA_INFO(0x07) {
                    @Override
                    ItemBlobEntry newBlobEntry() {
                        return new StigmaInfoBlobEntry();
                    }
                },
        STIGMA_SHARD(0x08) {
                    @Override
                    ItemBlobEntry newBlobEntry() {
                        return new StigmaShardInfoBlobEntry();
                    }
                },
        // missing(0x09), //15? [Not handled before]
        PREMIUM_OPTION(0x10) {
                    @Override
                    ItemBlobEntry newBlobEntry() {
                        return new PremiumOptionInfoBlobEntry();
                    }
                },
        POLISH_INFO(0x11) {
                    @Override
                    ItemBlobEntry newBlobEntry() {
                        return new PolishInfoBlobEntry();
                    }
                },
        STAT_BONUSES(0x0A) {
                    @Override
                    ItemBlobEntry newBlobEntry() {
                        return new BonusInfoBlobEntry();
                    }
                },
        // [Not handled before] retail send it xx times (smth dynamically changed)
        MANA_SOCKETS(0x0B) {
                    @Override
                    ItemBlobEntry newBlobEntry() {
                        return new ManaStoneInfoBlobEntry();
                    }
                },
        // 0x0C - not used?
        SLOTS_WING(0x0D) {
                    @Override
                    ItemBlobEntry newBlobEntry() {
                        return new WingInfoBlobEntry();
                    }
                },
        COMPOSITE_ITEM(0x0E) {
                    @Override
                    ItemBlobEntry newBlobEntry() {
                        return new CompositeItemBlobEntry();
                    }
                },
        CONDITIONING_INFO(0x0F) {
                    @Override
                    ItemBlobEntry newBlobEntry() {
                        return new ConditioningInfoBlobEntry();
                    }
                };

        private final int entryId;

        private ItemBlobType(int entryId) {
            this.entryId = entryId;
        }

        public int getEntryId() {
            return entryId;
        }

        abstract ItemBlobEntry newBlobEntry();
    }
}
