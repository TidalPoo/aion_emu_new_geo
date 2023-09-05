package com.aionemu.gameserver.network;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.BannedHddDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.world.World;
import java.sql.Timestamp;
import java.util.Map;
import javolution.util.FastMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alex
 */
public class BannedHDDManager {

    private final Logger log = LoggerFactory.getLogger(BannedHDDManager.class);

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final BannedHDDManager hddmanager = new BannedHDDManager();
    }

    public static BannedHDDManager getInstance() {
        return SingletonHolder.hddmanager;
    }
    private final BannedHddDAO dao = DAOManager.getDAO(BannedHddDAO.class);

    public BannedHDDManager() {
        dao.cleanExpiredBans();
        bannedHddList = dao.load();
        log.info("Loaded " + bannedHddList.size() + " banned hdd list.");
    }

    private Map<String, BannedHDDEntry> bannedHddList = new FastMap<>();

    public final void banAddress(String address, long newTime, String details) {
        for (Player player : World.getInstance().getAllPlayers()) {
            if (player.getClientConnection().getHddSerial().equals(address)) {
                player.getClientConnection().closeNow();
            }
        }
        BannedHDDEntry entry;
        if (bannedHddList.containsKey(address)) {
            if (bannedHddList.get(address).isActiveTill(newTime)) {
                return;
            } else {
                entry = bannedHddList.get(address);
                entry.updateTime(newTime);
            }
        } else {
            entry = new BannedHDDEntry(address, newTime);
        }

        entry.setDetails(details);

        bannedHddList.put(address, entry);
        this.dao.update(entry);
        log.info("banned " + address + " to " + entry.getTime().toString() + " for " + details);
    }

    public final boolean unbanAddress(String address, String details) {
        if (bannedHddList.containsKey(address)) {
            bannedHddList.remove(address);
            this.dao.remove(address);
            log.info("unbanned " + address + " for " + details);
            // LoginServer.getInstance().sendPacket(new SM_MACBAN_CONTROL((byte) 0, address, 0, details));
            return true;
        } else {
            return false;
        }
    }

    public final boolean isBanned(String address) {
        if (bannedHddList.containsKey(address)) {
            log.info("HDD_SERIAL: " + address + " is such banned list!");
            return this.bannedHddList.get(address).isActive();
        } else {
            return false;
        }
    }

    public final void dbLoad(String address, long time, String details) {
        this.bannedHddList.put(address, new BannedHDDEntry(address, new Timestamp(time), details));
    }
}
