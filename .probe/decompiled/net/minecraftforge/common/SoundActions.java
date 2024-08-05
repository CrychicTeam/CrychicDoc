package net.minecraftforge.common;

public final class SoundActions {

    public static final SoundAction BUCKET_FILL = SoundAction.get("bucket_fill");

    public static final SoundAction BUCKET_EMPTY = SoundAction.get("bucket_empty");

    public static final SoundAction FLUID_VAPORIZE = SoundAction.get("fluid_vaporize");

    private SoundActions() {
        throw new AssertionError("SoundActions should not be instantiated.");
    }
}