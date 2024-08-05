package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;

public class ChunkCopyBehaviour {

    public static final int COPY_NONE = 0;

    public static final int COPY_PALETTE = 1;

    public static final int COPY_ALL_SAFE = 4;

    public static final int COPY_ALL = 8;

    public static final int COPY_PHYS = 16;

    public static final int COPY_TEXTUAL = 32;

    public static final int COPY_TRANSPARENCY = 64;

    public static final int COPY_UNKNOWN = 128;

    public static final int COPY_ALMOSTALL = 256;

    private static boolean maskMatch(int v, int mask) {
        return (v & mask) != 0;
    }

    public static ChunkPredicate createPredicate(final int copyFromMask, final ImageInfo imgInfo) {
        return new ChunkPredicate() {

            @Override
            public boolean match(PngChunk chunk) {
                if (chunk.crit) {
                    if (chunk.id.equals("PLTE")) {
                        if (imgInfo.indexed && ChunkCopyBehaviour.maskMatch(copyFromMask, 1)) {
                            return true;
                        }
                        if (!imgInfo.greyscale && ChunkCopyBehaviour.maskMatch(copyFromMask, 8)) {
                            return true;
                        }
                    }
                } else {
                    boolean text = chunk instanceof PngChunkTextVar;
                    boolean safe = chunk.safe;
                    if (ChunkCopyBehaviour.maskMatch(copyFromMask, 8)) {
                        return true;
                    }
                    if (safe && ChunkCopyBehaviour.maskMatch(copyFromMask, 4)) {
                        return true;
                    }
                    if (chunk.id.equals("tRNS") && ChunkCopyBehaviour.maskMatch(copyFromMask, 64)) {
                        return true;
                    }
                    if (chunk.id.equals("pHYs") && ChunkCopyBehaviour.maskMatch(copyFromMask, 16)) {
                        return true;
                    }
                    if (text && ChunkCopyBehaviour.maskMatch(copyFromMask, 32)) {
                        return true;
                    }
                    if (ChunkCopyBehaviour.maskMatch(copyFromMask, 256) && !ChunkHelper.isUnknown(chunk) && !text && !chunk.id.equals("hIST") && !chunk.id.equals("tIME")) {
                        return true;
                    }
                    if (ChunkCopyBehaviour.maskMatch(copyFromMask, 128) && ChunkHelper.isUnknown(chunk)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
}