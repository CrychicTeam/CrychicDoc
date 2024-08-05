package com.github.alexthe666.citadel.repack.jcodec.common.tools;

import com.github.alexthe666.citadel.repack.jcodec.common.ArrayUtil;
import java.nio.ShortBuffer;

public class Debug {

    public static boolean debug = false;

    public static final void print8x8i(int[] output) {
        int i = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                System.out.printf("%3d, ", output[i]);
                i++;
            }
            System.out.println();
        }
    }

    public static final void print8x8s(short[] output) {
        int i = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                System.out.printf("%3d, ", output[i]);
                i++;
            }
            System.out.println();
        }
    }

    public static final void print8x8sb(ShortBuffer output) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                System.out.printf("%3d, ", output.get());
            }
            System.out.println();
        }
    }

    public static void prints(short[] table) {
        int i = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                System.out.printf("%3d, ", table[i]);
                i++;
            }
            System.out.println();
        }
    }

    public static void trace(Object... arguments) {
        if (debug && arguments.length > 0) {
            String format = (String) arguments[0];
            ArrayUtil.shiftLeft1(arguments);
            System.out.printf(format + ": %d\n", arguments);
        }
    }

    public static void printInt(int i) {
        if (debug) {
            System.out.print(i);
        }
    }

    public static void print(String string) {
        if (debug) {
            System.out.print(string);
        }
    }

    public static void println(String string) {
        if (debug) {
            System.out.println(string);
        }
    }
}