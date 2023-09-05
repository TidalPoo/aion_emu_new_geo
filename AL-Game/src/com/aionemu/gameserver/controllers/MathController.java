/*
 * Copyright (C) 2013 Steve
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.controllers;

import com.aionemu.gameserver.ai2.AI2Engine;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.controllers.observer.MathObjectObserver;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.math.MathObject;
import com.aionemu.gameserver.model.gameobjects.math.MathObjectReaction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.knownlist.Visitor;
import javolution.util.FastMap;

/**
 *
 * @author Steve
 */
public class MathController extends VisibleObjectController<MathObject> {

    FastMap<Creature, MathObjectObserver> observers = new FastMap<Creature, MathObjectObserver>().shared();

    @Override
    public void see(VisibleObject object) {
        super.see(object);

        if (getOwner().getReaction() == MathObjectReaction.PC) {
            if (!(object instanceof Player)) {
                return;
            }
        } else if (getOwner().getReaction() == MathObjectReaction.NPC) {
            if (!(object instanceof Npc)) {
                return;
            }
        } else if (getOwner().getReaction() == MathObjectReaction.ALL) {
            if (!(object instanceof Creature)) {
                return;
            }
        }
        Creature creature = (Creature) object;
        final MathObjectObserver observer = new MathObjectObserver(getOwner(), creature, getOwner().getType());
        creature.getObserveController().addObserver(observer);

        observers.put(creature, observer);
        observer.moved();
    }

    @Override
    public void notSee(VisibleObject object, boolean isOutOfRange) {
        super.notSee(object, isOutOfRange);
        if (getOwner().getReaction() == MathObjectReaction.PC) {
            if (!(object instanceof Player)) {
                return;
            }
        } else if (getOwner().getReaction() == MathObjectReaction.NPC) {
            if (!(object instanceof Npc)) {
                return;
            }
        } else if (getOwner().getReaction() == MathObjectReaction.ALL) {
            if (!(object instanceof Creature)) {
                return;
            }
        }
        if ((isOutOfRange) && (object instanceof Creature)) {
            Creature creature = (Creature) object;
            MathObjectObserver observer = observers.remove(creature);
            observer.clearShedules();
            creature.getObserveController().removeObserver(observer);
        }
    }

    public Npc spawn(int worldId, int npcId, float x, float y, float z, byte heading, int staticId, int instanceId, int randomWalk, String ai) {
        SpawnTemplate template = SpawnEngine.addNewSingleTimeSpawn(worldId, npcId, x, y, z, heading, randomWalk);
        template.setStaticId(staticId);
        Npc master = (Npc) SpawnEngine.spawnObject(template, instanceId);
        if (randomWalk > 0) {
            if (master.getObjectTemplate().getStatsTemplate().getWalkSpeed() == 0) {
                master.getObjectTemplate().getStatsTemplate().setRunSpeed(1);
                master.getObjectTemplate().getStatsTemplate().setWalkSpeed(1);
            }
            WalkManager.startWalking(((NpcAI2) master.getAi2()));
        }
        if (ai != null) {
            AI2Engine.getInstance().setupAI(ai, master);
            ((NpcAI2) master.getAi2()).setStateIfNot(AIState.IDLE);
        }
        getOwner().setMaster(master);
        return getOwner().getMaster();
    }

    public void onDelete(int delay) {
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                delete();
            }
        }, delay);
    }

    @Override
    public void delete() {
        if (getOwner().getMaster() != null) {
            getOwner().getMaster().getController().delete();
        }
        getOwner().getKnownList().doOnAllObjects(new Visitor<VisibleObject>() {
            @Override
            public void visit(VisibleObject object) {
                if (!(object instanceof Creature)) {
                    return;
                }
                Creature creature = (Creature) object;
                MathObjectObserver observer = observers.remove(creature);
                if (observer == null) {
                    return;
                }
                observer.clearShedules();
                creature.getObserveController().removeObserver(observer);
            }
        });
        super.delete();
    }
}
