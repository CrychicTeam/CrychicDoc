package com.simibubi.create.foundation.mixin.client;

import com.simibubi.create.foundation.block.render.BlockDestructionProgressExtension;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.BlockDestructionProgress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ BlockDestructionProgress.class })
public class BlockDestructionProgressMixin implements BlockDestructionProgressExtension {

    @Unique
    private Set<BlockPos> create$extraPositions;

    @Override
    public Set<BlockPos> getExtraPositions() {
        return this.create$extraPositions;
    }

    @Override
    public void setExtraPositions(Set<BlockPos> positions) {
        this.create$extraPositions = positions;
    }
}