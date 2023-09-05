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
package com.aionemu.gameserver.services;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.TribeClass;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.math.MathObject;
import com.aionemu.gameserver.model.gameobjects.math.MathObjectReaction;
import com.aionemu.gameserver.model.gameobjects.math.MathObjectType;
import com.aionemu.gameserver.model.templates.math.MathObjectGroup;
import com.aionemu.gameserver.model.templates.math.MathObjectMaps;
import com.aionemu.gameserver.model.templates.math.MathObjectPosition;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.skillengine.properties.TargetRelationAttribute;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import javolution.util.FastList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Steve
 */
public class MathObjectService {

    private static final Logger log = LoggerFactory.getLogger(MathObjectService.class);

    public MathObject spawn(VisibleObject obj, int npcId, int skillId, double minRadius, double maxRadius, int delay, int randomWalk, MathObjectReaction reaction) {
        return spawn(obj.getWorldId(), obj.getX(), obj.getY(), obj.getZ(), obj.getHeading(), obj.getInstanceId(), 0, npcId, randomWalk, skillId, minRadius, maxRadius, delay, MathObjectType.SKILL_USE, reaction);
    }

    public MathObject spawn(VisibleObject obj, int npcId, int skillId, double minRadius, double maxRadius, int delay, MathObjectReaction reaction, int duration) {
        return spawn(obj.getWorldId(), obj.getX(), obj.getY(), obj.getZ(), obj.getHeading(), obj.getInstanceId(), 0, npcId, 0, skillId, minRadius, maxRadius, delay, MathObjectType.SKILL_USE, reaction, duration, null);
    }

    public MathObject spawn(int worldId, float x, float y, float z, int instanceId, int npcId, int skillId, double minRadius, double maxRadius, int delay, MathObjectReaction reaction) {
        return spawn(worldId, x, y, z, (byte) 0, instanceId, 0, npcId, 0, skillId, minRadius, maxRadius, delay, MathObjectType.SKILL_USE, reaction);
    }

    public MathObject spawn(int worldId, float x, float y, float z, int instanceId, int npcId, int skillId, double minRadius, double maxRadius, int delay, MathObjectType type, MathObjectReaction reaction) {
        return spawn(worldId, x, y, z, (byte) 0, instanceId, 0, npcId, 0, skillId, minRadius, maxRadius, delay, type, reaction);
    }

    public MathObject spawn(int worldId, float x, float y, float z, int instanceId, int npcId, int skillId, double minRadius, double maxRadius, int delay, MathObjectType type, MathObjectReaction reaction, String ai) {
        return spawn(worldId, x, y, z, (byte) 0, instanceId, 0, npcId, 0, skillId, minRadius, maxRadius, delay, type, reaction, ai);
    }

    public MathObject spawn(int worldId, float x, float y, float z, int instanceId, int npcId, int skillId, double minRadius, double maxRadius, int delay, int randomWalk, MathObjectReaction reaction) {
        return spawn(worldId, x, y, z, (byte) 0, instanceId, 0, npcId, randomWalk, skillId, minRadius, maxRadius, delay, MathObjectType.SKILL_USE, reaction);
    }

    public MathObject spawn(int worldId, float x, float y, float z, int instanceId, int staticId, int npcId, int skillId, double minRadius, double maxRadius, int delay, MathObjectReaction reaction) {
        return spawn(worldId, x, y, z, (byte) 0, instanceId, staticId, npcId, 0, skillId, minRadius, maxRadius, delay, MathObjectType.SKILL_USE, reaction);
    }

    public MathObject spawn(int worldId, float x, float y, float z, byte heading, int instanceId, int staticId, int npcId, int randomWalk, int skillId, double minRadius, double maxRadius, int delay, MathObjectType type, MathObjectReaction reaction) {
        return spawn(worldId, x, y, z, (byte) 0, instanceId, staticId, npcId, 0, skillId, minRadius, maxRadius, delay, type, reaction, 0, null);
    }

    public MathObject spawn(int worldId, float x, float y, float z, byte heading, int instanceId, int staticId, int npcId, int randomWalk, int skillId, double minRadius, double maxRadius, int delay, MathObjectType type, MathObjectReaction reaction, String ai) {
        return spawn(worldId, x, y, z, (byte) 0, instanceId, staticId, npcId, 0, skillId, minRadius, maxRadius, delay, type, reaction, 0, ai);
    }

    public MathObject spawn(int worldId, float x, float y, float z, byte heading, int instanceId, int staticId, int npcId, int randomWalk, int skillId, double minRadius, double maxRadius, int delay, MathObjectType type, MathObjectReaction reaction, int duration, String ai) {
        SpawnTemplate spawn = SpawnEngine.addNewSingleTimeSpawn(worldId, npcId, x, y, z, heading);
        MathObject object = new MathObject(spawn, type, reaction, minRadius, maxRadius);
        spawn.setVisibleObject(object);
        object.setSkillId(skillId);
        object.setNpcId(npcId > 0 ? npcId : 207034);
        object.setDuration(duration);
        if (object.getNpcId() > 0) {
            TribeClass tc = TribeClass.USEALL;
            if (skillId > 0) {
                SkillTemplate tmp = DataManager.SKILL_DATA.getSkillTemplate(skillId);
                if (tmp.getProperties().getTargetRelation() == TargetRelationAttribute.ENEMY) {
                    tc = TribeClass.AGGRESSIVE_ALL;
                }
            }
            object.getController().spawn(worldId, object.getNpcId(), x, y, z, heading, staticId, instanceId, randomWalk, ai).getObjectTemplate().setTribe(tc);
        }

        if (delay > 0) {
            object.getController().onDelete(delay);
        }

        SpawnEngine.bringIntoWorld(object, spawn, instanceId);

        return object;
    }

    public void spawn() {
        log.info("Math object spawn service loading...");
        FastList<MathObjectMaps> maps = DataManager.MATH_OBJECT.getMaps();
        FastList<MathObject> object = FastList.newInstance();
        for (FastList.Node<MathObjectMaps> n = maps.head(), end = maps.tail(); (n = n.getNext()) != end;) {
            MathObjectMaps map = n.getValue();
            for (FastList.Node<MathObjectGroup> g = map.getMathObject().head(), endg = map.getMathObject().tail(); (g = g.getNext()) != endg;) {
                MathObjectGroup group = g.getValue();
                for (FastList.Node<MathObjectPosition> p = group.getPosition().head(), endp = group.getPosition().tail(); (p = p.getNext()) != endp;) {
                    MathObjectPosition pos = p.getValue();
                    object.add(spawn(map.getMapId(), pos.getX(), pos.getY(), pos.getZ(), pos.getH(), 1, pos.getStaticId(), group.getNpcId(), pos.getRandomWalk(), group.getSkillId(), group.getMixRadius(), group.getMaxRadius(), 0, group.getType(), group.getReaction(), group.getDuration(), null));
                }
            }
        }

        log.info("Spawned " + object.size() + " math objects");
        FastList.recycle(object);
        FastList.recycle(maps);
    }

    public static final MathObjectService getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {

        protected static final MathObjectService instance = new MathObjectService();
    }
}
