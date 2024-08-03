package snownee.lychee.dripstone_dripping;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.ExecutionException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import snownee.lychee.Lychee;
import snownee.lychee.RecipeTypes;
import snownee.lychee.util.CommonProxy;

public class DripstoneRecipeMod {

    public static final Cache<Block, DripParticleHandler> particleHandlers = CacheBuilder.newBuilder().build();

    public static final ParticleType<BlockParticleOption> DRIPSTONE_DRIPPING = CommonProxy.registerParticleType(BlockParticleOption.DESERIALIZER);

    public static final ParticleType<BlockParticleOption> DRIPSTONE_FALLING = CommonProxy.registerParticleType(BlockParticleOption.DESERIALIZER);

    public static final ParticleType<BlockParticleOption> DRIPSTONE_SPLASH = CommonProxy.registerParticleType(BlockParticleOption.DESERIALIZER);

    public static boolean spawnDripParticle(Level level, BlockPos blockPos, BlockState blockState) {
        BlockState sourceBlock = DripstoneRecipe.getBlockAboveStalactite(level, blockPos, blockState);
        if (sourceBlock != null && RecipeTypes.DRIPSTONE_DRIPPING.hasSource(sourceBlock.m_60734_())) {
            FluidState sourceFluid = sourceBlock.m_60819_();
            if (sourceFluid.is(FluidTags.LAVA) || sourceFluid.is(FluidTags.WATER)) {
                return false;
            } else if (CommonProxy.hasModdedDripParticle(sourceFluid)) {
                return false;
            } else {
                DripParticleHandler handler = getParticleHandler(level, sourceBlock);
                if (handler == null) {
                    return false;
                } else {
                    Vec3 vec3 = blockState.m_60824_(level, blockPos);
                    double e = (double) blockPos.m_123341_() + 0.5 + vec3.x;
                    double f = (double) ((float) (blockPos.m_123342_() + 1) - 0.6875F) - 0.0625;
                    double g = (double) blockPos.m_123343_() + 0.5 + vec3.z;
                    handler.addParticle(level, blockPos, sourceBlock, e, f, g);
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    public static DripParticleHandler getParticleHandler(Level level, BlockState sourceBlock) {
        Block block = sourceBlock.m_60734_();
        try {
            return (DripParticleHandler) particleHandlers.get(block, () -> {
                if (!CommonProxy.isPhysicalClient()) {
                    return DripParticleHandler.SIMPLE_DUMMY;
                } else {
                    BlockState defaultState = block.defaultBlockState();
                    int color = defaultState.m_284242_(level, BlockPos.ZERO).col;
                    return new DripParticleHandler.Simple(color, defaultState.m_60791_() > 4);
                }
            });
        } catch (ExecutionException var4) {
            Lychee.LOGGER.error("", var4);
            return null;
        }
    }
}