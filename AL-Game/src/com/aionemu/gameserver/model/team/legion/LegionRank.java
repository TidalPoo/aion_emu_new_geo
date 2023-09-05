/*
 * This file is part of aion-lightning <aion-lightning.com>.
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
package com.aionemu.gameserver.model.team.legion;

/**
 * @author Simple
 * @modifier Alex
 */
public enum LegionRank {

    /**
     * All Legion Ranks *
     */
    BRIGADE_GENERAL(0, "Легат"),
    DEPUTY(1, "Адьютант"),
    CENTURION(2, "Центурион"),
    LEGIONARY(3, "Легионер"),
    VOLUNTEER(4, "Новичок");

    private byte rank;
    private String rusname;

    private LegionRank(int rank, String rusname) {
        this.rank = (byte) rank;
        this.rusname = rusname;
    }

    /**
     * Returns client-side id for this
     *
     * @return byte
     */
    public byte getRankId() {
        return this.rank;
    }

    public String getRusname() {
        return rusname;
    }
}
