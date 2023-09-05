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
package com.aionemu.gameserver.restrictions;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.GroupConfig;
import com.aionemu.gameserver.configs.main.InGameShopConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.actions.PlayerMode;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.ingameshop.IGItem;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.model.templates.item.ArmorType;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.item.ItemUseLimits;
import com.aionemu.gameserver.model.templates.item.WeaponType;
import com.aionemu.gameserver.model.templates.zone.ZoneClassName;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.custom.MixFight;
import com.aionemu.gameserver.services.custom.ffa.FfaGroupService;
import com.aionemu.gameserver.services.custom.ffa.FfaPlayers;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.skillengine.model.Skill;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.skillengine.model.SkillType;
import com.aionemu.gameserver.skillengine.model.TransformType;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.audit.GMService;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author lord_rex modified by Sippolo, Alex
 */
public class PlayerRestrictions extends AbstractRestrictions {

    private boolean checkAvailableEquipSkills(Player owner, int[] requiredSkills) {
        boolean isSkillPresent = false;

        if (requiredSkills == null || requiredSkills.length == 0) {
            return true;
        }

        for (int skill : requiredSkills) {
            if (owner.getSkillList().isSkillPresent(skill)) {
                isSkillPresent = true;
                break;
            }
        }
        return isSkillPresent;
    }

