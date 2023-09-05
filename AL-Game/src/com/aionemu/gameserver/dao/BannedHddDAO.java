/**
 * SAO Project
 */
package com.aionemu.gameserver.dao;

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.gameserver.network.BannedHDDEntry;
import java.util.Map;

/**
 *
 * @author Alex
 */
public abstract class BannedHddDAO implements DAO {

    public abstract boolean update(BannedHDDEntry entry);

    public abstract boolean remove(String address);

    public abstract Map<String, BannedHDDEntry> load();

    public abstract void cleanExpiredBans();

    @Override
    public final String getClassName() {
        return BannedHddDAO.class.getName();
    }
}
