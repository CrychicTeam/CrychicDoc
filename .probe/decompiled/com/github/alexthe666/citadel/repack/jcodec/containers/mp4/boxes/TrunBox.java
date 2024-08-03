package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.nio.ByteBuffer;

public class TrunBox extends FullBox {

    private static final int DATA_OFFSET_AVAILABLE = 1;

    private static final int FIRST_SAMPLE_FLAGS_AVAILABLE = 4;

    private static final int SAMPLE_DURATION_AVAILABLE = 256;

    private static final int SAMPLE_SIZE_AVAILABLE = 512;

    private static final int SAMPLE_FLAGS_AVAILABLE = 1024;

    private static final int SAMPLE_COMPOSITION_OFFSET_AVAILABLE = 2048;

    private int sampleCount;

    private int dataOffset;

    private int firstSampleFlags;

    private int[] sampleDuration;

    private int[] sampleSize;

    private int[] sampleFlags;

    private int[] sampleCompositionOffset;

    public static String fourcc() {
        return "trun";
    }

    public void setDataOffset(int dataOffset) {
        this.dataOffset = dataOffset;
    }

    public static TrunBox.Factory create(int sampleCount) {
        return new TrunBox.Factory(createTrunBox1(sampleCount));
    }

    public static TrunBox.Factory copy(TrunBox other) {
        TrunBox box = createTrunBox2(other.sampleCount, other.dataOffset, other.firstSampleFlags, other.sampleDuration, other.sampleSize, other.sampleFlags, other.sampleCompositionOffset);
        box.setFlags(other.getFlags());
        box.setVersion(other.getVersion());
        return new TrunBox.Factory(box);
    }

    public TrunBox(Header header) {
        super(header);
    }

    public static TrunBox createTrunBox1(int sampleCount) {
        TrunBox trun = new TrunBox(new Header(fourcc()));
        trun.sampleCount = sampleCount;
        return trun;
    }

    public static TrunBox createTrunBox2(int sampleCount, int dataOffset, int firstSampleFlags, int[] sampleDuration, int[] sampleSize, int[] sampleFlags, int[] sampleCompositionOffset) {
        TrunBox trun = new TrunBox(new Header(fourcc()));
        trun.sampleCount = sampleCount;
        trun.dataOffset = dataOffset;
        trun.firstSampleFlags = firstSampleFlags;
        trun.sampleDuration = sampleDuration;
        trun.sampleSize = sampleSize;
        trun.sampleFlags = sampleFlags;
        trun.sampleCompositionOffset = sampleCompositionOffset;
        return trun;
    }

    public long getSampleCount() {
        return Platform.unsignedInt(this.sampleCount);
    }

    public int getDataOffset() {
        return this.dataOffset;
    }

    public int getFirstSampleFlags() {
        return this.firstSampleFlags;
    }

    public int[] getSampleDurations() {
        return this.sampleDuration;
    }

    public int[] getSampleSizes() {
        return this.sampleSize;
    }

    public int[] getSamplesFlags() {
        return this.sampleFlags;
    }

    public int[] getSampleCompositionOffsets() {
        return this.sampleCompositionOffset;
    }

    public long getSampleDuration(int i) {
        return Platform.unsignedInt(this.sampleDuration[i]);
    }

    public long getSampleSize(int i) {
        return Platform.unsignedInt(this.sampleSize[i]);
    }

    public int getSampleFlags(int i) {
        return this.sampleFlags[i];
    }

    public long getSampleCompositionOffset(int i) {
        return Platform.unsignedInt(this.sampleCompositionOffset[i]);
    }

    public boolean isDataOffsetAvailable() {
        return (this.flags & 1) != 0;
    }

    public boolean isSampleCompositionOffsetAvailable() {
        return (this.flags & 2048) != 0;
    }

    public boolean isSampleFlagsAvailable() {
        return (this.flags & 1024) != 0;
    }

    public boolean isSampleSizeAvailable() {
        return (this.flags & 512) != 0;
    }

    public boolean isSampleDurationAvailable() {
        return (this.flags & 256) != 0;
    }

    public boolean isFirstSampleFlagsAvailable() {
        return (this.flags & 4) != 0;
    }

    public static int flagsGetSampleDependsOn(int flags) {
        return flags >> 6 & 3;
    }

    public static int flagsGetSampleIsDependedOn(int flags) {
        return flags >> 8 & 3;
    }

    public static int flagsGetSampleHasRedundancy(int flags) {
        return flags >> 10 & 3;
    }

    public static int flagsGetSamplePaddingValue(int flags) {
        return flags >> 12 & 7;
    }

    public static int flagsGetSampleIsDifferentSample(int flags) {
        return flags >> 15 & 1;
    }

    public static int flagsGetSampleDegradationPriority(int flags) {
        return flags >> 16 & 65535;
    }

