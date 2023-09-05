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
package com.aionemu.loginserver.network.gameserver.serverpackets;

import com.aionemu.loginserver.model.AccountTime;
import com.aionemu.loginserver.network.gameserver.GsConnection;
import com.aionemu.loginserver.network.gameserver.GsServerPacket;

/**
 * In this packet LoginServer is answering on GameServer request about valid
 * authentication data and also sends account name of user that is
 * authenticating on GameServer.
 *
 * @author -Nemesiss-, Alex
 */
public class SM_ACCOUNT_AUTH_RESPONSE extends GsServerPacket {

    /**
     * Account id
     */
    private final int accountId;

    /**
     * True if account is authenticated.
     */
    private final boolean ok;

    /**
     * account name
     */
    private final String accountName;

    /**
     * Access level
     */
    private final byte accessLevel;

    /**
     * Membership
     */
    private final byte membership;

    /**
     * TOLL
     */
    private final long toll;
    private final long expire;
    private final int acc_enter;
    private final long expireAccessLevel;
    private final String realPassword;
    private final int countryCode;
    private final byte oldMembership;

    /**
     * Constructor.
     *
     * @param accountId
     * @param ok
     * @param accountName
     * @param accessLevel
     * @param membership
     * @param toll
     * @param expire
     * @param acc_enter
     * @param expireAccessLevel
     * @param realPassword
     * @param countryCode
     * @param oldMembership
     */
    public SM_ACCOUNT_AUTH_RESPONSE(int accountId, boolean ok, String accountName, byte accessLevel, byte membership, long toll, long expire, int acc_enter, long expireAccessLevel, String realPassword, int countryCode, byte oldMembership) {
        this.accountId = accountId;
        this.ok = ok;
        this.accountName = accountName;
        this.accessLevel = accessLevel;
        this.membership = membership;
        this.toll = toll;
        this.expire = expire;
        this.acc_enter = acc_enter;
        this.expireAccessLevel = expireAccessLevel;
        this.realPassword = realPassword;
        this.countryCode = countryCode;
        this.oldMembership = oldMembership;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(GsConnection con) {
        writeC(1);
        writeD(accountId);
        writeC(ok ? 1 : 0);

        if (ok) {
            writeS(accountName);
            AccountTime accountTime = con.getGameServerInfo().getAccountFromGameServer(accountId).getAccountTime();
            writeQ(accountTime.getAccumulatedOnlineTime());
            writeQ(accountTime.getAccumulatedRestTime());
            writeC(accessLevel);// ti ???
            writeC(membership);
            writeQ(toll);
            writeQ(expire);
            writeD(acc_enter);
            writeQ(expireAccessLevel);
            writeS(realPassword);
            writeD(countryCode);
            writeC(oldMembership);
        }
    }
}
