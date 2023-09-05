/*
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.gameobjects.player;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Alex
 */
public class RequestFriendList implements Iterable<RequestFriend> {

    private volatile byte requestListSent = 0;
    private final Queue<RequestFriend> requests;
    private Player player;

    public RequestFriendList(Player player) {
        this(player, new ConcurrentLinkedQueue<>());
    }

    public RequestFriendList(Player owner, Collection<RequestFriend> newRequests) {
        this.requests = new ConcurrentLinkedQueue<>(newRequests);
        this.player = owner;
    }

    public RequestFriend getRequest(int objId) {
        for (RequestFriend friend : requests) {
            if (friend.getOid() == objId) {
                return friend;
            }
        }
        return null;
    }

    public int getSize() {
        return requests.size();
    }

    public RequestFriend getRequest(String name) {
        for (RequestFriend friend : requests) {
            if (friend.getName().equalsIgnoreCase(name)) {
                return friend;
            }
        }
        return null;
    }

    public void delRequest(int requestOid) {
        Iterator<RequestFriend> it = iterator();
        while (it.hasNext()) {
            if (it.next().getOid() == requestOid) {
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
    public Iterator<RequestFriend> iterator() {
        return requests.iterator();
    }

    public boolean getIsRequestListSent() {
        return requestListSent == 1;
    }

    public void setIsRequestListSent(boolean value) {
        this.requestListSent = (byte) (value ? 1 : 0);
    }
}
