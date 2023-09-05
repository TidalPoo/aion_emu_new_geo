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
package com.aionemu.gameserver.model.stats.container;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.stats.calc.Stat2;

/**
 * @author ATracer
 */
public class TrapGameStats extends NpcGameStats {

    public TrapGameStats(Npc owner) {
        super(owner);
    }

    /**
     *
     * Spike Trap 100% -
     * 749072-749073-749074-749078-749079-749080-749092-749093-749238-749239 OK
     * Spike Bite Trap 100%	- 749096-749097-749242-749243-749071-749077 OK
     * Skybound Trap 100%	- 749278-749279-830170-830171 OK Explosion Trap 100%	-
     * 281545-749012-749018-749019-749020-749021-749032-749033-749240-749241 OK
     * Sleep Trap 50% - 749179-749180-749282-749293-749292 OK Poisoning Trap 50%
     * - 749190-749191-749270-749271-749143-749144-749145-749146-749147-749148
     * OK Slowing Trap 50%	-
     * 749196-749197-749290-749291-749296-749167-749168-749169-749170-749171-749172
     * OK Sandstorm Trap 50%	-
     * 749272-749273-296466-296479-296500-749034-749035-749036-749037
     *
     *
     * @param statEnum
     * @param base
     * @return
     */
    @Override
    public Stat2 getStat(StatEnum statEnum, int base) {
        Stat2 stat = super.getStat(statEnum, base);
        if (owner.getMaster() == null) {
            return stat;
        }
        switch (statEnum) {
            case BOOST_MAGICAL_SKILL:
                stat.setBonusRate(0.7f);
                return owner.getMaster().getGameStats().getItemStatBoost(statEnum, stat);
            case MAGICAL_ACCURACY:
                // bonus is calculated from stat bonus of master (only green value)
                if (owner.getNpcId() == 749072 || owner.getNpcId() == 749073 || owner.getNpcId() == 749074 || owner.getNpcId() == 749078
                        || owner.getNpcId() == 749079 || owner.getNpcId() == 749080 || owner.getNpcId() == 749092 || owner.getNpcId() == 749093
                        || owner.getNpcId() == 749238 || owner.getNpcId() == 749239) {
                    stat.setBonusRate(1f); //like retail 100% bonus of creator
                } else if (owner.getNpcId() == 749096 || owner.getNpcId() == 749097 || owner.getNpcId() == 749242 || owner.getNpcId() == 749243
                        || owner.getNpcId() == 749071 || owner.getNpcId() == 749077) {
                    stat.setBonusRate(1f); //like retail 100% bonus of creator
                } else if (owner.getNpcId() == 749278 || owner.getNpcId() == 749279 || owner.getNpcId() == 830170 || owner.getNpcId() == 830171) {
                    stat.setBonusRate(1f); //like retail 100% bonus of creator
                } else if (owner.getNpcId() == 281545 || owner.getNpcId() == 749012 || owner.getNpcId() == 749018 || owner.getNpcId() == 749019
                        || owner.getNpcId() == 749020 || owner.getNpcId() == 749021 || owner.getNpcId() == 749032 || owner.getNpcId() == 749033
                        || owner.getNpcId() == 749240 || owner.getNpcId() == 749241) {
                    stat.setBonusRate(1f); //like retail 100% bonus of creator
                } else if (owner.getNpcId() == 749179 || owner.getNpcId() == 749180 || owner.getNpcId() == 749282 || owner.getNpcId() == 749293 || owner.getNpcId() == 749292) {
                    stat.setBonusRate(0.5f); //like retail 50% bonus of creator
                } else if (owner.getNpcId() == 749190 || owner.getNpcId() == 749191 || owner.getNpcId() == 749270 || owner.getNpcId() == 749271
                        || owner.getNpcId() == 749143 || owner.getNpcId() == 749144 || owner.getNpcId() == 749145 || owner.getNpcId() == 749146
                        || owner.getNpcId() == 749147 || owner.getNpcId() == 749148) {
                    stat.setBonusRate(0.5f); //like retail 50% bonus of creator
                } else if (owner.getNpcId() == 749196 || owner.getNpcId() == 749197 || owner.getNpcId() == 749290 || owner.getNpcId() == 749291
                        || owner.getNpcId() == 749296 || owner.getNpcId() == 749167 || owner.getNpcId() == 749168 || owner.getNpcId() == 749169
                        || owner.getNpcId() == 749170 || owner.getNpcId() == 749171 || owner.getNpcId() == 749172) {
                    stat.setBonusRate(0.5f); //like retail 50% bonus of creator
                } else if (owner.getNpcId() == 749272 || owner.getNpcId() == 749273 || owner.getNpcId() == 296466 || owner.getNpcId() == 296479
                        || owner.getNpcId() == 296500 || owner.getNpcId() == 749034 || owner.getNpcId() == 749035 || owner.getNpcId() == 749036
                        || owner.getNpcId() == 749037) {
                    stat.setBonusRate(0.5f); //like retail 50% bonus of creator
                } else {
                    stat.setBonusRate(0.7f); //like retail 100% bonus of creator
                }
                return owner.getMaster().getGameStats().getItemStatBoost(statEnum, stat);

        }
        return stat;
    }
}
