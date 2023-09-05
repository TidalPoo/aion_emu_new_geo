/*
 * SAO Project
 */
package com.aionemu.gameserver.instance.handlers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Alex
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FfaType {

    /**
     * @return идентификатор ивента
     */
    int typeId();
}
