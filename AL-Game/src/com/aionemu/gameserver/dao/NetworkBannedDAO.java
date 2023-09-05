/**
 * SAO Project
 */
package com.aionemu.gameserver.dao;

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.gameserver.network.NetworkBanEntry;
import java.util.Map;

/**
 *
 * @author Alex
 */
public abstract class NetworkBannedDAO implements DAO {

    public abstract boolean update(NetworkBanEntry entry);

    public abstract boolean remove(String address);

    public abstract Map<String, NetworkBanEntry> load();

    public abstract void cleanExpiredBans();

    @Override
    public final String getClassName() {
        return NetworkBannedDAO.class.getName();
    }
}
