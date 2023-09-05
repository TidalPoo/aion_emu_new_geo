/**
 * This file is part of aion-emu <aion-emu.com>.
 *
 * aion-emu is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * aion-emu is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * aion-emu. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.loginserver.clientpackets;

import com.aionemu.gameserver.model.account.AccountTime;
import com.aionemu.gameserver.network.loginserver.LoginServer;
import com.aionemu.gameserver.network.loginserver.LsClientPacket;

/**
 * In this packet LoginServer is answering on GameServer request about valid
 * authentication data and also sends account name of user that is
 * authenticating on GameServer.
 *
 * @author -Nemesiss-
 * @modified Alex
 */
public class CM_ACOUNT_AUTH_RESPONSE extends LsClientPacket {

    private String realPassword;
    private int countryCode;
    private byte oldMembership;

    public CM_ACOUNT_AUTH_RESPONSE(int opCode) {
        super(opCode);
    }

    /**
     * accountId
     */
    private int accountId;

    /**
     * result - true = authed
     */
    private boolean result;

    /**
     * accountName [if response is ok]
     */
    private String accountName;
    /**
     * accountTime
     */
    private AccountTime accountTime;
    /**
     * access level - regular/gm/admin
     */
    private byte accessLevel;
    /**
     * Membership - regular/premium
     */
    private byte membership;

    /**
     * Toll
     */
    private long toll;
    private long expire;
    private int acc_enter;
    private long accessLevelExpire;

    /**
     * {@inheritDoc}
     */
    @Override
    public void readImpl() {
        accountId = readD();
        result = readC() == 1;

        if (result) {
            accountName = readS();
            accountTime = new AccountTime();
            accountTime.setAccumulatedOnlineTime(readQ());
            accountTime.setAccumulatedRestTime(readQ());
            accessLevel = (byte) readC();
            membership = (byte) readC();
            toll = readQ();
            expire = readQ();
            acc_enter = readD();
            accessLevelExpire = readQ();
            realPassword = readS();
            countryCode = readD();
            oldMembership = (byte) readC();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runImpl() {
        LoginServer.getInstance().accountAuthenticationResponse(accountId, accountName, result, accountTime, accessLevel, membership, toll, expire, acc_enter, accessLevelExpire, realPassword, countryCode, oldMembership);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
