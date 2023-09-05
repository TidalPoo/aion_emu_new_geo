/**
 * SAO Project
 */
package com.aionemu.gameserver.model.gameobjects.player;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MARK_FRIENDLIST;
import com.aionemu.gameserver.utils.PacketSendUtility;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Alex
 */
public class RequestList implements Iterable<Request> {

    private volatile byte friendListSent = 0;

    private final Queue<Request> requests;

    private Player player;

    public RequestList(Player player) {
        this(player, new ConcurrentLinkedQueue<>());
    }

    public RequestList(Player owner, Collection<Request> newFriends) {
        this.requests = new ConcurrentLinkedQueue<>(newFriends);
        this.player = owner;
    }

    public Request getFriend(int objId) {
        for (Request friend : requests) {
            if (friend.getOid() == objId) {
                return friend;
            }
        }
        return null;
    }

    public int getSize() {
        return requests.size();
    }

    public void addFriend(Request friend) {
        requests.add(friend);
    }

    public Request getFriend(String name) {
        for (Request friend : requests) {
            if (friend.getName().equalsIgnoreCase(name)) {
                return friend;
            }
        }
        return null;
    }

    public void delFriend(int friendOid) {
        Iterator<Request> it = iterator();
        while (it.hasNext()) {
            if (it.next().getOid() == friendOid) {
                it.remove();
            }
        }
    }

    public boolean isFull() {
        int MAX_FRIENDS = player.havePermission(MembershipConfig.ADVANCED_FRIENDLIST_ENABLE) ? MembershipConfig.ADVANCED_FRIENDLIST_SIZE
                : CustomConfig.FRIENDLIST_SIZE;
        return getSize() >= MAX_FRIENDS;
    }

    @Override
    public Iterator<Request> iterator() {
        return requests.iterator();
    }

    public boolean getIsFriendListSent() {
        return friendListSent == 1;
    }

    public void setIsFriendListSent(boolean value) {
        this.friendListSent = (byte) (value ? 1 : 0);
    }

    public RequestList.Status getStatus() {
        return status;
    }

    private Status status = Status.OFFLINE;

    public void setStatus(Status status, PlayerCommonData pcd) {
        //Status previousStatus = this.status;
        this.status = status;
        //те кому ты отправил запросы.
        for (Request request : requests) {
            if (request.isOnline()) {
                Player requestPlayer = request.getPlayer();
                if (requestPlayer == null) {
                    continue;
                }

                if (requestPlayer.getClientConnection() == null) {
                    continue;
                }
                PlayerCommonData p = requestPlayer.getRequestFriendList().getRequest(pcd.getPlayerObjId()).getPCD();
                p.setOnline(!p.isOnline());
                p.setLevel(pcd.getLevel());
                requestPlayer.getRequestFriendList().getRequest(pcd.getPlayerObjId()).setPCD(p);
                PacketSendUtility.sendPacket(requestPlayer, new SM_MARK_FRIENDLIST());
            }
        }
    }

    public enum Status {

        /**
         * User is offline or invisible
         */
        OFFLINE((byte) 0),
        /**
         * User is online
         */
        ONLINE((byte) 1),
        /**
         * User is away or busy
         */
        AWAY((byte) 3);

        byte value;

        private Status(byte value) {
            this.value = value;
        }

        public byte getId() {
            return value;
        }

        /**
         * Gets the Status from its int value<br />
         * Returns null if out of range
         *
         * @param value range 0-3
         * @return Status
         */
        public static Status getByValue(byte value) {
            for (Status stat : values()) {
                if (stat.getId() == value) {
                    return stat;
                }
            }
            return null;
        }
    }
}
