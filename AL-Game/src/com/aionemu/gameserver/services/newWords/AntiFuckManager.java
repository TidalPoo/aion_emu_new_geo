/*
 * AionLight project
 */
package com.aionemu.gameserver.services.newWords;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.PunishmentService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import javolution.util.FastList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alex
 */
public class AntiFuckManager {

    private static final Logger log = LoggerFactory.getLogger(AntiFuckManager.class);

    private static List<String> listC = new FastList<>();
    private static List<String> listT = new FastList<>();

    public static void wordsLoad() {
        getObsceneWordsInContext();
        getObsceneWordsInText();
    }

    public static void reload(Player player) {
        if (listC.size() > 0) {
            listC.clear();
        }
        if (listT.size() > 0) {
            listT.clear();
        }
        if (getObsceneWordsInContext()) {
            PacketSendUtility.sendMessage(player, "\u0417\u0430\u043f\u0440\u0435\u0449\u0435\u043d\u043d\u044b\u0435 \u0441\u043b\u043e\u0432\u0430 \u0432 \u043a\u043e\u043d\u0442\u0435\u043a\u0441\u0442\u0435 \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u043f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0436\u0435\u043d\u044b! \u0417\u0430\u0433\u0440\u0443\u0436\u0435\u043d\u043e: " + listC.size() + " \u0441\u043b\u043e\u0432");
        } else {
            PacketSendUtility.sendMessage(player, "\u041f\u0440\u043e\u0438\u0437\u043e\u0448\u043b\u0430 \u043e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u0437\u0430\u0433\u0440\u0443\u0437\u043a\u0435 \u0441\u043b\u043e\u0432 \u0434\u043b\u044f \u043f\u0440\u043e\u0432\u0435\u0440\u043a\u0438 \u043a\u043e\u043d\u0442\u0435\u043a\u0441\u0442\u0430");
        }

        if (getObsceneWordsInText()) {
            PacketSendUtility.sendMessage(player, "\u0417\u0430\u043f\u0440\u0435\u0449\u0435\u043d\u043d\u044b\u0435 \u0441\u043b\u043e\u0432\u0430 \u0432 \u0442\u0435\u043a\u0441\u0442\u0435 \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u043f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0436\u0435\u043d\u044b! \u0417\u0430\u0433\u0440\u0443\u0436\u0435\u043d\u043e: " + listT.size() + " \u0441\u043b\u043e\u0432");
        } else {
            PacketSendUtility.sendMessage(player, "\u041f\u0440\u043e\u0438\u0437\u043e\u0448\u043b\u0430 \u043e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u0437\u0430\u0433\u0440\u0443\u0437\u043a\u0435 \u043e\u0431\u044b\u0447\u043d\u044b\u0445 \u0441\u043b\u043e\u0432");
        }
    }

