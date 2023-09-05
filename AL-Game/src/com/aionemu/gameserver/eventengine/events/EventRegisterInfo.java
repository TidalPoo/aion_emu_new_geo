package com.aionemu.gameserver.eventengine.events;

import com.aionemu.gameserver.eventengine.debug.DebugInfo;
import com.aionemu.gameserver.eventengine.events.enums.EventRergisterState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 *
 * @author flashman
 */
public class EventRegisterInfo extends DebugInfo<EventRergisterState> {

    public EventRegisterInfo(EventRergisterState state, String message) {
        super(state, message);
    }

    public EventRegisterInfo(EventRergisterState state) {
        super(state);
    }

    public void sendMessageToGroupMebmers(PlayerGroup pg) {
        switch (this.state) {
            case GROUP_NOT_REGISTRED:
                break;
            default:
                for (Player p : pg.getMembers()) {
                    if (p != null && p.isOnline()) {
                        PacketSendUtility.sendMessage(p, message);
                    }
                }
                break;
        }
    }
}
