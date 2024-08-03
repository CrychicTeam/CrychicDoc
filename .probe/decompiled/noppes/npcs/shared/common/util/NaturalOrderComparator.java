package noppes.npcs.shared.common.util;

import java.util.Comparator;

public class NaturalOrderComparator implements Comparator<String> {

    int compareRight(String a, String b) {
        int bias = 0;
        int ia = 0;
        int ib = 0;
        while (true) {
            char ca = charAt(a, ia);
            char cb = charAt(b, ib);
            if (!Character.isDigit(ca) && !Character.isDigit(cb)) {
                return bias;
            }
            if (!Character.isDigit(ca)) {
                return -1;
            }
            if (!Character.isDigit(cb)) {
                return 1;
            }
            if (ca < cb) {
                if (bias == 0) {
                    bias = -1;
                }
            } else if (ca > cb) {
                if (bias == 0) {
                    bias = 1;
                }
            } else if (ca == 0 && cb == 0) {
                return bias;
            }
            ia++;
            ib++;
        }
    }

    public int compare(String o1, String o2) {
        String a = o1.toLowerCase();
        String b = o2.toLowerCase();
        int ia = 0;
        int ib = 0;
        int nza = 0;
        int nzb = 0;
        while (true) {
            nzb = 0;
            nza = 0;
            char ca = charAt(a, ia);
            char cb;
            for (cb = charAt(b, ib); Character.isSpaceChar(ca) || ca == '0'; ca = charAt(a, ++ia)) {
                if (ca == '0') {
                    nza++;
                } else {
                    nza = 0;
                }
            }
            for (; Character.isSpaceChar(cb) || cb == '0'; cb = charAt(b, ++ib)) {
                if (cb == '0') {
                    nzb++;
                } else {
                    nzb = 0;
                }
            }
            int result;
            if (Character.isDigit(ca) && Character.isDigit(cb) && (result = this.compareRight(a.substring(ia), b.substring(ib))) != 0) {
                return result;
            }
            if (ca == 0 && cb == 0) {
                return nza - nzb;
            }
            if (ca < cb) {
                return -1;
            }
            if (ca > cb) {
                return 1;
            }
            ia++;
            ib++;
        }
    }

    static char charAt(String s, int i) {
        return i >= s.length() ? '\u0000' : s.charAt(i);
    }
}