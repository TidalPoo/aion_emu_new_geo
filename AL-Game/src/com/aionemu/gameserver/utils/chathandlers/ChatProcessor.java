package com.aionemu.gameserver.utils.chathandlers;

import com.aionemu.commons.scripting.classlistener.AggregatedClassListener;
import com.aionemu.commons.scripting.classlistener.OnClassLoadUnloadListener;
import com.aionemu.commons.scripting.classlistener.ScheduledTaskClassListener;
import com.aionemu.commons.scripting.scriptmanager.ScriptManager;
import com.aionemu.commons.utils.PropertiesUtils;
import com.aionemu.gameserver.GameServerError;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.GameEngine;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.weddings.WeddingService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import javolution.util.FastMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author KID
 * @Modified Rolandas
 */
public class ChatProcessor implements GameEngine {

    private static final Logger log = LoggerFactory.getLogger("ADMINAUDIT_LOG");
    private static ChatProcessor instance = new ChatProcessor();
    private Map<String, ChatCommand> commands = new FastMap<>();
    private final Map<String, Byte> accessLevel = new FastMap<>();
    private ScriptManager sm = new ScriptManager();
    private Exception loadException = null;

    public static ChatProcessor getInstance() {
        return instance;
    }

    @Override
    public void load(CountDownLatch progressLatch) {
        try {
            log.info("Chat processor load started");
            init(sm, this);
        } finally {
            if (progressLatch != null) {
                progressLatch.countDown();
            }
        }
    }

    @Override
    public void shutdown() {
    }

    private ChatProcessor() {
    }

    private ChatProcessor(ScriptManager scriptManager) {
        init(scriptManager, this);
    }

    private void init(final ScriptManager scriptManager, ChatProcessor processor) {
        loadLevels();

        AggregatedClassListener acl = new AggregatedClassListener();
        acl.addClassListener(new OnClassLoadUnloadListener());
        acl.addClassListener(new ScheduledTaskClassListener());
        acl.addClassListener(new ChatCommandsLoader(processor));
        scriptManager.setGlobalClassListener(acl);

        final File[] files = new File[]{new File("./data/scripts/system/adminhandlers.xml"),
            new File("./data/scripts/system/playerhandlers.xml")/*, new File("./data/scripts/system/weddinghandlers.xml")*/};
        final CountDownLatch loadLatch = new CountDownLatch(files.length);

        for (int i = 0; i < files.length; i++) {
            final int index = i;
            ThreadPoolManager.getInstance().execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        scriptManager.load(files[index]);
                    } catch (Exception e) {
                        loadException = e;
                    } finally {
                        loadLatch.countDown();
                    }
                }
            });
        }

        try {
            loadLatch.await();
        } catch (InterruptedException e1) {
        }
        if (loadException != null) {
            throw new GameServerError("Can't initialize chat handlers.", loadException);
        }
    }

    public void registerCommand(ChatCommand cmd) {
        String command = cmd.getAlias();
        // byte access_Level = accessLevel.get(command);
        if (commands.containsKey(command)) {
            log.warn("Command " + command + " is already registered. Fail");
            return;
        }

        boolean noLvl = false;
        if (!accessLevel.containsKey(command)) {
            log.warn("Command " + command + " do not have access level. Fail");
            //return;
            noLvl = true;
        }

        cmd.setAccessLevel(noLvl ? 7 : accessLevel.get(command));
      
        commands.put(command, cmd);
    }

    public void reload(Player player) {
        ScriptManager tmpSM;
        final ChatProcessor adminCP;
        Map<String, ChatCommand> backupCommands = new FastMap<>(commands);
        commands.clear();
        loadException = null;

        try {
            tmpSM = new ScriptManager();
            adminCP = new ChatProcessor(tmpSM);
        } catch (Throwable e) {
            commands = backupCommands;
            String error = e + "";
            PacketSendUtility.sendMessage(player, "Произошла ошибка во время перезагрузки команд.");
            //PacketSendUtility.sendMessage(player, error.substring(0, error.length() > 100 ? 100 : error.length()));
            throw new GameServerError("Can't reload chat handlers.", e);
        }

        if (tmpSM != null && adminCP != null) {
            backupCommands.clear();
            sm.shutdown();
            sm = null;
            sm = tmpSM;
            instance = adminCP;
        }
    }

    private void loadLevels() {
        accessLevel.clear();
        try {
            java.util.Properties props = PropertiesUtils.load("config/administration/commands.properties");

            for (Object key : props.keySet()) {
                String str = (String) key;
                accessLevel.put(str, Byte.valueOf(props.getProperty(str).trim()));
            }
        } catch (IOException e) {
            log.error("Can't read commands.properties", e);
        }
    }

    public boolean handleChatCommand(Player player, String text) {
        if (text.split(" ").length == 0) {
            return false;
        }
        if ((text.startsWith("//") && (getCommand(text.substring(2)) instanceof AdminCommand
                || getCommand(text.substring(2)) instanceof PlayerCommand && player.getMembership() > 0))
                || (text.startsWith("...") && getCommand(text.substring(2)) instanceof WeddingCommand)) {
            return (getCommand(text.substring(2))).process(player, text.substring(2));
        } else if (text.startsWith(".")
                && (getCommand(text.substring(1)) instanceof PlayerCommand
                || (CustomConfig.ENABLE_ADMIN_DOT_COMMANDS && getCommand(text.substring(1)) instanceof AdminCommand))) {
            return (getCommand(text.substring(1))).process(player, text.substring(1));
        } else if (text.startsWith("..") && player.isMarried()) {
            return WeddingService.getInstance().WeddingCommand(player, text.substring(2));
        } else {
            if (text.startsWith("//")) {
                String com = text.substring(2);
                if (getCommand(com) instanceof PlayerCommand) {
                    PacketSendUtility.sendMessage(player, "Использование этой команды доступно через точку (.)");
                } else {
                    PacketSendUtility.sendMessage(player, "Не найдена команда: " + com + " !");
                }
                return true;
            } else if (text.startsWith(".")) {
                String com = text.substring(1);
                if (getCommand(com) instanceof AdminCommand) {
                    PacketSendUtility.sendMessage(player, "Использование этой команды доступно через два слеша (//)");
                } else {
                    PacketSendUtility.sendMessage(player, "Не найдена команда: " + com + " !");
                }
                return true;
            }
            return false;
        }
    }

    private ChatCommand getCommand(String text) {
        String alias = text.split(" ")[0];
        ChatCommand cmd = this.commands.get(alias);
        return cmd;
    }

    public void onCompileDone() {
        log.info("Loaded " + commands.size() + " commands.");
    }
}
