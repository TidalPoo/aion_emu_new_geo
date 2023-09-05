/*
 * AionLight project
 */
package com.aionemu.gameserver.services.html;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.configs.main.LoggingConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.dao.InGameShopLogDAO;
import com.aionemu.gameserver.dao.MySQL5AionDAO;
import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.ingameshop.InGameShopEn;
import com.aionemu.gameserver.network.loginserver.LoginServer;
import com.aionemu.gameserver.utils.PacketSendUtility;
import java.sql.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alex
 */
public class NewHTMLSettings {

    private static final Logger log = LoggerFactory.getLogger("INGAMESHOP_LOG");

    public static void parseChecked(Player player, String checkbox) {
        if (player.getMembership() >= 2 || checkbox.equals("\"1\"")) {
            return;
        }
        Account a = player.getClientConnection().getAccount();
        int m = player.getMembership() == 0 ? 1 : 2;
        int col = 0;
        long dayPremium = MembershipConfig.PREMIUM_DAY_2;
        long dayVip = MembershipConfig.VIP_DAY_2;
        String gp = CustomConfig.COL;
        if (player.getMembership() == 0) {
            switch (checkbox) {
                case "\"2\"":
                    col = MembershipConfig.MONEY_PREMIUM_30_DAY;
                    break;
                case "\"3\"":
                    dayPremium = MembershipConfig.PREMIUM_DAY_1;
                    col = MembershipConfig.MONEY_PREMIUM_7_DAY;
                    break;
                case "\"4\"":
                    col = MembershipConfig.MONEY_VIP_30_DAY;
                    m = 2;
                    break;
                case "\"5\"":
                    dayVip = MembershipConfig.VIP_DAY_1;
                    col = MembershipConfig.MONEY_VIP_7_DAY;
                    m = 2;
                    break;
            }
        } else if (player.getMembership() == 1) {
            switch (checkbox) {
                case "\"2\"":
                    col = MembershipConfig.MONEY_VIP_30_DAY;
                    break;
                case "\"3\"":
                    dayVip = MembershipConfig.VIP_DAY_1;
                    col = MembershipConfig.MONEY_VIP_7_DAY;
                    break;
            }
        }

        if (col == 0) {
            return;
        }
        int day = (int) (m == 1 ? dayPremium : dayVip);
        if (a.getToll() < col) {
            PacketSendUtility.sendMessage(player, "\u0423 \u0412\u0430\u0441 \u043d\u0435 \u0434\u043e\u0441\u0442\u0430\u0442\u043e\u0447\u043d\u043e \u0441\u0440\u0435\u0434\u0441\u0442\u0432 \u0434\u043b\u044f \u0430\u043a\u0442\u0438\u0432\u0430\u0446\u0438\u0438 " + (m == 1 ? "\u043f\u0440\u0435\u043c\u0438\u0443\u043c" : "\u0432\u0438\u043f") + " \u043f\u0440\u0438\u0432\u0438\u043b\u0435\u0433\u0438\u0439 \u043d\u0430 " + day + " \u0434\u043d. \u041d\u0435\u043e\u0431\u0445\u043e\u0434\u0438\u043c\u043e " + col + " " + gp);
            return;
        }

        InGameShopEn.getInstance().giveToll(player, col);
        PacketSendUtility.sendMessage(player, "\u0421 \u0412\u0430\u0448\u0435\u0433\u043e \u0430\u043a\u043a\u0430\u0443\u043d\u0442\u0430 \u0431\u044b\u043b\u043e \u0441\u043f\u0438\u0441\u0430\u043d\u043e " + col + " " + gp);
        PacketSendUtility.sendYellowMessageOnCenter(player, "\u041f\u043e\u0437\u0434\u0440\u0430\u0432\u043b\u044f\u0435\u043c! \u041f\u0440\u0438\u0432\u0438\u043b\u0435\u0433\u0438\u0438 (" + (m == 1 ? "\u041f\u0440\u0435\u043c\u0438\u0443\u043c" : "\u0412\u0418\u041f") + " " + day + " \u0434\u043d) \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u0430\u043a\u0442\u0438\u0432\u0438\u0440\u043e\u0432\u0430\u043d\u044b! \u041f\u0440\u0438\u044f\u0442\u043d\u043e\u0439 \u0438\u0433\u0440\u044b \u043d\u0430 \u043d\u0430\u0448\u0435\u043c \u0441\u0435\u0440\u0432\u0435\u0440\u0435!");
        LoginServer.getInstance().sendLsControlPacket(player.getAcountName(), player.getName(), GSConfig.SERVER_NAME, m, 2);
        MySQL5AionDAO.getInstance().setExpire(player.getPlayerAccount().getId(), new Timestamp(System.currentTimeMillis() + day * 24 * 60 * 60 * 1000));
        onHtml(player);
        if (LoggingConfig.LOG_INGAMESHOP) {
            log.info("[INGAMESHOP] > Account name: " + player.getAcountName() + ", PlayerName: " + player.getName() + " BUY MEMBERSHIP " + m + " " + gp + " = " + col);
        }
        if (LoggingConfig.LOG_INGAMESHOP_SQL) {
            DAOManager.getDAO(InGameShopLogDAO.class).log("BUY", new Timestamp(System.currentTimeMillis()), player.getName(), player.getAcountName(), player.getName(), m, 0, col);
        }
    }

