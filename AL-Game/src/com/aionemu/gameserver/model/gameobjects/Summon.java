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
package com.aionemu.gameserver.model.gameobjects;

import com.aionemu.gameserver.ai2.AI2Engine;
import com.aionemu.gameserver.controllers.CreatureController;
import com.aionemu.gameserver.controllers.SummonController;
import com.aionemu.gameserver.controllers.attack.AggroList;
import com.aionemu.gameserver.controllers.attack.PlayerAggroList;
import com.aionemu.gameserver.controllers.movement.SiegeWeaponMoveController;
import com.aionemu.gameserver.controllers.movement.SummonMoveController;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TribeClass;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.container.SummonGameStats;
import com.aionemu.gameserver.model.stats.container.SummonLifeStats;
import com.aionemu.gameserver.model.summons.SkillOrder;
import com.aionemu.gameserver.model.summons.SummonMode;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.stats.SummonStatsTemplate;
import com.aionemu.gameserver.world.WorldPosition;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Future;

/**
 * @author ATracer
 */
public class Summon extends Creature {

    private Player master;
    private SummonMode mode = SummonMode.GUARD;
    private byte level;
    private int liveTime;
    private Queue<SkillOrder> skillOrders = new LinkedList<>();
    private Future<?> releaseTask;

    /**
     * @param objId
     * @param controller
     * @param spawnTemplate
     * @param objectTemplate
     * @param time
     * @param level
     */
    public Summon(int objId, CreatureController<? extends Creature> controller, SpawnTemplate spawnTemplate,
            NpcTemplate objectTemplate, byte level, int time) {
        super(objId, controller, spawnTemplate, objectTemplate, new WorldPosition(spawnTemplate.getWorldId()));
        controller.setOwner(this);
        String ai = objectTemplate.getAi();
        AI2Engine.getInstance().setupAI(ai, this);
        moveController = ai.equals("siege_weapon") ? new SiegeWeaponMoveController(this) : new SummonMoveController(this);
        this.level = level;
        this.liveTime = time;
        SummonStatsTemplate statsTemplate = DataManager.SUMMON_STATS_DATA.getSummonTemplate(objectTemplate.getTemplateId(),
                level);
        setGameStats(new SummonGameStats(this, statsTemplate));
        setLifeStats(new SummonLifeStats(this));
    }

    @Override
    protected AggroList createAggroList() {
        return new PlayerAggroList(this);
    }

    @Override
    public SummonGameStats getGameStats() {
        return (SummonGameStats) super.getGameStats();
    }

    @Override
    public Player getMaster() {
        return master;
    }

    /**
     * @param master the master to set
     */
    public void setMaster(Player master) {
        this.master = master;
    }

    @Override
    public String getName() {
        return objectTemplate.getName();
    }

    /**
     * @return the level
     */
    @Override
    public byte getLevel() {
        return level;
    }

    @Override
    public NpcTemplate getObjectTemplate() {
        return (NpcTemplate) super.getObjectTemplate();
    }

    public int getNpcId() {
        return getObjectTemplate().getTemplateId();
    }

    public int getNameId() {
        return getObjectTemplate().getNameId();
    }

    /**
     * @return NpcObjectType.SUMMON
     */
    @Override
    public NpcObjectType getNpcObjectType() {
        return NpcObjectType.SUMMON;
    }

    @Override
    public SummonController getController() {
        return (SummonController) super.getController();
    }

    /**
     * @return the mode
     */
    public SummonMode getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(SummonMode mode) {
        if (mode != SummonMode.ATTACK) {
            skillOrders.clear();
        }
        this.mode = mode;
    }

    @Override
    public boolean isEnemy(Creature creature) {
        return master != null ? master.isEnemy(creature) : false;
    }

    @Override
    public boolean isEnemyFrom(Npc npc) {
        return master != null ? master.isEnemyFrom(npc) : false;
    }

    @Override
    public boolean isEnemyFrom(Player player) {
        return master != null ? master.isEnemyFrom(player) : false;
    }

    @Override
    public TribeClass getTribe() {
        if (master == null) {
            return ((NpcTemplate) objectTemplate).getTribe();
        }
        return master.getTribe();
    }

    @Override
    public SummonMoveController getMoveController() {
        return (SummonMoveController) super.getMoveController();
    }

    @Override
    public Creature getActingCreature() {
        return getMaster() == null ? this : getMaster();
    }

    @Override
    public Race getRace() {
        return getMaster() != null ? getMaster().getRace() : Race.NONE;
    }

    /**
     * @return liveTime in sec.
     */
    public int getLiveTime() {
        return liveTime;
    }

    /**
     * @param liveTime in sec.
     */
    public void setLiveTime(int liveTime) {
        this.liveTime = liveTime;
    }

    public Future<?> getReleaseTask() {
        return releaseTask;
    }

    public void setReleaseTask(Future<?> task) {
        releaseTask = task;
    }

    public void cancelReleaseTask() {
        if (releaseTask != null && !releaseTask.isDone()) {
            releaseTask.cancel(true);
        }
    }

    @Override
    public void setTarget(VisibleObject target) {
        if (!(target instanceof Creature)) {
            return;
        }
        SkillOrder order = skillOrders.peek();
        if (order != null && order.getTarget() != target) {
            skillOrders.clear();
        }
        super.setTarget(target);
    }

    public void addSkillOrder(int skillId, int skillLvl, boolean release, Creature target) {
        this.skillOrders.add(new SkillOrder(skillId, skillLvl, release, target));
    }

    public SkillOrder retrieveNextSkillOrder() {
        return skillOrders.poll();
    }

    public SkillOrder getNextSkillOrder() {
        return skillOrders.peek();
    }
}
