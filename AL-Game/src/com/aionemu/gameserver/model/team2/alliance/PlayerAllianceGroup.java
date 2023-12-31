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
package com.aionemu.gameserver.model.team2.alliance;

import com.aionemu.gameserver.model.team2.TemporaryPlayerTeam;

/**
 * @author ATracer
 */
public class PlayerAllianceGroup extends TemporaryPlayerTeam<PlayerAllianceMember> {

    private final PlayerAlliance alliance;
    private float x;
    private float y;
    private float z;

    public PlayerAllianceGroup(PlayerAlliance alliance, Integer objId) {
        super(objId);
        this.alliance = alliance;
    }

    @Override
    public void addMember(PlayerAllianceMember member) {
        super.addMember(member);
        member.setPlayerAllianceGroup(this);
        member.setAllianceId(getTeamId());
    }

    @Override
    public void removeMember(PlayerAllianceMember member) {
        super.removeMember(member);
        member.setPlayerAllianceGroup(null);
    }

    @Override
    public boolean isFull() {
        return size() == 6;
    }

    @Override
    public int getMinExpPlayerLevel() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getMaxExpPlayerLevel() {
        // TODO Auto-generated method stub
        return 0;
    }

    public PlayerAlliance getAlliance() {
        return alliance;
    }

    public void setFfaStartPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

}