    public static void onHtml(Player player) {
        StringBuilder s = new StringBuilder();
        StringBuilder s2 = new StringBuilder();
        int count = 0, size = 0, lenghtForum = 0, lenghtSite = 0;
        String premium = "";
        String newsSite = "<font color=\"0000ff\">Нет новых публикаций</font>", newsForum = "<font color=\"0000ff\">Нет новых публикаций</font>";
        if (player.getMembership() == 1) {
            premium = "<input type='checkbox'>Закрыть это окно</input>"
                    + "<input type='checkbox'>VIP (" + MembershipConfig.VIP_DAY_2 + " дн) за " + MembershipConfig.MONEY_VIP_30_DAY + " donatMoney</input>"
                    + "<input type='checkbox'>VIP (" + MembershipConfig.VIP_DAY_1 + " дн) за " + MembershipConfig.MONEY_VIP_7_DAY + " donatMoney</input>";
        } else if (player.getMembership() == 0) {
            premium = "<input type='checkbox'>Закрыть это окно</input>"
                    + "<input type='checkbox'>Премиум (" + MembershipConfig.PREMIUM_DAY_2 + " дн) за " + MembershipConfig.MONEY_PREMIUM_30_DAY + " donatMoney</input>"
                    + "<input type='checkbox'>Премиум (" + MembershipConfig.PREMIUM_DAY_1 + " дн) за " + MembershipConfig.MONEY_PREMIUM_7_DAY + " donatMoney</input>"
                    + "<input type='checkbox'>VIP (" + MembershipConfig.VIP_DAY_2 + " дн) за " + MembershipConfig.MONEY_VIP_30_DAY + " donatMoney</input>"
                    + "<input type='checkbox'>VIP (" + MembershipConfig.VIP_DAY_1 + " дн) за " + MembershipConfig.MONEY_VIP_7_DAY + " donatMoney</input>";
        }
        /* try {
         if (CustomConfig.SITE != null) {
         Document doc = Jsoup.connect(CustomConfig.SITE).get();
         //site
         for (Element link : doc.select("a[href]")) {
         String href = link.attr("href");
         String text = link.text();
         if (lenghtSite < 4) {
         if (href.contains("news") && !text.contains("\u0427\u0438\u0442\u0430\u0442\u044c \u0434\u0430\u043b\u044c\u0448\u0435") && text.length() > 2 && !text.contains("\u041d\u0430\u0447\u0430\u043b\u043e") && !text.contains("\u041a\u043e\u043d\u0435\u0446")) {
         s.append("<br><a href=\"").append(href).append("\">").append(link.text()).append("</a></br>");
         lenghtSite++;
         }
         }
         }
         newsSite = s.toString();
         } else {
         s.append(GSConfig.SERVER_NAME);
         }
         if (CustomConfig.FORUM != null) {
         Document doc2 = Jsoup.connect(CustomConfig.FORUM).get();
         //forum
         for (Element link2 : doc2.select("a[href]")) {
         String href2 = link2.attr("href");
         String text2 = link2.text();
         if (lenghtForum < 4) {
         if (text2.length() > 1 && href2.contains("topic") && !text2.startsWith("\u0421\u0435\u0433\u043e\u0434\u043d\u044f") && !text2.startsWith("\u0412\u0447\u0435\u0440\u0430") && !text2.contains("\u2192")) {
         s2.append("<br><a href=\"").append(href2).append("\">").append(link2.text()).append("</a></br>");
         lenghtForum++;
         }
         }
         }
         newsForum = s2.toString();
         } else {
         s2.append(GSConfig.SERVER_NAME);
         }
        
         String http = CustomConfig.HTTP_MMOTOP_VOTES;
         if (http != null) {
         String accountName = player.getAcountName();
         Document doc2 = Jsoup.connect(http).get();
         for (String link2 : doc2.text().split("2014")) {
         size++;
         if (link2.contains(accountName)) {
         count++;
         }
         }
         }
         } catch (IOException e) {
         //TODO show error message in log
         }*/
        //String wedding = NewWeddingService.getInstance().textInfo(player);
        HTMLService.showHTML(player, HtmlWelcome.getHTML(player, newsSite, newsForum,
                "", size, count, premium));
    }
}
