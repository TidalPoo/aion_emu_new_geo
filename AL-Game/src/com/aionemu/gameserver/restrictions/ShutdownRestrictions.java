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
package com.aionemu.gameserver.restrictions;

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.ingameshop.IGItem;
import com.aionemu.gameserver.skillengine.model.Skill;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author lord_rex
 */
public class ShutdownRestrictions extends AbstractRestrictions {

    @Override
    public boolean isRestricted(Player player, Class<? extends Restrictions> callingRestriction) {
        if (isInShutdownProgress(player)) {
            PacketSendUtility.sendMessage(player, "You are in shutdown progress!");
            return true;
        }
        return false;
    }

    @Override
    public boolean canAttack(Player player, VisibleObject target) {
        if (isInShutdownProgress(player)) {
            PacketSendUtility.sendMessage(player, "You cannot attack in Shutdown progress!");
            return false;
        }
        return true;
    }

    @Override
    public boolean canAffectBySkill(Player player, VisibleObject target, Skill skill) {
        return true;
    }

    @Override
    public boolean canUseSkill(Player player, Skill skill) {
        if (isInShutdownProgress(player)) {
            PacketSendUtility.sendMessage(player, "You cannot use skills in Shutdown progress!");
            return false;
        }
        return true;
    }

    @Override
    public boolean canChat(Player player) {
        if (isInShutdownProgress(player)) {
            PacketSendUtility.sendMessage(player, "You cannot chat in Shutdown progress!");
            return false;
        }
        return true;
    }

    @Override
    public boolean canInviteToGroup(Player player, Player target) {
        if (isInShutdownProgress(player)) {
            PacketSendUtility.sendMessage(player, "You cannot invite members to group in Shutdown progress!");
            return false;
        }
        return true;
    }

    @Override
    public boolean canInviteToAlliance(Player player, Player target) {
        if (isInShutdownProgress(player)) {
            PacketSendUtility.sendMessage(player, "You cannot invite members to alliance in Shutdown progress!");
            return false;
        }
        return true;
    }

    @Override
    public boolean canChangeEquip(Player player) {
        if (isInShutdownProgress(player)) {
            PacketSendUtility.sendMessage(player, "You cannot equip / unequip item in Shutdown progress!");
            return false;
        }
        return true;
    }

    @Override
    public boolean canTrade(Player player) {
        if (isInShutdownProgress(player)) {
            PacketSendUtility.sendMessage(player, "You cannot trade in Shutdown progress!");
            return false;
        }
        return true;
    }

    @Override
    public boolean canUseWarehouse(Player player) {
        if (isInShutdownProgress(player)) {
            PacketSendUtility.sendMessage(player, "You cannot use warehouse in Shutdown progress!");
            return false;
        }
        return true;
    }

    private boolean isInShutdownProgress(Player player) {
        return player.getController().isInShutdownProgress();
    }

    @Override
    public boolean canPrivateStore(Player player) {
        if (isInShutdownProgress(player)) {
            PacketSendUtility.sendMessage(player, "You cannot use private store in Shutdown progress!");
            return false;
        }
        return true;
    }

    @Override
    public boolean canBuyIngameshop(Player player, IGItem itemId) {
        if (isInShutdownProgress(player)) {
            PacketSendUtility.sendMessage(player, "You cannot use game shop in Shutdown progress!");
            return false;
        }
        return true;
    }

    @Override
    public boolean canShowIngameshop(Player player) {
        if (isInShutdownProgress(player)) {
            PacketSendUtility.sendMessage(player, "You cannot use game shop in Shutdown progress!");
            return false;
        }
        return true;
    }
}
