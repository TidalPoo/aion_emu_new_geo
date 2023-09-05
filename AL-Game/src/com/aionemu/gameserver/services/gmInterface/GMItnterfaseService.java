package com.aionemu.gameserver.services.gmInterface;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gminterface.ItemTemp;
import com.aionemu.gameserver.model.gminterface.NpcTemp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alex
 */
public class GMItnterfaseService {

    private static final Logger log = LoggerFactory.getLogger(GMItnterfaseService.class);
    private java.util.Map<String, ItemTemp> items;
    private java.util.Map<String, NpcTemp> npcs;

    public void initResultData() {
        log.info("Initializing result data...");
        items = DataManager.itd.getItems();
        npcs = DataManager.ntd.getNpcs();
        log.info("Loaded " + items.size() + " result items.");
        log.info("Loaded " + npcs.size() + " result npcs.");
    }

    public java.util.Map<String, ItemTemp> getItems() {
        return items;
    }

    public java.util.Map<String, NpcTemp> getNpcs() {
        return npcs;
    }

    public ItemTemp getResultItem(String name) {
        return items.get(name);
    }

    public ItemTemp getItemByItemId(int itemId) {
        for (ItemTemp it : items.values()) {
            if (it.getId() == itemId) {
                return it;
            }
        }
        return null;
    }

    public NpcTemp getResultNpc(String name) {
        return npcs.get(name);
    }

    public static GMItnterfaseService getInstance() {
        return GMServiceHolder.INSTANCE;
    }

    private static class GMServiceHolder {

        private static final GMItnterfaseService INSTANCE = new GMItnterfaseService();
    }
}
