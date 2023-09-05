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
package com.aionemu.gameserver.model;

/**
 * @author ATracer
 * @modified Alex (GAG, INGAMESHOP, PRIVATE_SHOP)
 */
public enum TaskId {

    DECAY,
    RESPAWN,
    PRISON,
    PROTECTION_ACTIVE,
    DROWN,
    DESPAWN,
    /**
     * Quest task with timer
     */
    QUEST_TIMER,
    /**
     * Follow task checker
     */
    QUEST_FOLLOW,
    PLAYER_UPDATE,
    INVENTORY_UPDATE,
    GAG,
    PRIVATESHOP,
    INGAMESHOP,
    TELEPORT,
    MOVE,
    ITEM_USE,
    ACTION_ITEM_NPC,
    HOUSE_OBJECT_USE,
    EXPRESS_MAIL_USE,
    SKILL_USE,
    GATHERABLE,
    PET_UPDATE,
    SUMMON_FOLLOW,
    MATERIAL_ACTION,
    FISHING,
    SURCHAGE,
    ONLINE_BONUS,
    WEDDINGS,
    GM_TELEPORTER;
}
