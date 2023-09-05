/*
 * AionLight project
 */
package com.aionemu.gameserver.utils;

import java.util.BitSet;
import java.util.Random;

/**
 *
 * @author Alex
 */
public class RandomFour {

    private final BitSet input;
    private final Random rnd;
    private final int Count;
    private int genCount = 0;

    public RandomFour(int in) {
        Count = in;
        rnd = new Random(in);
        input = new BitSet(in);
    }

    public int generate() {
        if (genCount >= Count) {
            return -1;
        }

        int next;
        do {
            next = rnd.nextInt(Count);
        } while (input.get(next));
        input.set(next);
        genCount++;
        return next;
    }
}
