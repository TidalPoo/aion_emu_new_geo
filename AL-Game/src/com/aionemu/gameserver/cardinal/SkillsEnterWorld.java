/*
 * SAO Project
 */
package com.aionemu.gameserver.cardinal;

/**
 *
 * @author Alex
 */
public class SkillsEnterWorld {

    //speed + 30%   phy attack + 9%  mag attack 9% crit mag +40 crit phy +125
    public static final int[] skillPlayer = new int[]{9960, 9965, 9959, 9958, 10766};//regular
    public static final int[] skillPlayerLevel = new int[]{3, 3, 3, 4, 1};//regular
    //speed + 30%   phy attack + 9%  mag attack 9% crit phy +150 crit mag +50 
    public static final int[] skillVip = new int[]{10460, 10461, 10462, 10463, 10464};//vip
    public static final int[] skillVipLevel = new int[]{3, 3, 3, 5, 5};//vip
}
