package com.aionemu.gameserver.network;

/**
 *
 * @author Alex
 */
public class WindowsVersion {

    public static String getWindows(int winVersion, int winSubVersion) {
        String version = "Другая ОС";
        if (winVersion == 6) {
            if (winSubVersion == 0) {
                version = "VISTA";
            } else if (winSubVersion == 1) {
                version = "7";
            } else if (winSubVersion == 2) {
                version = "8";
            } else if (winSubVersion == 3) {
                version = "8.1";
            }
        } else if (winVersion == 5) {
            if (winSubVersion == 1) {
                version = "XP x32";
            } else if (winSubVersion == 2) {
                version = "XP x64";
            }
        }
        return "Windows " + version;
    }
}
