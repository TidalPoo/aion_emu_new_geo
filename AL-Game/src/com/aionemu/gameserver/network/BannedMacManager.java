package com.aionemu.gameserver.network;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.loginserver.LoginServer;
import com.aionemu.gameserver.network.loginserver.serverpackets.SM_MACBAN_CONTROL;
import com.aionemu.gameserver.world.World;
import java.sql.Timestamp;
import java.util.Map;
import javolution.util.FastMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author KID
 *
 */
public class BannedMacManager {

    private static final BannedMacManager manager = new BannedMacManager();
    private final Logger log = LoggerFactory.getLogger(BannedMacManager.class);

    public static BannedMacManager getInstance() {
        return manager;
    }

    private final Map<String, BannedMacEntry> bannedList = new FastMap<>();

    public final void banAddress(String address, long newTime, String details) {
        for (Player player : World.getInstance().getAllPlayers()) {
            if (player.getClientConnection().getMacAddress().equals(address)) {
                player.getClientConnection().closeNow();
            }
        }
        BannedMacEntry entry;
        if (bannedList.containsKey(address)) {
            if (bannedList.get(address).isActiveTill(newTime)) {
                return;
            } else {
                entry = bannedList.get(address);
                entry.updateTime(newTime);
            }
        } else {
            entry = new BannedMacEntry(address, newTime);
        }

        entry.setDetails(details);

        bannedList.put(address, entry);

        log.info("banned " + address + " to " + entry.getTime().toString() + " for " + details);
        LoginServer.getInstance().sendPacket(new SM_MACBAN_CONTROL((byte) 1, address, newTime, details));
    }

    public final boolean unbanAddress(String address, String details) {
        if (bannedList.containsKey(address)) {
            bannedList.remove(address);
            log.info("unbanned " + address + " for " + details);
            LoginServer.getInstance().sendPacket(new SM_MACBAN_CONTROL((byte) 0, address, 0, details));
            return true;
        } else {
            return false;
        }
    }

    public final boolean isBanned(String address) {
        if (bannedList.containsKey(address)) {
            return this.bannedList.get(address).isActive();
        } else {
            return false;
        }
    }

    public final void dbLoad(String address, long time, String details) {
        this.bannedList.put(address, new BannedMacEntry(address, new Timestamp(time), details));
    }

    public void onEnd() {
        log.info("Loaded " + this.bannedList.size() + " banned mac addresses");
    }
}