    public static boolean isForbiddenByClientText(String text) {
        if (listT == null || !CustomConfig.ENABLE_FORBIDDEN_RUS_TEXT) {
            return false;
        }
        for (String s : listT) {
            if (text.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean getWordToTextFile(String text, Player player, boolean context) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("./config/" + (context ? "RussianObsceneWordsContext.txt" : "RussianObsceneWordsText.txt")));
            String line = null;
            while ((line = br.readLine()) != null) {
                for (String s : text.split(",")) {
                    if (line.equalsIgnoreCase(s)) {
                        PacketSendUtility.sendMessage(player, "\"" + s + "\" \u0443\u0436\u0435 \u0435\u0441\u0442\u044c \u0432 \u0437\u0430\u043f\u0440\u0435\u0442\u0435");
                        return true;
                    }
                }
            }
        } catch (IOException e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
        return false;
    }

    public static void addedWordToTextFile(String text, Player player, boolean context) {
        if (getWordToTextFile(text, player, context)) {
            return;
        }
        PrintWriter file = null;
        try {
            file = new PrintWriter(new OutputStreamWriter(new FileOutputStream("./config/" + (context ? "RussianObsceneWordsContext.txt" : "RussianObsceneWordsText.txt"), true)));
        } catch (FileNotFoundException e) {
            PacketSendUtility.sendMessage(player, "\u041f\u0440\u043e\u0438\u0437\u043e\u0448\u043b\u0430 \u043e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u0437\u0430\u0433\u0440\u0443\u0437\u043a\u0435 \u0441\u043b\u043e\u0432 \u0434\u043b\u044f \u043f\u0440\u043e\u0432\u0435\u0440\u043a\u0438 \u043a\u043e\u043d\u0442\u0435\u043a\u0441\u0442\u0430");
            return;
        }
        String[] allt = text.split(",");
        file.println("# \u0414\u043e\u0431\u0430\u0432\u0438\u043b " + player.getName() + " " + allt.length + " \u0448\u0442 " + new Date(System.currentTimeMillis()));
        for (String allt1 : allt) {
            file.println(allt1);
        }
        PacketSendUtility.sendMessage(player, "\u0412\u044b \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u0434\u043e\u0431\u0430\u0432\u0438\u043b\u0438 " + allt.length + " \u0441\u043b\u043e\u0432\u043e(\u0430) \u0432 \u0444\u0430\u0439\u043b \u043f\u0440\u043e\u0432\u0435\u0440\u043a\u0438 " + (context ? "\u043a\u043e\u043d\u0442\u0435\u043a\u0441\u0442\u0430" : "\u0442\u0435\u043a\u0441\u0442\u0430") + "\n\u0414\u043e\u0431\u0430\u0432\u043b\u0435\u043d\u043e:\n \"" + text + "\"");
        file.close();
    }

    public static int getTimer(Player player) {
        int time = 2;
        if (player.getObsceneWordsCount() == 5) {
            time = 5;
        } else if (player.getObsceneWordsCount() == 10) {
            time = 30;
        } else if (player.getObsceneWordsCount() == 20) {
            time = 60;
        }
        return time;
    }

    public static void gag(final Player player) {
        PunishmentService.setGAG(player, true, getTimer(player), "AUTO GAG");
        PacketSendUtility.sendMessage(player, "\u0412\u044b \u043f\u043e\u043b\u0443\u0447\u0438\u043b\u0438 \u0431\u0430\u043d \u0447\u0430\u0442\u0430 \u0437\u0430 \u043c\u0430\u0442 \u043d\u0430 " + getTimer(player) + " \u043c\u0438\u043d\u0443\u0442");
    }

    public static boolean isForbiddenByClient(String name) {
        if (listC == null || !CustomConfig.ENABLE_FORBIDDEN_RUS_CONTEXT) {
            return false;
        }

        for (String s : listC) {
            if (name.toLowerCase().contains(s)) {
                return true;
            }
        }
        return false;
    }

    public static void getTime24Hour(Player player) {
        if (player.getObsceneWordsTime() != 0) {
            long i = ((System.currentTimeMillis() - player.getObsceneWordsTime()) / 60 / 60 / 1000);
            if (i >= 24) {
                player.setObsceneWordsCount(0);
            }
        }
    }

    public static String filterMessage(String message) {
        for (String words : message.split(" ")) {
            if (isForbiddenByClient(words) || isForbiddenByClientText(message)) {
                message = message.replace(words, "***");
            }
        }
        return message;
    }

    public static int sizeC() {
        return listC.size();
    }

    public static int sizeT() {
        return listT.size();
    }

    public static boolean getObsceneWordsInContext() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("./config/RussianObsceneWordsContext.txt"));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || line.trim().length() == 0) {
                    continue;
                }
                listC.add(line);
            }
            if (listC.size() > 0) {
                log.info("ObsceneWords successfully loaded. Load: " + listC.size() + " words for replace to context");
                return true;
            }
        } catch (IOException e) {
            log.error("Error load words for replace to context");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
        return false;
    }

    public static boolean getObsceneWordsInText() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("./config/RussianObsceneWordsText.txt"));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || line.trim().length() == 0) {
                    continue;
                }
                listT.add(line);
            }
            if (listT.size() > 0) {
                log.info("ObsceneWords successfully loaded. Load: " + listT.size() + " words for replace to text");
                return true;
            }
        } catch (IOException e) {
            log.error("Error load words for replace to text");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
        return false;
    }
}
