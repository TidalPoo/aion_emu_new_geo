/*
 * AionLight project
 */
package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

/**
 *
 * @author Alex
 */
public class PetitionConfig {

    /**
     * MAX_COUNT_OF_DAY
     */
    @Property(key = "gameserver.petition.max.countofday", defaultValue = "50")
    public static int MAX_COUNT_OF_DAY;
}
