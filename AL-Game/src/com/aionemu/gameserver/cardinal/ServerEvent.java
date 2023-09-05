/*
 * SAO Project by Alex
 */
package com.aionemu.gameserver.cardinal;

import com.aionemu.gameserver.configs.main.GSConfig;
import java.util.Date;

/**
 *
 * @author Alex
 */
public class ServerEvent {

    static int day;
    static int month;
    static int year;

    public static boolean start() {
        int count = 0;
        for (String time : GSConfig.SERVER_START_TIME.split(".")) {
            count++;
            int t = Integer.parseInt(time);
            switch (count) {
                case 1:
                    day = t;
                    break;
                case 2:
                    month = t;
                    break;
                case 3:
                    year = t;
                    break;
            }
        }
        return day != 0 && month != 0 && year != 0;
    }

    public static void startEvent() {
        long time = System.currentTimeMillis();
        long sec = time / 1000;
        long min = sec / 60;
        long hour = min / 60;
        long d = hour / 24;
        Date date = new Date(time);
    }
}
