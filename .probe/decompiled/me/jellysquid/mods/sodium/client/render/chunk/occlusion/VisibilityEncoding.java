package me.jellysquid.mods.sodium.client.render.chunk.occlusion;

import net.minecraft.client.renderer.chunk.VisibilitySet;
import org.jetbrains.annotations.NotNull;

public class VisibilityEncoding {

    public static final long NULL = 0L;

    public static long encode(@NotNull VisibilitySet occlusionData) {
        long visibilityData = 0L;
        for (int from = 0; from < 6; from++) {
            for (int to = 0; to < 6; to++) {
                if (occlusionData.visibilityBetween(GraphDirection.toEnum(from), GraphDirection.toEnum(to))) {
                    visibilityData |= 1L << bit(from, to);
                }
            }
        }
        return visibilityData;
    }

    private static int bit(int from, int to) {
        return from * 8 + to;
    }

    public static int getConnections(long visibilityData, int incoming) {
        return foldOutgoingDirections(visibilityData & createMask(incoming));
    }

    public static int getConnections(long visibilityData) {
        return foldOutgoingDirections(visibilityData);
    }

    private static long createMask(int incoming) {
        long expanded = 34630287489L * Integer.toUnsignedLong(incoming);
        return (expanded & 1103823438081L) * 255L;
    }

    private static int foldOutgoingDirections(long data) {
        long folded = data | data >> 32;
        folded |= folded >> 16;
        folded |= folded >> 8;
        return (int) (folded & 63L);
    }
}