package me.jellysquid.mods.sodium.client.render.chunk.occlusion;

public class GraphDirectionSet {

    public static final int NONE = 0;

    public static final int ALL = 63;

    public static int of(int direction) {
        return 1 << direction;
    }

    public static boolean contains(int set, int direction) {
        return (set & of(direction)) != 0;
    }
}