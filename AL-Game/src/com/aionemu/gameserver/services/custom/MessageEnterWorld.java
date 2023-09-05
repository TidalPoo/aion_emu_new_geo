/*
 * SAO Project
 */
package com.aionemu.gameserver.services.custom;

import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.ChatUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 *
 * @author Alex
 */
public class MessageEnterWorld {

    public static void sendMessage(Player player) {
        switch (GSConfig.SERVER_NAME) {
            case "ServerAionOnline":
            case "SAO Project":
                PacketSendUtility.sendMessage(player, "[color:=====;0 255 0][color:===;0 255 0][color:=====;0 255 0][color:=====;0 255 0][color:=====;0 255 0][color:===;0 255 0][color:====;0 255 0][color:====;0 255 0]");
                PacketSendUtility.sendMessage(player, "                  [color:<;0 255 0][color:<Serv;0 255 0][color:erAio;0 255 0][color:nOnli;0 255 0][color:ne>;0 255 0][color:>;0 255 0]");
                PacketSendUtility.sendMessage(player, "[color:=====;0 255 0][color:===;0 255 0][color:=====;0 255 0][color:=====;0 255 0][color:=====;0 255 0][color:===;0 255 0][color:====;0 255 0][color:====;0 255 0]");
                PacketSendUtility.sendMessage(player, "[color:Наш ;0 255 0][color:сайт;0 255 0] " + ChatUtil.webLinkMmoSaoRu());
                PacketSendUtility.sendMessage(player, "[color:Наш ;0 255 0][color:форум;0 255 0] " + ChatUtil.webLinkForumMmoSaoRu());
                PacketSendUtility.sendMessage(player, "[color:Помощ;0 255 0][color:ь про;0 255 0][color:екту;0 255 0] " + ChatUtil.webLinkAddFunds());
                PacketSendUtility.sendMessage(player, "[color:=====;0 255 0][color:=====;0 255 0][color:=====;0 255 0][color:=====;0 255 0][color:=====;0 255 0][color:=====;0 255 0][color:====;0 255 0]");
                PacketSendUtility.sendMessage(player, "         [color:<;0 255 0][color:Админ;0 255 0][color:истра;0 255 0][color:ция;0 255 0] [color:SAO;0 255 0] [color:Proje;0 255 0][color:ct>;0 255 0]");
                PacketSendUtility.sendMessage(player, "[color:=====;0 255 0][color:=====;0 255 0][color:=====;0 255 0][color:=====;0 255 0][color:=====;0 255 0][color:=====;0 255 0][color:====;0 255 0]");
                PacketSendUtility.sendMessage(player, "[color:Главн;0 255 0][color:ый ад;0 255 0][color:минис;0 255 0][color:трато;0 255 0][color:р: ;0 255 0][color:Sun;255 255 0]\n"
                        + "  [color:skype;0 255 0][color:: ser;255 255 0][color:verai;255 255 0][color:ononl;255 255 0][color:ine;255 255 0]\n"
                        + "  [color:email;0 255 0][color:: ser;255 255 0][color:verai;255 255 0][color:ononl;255 255 0][color:ine@e;255 255 0][color:mail.;255 255 0][color:ua;255 255 0]");
                PacketSendUtility.sendMessage(player, "[color:Зам. ;0 255 0][color:главн;0 255 0][color:ого ;0 255 0][color:админ;0 255 0][color:истра;0 255 0][color:тора:;0 255 0] [color:Drusi;255 255 0][color:k;255 255 0]\n"
                        + "  [color:skype;0 255 0][color:: aio;255 255 0][color:n-lig;255 255 0][color:ht;255 255 0]");
                PacketSendUtility.sendMessage(player, "[color:Разра;0 255 0][color:ботчи;0 255 0][color:к;0 255 0] [color:Alex;255 255 0]");
                PacketSendUtility.sendMessage(player, "[color:=====;0 255 0][color:=====;0 255 0][color:=====;0 255 0][color:=====;0 255 0][color:=====;0 255 0][color:=====;0 255 0][color:====;0 255 0]");
                PacketSendUtility.sendMessage(player, "                         [color:<;0 255 0][color:Базы;0 255 0] [color:знани;0 255 0][color:й>;0 255 0]");
                PacketSendUtility.sendMessage(player, "[color:=====;0 255 0][color:=====;0 255 0][color:=====;0 255 0][color:=====;0 255 0][color:=====;0 255 0][color:=====;0 255 0][color:====;0 255 0]");
                PacketSendUtility.sendMessage(player, "                     " + ChatUtil.webLinkAionDatabaseNet());
                PacketSendUtility.sendMessage(player, "                             " + ChatUtil.webLinkAIDB());
                PacketSendUtility.sendMessage(player, "[color:=====;0 255 0][color:=====;0 255 0][color:=====;0 255 0][color:=====;0 255 0][color:=====;0 255 0][color:=====;0 255 0][color:====;0 255 0]");
                break;
            case "aion-utter"://например
                PacketSendUtility.sendMessage(player, "new.aion-utter.com");
                break;
        }
    }
}
