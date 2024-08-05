package com.github.alexthe666.citadel.repack.jaad.aac;

public enum ChannelConfiguration {

    CHANNEL_CONFIG_UNSUPPORTED(-1, "invalid"),
    CHANNEL_CONFIG_NONE(0, "No channel"),
    CHANNEL_CONFIG_MONO(1, "Mono"),
    CHANNEL_CONFIG_STEREO(2, "Stereo"),
    CHANNEL_CONFIG_STEREO_PLUS_CENTER(3, "Stereo+Center"),
    CHANNEL_CONFIG_STEREO_PLUS_CENTER_PLUS_REAR_MONO(4, "Stereo+Center+Rear"),
    CHANNEL_CONFIG_FIVE(5, "Five channels"),
    CHANNEL_CONFIG_FIVE_PLUS_ONE(6, "Five channels+LF"),
    CHANNEL_CONFIG_SEVEN_PLUS_ONE(8, "Seven channels+LF");

    private final int chCount;

    private final String descr;

    public static ChannelConfiguration forInt(int i) {
        return switch(i) {
            case 0 ->
                CHANNEL_CONFIG_NONE;
            case 1 ->
                CHANNEL_CONFIG_MONO;
            case 2 ->
                CHANNEL_CONFIG_STEREO;
            case 3 ->
                CHANNEL_CONFIG_STEREO_PLUS_CENTER;
            case 4 ->
                CHANNEL_CONFIG_STEREO_PLUS_CENTER_PLUS_REAR_MONO;
            case 5 ->
                CHANNEL_CONFIG_FIVE;
            case 6 ->
                CHANNEL_CONFIG_FIVE_PLUS_ONE;
            case 7, 8 ->
                CHANNEL_CONFIG_SEVEN_PLUS_ONE;
            default ->
                CHANNEL_CONFIG_UNSUPPORTED;
        };
    }

    private ChannelConfiguration(int chCount, String descr) {
        this.chCount = chCount;
        this.descr = descr;
    }

    public int getChannelCount() {
        return this.chCount;
    }

    public String getDescription() {
        return this.descr;
    }

    public String toString() {
        return this.descr;
    }
}