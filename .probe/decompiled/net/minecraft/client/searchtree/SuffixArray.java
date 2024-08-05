package net.minecraft.client.searchtree;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.Arrays;
import it.unimi.dsi.fastutil.Swapper;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;

public class SuffixArray<T> {

    private static final boolean DEBUG_COMPARISONS = Boolean.parseBoolean(System.getProperty("SuffixArray.printComparisons", "false"));

    private static final boolean DEBUG_ARRAY = Boolean.parseBoolean(System.getProperty("SuffixArray.printArray", "false"));

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int END_OF_TEXT_MARKER = -1;

    private static final int END_OF_DATA = -2;

    protected final List<T> list = Lists.newArrayList();

    private final IntList chars = new IntArrayList();

    private final IntList wordStarts = new IntArrayList();

    private IntList suffixToT = new IntArrayList();

    private IntList offsets = new IntArrayList();

    private int maxStringLength;

    public void add(T t0, String string1) {
        this.maxStringLength = Math.max(this.maxStringLength, string1.length());
        int $$2 = this.list.size();
        this.list.add(t0);
        this.wordStarts.add(this.chars.size());
        for (int $$3 = 0; $$3 < string1.length(); $$3++) {
            this.suffixToT.add($$2);
            this.offsets.add($$3);
            this.chars.add(string1.charAt($$3));
        }
        this.suffixToT.add($$2);
        this.offsets.add(string1.length());
        this.chars.add(-1);
    }

    public void generate() {
        int $$0 = this.chars.size();
        int[] $$1 = new int[$$0];
        int[] $$2 = new int[$$0];
        int[] $$3 = new int[$$0];
        int[] $$4 = new int[$$0];
        IntComparator $$5 = (p_194458_, p_194459_) -> $$2[p_194458_] == $$2[p_194459_] ? Integer.compare($$3[p_194458_], $$3[p_194459_]) : Integer.compare($$2[p_194458_], $$2[p_194459_]);
        Swapper $$6 = (p_194464_, p_194465_) -> {
            if (p_194464_ != p_194465_) {
                int $$5x = $$2[p_194464_];
                $$2[p_194464_] = $$2[p_194465_];
                $$2[p_194465_] = $$5x;
                $$5x = $$3[p_194464_];
                $$3[p_194464_] = $$3[p_194465_];
                $$3[p_194465_] = $$5x;
                $$5x = $$4[p_194464_];
                $$4[p_194464_] = $$4[p_194465_];
                $$4[p_194465_] = $$5x;
            }
        };
        for (int $$7 = 0; $$7 < $$0; $$7++) {
            $$1[$$7] = this.chars.getInt($$7);
        }
        int $$8 = 1;
        for (int $$9 = Math.min($$0, this.maxStringLength); $$8 * 2 < $$9; $$8 *= 2) {
            for (int $$10 = 0; $$10 < $$0; $$4[$$10] = $$10++) {
                $$2[$$10] = $$1[$$10];
                $$3[$$10] = $$10 + $$8 < $$0 ? $$1[$$10 + $$8] : -2;
            }
            Arrays.quickSort(0, $$0, $$5, $$6);
            for (int $$11 = 0; $$11 < $$0; $$11++) {
                if ($$11 > 0 && $$2[$$11] == $$2[$$11 - 1] && $$3[$$11] == $$3[$$11 - 1]) {
                    $$1[$$4[$$11]] = $$1[$$4[$$11 - 1]];
                } else {
                    $$1[$$4[$$11]] = $$11;
                }
            }
        }
        IntList $$12 = this.suffixToT;
        IntList $$13 = this.offsets;
        this.suffixToT = new IntArrayList($$12.size());
        this.offsets = new IntArrayList($$13.size());
        for (int $$14 = 0; $$14 < $$0; $$14++) {
            int $$15 = $$4[$$14];
            this.suffixToT.add($$12.getInt($$15));
            this.offsets.add($$13.getInt($$15));
        }
        if (DEBUG_ARRAY) {
            this.print();
        }
    }

    private void print() {
        for (int $$0 = 0; $$0 < this.suffixToT.size(); $$0++) {
            LOGGER.debug("{} {}", $$0, this.getString($$0));
        }
        LOGGER.debug("");
    }

    private String getString(int int0) {
        int $$1 = this.offsets.getInt(int0);
        int $$2 = this.wordStarts.getInt(this.suffixToT.getInt(int0));
        StringBuilder $$3 = new StringBuilder();
        for (int $$4 = 0; $$2 + $$4 < this.chars.size(); $$4++) {
            if ($$4 == $$1) {
                $$3.append('^');
            }
            int $$5 = this.chars.getInt($$2 + $$4);
            if ($$5 == -1) {
                break;
            }
            $$3.append((char) $$5);
        }
        return $$3.toString();
    }

    private int compare(String string0, int int1) {
        int $$2 = this.wordStarts.getInt(this.suffixToT.getInt(int1));
        int $$3 = this.offsets.getInt(int1);
        for (int $$4 = 0; $$4 < string0.length(); $$4++) {
            int $$5 = this.chars.getInt($$2 + $$3 + $$4);
            if ($$5 == -1) {
                return 1;
            }
            char $$6 = string0.charAt($$4);
            char $$7 = (char) $$5;
            if ($$6 < $$7) {
                return -1;
            }
            if ($$6 > $$7) {
                return 1;
            }
        }
        return 0;
    }

    public List<T> search(String string0) {
        int $$1 = this.suffixToT.size();
        int $$2 = 0;
        int $$3 = $$1;
        while ($$2 < $$3) {
            int $$4 = $$2 + ($$3 - $$2) / 2;
            int $$5 = this.compare(string0, $$4);
            if (DEBUG_COMPARISONS) {
                LOGGER.debug("comparing lower \"{}\" with {} \"{}\": {}", new Object[] { string0, $$4, this.getString($$4), $$5 });
            }
            if ($$5 > 0) {
                $$2 = $$4 + 1;
            } else {
                $$3 = $$4;
            }
        }
        if ($$2 >= 0 && $$2 < $$1) {
            int $$6 = $$2;
            $$3 = $$1;
            while ($$2 < $$3) {
                int $$7 = $$2 + ($$3 - $$2) / 2;
                int $$8 = this.compare(string0, $$7);
                if (DEBUG_COMPARISONS) {
                    LOGGER.debug("comparing upper \"{}\" with {} \"{}\": {}", new Object[] { string0, $$7, this.getString($$7), $$8 });
                }
                if ($$8 >= 0) {
                    $$2 = $$7 + 1;
                } else {
                    $$3 = $$7;
                }
            }
            int $$9 = $$2;
            IntSet $$10 = new IntOpenHashSet();
            for (int $$11 = $$6; $$11 < $$9; $$11++) {
                $$10.add(this.suffixToT.getInt($$11));
            }
            int[] $$12 = $$10.toIntArray();
            java.util.Arrays.sort($$12);
            Set<T> $$13 = Sets.newLinkedHashSet();
            for (int $$14 : $$12) {
                $$13.add(this.list.get($$14));
            }
            return Lists.newArrayList($$13);
        } else {
            return Collections.emptyList();
        }
    }
}