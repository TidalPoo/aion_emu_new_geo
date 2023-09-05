package com.aionemu.gameserver.dao;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alex
 *
 */
public class MySQL5AionDAO {

    private static final Logger log = LoggerFactory.getLogger(MySQL5AionDAO.class);

    public void insertIP(int accountId, String ip) {
        Connection con = null;
        boolean ff = false;
        try {
            con = DatabaseFactory.getConnection();
            try (PreparedStatement localPreparedStatement = con.prepareStatement("SELECT * FROM ip_list WHERE account_id = ? and ip = ?")) {
                localPreparedStatement.setInt(1, accountId);
                localPreparedStatement.setString(2, ip);
                ResultSet rs = localPreparedStatement.executeQuery();
                if (rs.next()) {
                    ff = true;
                    int count = rs.getInt("count");
                    count++;
                    try (PreparedStatement smtp = con.prepareStatement("UPDATE ip_list SET count = ?, time = ? WHERE ip = ? and account_id = ? and time < CURRENT_TIMESTAMP")) {
                        smtp.setInt(1, count);
                        smtp.setTimestamp(2, new Timestamp(System.currentTimeMillis() + 1L * 24 * 60 * 60 * 1000));
                        smtp.setString(3, ip);
                        smtp.setInt(4, accountId);
                        smtp.execute();
                    }
                }
            }
            if (!ff) {
                try (PreparedStatement smtp = con.prepareStatement("INSERT INTO `ip_list` (account_id, ip, time, count) VALUES (?, ?, ?, ?)")) {
                    smtp.setInt(1, accountId);
                    smtp.setString(2, ip);
                    smtp.setTimestamp(3, new Timestamp(System.currentTimeMillis() + 1L * 24 * 60 * 60 * 1000));
                    smtp.setInt(4, 1);
                    smtp.execute();
                }
            }
        } catch (SQLException ex) {
            log.error(ex + "");
        } finally {
            DatabaseFactory.close(con);
        }
    }

    public void insertMac(int accountId, String mac) {
        Connection con = null;
        boolean ff = false;
        try {
            con = DatabaseFactory.getConnection();
            try (PreparedStatement localPreparedStatement = con.prepareStatement("SELECT * FROM mac_list WHERE account_id = ? and mac_address = ?")) {
                localPreparedStatement.setInt(1, accountId);
                localPreparedStatement.setString(2, mac);
                ResultSet rs = localPreparedStatement.executeQuery();
                if (rs.next()) {
                    int count = rs.getInt("count");
                    count++;
                    try (PreparedStatement smtp = con.prepareStatement("UPDATE mac_list SET count = ?, time = ? WHERE mac_address = ? and account_id = ?")) {
                        smtp.setInt(1, count);
                        smtp.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                        smtp.setString(3, mac);
                        smtp.setInt(4, accountId);
                        smtp.execute();
                        ff = true;
                    }
                }
            }
            if (!ff) {
                try (PreparedStatement smtp = con.prepareStatement("INSERT INTO `mac_list` (account_id, mac_address, time, count) VALUES (?, ?, ?, ?)")) {
                    smtp.setInt(1, accountId);
                    smtp.setString(2, mac);
                    smtp.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                    smtp.setInt(4, 1);
                    smtp.execute();
                }
            }
        } catch (SQLException ex) {
            log.error(ex + "");
        } finally {
            DatabaseFactory.close(con);
        }
    }

