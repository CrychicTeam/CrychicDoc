package net.mehvahdjukaar.supplementaries.integration;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.mehvahdjukaar.supplementaries.integration.forge.DecoBlocksCompatImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Contract;

public class DecoBlocksCompat {

    @Contract
    @ExpectPlatform
    @Transformed
    public static boolean isPalisade(BlockState state) {
        return DecoBlocksCompatImpl.isPalisade(state);
    }

    @ExpectPlatform
    @Transformed
    public static void tryConvertingRopeChandelier(BlockState facingState, LevelAccessor worldIn, BlockPos facingPos) {
        DecoBlocksCompatImpl.tryConvertingRopeChandelier(facingState, worldIn, facingPos);
    }

    @ExpectPlatform
    @Transformed
    public static void init() {
        DecoBlocksCompatImpl.init();
    }

    @ExpectPlatform
    @Transformed
    public static void setupClient() {
        DecoBlocksCompatImpl.setupClient();
    }
}