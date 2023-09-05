package com.aionemu.gameserver.network;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.NetworkBannedDAO;
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
public class NetworkBannedManager {

    private Map<String, NetworkBanEntry> bannedList = new FastMap<>();
    private final Logger log = LoggerFactory.getLogger(NetworkBannedManager.class);

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final NetworkBannedManager networkban = new NetworkBannedManager();
    }

    public static NetworkBannedManager getInstance() {
        return SingletonHolder.networkban;
    }
    private final NetworkBannedDAO dao = DAOManager.getDAO(NetworkBannedDAO.class);

    public NetworkBannedManager() {
        dao.cleanExpiredBans();
        bannedList = dao.load();
        log.info("Loaded " + bannedList.size() + " banned nip list.");
    }

    public final void banAddress(String address, long newTime, String details) {
        for (Player player : World.getInstance().getAllPlayers()) {
            if (player.getClientConnection().getIP().startsWith(address)) {
                player.getClientConnection().closeNow();
            }
        }
        NetworkBanEntry entry;
        if (bannedList.containsKey(address)) {
            if (bannedList.get(address).isActiveTill(newTime)) {
                return;
            } else {
                entry = bannedList.get(address);
                entry.updateTime(newTime);
            }
        } else {
            entry = new NetworkBanEntry(address, newTime);
        }

        entry.setDetails(details);

        bannedList.put(address, entry);
        this.dao.update(entry);
        log.info("banned " + address + " to " + entry.getTime().toString() + " for " + details);
    }

    public final boolean unbanAddress(String address, String details) {
        if (bannedList.containsKey(address)) {
            bannedList.remove(address);
            this.dao.remove(address);
            log.info("unbanned " + address + " for " + details);
            // LoginServer.getInstance().sendPacket(new SM_MACBAN_CONTROL((byte) 0, address, 0, details));
            return true;
        } else {
            return false;
        }
    }

    public final boolean isBanned(String address) {
        if (bannedList.containsKey(address)) {
            log.info("IP: " + address + " is such banned list!");
            return this.bannedList.get(address).isActive();
        } else {
            return false;
        }
    }

    public final void dbLoad(String address, long time, String details) {
        this.bannedList.put(address, new NetworkBanEntry(address, new Timestamp(time), details));
    }
}
