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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.Letter;
import com.aionemu.gameserver.model.gameobjects.LetterType;
import com.aionemu.gameserver.model.gameobjects.player.Mailbox;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.mail.MailMessage;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.MailServicePacket;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.collections.ListSplitter;
import java.util.Collection;

/**
 * @author kosyachok, Source
 */
public class SM_MAIL_SERVICE extends MailServicePacket {

    private int serviceId;
    private Collection<Letter> letters;
    private int totalCount;
    private int unreadCount;
    private int unreadExpressCount;
    private int unreadBlackCloudCount;
    private int mailMessage;
    private Letter letter;
    private long time;
    private int letterId;
    private int[] letterIds;
    private int attachmentType;
    private boolean isExpress;
    private boolean gmpanel = false;

    public SM_MAIL_SERVICE(Mailbox mailbox, boolean gmpanel) {
        super(null);
        this.serviceId = 0;
        this.gmpanel = gmpanel;
    }

    /**
     * Send mailMessage(ex. Send OK, Mailbox full etc.)
     *
     * @param mailMessage
     * @param gmpanel
     */
    public SM_MAIL_SERVICE(MailMessage mailMessage, boolean gmpanel) {
        super(null);
        this.serviceId = 1;
        this.mailMessage = mailMessage.getId();
        this.gmpanel = gmpanel;
    }

    /**
     * Send mailbox info
     *
     * @param player
     * @param letters
     * @param gmpanel
     */
    public SM_MAIL_SERVICE(Player player, Collection<Letter> letters, boolean gmpanel) {
        super(player);
        this.serviceId = 2;
        this.letters = letters;
        this.gmpanel = gmpanel;
    }

    /**
     * Send mailbox info
     *
     * @param player
     * @param letters
     * @param isExpress
     * @param gmpanel
     */
    public SM_MAIL_SERVICE(Player player, Collection<Letter> letters, boolean isExpress, boolean gmpanel) {
        super(player);
        this.serviceId = 2;
        this.letters = letters;
        this.isExpress = isExpress;
        this.gmpanel = gmpanel;
    }

    /**
     * used when reading letter
     *
     * @param player
     * @param letter
     * @param time
     * @param gmpanel
     */
    public SM_MAIL_SERVICE(Player player, Letter letter, long time, boolean gmpanel) {
        super(player);
        this.serviceId = 3;
        this.letter = letter;
        this.time = time;
        this.gmpanel = gmpanel;
    }

    /**
     * used when getting attached items
     *
     * @param letterId
     * @param attachmentType
     * @param gmpanel
     */
    public SM_MAIL_SERVICE(int letterId, int attachmentType, boolean gmpanel) {
        super(null);
        this.serviceId = 5;
        this.letterId = letterId;
        this.attachmentType = attachmentType;
        this.gmpanel = gmpanel;
    }

    /**
     * used when deleting letter
     *
     * @param letterIds
     * @param gmpanel
     */
    public SM_MAIL_SERVICE(int[] letterIds, boolean gmpanel) {
        super(null);
        this.serviceId = 6;
        this.letterIds = letterIds;
        this.gmpanel = gmpanel;
    }

    @Override
    protected void writeImpl(AionConnection con) {
        Mailbox mailbox = con.getActivePlayer().getMailbox();
        this.totalCount = mailbox.size();
        this.unreadCount = mailbox.getUnreadCount();
        this.unreadExpressCount = mailbox.getUnreadCountByType(LetterType.EXPRESS);
        this.unreadBlackCloudCount = mailbox.getUnreadCountByType(LetterType.BLACKCLOUD);
        writeC(serviceId);
        PacketSendUtility.sendMessage(player, serviceId+"");
        switch (serviceId) {
            case 0:
                mailbox.isMailListUpdateRequired = true;
                writeMailboxState(totalCount, unreadCount, unreadExpressCount, unreadBlackCloudCount);
                break;
            case 1:
                writeMailMessage(mailMessage);
                break;
            case 2:
                Collection<Letter> _letters;
                if (!letters.isEmpty()) {
                    ListSplitter<Letter> splittedLetters = new ListSplitter<>(letters, 100);
                    _letters = splittedLetters.getNext();
                } else {
                    _letters = letters;
                }
                writeLettersList(_letters, player, isExpress, unreadExpressCount + unreadBlackCloudCount, gmpanel);
                break;
            case 3:
                writeLetterRead(letter, time, totalCount, unreadCount, unreadExpressCount, unreadBlackCloudCount);
                break;
            case 5:
                writeLetterState(letterId, attachmentType);
                break;
            case 6:
                mailbox.isMailListUpdateRequired = true;
                writeLetterDelete(totalCount, unreadCount, unreadExpressCount, unreadBlackCloudCount, letterIds);
                break;
        }
    }

}
