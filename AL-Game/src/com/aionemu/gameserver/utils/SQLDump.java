/*
 * SAO Project
 */
package com.aionemu.gameserver.utils;

import com.aionemu.commons.configs.DatabaseConfig;
import com.aionemu.commons.database.DatabaseFactory;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alex
 */
public class SQLDump {

    static Logger log = LoggerFactory.getLogger(SQLDump.class);

    public static void createFile() {
        String dbName;
        try {
            dbName = DatabaseFactory.getDatabaseName();
            String dbUser = DatabaseConfig.DATABASE_USER;
            String dbPass = DatabaseConfig.DATABASE_PASSWORD;
            String fileName = new SimpleDateFormat("E-dd-MMM-yyyyг.-HHч.mmм.", new Locale("ru", "RU")).format(new Date());
            String executeCmd = "mysqldump -u " + dbUser + " -p" + dbPass + " " + dbName + " -r " + fileName + ".sql";
            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();
            if (processComplete == 0) {
                //success
                log.info("successfully created sql file Game\\" + fileName + ".sql");
            }
        } catch (IOException | InterruptedException ex) {
            log.error("Error create sql file: " + ex);
        }
    }
}
