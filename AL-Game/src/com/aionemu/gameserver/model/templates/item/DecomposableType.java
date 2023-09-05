/*
 * This file is part of [JS]Emulator Copyright (C) 2014 <http://www.js-emu.ru>
 *
 * [JS]Emulator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * [JS]Emulator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with [JS]Emulator.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.templates.item;

import javax.xml.bind.annotation.XmlEnum;

/**
 *
 * @author Steve
 */
@XmlEnum
public enum DecomposableType {

    NONE,
    JOBDROP;

    private DecomposableType() {

    }
}
