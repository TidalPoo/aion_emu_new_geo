package com.aionemu.gameserver.network.telnet;

import java.util.Set;

/**
 * @author Amiko.Yuki
 */
public interface TelnetCommandHolder {

    /**
     * Get handler commands
     *
     * @return
     */
    public Set<TelnetCommand> getCommands();

}
