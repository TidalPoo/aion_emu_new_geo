package com.aionemu.gameserver.model.ingameshop;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.cardinal.AddItemType;
import com.aionemu.gameserver.configs.ingameshop.InGameShopProperty;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.configs.main.InGameShopConfig;
import com.aionemu.gameserver.configs.main.LoggingConfig;
import com.aionemu.gameserver.dao.InGameShopDAO;
import com.aionemu.gameserver.dao.InGameShopLogDAO;
import com.aionemu.gameserver.dao.InventoryDAO;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.LetterType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.templates.mail.MailMessage;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MAIL_SERVICE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TOLL_INFO;
import com.aionemu.gameserver.network.loginserver.LoginServer;
import com.aionemu.gameserver.network.loginserver.serverpackets.SM_PREMIUM_CONTROL;
import com.aionemu.gameserver.restrictions.RestrictionsManager;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.mail.SystemMailService;
import com.aionemu.gameserver.utils.ColorUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;
import javolution.util.FastList;
import javolution.util.FastMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author KID, xTz
 * @modified Alex
 *
 */
public class InGameShopEn {

    private static InGameShopEn instance = new InGameShopEn();
    private final Logger log = LoggerFactory.getLogger("INGAMESHOP_LOG");

    public static InGameShopEn getInstance() {
        return instance;
    }
    private FastMap<Byte, List<IGItem>> items;
    private InGameShopDAO dao;
    private InGameShopProperty iGProperty;
    private int lastRequestId = 0;
    private CopyOnWriteArrayList<IGRequest> activeRequests;

    public InGameShopEn() {
        if (!InGameShopConfig.ENABLE_IN_GAME_SHOP) {
            log.info("InGameShop is disabled.");
            return;
        }
        iGProperty = InGameShopProperty.load();
        dao = DAOManager.getDAO(InGameShopDAO.class);
        items = FastMap.newInstance();
        activeRequests = new CopyOnWriteArrayList<>();
        items = dao.loadInGameShopItems();
        log.info("Loaded with " + items.size() + " items.");
    }

    public InGameShopProperty getIGSProperty() {
        return iGProperty;
    }

    public void reload() {
        if (!InGameShopConfig.ENABLE_IN_GAME_SHOP) {
            log.info("InGameShop is disabled.");
            return;
        }
        iGProperty.clear();
        iGProperty = InGameShopProperty.load();
        items = DAOManager.getDAO(InGameShopDAO.class).loadInGameShopItems();
        log.info("Loaded with " + items.size() + " items.");
    }

    public IGItem getIGItem(int id) {
        for (byte key : items.keySet()) {
            for (IGItem item : items.get(key)) {
                if (item.getObjectId() == id) {
                    return item;
                }
            }
        }
        return null;
    }

    public Collection<IGItem> getItems(byte category) {
        if (!items.containsKey(category)) {
            return Collections.emptyList();
        }
        return this.items.get(category);
    }

    @SuppressWarnings("unchecked")
    public FastList<Integer> getTopSales(int subCategory, byte category) {
        byte max = 6;
        TreeMap<Integer, Integer> map = new TreeMap<>(new DescFilter());
        if (!items.containsKey(category)) {
            return FastList.newInstance();
        }
        for (IGItem item : this.items.get(category)) {
            if (item.getSalesRanking() == 0) {
                continue;
            }
            if (subCategory != 2 && item.getSubCategory() != subCategory) {
                continue;
            }
            map.put(item.getSalesRanking(), item.getObjectId());
        }
        FastList<Integer> top = FastList.newInstance();
        byte cnt = 0;
        for (int objId : map.values()) {
            if (cnt <= max) {
                top.add(objId);
                cnt++;
            } else {
                break;
            }
        }
        map.clear();
        return top;
    }

