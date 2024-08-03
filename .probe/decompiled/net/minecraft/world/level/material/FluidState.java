package net.minecraft.world.level.material;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class FluidState extends StateHolder<Fluid, FluidState> {

    public static final Codec<FluidState> CODEC = m_61127_(BuiltInRegistries.FLUID.m_194605_(), Fluid::m_76145_).stable();

    public static final int AMOUNT_MAX = 9;

    public static final int AMOUNT_FULL = 8;

    public FluidState(Fluid fluid0, ImmutableMap<Property<?>, Comparable<?>> immutableMapPropertyComparable1, MapCodec<FluidState> mapCodecFluidState2) {
        super(fluid0, immutableMapPropertyComparable1, mapCodecFluidState2);
    }

    public Fluid getType() {
        return (Fluid) this.f_61112_;
    }

    public boolean isSource() {
        return this.getType().isSource(this);
    }

    public boolean isSourceOfType(Fluid fluid0) {
        return this.f_61112_ == fluid0 && ((Fluid) this.f_61112_).isSource(this);
    }

    public boolean isEmpty() {
        return this.getType().isEmpty();
    }

    public float getHeight(BlockGetter blockGetter0, BlockPos blockPos1) {
        return this.getType().getHeight(this, blockGetter0, blockPos1);
    }

    public float getOwnHeight() {
        return this.getType().getOwnHeight(this);
    }

    public int getAmount() {
        return this.getType().getAmount(this);
    }

    public boolean shouldRenderBackwardUpFace(BlockGetter blockGetter0, BlockPos blockPos1) {
        for (int $$2 = -1; $$2 <= 1; $$2++) {
            for (int $$3 = -1; $$3 <= 1; $$3++) {
                BlockPos $$4 = blockPos1.offset($$2, 0, $$3);
                FluidState $$5 = blockGetter0.getFluidState($$4);
                if (!$$5.getType().isSame(this.getType()) && !blockGetter0.getBlockState($$4).m_60804_(blockGetter0, $$4)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void tick(Level level0, BlockPos blockPos1) {
        this.getType().tick(level0, blockPos1, this);
    }

    public void animateTick(Level level0, BlockPos blockPos1, RandomSource randomSource2) {
        this.getType().animateTick(level0, blockPos1, this, randomSource2);
    }

    public boolean isRandomlyTicking() {
        return this.getType().isRandomlyTicking();
    }

    public void randomTick(Level level0, BlockPos blockPos1, RandomSource randomSource2) {
        this.getType().randomTick(level0, blockPos1, this, randomSource2);
    }

    public Vec3 getFlow(BlockGetter blockGetter0, BlockPos blockPos1) {
        return this.getType().getFlow(blockGetter0, blockPos1, this);
    }

    public BlockState createLegacyBlock() {
        return this.getType().createLegacyBlock(this);
    }

    @Nullable
    public ParticleOptions getDripParticle() {
        return this.getType().getDripParticle();
    }

    public boolean is(TagKey<Fluid> tagKeyFluid0) {
        return this.getType().builtInRegistryHolder().is(tagKeyFluid0);
    }

    public boolean is(HolderSet<Fluid> holderSetFluid0) {
        return holderSetFluid0.contains(this.getType().builtInRegistryHolder());
    }

    public boolean is(Fluid fluid0) {
        return this.getType() == fluid0;
    }

    public float getExplosionResistance() {
        return this.getType().getExplosionResistance();
    }

    public boolean canBeReplacedWith(BlockGetter blockGetter0, BlockPos blockPos1, Fluid fluid2, Direction direction3) {
        return this.getType().canBeReplacedWith(this, blockGetter0, blockPos1, fluid2, direction3);
    }

    public VoxelShape getShape(BlockGetter blockGetter0, BlockPos blockPos1) {
        return this.getType().getShape(this, blockGetter0, blockPos1);
    }

    public Holder<Fluid> holder() {
        return ((Fluid) this.f_61112_).builtInRegistryHolder();
    }

    public Stream<TagKey<Fluid>> getTags() {
        return ((Fluid) this.f_61112_).builtInRegistryHolder().tags();
    }
}