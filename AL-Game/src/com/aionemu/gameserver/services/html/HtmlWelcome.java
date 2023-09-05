/*
 * AionLight project
 */
package com.aionemu.gameserver.services.html;

import com.aionemu.gameserver.cache.HTMLCache;
import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.MembershipEnum;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang.time.DurationFormatUtils;

/**
 * @author Alex
 */
public class HtmlWelcome {

    public static String getHTML(Player player, String news1, String news2, String weddings,
            int mmotop_all, int mmotop_account, String premium) {
        Account account = player.getClientConnection().getAccount();
        String info = "Опыт: <font color=\"7fff00\">x" + player.getRates().getXpRate() + "</font><br>"
                + "Опыт в группе: <font color=\"7fff00\">x" + player.getRates().getGroupXpRate() + "</font><br>"
                + "Квесты: <font color=\"7fff00\">x" + player.getRates().getQuestXpRate() + "</font><br>"
                + "Дроп: <font color=\"7fff00\">x" + player.getRates().getDropRate() + "</font><br>"
                + "Крафт: (опыт)<font color=\"7fff00\">x" + player.getRates().getCraftingXPRate() + "</font> "
                + "(крит)<font color=\"7fff00\">x" + player.getRates().getCraftCritRate() + "</font><br>"
                + "Сбор: (опыт)<font color=\"7fff00\">x" + player.getRates().getGatheringXPRate() + "</font> "
                + "(кол-во)<font color=\"7fff00\">x" + player.getRates().getGatheringCountRate() + "</font><br>"
                + "Получение AP: <font color=\"7fff00\">x" + player.getRates().getApPlayerGainRate() + "</font><br>"
                + "Потеря AP: <font color=\"7fff00\">x" + player.getRates().getApPlayerLossRate() + "</font><br>";
        String expire = "";
        if (account.getExpire() != 0 && player.getMembership() > 0) {
            expire = "Дата окончания привилегий: <font color=\"7fff00\">" + new SimpleDateFormat("yyyy.MM.dd").format(new Date(account.getExpire())) + "</font>"
                    + "<br>Осталось: " + DurationFormatUtils.formatDuration(account.getExpire() - System.currentTimeMillis(), "<font color=\"7fff00\">dd</font> дн");
        }
        String context = HTMLCache.getInstance().getHTML("welcome.xhtml");
        context = context.replace("%name%", player.getName());
        context = context.replace("%premium%", premium);
        //context = context.replace("%news1%", news1);
        //context = context.replace("%news2%", news2);
        context = context.replace("%account_type%", MembershipEnum.getAlType(player.getMembership()) != null ? MembershipEnum.getAlType(player.getMembership()).getS() : "TEST");
        context = context.replace("%account_toll%", player.getClientConnection().getAccount().getToll() + "");
        context = context.replace("%rate%", info);
        context = context.replace("%expire%", expire);
        context = context.replace("%account_name%", player.getClientConnection().getAccount().getName());
        context = context.replace("%weddings%", weddings);
        context = context.replace("%mmotop_all%", "" + mmotop_all);
        context = context.replace("%mmotop_account%", "" + mmotop_account);
        return context;
    }
}