    @SuppressWarnings("rawtypes")
    class DescFilter implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            Integer i1 = (Integer) o1;
            Integer i2 = (Integer) o2;
            return -i1.compareTo(i2);
        }
    }

    public int getMaxList(byte subCategoryId, byte category) {
        int id = 0;
        if (!items.containsKey(category)) {
            return id;
        }
        for (IGItem item : items.get(category)) {
            if (item.getSubCategory() == subCategoryId) {
                if (item.getList() > id) {
                    id = item.getList();
                }
            }
        }
        return id;
    }

    public void acceptRequest(Player player, int itemObjId) {
        if (player.getInventory().isFull()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DICE_INVEN_ERROR);
            return;
        }
        IGItem item = InGameShopEn.getInstance().getIGItem(itemObjId);
        if (item == null) {
            PacketSendUtility.sendBrightYellowMessageOnCenter(player, "ITEM == null!!!");
            return;
        }
        if (!RestrictionsManager.canBuyIngameshop(player, item)) {
            return;
        }

        lastRequestId++;
        IGRequest request = new IGRequest(lastRequestId, player.getObjectId(), itemObjId);
        request.accountId = player.getClientConnection().getAccount().getId();
        long price = item.getPriceType() == IGPriceType.TOLL ? item.getItemPriceCount() : 0;
        if (LoginServer.getInstance().sendPacket(new SM_PREMIUM_CONTROL(request, price))) {
            activeRequests.add(request);
        }
        if (LoggingConfig.LOG_INGAMESHOP) {
            log.info("[INGAMESHOP] > Account name: " + player.getAcountName() + ", PlayerName: " + player.getName() + " is watching item:" + item.getItemId() + " cost " + item.getItemPriceCount() + " toll.");
        }
    }

    public void sendRequest(Player player, String receiver, String message, int itemObjId) {
        if (receiver.equalsIgnoreCase(player.getName())) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INGAMESHOP_CANNOT_GIVE_TO_ME);
            return;
        }
        if (!InGameShopConfig.ALLOW_GIFTS) {
            PacketSendUtility.sendMessage(player, "\u041f\u043e\u0434\u0430\u0440\u043a\u0438 \u043e\u0442\u043a\u043b\u044e\u0447\u0435\u043d\u044b \u0430\u0434\u043c\u0438\u043d\u0438\u0441\u0442\u0440\u0430\u0446\u0438\u0435\u0439.");
            return;
        }
        if (!DAOManager.getDAO(PlayerDAO.class).isNameUsed(receiver)) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INGAMESHOP_NO_USER_TO_GIFT);
            return;
        }
        PlayerCommonData recipientCommonData = DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonDataByName(receiver);
        if (recipientCommonData.getMailboxLetters() >= 100) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MAIL_MSG_RECIPIENT_MAILBOX_FULL(recipientCommonData.getName()));
            return;
        }
        if (!InGameShopConfig.ENABLE_GIFT_OTHER_RACE && !player.isGM()) {
            if (player.getRace() != recipientCommonData.getRace()) {
                PacketSendUtility.sendPacket(player, new SM_MAIL_SERVICE(MailMessage.MAIL_IS_ONE_RACE_ONLY, false));
                return;
            }
        }
        IGItem item = getIGItem(itemObjId);
        lastRequestId++;
        IGRequest request = new IGRequest(lastRequestId, player.getObjectId(), receiver, message, itemObjId);
        request.accountId = player.getClientConnection().getAccount().getId();
        long price = item.getPriceType() == IGPriceType.TOLL ? item.getItemPriceCount() : 0;
        if (LoginServer.getInstance().sendPacket(new SM_PREMIUM_CONTROL(request, price))) {
            activeRequests.add(request);
        }
    }

    public void addToll(Player player, long cnt) {
        if (!InGameShopConfig.ENABLE_IN_GAME_SHOP) {
            PacketSendUtility.sendMessage(player, "You can't add toll if ingameshop is disabled!");
            return;
        }
        lastRequestId++;
        IGRequest request = new IGRequest(lastRequestId, player.getObjectId(), 0);
        request.accountId = player.getClientConnection().getAccount().getId();
        if (LoginServer.getInstance().sendPacket(new SM_PREMIUM_CONTROL(request, cnt * -1))) {
            activeRequests.add(request);
        }
        PacketSendUtility.sendMessage(player, ColorUtil.convertTextToColor("Получено " + CustomConfig.COL + ": ", "255 255 0") + ColorUtil.convertTextToColor(cnt + "", "1 1 1"));
    }

    public void giveToll(Player player, long cnt) {
        lastRequestId++;
        Account account = player.getClientConnection().getAccount();
        IGRequest request = new IGRequest(lastRequestId, player.getObjectId(), 0);
        request.accountId = account.getId();
        request.itemObjId = -2416;// give toll :D
        if (LoginServer.getInstance().sendPacket(new SM_PREMIUM_CONTROL(request, cnt))) {
            activeRequests.add(request);
        }
        PacketSendUtility.sendMessage(player, "\u041f\u043e\u0442\u0440\u0430\u0447\u0435\u043d\u043e " + CustomConfig.COL + ": " + cnt);
    }

    public void finishRequest(int requestId, int result, long toll) {
        for (IGRequest request : activeRequests) {
            if (request.requestId == requestId) {
                Player player = World.getInstance().findPlayer(request.playerId);
                if (player != null) {
                    if (result == 1) {
                        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INGAMESHOP_ERROR);
                    } else if (result == 2) {
                        IGItem item = getIGItem(request.itemObjId);
                        if (item == null) {
                            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INGAMESHOP_ERROR);
                            log.error("player " + player.getName() + " requested " + request.itemObjId + " that was not exists in list.");
                            return;
                        }
                        long resultCol = item.getItemPriceCount() - toll;
                        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_INGAMESHOP_NOT_ENOUGH_CASH(resultCol + " " + CustomConfig.COL));
                        PacketSendUtility.sendPacket(player, new SM_TOLL_INFO(toll));
                        if (LoggingConfig.LOG_INGAMESHOP) {
                            log.info("[INGAMESHOP] > Account name: " + player.getAcountName() + ", PlayerName: " + player.getName() + " has not bought item: " + item.getItemId() + " count: " + item.getItemCount() + " Cause: NOT ENOUGH TOLLS");
                        }
                    } else if (result == 3) {
                        //give toll
                        if (request.itemObjId == -2416) {
                            player.getClientConnection().getAccount().setToll(toll);
                            PacketSendUtility.sendPacket(player, new SM_TOLL_INFO(toll));
                        } else {
                            IGItem item = getIGItem(request.itemObjId);
                            if (item == null) {
                                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INGAMESHOP_ERROR);
                                log.error("player " + player.getName() + " requested " + request.itemObjId + " that was not exists in list.");
                                return;
                            }

                            if (item.getPriceType() == IGPriceType.TOLL) {
                                player.getClientConnection().getAccount().setToll(toll);
                                PacketSendUtility.sendPacket(player, new SM_TOLL_INFO(toll));
                                PacketSendUtility.sendMessage(player, "\u041f\u043e\u0442\u0440\u0430\u0447\u0435\u043d\u043e " + CustomConfig.COL + " " + item.getItemPriceCount() + ". \u041e\u0441\u0442\u0430\u043b\u043e\u0441\u044c: " + toll);
                            } else {
                                PacketSendUtility.sendMessage(player, "Поздравляем! Вы успещно приобрели предмет: [item:" + item.getItemId() + "] за " + item.getPriceTypeRusname());
                            }
                            if (request.gift) {
                                SystemMailService.getInstance().sendMail(player.getName(), request.receiver, "In Game Shop", request.message, item.getItemId(), item.getItemCount(), 0, LetterType.BLACKCLOUD);
                                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INGAMESHOP_GIFT_SUCCESS);
                                if (LoggingConfig.LOG_INGAMESHOP) {
                                    log.info("[INGAMESHOP] > Account name: " + player.getAcountName() + ", PlayerName: " + player.getName() + " BUY ITEM: " + item.getItemId() + " COUNT: " + item.getItemCount() + " FOR PlayerName: " + request.receiver);
                                }
                                if (LoggingConfig.LOG_INGAMESHOP_SQL) {
                                    DAOManager.getDAO(InGameShopLogDAO.class).log("GIFT", new Timestamp(System.currentTimeMillis()), player.getName(), player.getAcountName(), request.receiver, item.getItemId(), item.getItemCount(), item.getItemPriceCount());
                                }
                            } else {
                                ItemService.addItem(player, item.getItemId(), item.getItemCount(), new ItemService.ItemUpdatePredicate() {
                                    @Override
                                    public boolean changeItem(Item item) {
                                        // Shop AionLight
                                        item.setItemCreator("\u041c\u0430\u0433\u0430\u0437\u0438\u043d-" + GSConfig.SERVER_NAME + "");
                                        return true;
                                    }
                                }, AddItemType.SHOP, null);

                                if (LoggingConfig.LOG_INGAMESHOP) {
                                    log.info("[INGAMESHOP] > Account name: " + player.getAcountName() + ", PlayerName: " + player.getName() + " BUY ITEM: " + item.getItemId() + " COUNT: " + item.getItemCount());
                                }
                                if (LoggingConfig.LOG_INGAMESHOP_SQL) {
                                    DAOManager.getDAO(InGameShopLogDAO.class).log("BUY", new Timestamp(System.currentTimeMillis()), player.getName(), player.getAcountName(), player.getName(), item.getItemId(), item.getItemCount(), item.getItemPriceCount());
                                }
                                DAOManager.getDAO(InventoryDAO.class).store(player);
                            }
                            item.increaseSales();
                            dao.increaseSales(item.getObjectId(), item.getSalesRanking());
                        }
                    } else if (result == 4) {
                        // ответ от логина
                        player.getClientConnection().getAccount().setToll(toll);
                        PacketSendUtility.sendPacket(player, new SM_TOLL_INFO(toll));
                    }
                }
                activeRequests.remove(request);
                break;
            }
        }
    }
}
