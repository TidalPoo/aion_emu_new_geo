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
package com.aionemu.gameserver.model.gameobjects.math;

import com.aionemu.gameserver.controllers.MathController;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import com.aionemu.gameserver.world.WorldPosition;
import com.aionemu.gameserver.world.knownlist.CreatureAwareKnownList;
import com.aionemu.gameserver.world.knownlist.NpcKnownList;
import com.aionemu.gameserver.world.knownlist.PlayerAwareKnownList;

/**
 *
 * @author Steve
 */
public class MathObject extends VisibleObject {

    private double minRange;
    private double maxRange;
    private int skillId;
    private int npcId;
    private Npc master;
    private MathObjectType type;
    private MathObjectReaction reaction = MathObjectReaction.PC;
    private int duration;

    public MathObject(SpawnTemplate spawnTemplate, MathObjectType type, MathObjectReaction reaction, double minRange, double maxRange) {
        super(IDFactory.getInstance().nextId(), new MathController(), spawnTemplate, null, new WorldPosition(spawnTemplate.getWorldId()));
        this.type = type;
        this.reaction = reaction;
        this.minRange = minRange;
        this.maxRange = maxRange;
        getController().setOwner(this);
        switch (this.reaction) {
            case PC:
                setKnownlist(new PlayerAwareKnownList(this));
                break;
            case NPC:
                setKnownlist(new NpcKnownList(this));
                break;
            case ALL:
                setKnownlist(new CreatureAwareKnownList(this));
                break;
        }
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public void setNpcId(int npcId) {
        this.npcId = npcId;
    }

    @Override
    public MathController getController() {
        return (MathController) super.getController();
    }

    public double getMinRange() {
        return minRange;
    }

    public double getMaxRange() {
        return maxRange;
    }

    public int getSkillId() {
        return skillId;
    }

    public int getNpcId() {
        return npcId;
    }

    public Npc getMaster() {
        return master;
    }

    public void setMaster(Npc master) {
        this.master = master;
    }

    public MathObjectType getType() {
        return type;
    }

    public MathObjectReaction getReaction() {
        return reaction;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public float getVisibilityDistance() {
        return (float) (getMaxRange() + 5D);
    }

    @Override
    public float getMaxZVisibleDistance() {
        return (float) (getMaxRange() + 5D);
    }

    @Override
    public String getName() {
        return "Geometric Object";
    }
}
