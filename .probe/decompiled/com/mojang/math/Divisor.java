package com.mojang.math;

import com.google.common.annotations.VisibleForTesting;
import it.unimi.dsi.fastutil.ints.IntIterator;
import java.util.NoSuchElementException;

public class Divisor implements IntIterator {

    private final int denominator;

    private final int quotient;

    private final int mod;

    private int returnedParts;

    private int remainder;

    public Divisor(int int0, int int1) {
        this.denominator = int1;
        if (int1 > 0) {
            this.quotient = int0 / int1;
            this.mod = int0 % int1;
        } else {
            this.quotient = 0;
            this.mod = 0;
        }
    }

    public boolean hasNext() {
        return this.returnedParts < this.denominator;
    }

    public int nextInt() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        } else {
            int $$0 = this.quotient;
            this.remainder = this.remainder + this.mod;
            if (this.remainder >= this.denominator) {
                this.remainder = this.remainder - this.denominator;
                $$0++;
            }
            this.returnedParts++;
            return $$0;
        }
    }

    @VisibleForTesting
    public static Iterable<Integer> asIterable(int int0, int int1) {
        return () -> new Divisor(int0, int1);
    }
}