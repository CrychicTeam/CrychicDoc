package me.jellysquid.mods.sodium.mixin.features.render.world;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.WritableLevelData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = { ClientLevel.class }, priority = 500)
public abstract class ClientLevelMixin extends Level {

    private BlockPos.MutableBlockPos embeddium$particlePos;

    private final Consumer<AmbientParticleSettings> embeddium$particleSettingsConsumer = settings -> this.m_263888_(this.embeddium$particlePos, settings);

    protected ClientLevelMixin(WritableLevelData writableLevelData0, ResourceKey<Level> resourceKeyLevel1, RegistryAccess registryAccess2, Holder<DimensionType> holderDimensionType3, Supplier<ProfilerFiller> supplierProfilerFiller4, boolean boolean5, boolean boolean6, long long7, int int8) {
        super(writableLevelData0, resourceKeyLevel1, registryAccess2, holderDimensionType3, supplierProfilerFiller4, boolean5, boolean6, long7, int8);
    }

    @Shadow
    private void m_263888_(BlockPos.MutableBlockPos pos, AmbientParticleSettings settings) {
        throw new AssertionError();
    }

    @Shadow
    protected abstract void trySpawnDripParticles(BlockPos var1, BlockState var2, ParticleOptions var3, boolean var4);

    @Redirect(method = { "animateTick" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;create()Lnet/minecraft/util/RandomSource;"))
    private RandomSource createLocal() {
        return new SingleThreadedRandomSource(RandomSupport.generateUniqueSeed());
    }

    @Overwrite
    public void doAnimateTick(int xCenter, int yCenter, int zCenter, int radius, RandomSource random, @Nullable Block markerBlock, BlockPos.MutableBlockPos pos) {
        int x = xCenter + (random.nextInt(radius) - random.nextInt(radius));
        int y = yCenter + (random.nextInt(radius) - random.nextInt(radius));
        int z = zCenter + (random.nextInt(radius) - random.nextInt(radius));
        pos.set(x, y, z);
        BlockState blockState = this.m_8055_(pos);
        blockState.m_60734_().animateTick(blockState, this, pos, random);
        FluidState fluidState = blockState.m_60819_();
        if (!fluidState.isEmpty()) {
            fluidState.animateTick(this, pos, random);
            ParticleOptions particleoptions = fluidState.getDripParticle();
            if (particleoptions != null && random.nextInt(10) == 0) {
                boolean flag = blockState.m_60783_(this, pos, Direction.DOWN);
                BlockPos blockpos = pos.m_7495_();
                this.trySpawnDripParticles(blockpos, this.m_8055_(blockpos), particleoptions, flag);
            }
        }
        if (blockState.m_60734_() == markerBlock) {
            this.m_7106_(new BlockParticleOption(ParticleTypes.BLOCK_MARKER, blockState), (double) x + 0.5, (double) y + 0.5, (double) z + 0.5, 0.0, 0.0, 0.0);
        }
        if (!blockState.m_60838_(this, pos)) {
            this.embeddium$particlePos = pos;
            ((Biome) this.m_204166_(pos).value()).getAmbientParticle().ifPresent(this.embeddium$particleSettingsConsumer);
        }
    }
}