    @Override
    public boolean canBuyIngameshop(Player player, IGItem item) {
        ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(item.getItemId());

        if (player.isBannedGameShop()) {
            PacketSendUtility.sendWhiteMessageOnCenter(player, "Вам запрещено использовать игровой магазин на " + player.getGShopTimer() / 1000 + " минут." + player.getGameShopBanReason());
            return false;
        }
        if (InGameShopConfig.ALLOW_GIFTS) {
            if (InGameShopConfig.SHOP_MEMBERSHIP_TYPE == 1 && !player.havePermission((byte) item.getMembership())) {
                PacketSendUtility.sendYellowMessageOnCenter(player, "Для покупки этого предмета необходим " + (item.getMembership() == 1 ? "премиум аккаунт или выше" : "VIP аккаунт"));
                return false;
            }
            if (InGameShopConfig.DISABLE_BUY_ITEM_OTHER_RACE) {
                Race r = itemTemplate.getRace();
                if (r != null && r != Race.PC_ALL && r != player.getRace()) {
                    //<body>Покупка этого предмета не доступна для вашей расы.</body>
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400109));
                    return false;
                }
            }
            if (InGameShopConfig.DISABLE_BUY_ITEM_OTHER_GENDER) {
                ItemUseLimits limits = itemTemplate.getUseLimits();
                if (limits != null && limits.getGenderPermitted() != null && limits.getGenderPermitted() != player.getGender()) {
                    //<body>Покупка этого предмета недоступна для вашего пола.</body>
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400110));
                    return false;
                }
            }
            if (InGameShopConfig.DISABLE_BUY_ITEM_OTHER_CLASS) {
                if (!itemTemplate.isClassSpecific(player.getPlayerClass())) {
                    //<body>Покупка этого предмета недоступна для вашего класса.</body>
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400111));
                    return false;
                }

                ArmorType at = itemTemplate.getArmorType();
                if (at != null) {
                    int[] requiredSkillsArmor = at.getRequiredSkills();
                    if (requiredSkillsArmor != null && !checkAvailableEquipSkills(player, requiredSkillsArmor)) {
                        //<body>Покупка этого предмета недоступна для вашего класса.</body>
                        PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400111));
                        return false;
                    }
                }
                WeaponType wt = itemTemplate.getWeaponType();
                if (wt != null) {
                    int[] requiredSkillsWeapon = wt.getRequiredSkills();
                    if (requiredSkillsWeapon != null && !checkAvailableEquipSkills(player, requiredSkillsWeapon)) {
                        //<body>Покупка этого предмета недоступна для вашего класса.</body>
                        PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400111));
                        return false;
                    }
                }
            }
        }
        switch (item.getPriceType()) {
            case KINAH:
                if (!player.getInventory().tryDecreaseKinah(item.getItemPriceCount())) {
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_NOT_ENOUGH_KINA(item.getItemPriceCount()));
                    return false;
                }
                break;
            case ITEM_ID:
                if (player.getInventory().getItemCountByItemId(item.getPriceItemId()) < item.getItemPriceCount()) {
                    PacketSendUtility.sendMessage(player, "Для покупки [item:" + item.getItemId() + "] необходимо [item:" + item.getPriceItemId() + "] " + item.getItemPriceCount() + " шт.");
                    return false;
                }
                player.getInventory().decreaseByItemId(item.getPriceItemId(), item.getItemPriceCount());
                break;
            case AP:
                if (player.getAbyssRank().getAp() < item.getItemPriceCount()) {
                    PacketSendUtility.sendMessage(player, "Для покупки [item:" + item.getItemId() + "] необходимо " + item.getItemPriceCount() + " очков бездны.");
                    return false;
                }
                long ap = -item.getItemPriceCount();
                AbyssPointsService.setAp(player, (int) ap);
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300965, ap * -1));
                break;
            case TOLL:
            case FREE:
                return true;
        }

        return true;
    }

    @Override
    public boolean canShowIngameshop(Player player) {
        if (!InGameShopConfig.ENABLE_IN_GAME_SHOP) {
            PacketSendUtility.sendWhiteMessageOnCenter(player, "\u0418\u0433\u0440\u043e\u0432\u043e\u0439 \u043c\u0430\u0433\u0430\u0437\u0438\u043d \u043e\u0442\u043a\u043b\u044e\u0447\u0435\u043d \u0430\u0434\u043c\u0438\u043d\u0438\u0441\u0442\u0440\u0430\u0446\u0438\u0435\u0439 \u0441\u0435\u0440\u0432\u0435\u0440\u0430");
            return false;
        }
        if (player.isBannedGameShop()) {
            PacketSendUtility.sendWhiteMessageOnCenter(player, "Вам запрещено использовать игровой магазин на " + player.getGShopTimer() / 1000 + " минут." + player.getGameShopBanReason());
            return false;
        }
        return true;
    }

    @Override
    public boolean canChangeEquip(Player player) {
        if (player.isBanItems()) {
            PacketSendUtility.sendMessage(player, "Вам запрещено снимать и надевать предметы.");
            return false;
        }
        return true;
    }

    @Override
    public boolean canAffectBySkill(Player player, VisibleObject target, Skill skill) {
        if (skill == null) {
            return false;
        }

        // dont allow to use skills in Fly Teleport state
        if (target instanceof Player && ((Player) target).isProtectionActive()) {
            return false;
        }

        if (player.isUsingFlyTeleport() || (target instanceof Player && ((Player) target).isUsingFlyTeleport())) {
            return false;
        }

        if (((Creature) target).getLifeStats().isAlreadyDead() && !skill.getSkillTemplate().hasResurrectEffect()
                && !skill.checkNonTargetAOE()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_TARGET_IS_NOT_VALID);
            return false;
        }

        //cant ressurect non players and non dead
        if (skill.getSkillTemplate().hasResurrectEffect() && (!(target instanceof Player)
                || !((Creature) target).getLifeStats().isAlreadyDead() || (!((Creature) target).isInState(CreatureState.DEAD)
                && !((Creature) target).isInState(CreatureState.FLOATING_CORPSE)))) {
            return false;
        }

        if (skill.getSkillTemplate().hasItemHealFpEffect() && !player.isInFlyingState()) { // player must be
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_RESTRICTION_FLY_ONLY);
            return false;
        }

        if (!skill.getSkillTemplate().hasEvadeEffect()) {
            if (player.getEffectController().isAbnormalState(AbnormalState.CANT_ATTACK_STATE)) {
                return false;
            }
        }

        // Fix for Summon Group Member, cannot be used while either caster or summoned is actively in combat
        //example skillId: 1606
        if (skill.getSkillTemplate().hasRecallInstant()) {
            // skill properties should already filter only players
            if (player.getController().isInCombat() || ((Player) target).getController().isInCombat()) {
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_Recall_CANNOT_ACCEPT_EFFECT(target.getName()));
                return false;
            }
        }

        if (player.isInState(CreatureState.PRIVATE_SHOP)) { // You cannot use an item while running a Private Store.
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANNOT_USE_ITEM_DURING_PATH_FLYING(new DescriptionId(2800123)));
            return false;
        }

        return true;
    }

    private boolean checkFly(Player player, VisibleObject target) {
        if (player.isUsingFlyTeleport() || player.isInPlayerMode(PlayerMode.WINDSTREAM)) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_RESTRICTION_NO_FLY);
            return false;
        }

        if (target != null && target instanceof Player) {
            Player playerTarget = (Player) target;
            if (playerTarget.isUsingFlyTeleport() || playerTarget.isInPlayerMode(PlayerMode.WINDSTREAM)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canUseSkill(Player player, Skill skill) {
        VisibleObject target = player.getTarget();
        SkillTemplate template = skill.getSkillTemplate();

        if (!checkFly(player, target)) {
            return false;
        }

        // check if is casting to avoid multicast exploit
        // TODO cancel skill if other is used
        if (player.isCasting()) {
            return false;
        }

        if ((!player.canAttack()) && !template.hasEvadeEffect()) {
            return false;
        }

        if (target instanceof Player && target != player && ((Player) target).getAccessLevel() > 0) {
            PacketSendUtility.sendMessage(player, "\u041d\u0435\u043b\u044c\u0437\u044f \u0430\u0442\u0430\u043a\u043e\u0432\u0430\u0442\u044c \u0413\u0435\u0439\u043c \u041c\u0430\u0441\u0442\u0435\u0440\u0430");
            return false;
        }

        if (AdminConfig.ENABLE_GM_NO_ATTACK && player.getAccessLevel() != 0 && player.getAccessLevel() <= AdminConfig.GM_NO_ATTACK) {
            PacketSendUtility.sendMessage(player, "DEVELOPER: GM(" + AdminConfig.GM_NO_ATTACK + ") can not use skill!!");
            return false;
        }

        if (template.isDeityAvatar() && FfaPlayers.isInFFA(player)) {
            PacketSendUtility.sendMessage(player, "\u041d\u0435\u043b\u044c\u0437\u044f \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u044c \u0432 \u0434\u0430\u043d\u043d\u043e\u0439 \u043b\u043e\u043a\u0430\u0446\u0438\u0438");
            return false;
        }

        //in 3.0 players can use remove shock even when silenced
        if (template.getType() == SkillType.MAGICAL && player.getEffectController().isAbnormalSet(AbnormalState.SILENCE) && !template.hasEvadeEffect()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_CANT_CAST_MAGIC_SKILL_WHILE_SILENCED);
            return false;
        }

        if (template.getType() == SkillType.PHYSICAL && player.getEffectController().isAbnormalSet(AbnormalState.BIND)) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_CANT_CAST_PHYSICAL_SKILL_IN_FEAR);
            return false;
        }

        if (player.isSkillDisabled(template)) {
            return false;
        }

        //cannot use skills while transformed
        if (player.getTransformModel().isActive()) {
            if (player.getTransformModel().getType() == TransformType.NONE) {
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_CAN_NOT_CAST_IN_SHAPECHANGE);
                return false;
            }
        }

        if (template.hasResurrectEffect()) {
            if (!(target instanceof Player)) {
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_TARGET_IS_NOT_VALID);
                return false;
            }
            Player targetPlayer = (Player) target;
            if (!targetPlayer.isInState(CreatureState.DEAD)) {
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_TARGET_IS_NOT_VALID);
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean canInviteToGroup(Player player, Player target) {
        final com.aionemu.gameserver.model.team2.group.PlayerGroup group = player.getPlayerGroup2();

        if (player.isInFfa() || target.isInFfa() || FfaGroupService.isInFFA(player) || FfaGroupService.isInFFA(target) || MixFight.isInFFA(player) || MixFight.isInFFA(target)) {
            PacketSendUtility.sendMessage(player, "\u041d\u0435\u043b\u044c\u0437\u044f \u043f\u0440\u0438\u0433\u043b\u0430\u0448\u0430\u0442\u044c \u0432 \u0433\u0440\u0443\u043f\u043f\u0443 \u043d\u0430\u0445\u043e\u0434\u044f\u0441\u044c \u0438\u043b\u0438 \u043d\u0430\u0445\u043e\u0434\u044f\u0449\u0435\u0433\u043e\u0441\u044f \u0432 \u0444\u0444\u0430 \u043b\u043e\u043a\u0430\u0446\u0438\u0438");
            return false;
        }

        if (group != null && group.isFull()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_PARTY_CANT_ADD_NEW_MEMBER);
            return false;
        } else if (group != null && !player.getObjectId().equals(group.getLeader().getObjectId())) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_PARTY_ONLY_LEADER_CAN_INVITE);
            return false;
        } else if (target == null) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_PARTY_NO_USER_TO_INVITE);
            return false;
        } else if (target.getRace() != player.getRace() && !GroupConfig.GROUP_INVITEOTHERFACTION && !player.isGM()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_PARTY_CANT_INVITE_OTHER_RACE);
            return false;
        } else if (target.sameObjectId(player.getObjectId())) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_PARTY_CAN_NOT_INVITE_SELF);
            return false;
        } else if (target.getLifeStats().isAlreadyDead()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_UI_PARTY_DEAD);
            return false;
        } else if (player.getLifeStats().isAlreadyDead()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_PARTY_CANT_INVITE_WHEN_DEAD);
            return false;
        } else if (player.isInGroup2() && target.isInGroup2()
                && player.getPlayerGroup2() == target.getPlayerGroup2()) {
            PacketSendUtility.sendPacket(player,
                    SM_SYSTEM_MESSAGE.STR_PARTY_HE_IS_ALREADY_MEMBER_OF_OUR_PARTY(target.getName()));
        } else if (target.isInGroup2()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_PARTY_HE_IS_ALREADY_MEMBER_OF_OTHER_PARTY(target.getName()));
            return false;
        } else if (target.isInAlliance2()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FORCE_ALREADY_OTHER_FORCE(target.getName()));
            return false;
        } else if (target.isInFfa()) {
            PacketSendUtility.sendMessage(player, "\u0414\u0430\u043d\u043d\u044b\u0439 \u0438\u0433\u0440\u043e\u043a \u043d\u0430\u0445\u043e\u0434\u0438\u0442\u0441\u044f \u043d\u0430 FFA. \u041d\u0435\u043b\u044c\u0437\u044f \u0432\u0437\u044f\u0442\u044c \u0432 \u0433\u0440\u0443\u043f\u043f\u0443");
            return false;
        } else if (player.isInFfa()) {
            PacketSendUtility.sendMessage(player, "\u0412\u044b \u043d\u0430\u0445\u043e\u0434\u0438\u0442\u0435\u0441\u044c \u043d\u0430 FFA. \u041d\u0435\u043b\u044c\u0437\u044f \u043f\u0440\u0435\u0434\u043b\u043e\u0436\u0438\u0442\u044c \u0433\u0440\u0443\u043f\u043f\u0443");
            return false;
        } else if (target.isAccueseBan()) {
            //<body>Невозможно пригласить цель в группу и альянс.</body>
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400092));
            //<body>Из-за своего текущего состояния персонаж не может пригласить собеседника.</body>
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400093));
            return false;
        } else if (player.isAccueseBan()) {
            //1400094
            //<body>Вы не можете вступить в группу, так как на вас поступила жалоба о том,
            //что вы используете программы автоматизации игрового процесса.</body>
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400094));
            return false;
        }

        return true;
    }

    @Override
    public boolean canInviteToAlliance(Player player, Player target) {
        if (target == null) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FORCE_NO_USER_TO_INVITE);
            return false;
        }
        if (FfaGroupService.isInFFA(player) || FfaGroupService.isInFFA(target)) {
            PacketSendUtility.sendMessage(player, "\u041d\u0435\u043b\u044c\u0437\u044f \u043f\u0440\u0438\u0433\u043b\u0430\u0448\u0430\u0442\u044c \u0432 \u0430\u043b\u044c\u044f\u043d\u0441 \u043d\u0430\u0445\u043e\u0434\u044f\u0441\u044c \u0438\u043b\u0438 \u043d\u0430\u0445\u043e\u0434\u044f\u0449\u0435\u0433\u043e\u0441\u044f \u0432 \u0444\u0444\u0430 \u043b\u043e\u043a\u0430\u0446\u0438\u0438");
            return false;
        }

        if (target.getRace() != player.getRace() && !GroupConfig.ALLIANCE_INVITEOTHERFACTION && !player.isGM()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_PARTY_CANT_INVITE_OTHER_RACE);
            return false;
        }

        final com.aionemu.gameserver.model.team2.alliance.PlayerAlliance alliance = player.getPlayerAlliance2();

        if (target.isInAlliance2()) {
            if (target.getPlayerAlliance2() == alliance) {
                PacketSendUtility.sendPacket(player,
                        SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_HE_IS_ALREADY_MEMBER_OF_OUR_ALLIANCE(target.getName()));
                return false;
            } else {
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FORCE_ALREADY_OTHER_FORCE(target.getName()));
                return false;
            }
        }

        if (target.isInFfa()) {
            PacketSendUtility.sendMessage(player, "\u0414\u0430\u043d\u043d\u044b\u0439 \u0438\u0433\u0440\u043e\u043a \u043d\u0430\u0445\u043e\u0434\u0438\u0442\u0441\u044f \u0432 FFA. \u041d\u0435\u043b\u044c\u0437\u044f \u043f\u0440\u0438\u0433\u043b\u0430\u0441\u0438\u0442\u044c \u0432 \u0430\u043b\u044c\u044f\u043d\u0441");
            return false;
        }
        if (player.isInFfa()) {
            PacketSendUtility.sendMessage(player, "\u041d\u0435\u043b\u044c\u0437\u044f \u043f\u0440\u0438\u0433\u043b\u0430\u0448\u0430\u0442\u044c \u0432 \u0430\u043b\u044c\u044f\u043d\u0441 \u043d\u0430\u0445\u043e\u0434\u044f\u0441\u044c \u0432 FFA \u043b\u043e\u043a\u0430\u0446\u0438\u0438");
            return false;
        }

        if (alliance != null && alliance.isFull()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_CANT_ADD_NEW_MEMBER);
            return false;
        }

        if (alliance != null && !alliance.isSomeCaptain(player)) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_ONLY_PARTY_LEADER_CAN_LEAVE_ALLIANCE);
            return false;
        }

        if (target.sameObjectId(player.getObjectId())) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FORCE_CAN_NOT_INVITE_SELF);
            return false;
        }

        if (target.getLifeStats().isAlreadyDead()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_UI_PARTY_DEAD);
            return false;
        }

        if (player.getLifeStats().isAlreadyDead()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FORCE_CANT_INVITE_WHEN_DEAD);
            return false;
        }

        if (target.isInGroup2()) {
            PlayerGroup targetGroup = target.getPlayerGroup2();
            if (targetGroup.isLeader(target)) {
                PacketSendUtility.sendPacket(player,
                        SM_SYSTEM_MESSAGE.STR_FORCE_INVITE_PARTY_HIM(target.getName(), targetGroup.getLeader().getName()));
                return false;
            }
            if (alliance != null && (targetGroup.size() + alliance.size() >= 24)) {
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FORCE_INVITE_FAILED_NOT_ENOUGH_SLOT);
                return false;
            }
        }
        if (target.isAccueseBan()) {
            //<body>Невозможно пригласить цель в группу и альянс.</body>
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400092));
            //<body>Из-за своего текущего состояния персонаж не может пригласить собеседника.</body>
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400093));
            return false;
        } else if (player.isAccueseBan()) {
            //1400095
            //<body>Вы не можете вступить в альянс, так как на вас поступила жалоба о том,
            //что вы используете программы автоматизации игрового процесса.</body>
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400095));
            return false;
        }

        return true;
    }

    @Override
    public boolean canAttack(Player player, VisibleObject target) {
        if (target == null) {
            return false;
        }

        if (!checkFly(player, target)) {
            return false;
        }

        if (!(target instanceof Creature)) {
            return false;
        }

        if (target instanceof Player && !player.isDeveloper()) {
            if (target != player && ((Player) target).getAccessLevel() > 0) {
                PacketSendUtility.sendMessage(player, "Нельзя использовать на Гейм Мастера");
                return false;
            }

            if (AdminConfig.ENABLE_GM_NO_ATTACK && target != player && player.getAccessLevel() != 0 && player.getAccessLevel() <= AdminConfig.GM_NO_ATTACK) {
                PacketSendUtility.sendMessage(player, "DEVELOPER: GM(" + AdminConfig.GM_NO_ATTACK + ") can not attack!!");
                return false;
            }
            if (player.isBanAttack()) {
                PacketSendUtility.sendMessage(player, "Вам запрещено атаковать!");
                return false;
            }
        }
        Creature creature = (Creature) target;

        if (creature.getLifeStats().isAlreadyDead()) {
            return false;
        }

        return player.isEnemy(creature);
    }

    @Override
    public boolean canUseWarehouse(Player player) {
        if (player == null || !player.isOnline()) {
            return false;
        }

        return !player.isTrading();
    }

    @Override
    public boolean canTrade(Player player) {
        if (player == null || !player.isOnline()) {
            return false;
        }

        if (player.isTrading()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_EXCHANGE_PARTNER_IS_EXCHANGING_WITH_OTHER);
            return false;
        }

        return true;
    }

    @Override
    public boolean canChat(Player player) {
        if (player == null || !player.isOnline()) {
            return false;
        }

        if (GMService.getInstance().isMessageGM()) {
            //STR_CANT_CHAT_DURING_NOTIFICATION
            //Ожидается важное объявление от гейм-мастера. Пожалуйста, подождите немного.
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300626));
            return false;
        }

        if (player.getLifeStats().isAlreadyDead()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANT_CHAT_IN_DEAD_STATE);
            return false;
        }

        if (player.isInFfa() && !player.isDeveloper()) {
            PacketSendUtility.sendMessage(player, "Невозможно использовать в FFA");
            return false;
        }
        int level = 10;
        if (player.getLevel() < level) {
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1310002, level));
            return false;
        }
        if (player.isGagged()) {
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300811));
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300814, player.getGAGTimer() / 1000 / 60));
            return false;
        }
        return true;
    }

    @Override
    public boolean canUseItem(Player player, Item item) {
        if (player == null || !player.isOnline()) {
            return false;
        }

        if (player.getEffectController().isAbnormalState(AbnormalState.CANT_ATTACK_STATE)) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SKILL_CAN_NOT_USE_ITEM_WHILE_IN_ABNORMAL_STATE);
            return false;
        }

        if (item.getItemTemplate().hasAreaRestriction()) {
            ZoneName restriction = item.getItemTemplate().getUseArea();
            if (restriction == ZoneName.get("_ABYSS_CASTLE_AREA_")) {
                boolean isInFortZone = false;
                for (ZoneInstance zone : player.getPosition().getMapRegion().getZones(player)) {
                    if (zone.getZoneTemplate().getZoneType().equals(ZoneClassName.FORT)) {
                        isInFortZone = true;
                        break;
                    }
                }
                if (!isInFortZone) {
                    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300143));
                    return false;
                }
            } else if (restriction != null && !player.isInsideItemUseZone(restriction)) {
                // You cannot use that item here.
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300143));
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canPrivateStore(Player player) {
        /*if (DeveloperConfig.ENABLE_GM_PVP) {
         PacketSendUtility.sendBrightYellowMessageOnCenter(player, "Отключено администрацией сервера!");
         return false;
         }*/
        if (player.getMoveController().isInMove()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_PERSONAL_SHOP_DISABLED_IN_MOVING_OBJECT);
            return false;
        }
        if (player.getController().isInCombat()) {
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300663));
            return false;
        }
        if (player.getLevel() < CustomConfig.BUY_PRIVATE_SHOP_LEVEL) {
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400399, CustomConfig.BUY_PRIVATE_SHOP_LEVEL));
            return false;
        }
        if (player.isBannedPStore()) {
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300812));
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300815, player.getPShopTimer() / 1000));
            return false;
        }
        if (player.isInFfa()) {
            PacketSendUtility.sendMessage(player, "Невозможно использовать в FFA");
            return false;
        }
        return true;
    }

    @Override
    public boolean canTeleport(Player player) {
        if (player.isTeleportBanned()) {
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300810));
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300813, player.getTeleportTimer() / 1000));
            return false;
        }
        //TODO 1300676
        //Вы не можете перемещаться, поэтому не можете сменить зону PVP.
        return true;
    }

    @Override
    public boolean canMove(Player player) {
        if (player.isMoveBanned()) {
            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300813, player.getMoveTimer() / 1000));
            return false;
        }
        return true;
    }

}
