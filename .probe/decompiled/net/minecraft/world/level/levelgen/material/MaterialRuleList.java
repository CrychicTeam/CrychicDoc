package net.minecraft.world.level.levelgen.material;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseChunk;

public record MaterialRuleList(List<NoiseChunk.BlockStateFiller> f_191545_) implements NoiseChunk.BlockStateFiller {

    private final List<NoiseChunk.BlockStateFiller> materialRuleList;

    public MaterialRuleList(List<NoiseChunk.BlockStateFiller> f_191545_) {
        this.materialRuleList = f_191545_;
    }

    @Nullable
    @Override
    public BlockState calculate(DensityFunction.FunctionContext p_209815_) {
        for (NoiseChunk.BlockStateFiller $$1 : this.materialRuleList) {
            BlockState $$2 = $$1.calculate(p_209815_);
            if ($$2 != null) {
                return $$2;
            }
        }
        return null;
    }
}