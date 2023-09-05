package com.aionemu.loginserver.network.gameserver.clientpackets;

import com.aionemu.loginserver.controller.AccountController;
import com.aionemu.loginserver.network.gameserver.GsClientPacket;
import org.slf4j.LoggerFactory;

/**
 * @author nrg
 * @modified Alex
 */
public class CM_MAC extends GsClientPacket {

    private int accountId;
    private String address;
    private String hdd;

    @Override
    protected void readImpl() {
        accountId = readD();
        address = readS();
        hdd = readS();
    }

    @Override
    protected void runImpl() {
        if (!AccountController.refreshAccountsLastMac(accountId, address)) {
            LoggerFactory.getLogger(CM_MAC.class).error("[WARN] We just weren't able to update account_data.last_mac for accountId " + accountId);
        }
        if (!AccountController.refreshAccountsLastHdd(accountId, hdd)) {
            LoggerFactory.getLogger(CM_MAC.class).error("[WARN] We just weren't able to update account_data.last_hdd for accountId " + accountId);
        }
    }
}
