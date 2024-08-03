package fuzs.puzzleslib.api.core.v1;

import java.util.stream.Stream;

public enum ModLoader {

    FABRIC, NEOFORGE, FORGE, QUILT;

    public static ModLoader[] getFabricLike() {
        return (ModLoader[]) Stream.of(values()).filter(ModLoader::isFabricLike).toArray(ModLoader[]::new);
    }

    public static ModLoader[] getForgeLike() {
        return (ModLoader[]) Stream.of(values()).filter(ModLoader::isForgeLike).toArray(ModLoader[]::new);
    }

    public boolean isFabric() {
        return this == FABRIC;
    }

    public boolean isNeoForge() {
        return this == NEOFORGE;
    }

    public boolean isForge() {
        return this == FORGE;
    }

    public boolean isQuilt() {
        return this == QUILT;
    }

    public boolean isFabricLike() {
        return this.isFabric() || this.isQuilt();
    }

    public boolean isForgeLike() {
        return this.isNeoForge() || this.isForge();
    }
}