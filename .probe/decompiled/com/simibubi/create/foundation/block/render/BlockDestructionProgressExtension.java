package com.simibubi.create.foundation.block.render;

import java.util.Set;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

public interface BlockDestructionProgressExtension {

    @Nullable
    Set<BlockPos> getExtraPositions();

    void setExtraPositions(@Nullable Set<BlockPos> var1);
}