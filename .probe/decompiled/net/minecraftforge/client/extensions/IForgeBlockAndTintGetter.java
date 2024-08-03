package net.minecraftforge.client.extensions;

import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;

public interface IForgeBlockAndTintGetter {

    private BlockAndTintGetter self() {
        return (BlockAndTintGetter) this;
    }

    default float getShade(float normalX, float normalY, float normalZ, boolean shade) {
        return this.self().getShade(Direction.getNearest(normalX, normalY, normalZ), shade);
    }
}