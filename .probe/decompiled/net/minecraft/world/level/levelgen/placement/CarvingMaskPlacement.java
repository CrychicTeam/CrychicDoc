package net.minecraft.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.GenerationStep;

public class CarvingMaskPlacement extends PlacementModifier {

    public static final Codec<CarvingMaskPlacement> CODEC = GenerationStep.Carving.CODEC.fieldOf("step").xmap(CarvingMaskPlacement::new, p_191593_ -> p_191593_.step).codec();

    private final GenerationStep.Carving step;

    private CarvingMaskPlacement(GenerationStep.Carving generationStepCarving0) {
        this.step = generationStepCarving0;
    }

    public static CarvingMaskPlacement forStep(GenerationStep.Carving generationStepCarving0) {
        return new CarvingMaskPlacement(generationStepCarving0);
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext placementContext0, RandomSource randomSource1, BlockPos blockPos2) {
        ChunkPos $$3 = new ChunkPos(blockPos2);
        return placementContext0.getCarvingMask($$3, this.step).stream($$3);
    }

    @Override
    public PlacementModifierType<?> type() {
        return PlacementModifierType.CARVING_MASK_PLACEMENT;
    }
}