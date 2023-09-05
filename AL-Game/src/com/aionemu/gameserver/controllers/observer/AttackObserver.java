package com.aionemu.gameserver.controllers.observer;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;

public abstract class AttackObserver
        extends ActionObserver {

    public AttackObserver() {
        super(ObserverType.ATTACK);
    }

    @Override
    public void attack(Creature creature) {
        if (!(creature instanceof Player)) {
            return;
        }
        attacked();
    }

    public abstract void attacked();
}
