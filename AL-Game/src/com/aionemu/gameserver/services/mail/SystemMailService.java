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
package com.aionemu.gameserver.services.mail;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.cardinal.AddItemType;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.configs.main.LoggingConfig;
import com.aionemu.gameserver.dao.InventoryDAO;
import com.aionemu.gameserver.dao.MailDAO;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Letter;
import com.aionemu.gameserver.model.gameobjects.LetterType;
import com.aionemu.gameserver.model.gameobjects.player.Mailbox;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.items.storage.StorageType;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MAIL_SERVICE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemFactory;
import com.aionemu.gameserver.services.player.PlayerMailboxState;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import com.aionemu.gameserver.world.World;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xTz
 */
public class SystemMailService {

    private static final Logger log = LoggerFactory.getLogger("SYSMAIL_LOG");

    public static final SystemMailService getInstance() {
        return SingletonHolder.instance;
    }

    private SystemMailService() {
        log.info("SystemMailService: Initialized.");
    }

    /**
     * @param sender
     * @param recipientName
     * @param title
     * @param message
     * @param attachedItemObjId
     * @param attachedItemCount
     * @param attachedKinahCount
     * @param letterType
     * @return
     */
    public int sendMail(String sender, String recipientName, String title, String message, int attachedItemObjId, long attachedItemCount,
            long attachedKinahCount, LetterType letterType) {

        if (attachedItemObjId != 0) {
            ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(attachedItemObjId);
            if (itemTemplate == null) {
                log.info("[SYSMAILSERVICE] > [SenderName: " + sender + "] [RecipientName: " + recipientName + "] RETURN ITEM ID:" + itemTemplate
                        + " ITEM COUNT " + attachedItemCount + " KINAH COUNT " + attachedKinahCount + " ITEM TEMPLATE IS MISSING ");
                return -1;
            }
        }

        if (attachedItemCount == 0 && attachedItemObjId != 0) {
            return -1;
        }

        if (recipientName.length() > 16) {
            log.info("[SYSMAILSERVICE] > [SenderName: " + sender + "] [RecipientName: " + recipientName + "] ITEM RETURN" + attachedItemObjId
                    + " ITEM COUNT " + attachedItemCount + " KINAH COUNT " + attachedKinahCount + " RECIPIENT NAME LENGTH > 16 ");
            return -1;
        }

        if (!sender.startsWith("$$") && sender.length() > 16) {
            log.info("[SYSMAILSERVICE] > [SenderName: " + sender + "] [RecipientName: " + recipientName + "] ITEM RETURN" + attachedItemObjId
                    + " ITEM COUNT " + attachedItemCount + " KINAH COUNT " + attachedKinahCount + " SENDER NAME LENGTH > 16 ");
            return -1;
        }

        if (title.length() > 20) {
            title = title.substring(0, 20);
        }

        if (message.length() > 1000) {
            message = message.substring(0, 1000);
        }

        PlayerCommonData recipientCommonData = DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonDataByName(recipientName);

        if (recipientCommonData == null) {
            log.info("[SYSMAILSERVICE] > [RecipientName: " + recipientName + "] NO SUCH CHARACTER NAME.");
            return -1;
        }

        Player recipient = World.getInstance().findPlayer(recipientCommonData.getPlayerObjId());
        if (recipient != null) {
            if (recipient.getMailbox() != null && !(recipient.getMailbox().size() < 200)) {
                log.info("[SYSMAILSERVICE] > [SenderName: " + sender + "] [RecipientName: " + recipientCommonData.getName() + "] ITEM RETURN"
                        + attachedItemObjId + " ITEM COUNT " + attachedItemCount + " KINAH COUNT " + attachedKinahCount + " MAILBOX FULL ");
                return -1;
            }
        } else if (recipientCommonData.getMailboxLetters() > 199) {
            return -1;
        }
        Item attachedItem = null;
        long finalAttachedKinahCount = 0;
        int itemId = attachedItemObjId;
        long count = attachedItemCount;

        if (itemId != 0) {
            Item senderItem = ItemFactory.newItem(itemId, count, AddItemType.MAIL, "sender: " + sender);
            if (senderItem != null) {
                senderItem.setEquipped(false);
                senderItem.setEquipmentSlot(0);
                senderItem.setItemLocation(StorageType.MAILBOX.getId());
                attachedItem = senderItem;
            }
        }

        if (attachedKinahCount > 0) {
            finalAttachedKinahCount = attachedKinahCount;
        }

        String finalSender = sender;
        Timestamp time = new Timestamp(Calendar.getInstance().getTimeInMillis());
        Letter newLetter = new Letter(IDFactory.getInstance().nextId(), recipientCommonData.getPlayerObjId(), attachedItem,
                finalAttachedKinahCount, title, message, finalSender, time, true, letterType);
        if (sender.equalsIgnoreCase(GSConfig.SERVER_NAME)) {
            recipient.setGP(true);
        }

        if (!DAOManager.getDAO(MailDAO.class).storeLetter(time, newLetter)) {
            return -1;
        }

        if (attachedItem != null) {
            if (!DAOManager.getDAO(InventoryDAO.class).store(attachedItem, recipientCommonData.getPlayerObjId())) {
                return -1;
            }
        }

        /**
         * Send mail update packets
         */
        if (recipient != null) {
            Mailbox recipientMailbox = recipient.getMailbox();
            recipientMailbox.putLetterToMailbox(newLetter);

            PacketSendUtility.sendPacket(recipient, new SM_MAIL_SERVICE(recipient.getMailbox(), false));
            recipientMailbox.isMailListUpdateRequired = true;

            // if recipient have opened mail list we should update it
            if (recipientMailbox.mailBoxState != 0) {
                boolean isPostman = (recipientMailbox.mailBoxState & PlayerMailboxState.EXPRESS) == PlayerMailboxState.EXPRESS;
                PacketSendUtility.sendPacket(recipient, new SM_MAIL_SERVICE(recipient, recipientMailbox.getLetters(), isPostman, false));
            }

            if (letterType == LetterType.EXPRESS) {
                PacketSendUtility.sendPacket(recipient, SM_SYSTEM_MESSAGE.STR_POSTMAN_NOTIFY);
            }
            /*
             else if (letterType == LetterType.BLACKCLOUD)
             PacketSendUtility.sendPacket(recipient, SM_SYSTEM_MESSAGE.STR_MAIL_CASHITEM_BUY(itemId));
             */
        }

        /**
         * Update loaded common data and db if player is offline
         */
        if (!recipientCommonData.isOnline()) {
            recipientCommonData.setMailboxLetters(recipientCommonData.getMailboxLetters() + 1);
            DAOManager.getDAO(MailDAO.class).updateOfflineMailCounter(recipientCommonData);
        }
        if (LoggingConfig.LOG_SYSMAIL) {
            log.info("[SYSMAILSERVICE] > [SenderName: " + sender + "] [RecipientName: " + recipientName + "] RETURN ITEM ID:" + itemId
                    + " ITEM COUNT " + attachedItemCount + " KINAH COUNT " + attachedKinahCount + " MESSAGE SUCCESSFULLY SENDED ");
        }
        return newLetter.getObjectId();
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final SystemMailService instance = new SystemMailService();
    }

    private void SendEmail(Player player, String to, String text) {
        String host = "email.ua";
        final String user = "AionLight";//от кого
        final String password = "MjFwMjQxNkFsZXhBaW9uQW5nZWxzYnlMaWdodEFkbWluMg";//пароль

        Properties props = new Properties();
        props.put("host", host);
        props.put("auth", "false");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("123");
            message.setText(text);
            //send the message
            Transport.send(message);
            PacketSendUtility.sendMessage(player, "message sent successfully to " + to + "...");
        } catch (MessagingException e) {
            PacketSendUtility.sendMessage(player, e + "");
        }
    }
}
