/**
 * This file is part of aion-lightning <aion-lightning.smfnew.com>.
 *
 * aion-lightning is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * aion-lightning is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * aion-lightning. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author alexa026
 * @author ATracer
 * @author kecimis
 */
public class SM_ATTACK_STATUS extends AionServerPacket {

    private Creature creature;
    private TYPE type;
    private int skillId;
    private int value;
    private LOG log;

    public static enum TYPE {

        NATURAL_HP(3),
        USED_HP(4),//when skill uses hp as cost parameter
        REGULAR(5),
        ABSORBED_HP(6),
        DAMAGE(7),
        HP(7),
        PROTECTDMG(8),
        DELAYDAMAGE(10),
        FALL_DAMAGE(17),
        HEAL_MP(19),
        ABSORBED_MP(20),
        MP(21),
        NATURAL_MP(22),
        FP_RINGS(24),
        FP(26),
        NATURAL_FP(27),
        TEST(92),//зеленый
        TEST2(90),//зеленый
        TEST3(171),//зеленый
        TEST4(1),//зеленый
        TEST5(2),//зеленый
        TEST6(170),//зеленый
        TEST7(130),//зеленый
        TEST8(133),//зеленый
        TEST9(190),//зеленый
        TEST10(25);//желтый при отрицательном значении...
        private final int value;

        private TYPE(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

    }

    public static enum LOG {

        SPELLATK(1),
        HEAL(3),
        MPHEAL(4),
        SKILLLATKDRAININSTANT(23),
        SPELLATKDRAININSTANT(24),
        POISON(25),
        BLEED(26),
        PROCATKINSTANT(92),
        DELAYEDSPELLATKINSTANT(95),
        SPELLATKDRAIN(130),
        FPHEAL(133),
        REGULARHEAL(170),
        REGULAR(184),
        ATTACK(190);
        private final int value;

        private LOG(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

    }

    public SM_ATTACK_STATUS(Creature creature, TYPE type, int skillId, int value, LOG log) {
        this.creature = creature;
        this.type = type;
        this.skillId = skillId;
        this.value = value;
        this.log = log;
    }

    public SM_ATTACK_STATUS(Creature creature, TYPE type, int skillId, int value) {
        this(creature, type, skillId, value, LOG.REGULAR);
    }

    public SM_ATTACK_STATUS(Creature creature, int value) {
        this(creature, TYPE.REGULAR, 0, value, LOG.REGULAR);
    }

    /**
     * {@inheritDoc} ddchcc
     */
    @Override
    protected void writeImpl(AionConnection con) {
        writeD(creature.getObjectId());
        switch (type) {
            case DAMAGE:
            case DELAYDAMAGE:
                writeD(-value);
                break;
            default:
                writeD(value);
        }
        writeC(type.getValue());
        writeC(creature.getLifeStats().getHpPercentage());
        writeH(skillId);
        writeH(log.getValue());
        //todo config if development ? show

        boolean test = false;
        if (test) {
            if (con.getActivePlayer() != null) {
                PacketSendUtility.sendMessage(con.getActivePlayer(), "type: " + type.name() + "(" + type.getValue() + ") log: " + log.name() + "(" + log.getValue() + ")");
            }
        }
    }

	// logId
    // depends on effecttemplate
    // effecttemplate (TYPE) LOG.getValue()
    // spellattack(hp) 1
    // poison(hp) 25
    // delaydamage(hp) 95
    // bleed(hp) 26
    // mp regen(natural_mp) 171
    // hp regen(natural_hp) 171
    // fp regen(natural_fp) 171
    // fp pot(fp) 171
    // prochp(hp) 171
    // procmp(mp) 171
    // heal_instant (regular) 171
    // SpellAtkDrainInstantEffect(absorbed_mp) 24(refactoring shard)
    // mpheal(mp) 4
    // heal(hp) 3
    // fpheal(fp) 133
    // spellatkdrain(hp) 130
    // falldmg (17) 170
    // mpheal (19) 171
    // hp as cost parameter(4) logId 170
    // procatkinstant - (7) 92
    // protecteffect on protector - (8) 171
    // TODO find rest of logIds
    /*attackStatus
    <enum display="Dodge"/>
     <enum display="Off. Dodge"/>
     <enum display="Parry"/>
     <enum display="Off. Parry"/>
     <enum display="Block"/>
     <enum display="Off. Block"/>
     <enum display="Resist"/>
     <enum display="Off. Resist"/>
     <enum display="Buf?"/>
     <enum display="Off. Buf"/>
     <enum display="Normal Hit"/>
     <enum display="Off. Normal Hit"/>
     <enum val="-64" display="Critical Dodge"/>
     <enum val="-62" display="Critical Parry"/>
     <enum val="-60" display="Critical Block"/>
     <enum val="-58" display="Critical Resist"/>
     <enum val="-54" display="Critical"/>
     <enum val="-47" display="Off. Critical Dodge"/>
     <enum val="-45" display="Off. Critical Parry"/>
     <enum val="-43" display="Off. Critical Block"/>
     <enum val="-41" display="Off. Critical Resist"/>
     <enum val="-37" display="Off. Critical"/>*/
}
