/**
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 * aion-lightning is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * aion-lightning is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * aion-lightning. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * SAO Project
 */
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.cardinal.CardinalManager;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.reward.RewardService;

/**
 * @author ginho1
 * @modified Alex - realize full online bonus in the one hour online :)
 */
public class CM_PLAYER_LISTENER extends AionClientPacket {

    /*
     * This CM is send every five minutes by client.
     */
    public CM_PLAYER_LISTENER(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        //empty?
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        Account a = getConnection().getAccount();
        if (player != null) {
            if (CustomConfig.ENABLE_REWARD_SERVICE) {
                RewardService.getInstance().verify(player);
            }
            //online bonus by Alex :)
            long time = player.getOnlineBonusTime();
            if (time == 0) {
                long poggreTime = (System.currentTimeMillis() - player.getOnlineTime2()) / 1000 / 60;
                if (poggreTime < 5) {
                    long newTime = player.getOnlineTime2() + (poggreTime * 1000 * 60);
                    player.setOnlineBonusTime(newTime);
                    time = player.getOnlineBonusTime();
                    //PacketSendUtility.sendMessage(player, "success");
                }
            }
            long onlineBonusTime = (System.currentTimeMillis() - time) / 1000 / 60;
            if (onlineBonusTime >= 60) {
                CardinalManager.onlineBonus(player);
                player.setOnlineBonusTime(System.currentTimeMillis());
            }
            //А PacketSendUtility.sendMessage(player, "" + (onlineBonusTime));
        } else if (a != null) {
            a.setOnlineTimeChecker();
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
