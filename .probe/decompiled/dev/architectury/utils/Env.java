package dev.architectury.utils;

import net.minecraftforge.api.distmarker.Dist;

public enum Env {

    CLIENT, SERVER;

    public static Env fromPlatform(Object type) {
        return type == Dist.CLIENT ? CLIENT : (type == Dist.DEDICATED_SERVER ? SERVER : null);
    }

    public Dist toPlatform() {
        return this == CLIENT ? Dist.CLIENT : Dist.DEDICATED_SERVER;
    }
}