    public void insertHdd(int accountId, String hdd) {
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            boolean ff = false;
            try (PreparedStatement localPreparedStatement = con.prepareStatement("SELECT * FROM hdd_list WHERE account_id = ? and hdd_serial = ?")) {
                localPreparedStatement.setInt(1, accountId);
                localPreparedStatement.setString(2, hdd);
                ResultSet rs = localPreparedStatement.executeQuery();
                if (rs.next()) {
                    int count = rs.getInt("count");
                    count++;
                    try (PreparedStatement smtp = con.prepareStatement("UPDATE hdd_list SET count = ?, time = ? WHERE hdd_serial = ? and account_id = ?")) {
                        smtp.setInt(1, count);
                        smtp.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                        smtp.setString(3, hdd);
                        smtp.setInt(4, accountId);
                        smtp.execute();
                        ff = true;
                    }
                }
            }
            if (!ff) {
                try (PreparedStatement smtp = con.prepareStatement("INSERT INTO `hdd_list` (account_id, hdd_serial, time, count) VALUES (?, ?, ?, ?)")) {
                    smtp.setInt(1, accountId);
                    smtp.setString(2, hdd);
                    smtp.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                    smtp.setInt(4, 1);
                    smtp.execute();
                }
            }
        } catch (SQLException ex) {
            log.error(ex + "");
        } finally {
            DatabaseFactory.close(con);
        }
    }

    public void insertTraceroute(int accountId, String ips) {
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            boolean ff = false;
            try (PreparedStatement localPreparedStatement = con.prepareStatement("SELECT * FROM ip_traceroute_list WHERE account_id = ? and ips = ?")) {
                localPreparedStatement.setInt(1, accountId);
                localPreparedStatement.setString(2, ips);
                ResultSet rs = localPreparedStatement.executeQuery();
                if (rs.next()) {
                    int count = rs.getInt("count");
                    count++;
                    try (PreparedStatement smtp = con.prepareStatement("UPDATE ip_traceroute_list SET count = ?, time = ? WHERE ips = ? and account_id = ?")) {
                        smtp.setInt(1, count);
                        smtp.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                        smtp.setString(3, ips);
                        smtp.setInt(4, accountId);
                        smtp.execute();
                        ff = true;
                    }
                }
            }
            if (!ff) {
                try (PreparedStatement smtp = con.prepareStatement("INSERT INTO `ip_traceroute_list` (account_id, ips, time, count) VALUES (?, ?, ?, ?)")) {
                    smtp.setInt(1, accountId);
                    smtp.setString(2, ips);
                    smtp.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                    smtp.setInt(4, 1);
                    smtp.execute();
                }
            }
        } catch (SQLException ex) {
            log.error(ex + "");
        } finally {
            DatabaseFactory.close(con);
        }
    }

    public void getWords(Player player) {
        Connection localConnection = null;

        try {
            localConnection = DatabaseFactory.getConnection();
            try (PreparedStatement localPreparedStatement = localConnection.prepareStatement("SELECT * FROM players WHERE id = ?")) {
                localPreparedStatement.setInt(1, player.getObjectId());
                ResultSet localResultSet = localPreparedStatement.executeQuery();
                if (localResultSet.next()) {
                    player.setObsceneWordsCount(localResultSet.getInt("words"));
                    player.setObsceneWordsTime(localResultSet.getLong("words_time"));
                }
            }
        } catch (SQLException localSQLException) {
            log.error("Can't get words for " + player.getObjectId(), localSQLException);
        } finally {
            DatabaseFactory.close(localConnection);
        }
    }

    public void setExpire(int accountID, Timestamp time) {
        Connection localConnection = null;
        try {
            localConnection = DatabaseFactory.getConnection();
            try (PreparedStatement localPreparedStatement = localConnection.prepareStatement("UPDATE account_data SET expire = ? WHERE id = ?")) {
                localPreparedStatement.setTimestamp(1, time);
                localPreparedStatement.setInt(2, accountID);
                localPreparedStatement.execute();
            }
        } catch (SQLException localSQLException) {
            log.error("Can't update expire for " + accountID, localSQLException);
        } finally {
            DatabaseFactory.close(localConnection);
        }
    }

    public void setTimeAndCountWords(int playerID, int count, Timestamp time) {
        Connection localConnection = null;
        try {
            localConnection = DatabaseFactory.getConnection();
            try (PreparedStatement localPreparedStatement = localConnection.prepareStatement("UPDATE players SET words = ?, words_time = ? WHERE id = ?")) {
                localPreparedStatement.setInt(1, count);
                localPreparedStatement.setTimestamp(2, time);
                localPreparedStatement.setInt(3, playerID);
                localPreparedStatement.execute();
            }
        } catch (SQLException localSQLException) {
            log.error("Can't update words", localSQLException);
        } finally {
            DatabaseFactory.close(localConnection);
        }
    }

    public void insertAccuese(int accountId, int playerId, Timestamp time) {
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            try (PreparedStatement smtp = con.prepareStatement("INSERT INTO accuese_log (account_id, player_id, send_time) VALUES (?, ?, ?)")) {
                smtp.setInt(1, accountId);
                smtp.setInt(2, playerId);
                smtp.setTimestamp(3, time);
                smtp.execute();
            }
        } catch (SQLException ex) {
            log.error(ex + "");
        } finally {
            DatabaseFactory.close(con);
        }
    }

    public void loadAccuese(Player player) {
        Connection localConnection = null;
        try {
            localConnection = DatabaseFactory.getConnection();
            try (PreparedStatement localPreparedStatement = localConnection.prepareStatement("SELECT * FROM accuese_log WHERE player_id = ?")) {
                localPreparedStatement.setInt(1, player.getObjectId());
                ResultSet localResultSet = localPreparedStatement.executeQuery();
                if (localResultSet.next()) {
                    player.loadAccuese(localResultSet.getInt("account_id"));
                }
            }
        } catch (SQLException localSQLException) {
            log.error("Can't get accuese for " + player.getObjectId(), localSQLException);
        } finally {
            DatabaseFactory.close(localConnection);
        }
    }

    public static MySQL5AionDAO getInstance() {
        return new MySQL5AionDAO();
    }
}
