package com.github.alexthe666.citadel.repack.jcodec.common;

import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import java.nio.ByteBuffer;

public abstract class RunLength {

    protected IntArrayList counts = IntArrayList.createIntArrayList();

    public int estimateSize() {
        int[] counts = this.getCounts();
        int recCount = 0;
        for (int i = 0; i < counts.length; recCount++) {
            for (int count = counts[i]; count >= 256; count -= 256) {
                recCount++;
            }
            i++;
        }
        return recCount * this.recSize() + 4;
    }

    protected abstract int recSize();

    protected abstract void finish();

    public int[] getCounts() {
        this.finish();
        return this.counts.toArray();
    }

    public static class Integer extends RunLength {

        private static final int MIN_VALUE = java.lang.Integer.MIN_VALUE;

        private int lastValue;

        private int count = 0;

        private IntArrayList values;

        public Integer() {
            this.lastValue = java.lang.Integer.MIN_VALUE;
            this.values = IntArrayList.createIntArrayList();
        }

        public void add(int value) {
            if (this.lastValue == java.lang.Integer.MIN_VALUE || this.lastValue != value) {
                if (this.lastValue != java.lang.Integer.MIN_VALUE) {
                    this.values.add(this.lastValue);
                    this.counts.add(this.count);
                    this.count = 0;
                }
                this.lastValue = value;
            }
            this.count++;
        }

        public int[] getValues() {
            this.finish();
            return this.values.toArray();
        }

        @Override
        protected void finish() {
            if (this.lastValue != java.lang.Integer.MIN_VALUE) {
                this.values.add(this.lastValue);
                this.counts.add(this.count);
                this.lastValue = java.lang.Integer.MIN_VALUE;
                this.count = 0;
            }
        }

        public void serialize(ByteBuffer bb) {
            ByteBuffer dup = bb.duplicate();
            int[] counts = this.getCounts();
            int[] values = this.getValues();
            NIOUtils.skip(bb, 4);
            int recCount = 0;
            for (int i = 0; i < counts.length; recCount++) {
                int count;
                for (count = counts[i]; count >= 256; count -= 256) {
                    bb.put((byte) -1);
                    bb.putInt(values[i]);
                    recCount++;
                }
                bb.put((byte) (count - 1));
                bb.putInt(values[i]);
                i++;
            }
            dup.putInt(recCount);
        }

        public static RunLength.Integer parse(ByteBuffer bb) {
            RunLength.Integer rl = new RunLength.Integer();
            int recCount = bb.getInt();
            for (int i = 0; i < recCount; i++) {
                int count = (bb.get() & 255) + 1;
                int value = bb.getInt();
                rl.counts.add(count);
                rl.values.add(value);
            }
            return rl;
        }

        @Override
        protected int recSize() {
            return 5;
        }

        public int[] flattern() {
            int[] counts = this.getCounts();
            int total = 0;
            for (int i = 0; i < counts.length; i++) {
                total += counts[i];
            }
            int[] values = this.getValues();
            int[] result = new int[total];
            int i = 0;
            for (int ind = 0; i < counts.length; i++) {
                for (int j = 0; j < counts[i]; ind++) {
                    result[ind] = values[i];
                    j++;
                }
            }
            return result;
        }
    }

    public static class Long extends RunLength {

        private static final long MIN_VALUE = java.lang.Long.MIN_VALUE;

        private long lastValue;

        private int count = 0;

        private LongArrayList values;

        public Long() {
            this.lastValue = java.lang.Long.MIN_VALUE;
            this.values = LongArrayList.createLongArrayList();
        }

        public void add(long value) {
            if (this.lastValue == java.lang.Long.MIN_VALUE || this.lastValue != value) {
                if (this.lastValue != java.lang.Long.MIN_VALUE) {
                    this.values.add(this.lastValue);
                    this.counts.add(this.count);
                    this.count = 0;
                }
                this.lastValue = value;
            }
            this.count++;
        }

        @Override
        public int[] getCounts() {
            this.finish();
            return this.counts.toArray();
        }

        public long[] getValues() {
            this.finish();
            return this.values.toArray();
        }

        @Override
        protected void finish() {
            if (this.lastValue != java.lang.Long.MIN_VALUE) {
                this.values.add(this.lastValue);
                this.counts.add(this.count);
                this.lastValue = java.lang.Long.MIN_VALUE;
                this.count = 0;
            }
        }

        public void serialize(ByteBuffer bb) {
            ByteBuffer dup = bb.duplicate();
            int[] counts = this.getCounts();
            long[] values = this.getValues();
            NIOUtils.skip(bb, 4);
            int recCount = 0;
            for (int i = 0; i < counts.length; recCount++) {
                int count;
                for (count = counts[i]; count >= 256; count -= 256) {
                    bb.put((byte) -1);
                    bb.putLong(values[i]);
                    recCount++;
                }
                bb.put((byte) (count - 1));
                bb.putLong(values[i]);
                i++;
            }
            dup.putInt(recCount);
        }

        public static RunLength.Long parse(ByteBuffer bb) {
            RunLength.Long rl = new RunLength.Long();
            int recCount = bb.getInt();
            for (int i = 0; i < recCount; i++) {
                int count = (bb.get() & 255) + 1;
                long value = bb.getLong();
                rl.counts.add(count);
                rl.values.add(value);
            }
            return rl;
        }

        @Override
        protected int recSize() {
            return 9;
        }

        public long[] flattern() {
            int[] counts = this.getCounts();
            int total = 0;
            for (int i = 0; i < counts.length; i++) {
                total += counts[i];
            }
            long[] values = this.getValues();
            long[] result = new long[total];
            int i = 0;
            for (int ind = 0; i < counts.length; i++) {
                for (int j = 0; j < counts[i]; ind++) {
                    result[ind] = values[i];
                    j++;
                }
            }
            return result;
        }
    }
}