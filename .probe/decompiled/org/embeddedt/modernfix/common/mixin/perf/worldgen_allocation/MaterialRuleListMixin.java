package org.embeddedt.modernfix.common.mixin.perf.worldgen_allocation;

import java.util.List;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.material.MaterialRuleList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = { MaterialRuleList.class }, priority = 100)
public class MaterialRuleListMixin {

    @Shadow
    @Final
    private List<NoiseChunk.BlockStateFiller> materialRuleList;

    @Overwrite
    @Nullable
    public BlockState calculate(DensityFunction.FunctionContext arg) {
        BlockState state = null;
        int s = this.materialRuleList.size();
        for (int i = 0; state == null && i < s; i++) {
            NoiseChunk.BlockStateFiller blockStateFiller = (NoiseChunk.BlockStateFiller) this.materialRuleList.get(i);
            state = blockStateFiller.calculate(arg);
        }
        return state;
    }
}