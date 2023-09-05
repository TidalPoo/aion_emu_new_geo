/*
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 * aion-lightning is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-lightning is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.gm;

/**
 *
 * @author pixfid
 */
public enum GmCommands {

    gm_mail_list(0),
    inventory(1),
    skill(2),
    teleportto(3),
    status(4),
    search(5),
    quest(6),
    gm_guildhistory(7),
    gm_buddy_list(8),
    recall(9),
    gm_comment_list(10),
    gm_comment_add(11),
    check_bot1(12),
    check_bot99(13),
    Bookmark_add(14),
    guild(15);

    private final int value;

    GmCommands(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
