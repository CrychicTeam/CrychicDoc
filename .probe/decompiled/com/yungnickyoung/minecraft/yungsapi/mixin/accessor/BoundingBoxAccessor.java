package com.yungnickyoung.minecraft.yungsapi.mixin.accessor;

import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ BoundingBox.class })
public interface BoundingBoxAccessor {

    @Accessor("minX")
    void setMinX(int var1);

    @Accessor("minY")
    void setMinY(int var1);

    @Accessor("minZ")
    void setMinZ(int var1);

    @Accessor("maxX")
    void setMaxX(int var1);

    @Accessor("maxY")
    void setMaxY(int var1);

    @Accessor("maxZ")
    void setMaxZ(int var1);
}