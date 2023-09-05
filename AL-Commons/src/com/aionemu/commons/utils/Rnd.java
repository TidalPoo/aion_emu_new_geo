/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 * aion-emu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-emu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.commons.utils;

import java.util.List;

/**
 * @author Balancer
 */
public class Rnd {

    private static final MTRandom rnd = new MTRandom();

    /**
     * @return rnd
     */
    public static float get() // get random number from 0 to 1
    {
        return rnd.nextFloat();
    }

    /**
     * Gets a random number from 0(inclusive) to n(exclusive)
     *
     * @param n The superior limit (exclusive)
     * @return A number from 0 to n-1
     */
    public static int get(int n) {
        return (int) Math.floor(rnd.nextDouble() * n);
    }

    /**
     * @param min
     * @param max
     * @return value
     */
    public static int get(int min, int max) // get random number from
    // min to max (not max-1 !)
    {
        return min + (int) Math.floor(rnd.nextDouble() * (max - min + 1));
    }

    /**
     * Рандомайзер для подсчета шансов.<br>
     * Рекомендуется к использованию вместо Rnd.get()
     *
     * @param chance в процентах от 0 до 100
     * @return true в случае успешного выпадания.
     * <li>Если chance <= 0, вернет false <li>Если chance >= 100, вернет true
     */
    public static boolean chance(int chance) {
        return chance >= 1 && (chance > 99 || nextInt(99) + 1 <= chance);
    }

    /**
     * Рандомайзер для подсчета шансов.<br>
     * Рекомендуется к использованию вместо Rnd.get() если нужны очень маленькие
     * шансы
     *
     * @param chance в процентах от 0 до 100
     * @return true в случае успешного выпадания.
     * <li>Если chance <= 0, вернет false <li>Если chance >= 100, вернет true
     */
    public static boolean chance(double chance) {
        return nextDouble() <= chance / 100.;
    }

    public static <E> E get(E[] list) {
        return list[get(list.length)];
    }

    public static int get(int[] list) {
        return list[get(list.length)];
    }

    public static <E> E get(List<E> list) {
        return list.get(get(list.size()));
    }

    /**
     * @param n
     * @return n
     */
    public static int nextInt(int n) {
        return (int) Math.floor(rnd.nextDouble() * n);
    }

    /**
     * @return int
     */
    public static int nextInt() {
        return rnd.nextInt();
    }

    /**
     * @return double
     */
    public static double nextDouble() {
        return rnd.nextDouble();
    }

    /**
     * @return double
     */
    public static double nextGaussian() {
        return rnd.nextGaussian();
    }

    /**
     * @return double
     */
    public static boolean nextBoolean() {
        return rnd.nextBoolean();
    }
}
