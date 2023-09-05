/*
 * AionLight project
 */
package com.aionemu.gameserver.model.account;

/**
 *
 * @author Alex
 */
public class CountryCode {

    private int countryCode;
    private int accountId;

    public CountryCode(int accountId, int countryCode) {
        this.accountId = accountId;
        this.countryCode = countryCode;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public int getAccountId() {
        return accountId;
    }
}
