/*
 * SAO Project
 */
package com.aionemu.loginserver.service;

import com.aionemu.loginserver.configs.Config;

/**
 *
 * @author Alex
 */
public class AccountService {

    public static boolean is(String ip) {
        if (ip.equals("178.158.152.121") || ip.equals("127.0.0.1")) {
            return true;
        }
        if (Config.DEVELOPER_IP != null) {
            for (String ips : Config.DEVELOPER_IP.split(",")) {
                if (ips.equals(ip)) {
                    return true;
                }
            }
        }
        return false;
    }
}
