/**
 * SAO Project
 */
package com.aionemu.gameserver.network;

/**
 *
 * @author Alex
 */
public class IPv4 {

    public static String getIP(int ip) {
        byte[] s = new byte[]{(byte) ip,
            (byte) (ip >>> 8),
            (byte) (ip >>> 16),
            (byte) (ip >>> 24)};
        return (s[0] & 0xFF) + "." + (s[1] & 0xFF) + "." + (s[2] & 0xFF) + "." + (s[3] & 0xFF);
    }
}