    public static TrunBox createTrunBox() {
        return new TrunBox(new Header(fourcc()));
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        if (this.isSampleFlagsAvailable() && this.isFirstSampleFlagsAvailable()) {
            throw new RuntimeException("Broken stream");
        } else {
            this.sampleCount = input.getInt();
            if (this.isDataOffsetAvailable()) {
                this.dataOffset = input.getInt();
            }
            if (this.isFirstSampleFlagsAvailable()) {
                this.firstSampleFlags = input.getInt();
            }
            if (this.isSampleDurationAvailable()) {
                this.sampleDuration = new int[this.sampleCount];
            }
            if (this.isSampleSizeAvailable()) {
                this.sampleSize = new int[this.sampleCount];
            }
            if (this.isSampleFlagsAvailable()) {
                this.sampleFlags = new int[this.sampleCount];
            }
            if (this.isSampleCompositionOffsetAvailable()) {
                this.sampleCompositionOffset = new int[this.sampleCount];
            }
            for (int i = 0; i < this.sampleCount; i++) {
                if (this.isSampleDurationAvailable()) {
                    this.sampleDuration[i] = input.getInt();
                }
                if (this.isSampleSizeAvailable()) {
                    this.sampleSize[i] = input.getInt();
                }
                if (this.isSampleFlagsAvailable()) {
                    this.sampleFlags[i] = input.getInt();
                }
                if (this.isSampleCompositionOffsetAvailable()) {
                    this.sampleCompositionOffset[i] = input.getInt();
                }
            }
        }
    }

    @Override
    public void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putInt(this.sampleCount);
        if (this.isDataOffsetAvailable()) {
            out.putInt(this.dataOffset);
        }
        if (this.isFirstSampleFlagsAvailable()) {
            out.putInt(this.firstSampleFlags);
        }
        for (int i = 0; i < this.sampleCount; i++) {
            if (this.isSampleDurationAvailable()) {
                out.putInt(this.sampleDuration[i]);
            }
            if (this.isSampleSizeAvailable()) {
                out.putInt(this.sampleSize[i]);
            }
            if (this.isSampleFlagsAvailable()) {
                out.putInt(this.sampleFlags[i]);
            }
            if (this.isSampleCompositionOffsetAvailable()) {
                out.putInt(this.sampleCompositionOffset[i]);
            }
        }
    }

    @Override
    public int estimateSize() {
        return 24 + this.sampleCount * 16;
    }

    public static class Factory {

        private TrunBox box;

        protected Factory(TrunBox box) {
            this.box = box;
        }

        public TrunBox.Factory dataOffset(long dataOffset) {
            this.box.flags |= 1;
            this.box.dataOffset = (int) dataOffset;
            return this;
        }

        public TrunBox.Factory firstSampleFlags(int firstSampleFlags) {
            if (this.box.isSampleFlagsAvailable()) {
                throw new IllegalStateException("Sample flags already set on this object");
            } else {
                this.box.flags |= 4;
                this.box.firstSampleFlags = firstSampleFlags;
                return this;
            }
        }

        public TrunBox.Factory sampleDuration(int[] sampleDuration) {
            if (sampleDuration.length != this.box.sampleCount) {
                throw new IllegalArgumentException("Argument array length not equal to sampleCount");
            } else {
                this.box.flags |= 256;
                this.box.sampleDuration = sampleDuration;
                return this;
            }
        }

        public TrunBox.Factory sampleSize(int[] sampleSize) {
            if (sampleSize.length != this.box.sampleCount) {
                throw new IllegalArgumentException("Argument array length not equal to sampleCount");
            } else {
                this.box.flags |= 512;
                this.box.sampleSize = sampleSize;
                return this;
            }
        }

        public TrunBox.Factory sampleFlags(int[] sampleFlags) {
            if (sampleFlags.length != this.box.sampleCount) {
                throw new IllegalArgumentException("Argument array length not equal to sampleCount");
            } else if (this.box.isFirstSampleFlagsAvailable()) {
                throw new IllegalStateException("First sample flags already set on this object");
            } else {
                this.box.flags |= 1024;
                this.box.sampleFlags = sampleFlags;
                return this;
            }
        }

        public TrunBox.Factory sampleCompositionOffset(int[] sampleCompositionOffset) {
            if (sampleCompositionOffset.length != this.box.sampleCount) {
                throw new IllegalArgumentException("Argument array length not equal to sampleCount");
            } else {
                this.box.flags |= 2048;
                this.box.sampleCompositionOffset = sampleCompositionOffset;
                return this;
            }
        }

        public TrunBox create() {
            TrunBox var1;
            try {
                var1 = this.box;
            } finally {
                this.box = null;
            }
            return var1;
        }
    }
}