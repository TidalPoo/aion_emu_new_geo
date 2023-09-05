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
package com.aionemu.gameserver.model.team2.group;

import com.aionemu.gameserver.model.team2.TeamType;
import com.aionemu.gameserver.model.team2.TemporaryPlayerTeam;

/**
 * @author ATracer
 */
public class PlayerGroup extends TemporaryPlayerTeam<PlayerGroupMember> {

    private final PlayerGroupStats playerGroupStats;
    private final TeamType type;
    private int ffakills;
    private float x;
    private float y;
    private float z;
    // mix
    private float x2;
    private float y2;
    private float z2;

    public PlayerGroup(PlayerGroupMember leader, TeamType type, int id) {
        super(id);
        this.playerGroupStats = new PlayerGroupStats(this);
        this.type = type;
        initializeTeam(leader);
    }

    @Override
    public void addMember(PlayerGroupMember member) {
        super.addMember(member);
        playerGroupStats.onAddPlayer(member);
        member.getObject().setPlayerGroup2(this);
    }

    @Override
    public void removeMember(PlayerGroupMember member) {
        super.removeMember(member);
        playerGroupStats.onRemovePlayer(member);
        member.getObject().setPlayerGroup2(null);
    }

    @Override
    public boolean isFull() {
        return size() == 6;
    }

    @Override
    public int getMinExpPlayerLevel() {
        return playerGroupStats.getMinExpPlayerLevel();
    }

    @Override
    public int getMaxExpPlayerLevel() {
        return playerGroupStats.getMaxExpPlayerLevel();
    }

    public TeamType getTeamType() {
        return type;
    }

    public int getFfakills() {
        return ffakills;
    }

    public void setFfakills(int ffakills) {
        this.ffakills = ffakills;
    }

    public void increaseFfaKills() {
        ffakills++;
    }

    public void setFfaStartPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setMixStartPosition(float x, float y, float z) {
        this.x2 = x;
        this.y2 = y;
        this.z2 = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getMixX() {
        return x2;
    }

    public float getMixY() {
        return y2;
    }

    public float getMixZ() {
        return z2;
    }
}
