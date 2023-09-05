/**
 * AionLight Project
 */
package com.aionemu.loginserver.controller;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.IUStH;
import com.aionemu.commons.services.CronService;
import com.aionemu.loginserver.model.Account;
import com.aionemu.loginserver.utils.AccountUtils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alex
 */
public class GMController {

    private static final Logger log = LoggerFactory.getLogger("GM_AUDIT");

    public boolean sequrityGmOnLogin(Account account, String ipAddress) {
        if (account.getAccessLevel() == 0 || ipAddress.equals("127.0.0.1")) {
            return true;
        }
        BufferedReader br = null;
        int id = account.getId();
        try {
            br = new BufferedReader(new FileReader("./config/GameMasterIP.txt"));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }

                if (line.replaceAll(" ", "").contains("AccountID:" + id + "|")) {
                    int index = line.lastIndexOf("IP:");
                    int ind2 = line.indexOf("AL:");
                    String parseIP = line.substring(index + 3).replaceAll(" ", "");
                    String al = line.substring(ind2 + 3, index - 2).replaceAll(" ", "");
                    if (!al.equals("*")) {
                        int accesslevel = Integer.parseInt(al);
                        if (account.getAccessLevel() > accesslevel) {
                            return false;
                        }
                    }
                    if ("*".equals(parseIP)) {
                        return true;
                    }
                    String[] ap = parseIP.split(",");
                    List<String> ints = new ArrayList<>();
                    ints.addAll(Arrays.asList(ap));
                    for (String is : ints) {
                        return is.equals(ipAddress);
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

    public void startTask() {
        String cron = "0 0 0,8 ? * SUN";
        CronService.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                NewGennericPasswoord();
            }
        }, cron);
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final GMController instance = new GMController();
    }

    public static GMController getInstance() {
        return SingletonHolder.instance;
    }

    public void NewGennericPasswoord() {
        getPhones();
        for (PhoneMap p : ph.values()) {
            String pass = password();
            try {
                String newpasswoord = AccountUtils.encodePassword(pass);
                updatePassword(p.getId(), newpasswoord);
                sms_alert.setPhone(p.getPhone());
                sms_alert.Text("Ваш новый пароль от аккаунта " + p.getName() + " :"
                        + "\n" + pass);
                sms_alert.main(null);
                log.info("Account: " + p.getName() + " NewPassword: " + pass);
            } catch (MalformedURLException ex) {
            } catch (IOException ex) {
            }
        }
    }

    public int generatePort() {

        return 0;
    }

    public String password() {
        String pass = "";
        Random r = new Random();
        int to = 12;
        int from = 6;
        int cntchars = from + r.nextInt(to - from + 1);
        for (int i = 0; i < cntchars; ++i) {
            char next = 0;
            int range = 10;

            switch (r.nextInt(3)) {
                case 0: {
                    next = '0';
                    range = 10;
                }
                break;
                case 1: {
                    next = 'a';
                    range = 26;
                }
                break;
                case 2: {
                    next = 'A';
                    range = 26;
                }
                break;
            }
            pass += (char) ((r.nextInt(range)) + next);
        }
        return pass;
    }

    public boolean updatePassword(final int accountId, final String pass) {
        return DB.insertUpdate("UPDATE account_data SET password = ? WHERE id = ?", new IUStH() {
            @Override
            public void handleInsertUpdate(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, pass);
                preparedStatement.setInt(2, accountId);
                preparedStatement.execute();
            }
        });
    }
    private Map<Integer, PhoneMap> ph = new HashMap<>();

    public void getPhones() {
        PreparedStatement st = DB.prepareStatement("SELECT id, name, password, phone_number FROM account_data WHERE access_level > 0 AND phone_number != 0");
        try {
            ResultSet rs = st.executeQuery();
            rs.last();
            int count = rs.getRow();
            rs.beforeFirst();
            for (int i = 0; i < count; i++) {
                rs.next();
                ph.put(rs.getInt("id"), new PhoneMap(rs.getInt("id"), rs.getString("name"), rs.getString("password"), rs.getString("phone_number")));
            }
        } catch (SQLException e) {
        } finally {
            DB.close(st);
        }
    }
}
