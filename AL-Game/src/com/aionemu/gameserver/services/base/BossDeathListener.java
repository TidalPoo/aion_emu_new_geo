/*
 * This file is part of archeage-emu <http://archeage-emu.com/>.
 * 
 * archeage-emu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * archeage-emu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with archeage-emu. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.services.base;

import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.ai2.eventcallback.OnDieEventCallback;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.AionObject;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.Team;
import com.aionemu.gameserver.model.team2.alliance.PlayerAlliance;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceGroup;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceMember;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.model.team2.group.PlayerGroupMember;
import com.aionemu.gameserver.model.team2.league.League;
import com.aionemu.gameserver.model.team2.league.LeagueMember;
import com.aionemu.gameserver.services.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Source
 */
@SuppressWarnings("rawtypes")
public class BossDeathListener extends OnDieEventCallback {

    private static final Logger log = LoggerFactory.getLogger(BossDeathListener.class);

    private final Base<?> base;

    public BossDeathListener(Base base) {
        this.base = base;
    }

    @SuppressWarnings("unused")
    @Override
    public void onBeforeDie(AbstractAI obj) {
        AionObject killer = base.getBoss().getAggroList().getMostDamage();
        Npc boss = base.getBoss();
        Race race = null;

        if (killer instanceof PlayerGroup) {
            race = ((Team<Player, PlayerGroupMember>) killer).getRace();
        } else if (killer instanceof PlayerAlliance) {
            race = ((Team<Player, PlayerAllianceMember>) killer).getRace();
        } else if (killer instanceof PlayerAllianceGroup) {
            race = ((Team<Player, PlayerAllianceMember>) killer).getRace();
        } else if (killer instanceof League) {
            race = ((Team<PlayerAlliance, LeagueMember>) killer).getRace();
            /* for (PlayerAlliance playerAlliance : ((League) killer).getMembers()) {
             }*/
        } else if (killer instanceof Player) {
            race = ((Creature) killer).getRace();
        } else if (killer instanceof Creature) {
            race = ((Creature) killer).getRace();
        }

        base.setRace(race);
        BaseService.getInstance().capture(base.getId(), base.getRace());
        log.info("Legat kill ! BOSS: " + boss + " in BaseId: " + base.getBaseLocation().getId() + "killed by RACE: " + race);

        //Add activity point to group
    }

    @Override
    public void onAfterDie(AbstractAI obj) {
    }
}
