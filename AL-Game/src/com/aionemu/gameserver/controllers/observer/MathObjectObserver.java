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
package com.aionemu.gameserver.controllers.observer;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.math.MathObject;
import com.aionemu.gameserver.model.gameobjects.math.MathObjectType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import java.util.concurrent.ScheduledFuture;

/**
 *
 * @author Steve
 */
public class MathObjectObserver extends ActionObserver {

    private final Creature creature;
    private final MathObject mathObject;
    private final MathObjectType type;
    private ScheduledFuture<?> shedules;
    private SkillTemplate template;

    public MathObjectObserver() {
        super(ObserverType.MOVE);
        this.creature = null;
        this.mathObject = null;
        this.type = MathObjectType.SKILL_USE;
    }

    public MathObjectObserver(MathObject mathObject, Creature creature, MathObjectType type) {
        super(ObserverType.MOVE);
        this.creature = creature;
        this.mathObject = mathObject;
        this.type = type;
        this.template = DataManager.SKILL_DATA.getSkillTemplate(mathObject.getSkillId());
    }

    @Override
    public void moved() {
        if (creature == null || mathObject == null) {
            return;
        }
        if (creature instanceof Player) {
            if (!((Player) creature).isOnline()) {
                clearShedules();
                return;
            }
        }
        if (creature.getLifeStats().isAlreadyDead()) {
            clearShedules();
            return;
        }

        double distance = MathUtil.getDistance(mathObject.getMaster(), creature);
        switch (type) {
            case SKILL_USE:
                if (creature.getVisualState() == 20) {
                    return;
                }
                if (distance >= mathObject.getMinRange() && distance <= mathObject.getMaxRange()) {
                    if (shedules != null) {
                        return;
                    }
                    onActionEvent();
                    shedulesEvent();
                } else {
                    clearShedules();
                }
                break;
            /*            case MOVED:
             if (distance <= mathObject.getMinRange() && distance != 0.0d) {
             EventEngineService.getInstance().getEventHandler().onEnterCircle(mathObject, creature);
             } else if (distance >= mathObject.getMaxRange() && distance != 0.0d) {
             EventEngineService.getInstance().getEventHandler().onLeaveCircle(mathObject, creature);
             }
             break;*/
        }
    }

    private void shedulesEvent() {
        int delay = template != null && template.getDuration() >= 1000 ? template.getDuration() : mathObject.getDuration() >= 1000 ? mathObject.getDuration() : 1000;
        shedules = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (!mathObject.isSpawned()) {
                    creature.getObserveController().removeObserver(MathObjectObserver.this);
                    clearShedules();
                    return;
                }
                onActionEvent();

            }
        }, delay, delay);
    }

    private void onActionEvent() {
        switch (type) {
            case SKILL_USE:
                if (!creature.getEffectController().hasAbnormalEffect(mathObject.getSkillId())) {
                    if (template == null) {
                        return;
                    }
                    SkillEngine.getInstance().applyEffectDirectly(mathObject.getSkillId(), mathObject.getMaster(), creature, template.getDuration());
                }
                break;
            case MOVED:
                break;
        }
    }

    public void clearShedules() {
        if (shedules != null) {
            shedules.cancel(true);
            shedules = null;
        }
    }
}
