/*
 * AionLight project
 */
package com.aionemu.gameserver.services;

import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MEGAPHONE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang.time.DurationFormatUtils;

/**
 *
 * @author Alex
 */
public class MembershipService {

    public static void getMembershipExpireTo(Player player) {
        Account account = player.getPlayerAccount();
        PacketSendUtility.sendPacket(player, new SM_MEGAPHONE(GSConfig.SERVER_NAME,
                "\nРасовый чат - введите .r <сообщение своей расе> или .w <сообщение всем> ", 0));
        if (player.getMembership() == 0) {
            PacketSendUtility.sendMessage(player,
                    "Для покупки VIP привилегий обратитесь к главному администратору проекта.");
        } else {
            if (account.getExpire() != 0) {
                long t = account.getExpire() - System.currentTimeMillis();
                String expire = "";
                if (account.getExpire() != 0 && player.getMembership() > 0) {
                    expire = "\n[color:Дата ;0 255 0][color:оконч;0 255 0][color:ания ;0 255 0][color:приви;0 255 0][color:легий;0 255 0][color::;0 255 0] " + new SimpleDateFormat("yyyy.MM.dd").format(new Date(account.getExpire())) + "\n"
                            + "[color:Остал;0 255 0][color:ось:;0 255 0] " + DurationFormatUtils.formatDuration(account.getExpire() - System.currentTimeMillis(), "dd дн");
                }
                /* int timeDay = (int) (t / 1000 / 60 / 60 / 24);
                 if (timeDay > 0 && timeDay < 7) {
                 MembershipLevelEnum m = MembershipLevelEnum.getMlType(player.getMembership());
                 PacketSendUtility.sendYellowMessageOnCenter(player,
                 "Уважаемый " + player.getName() + "! Напоминаем вам о скором окончании привилегий \"" + m.getStatusName() + "\". Осталось: " + timeDay + " дней.");
                 }*/
                PacketSendUtility.sendMessage(player, expire);
            }
        }

    }
}
