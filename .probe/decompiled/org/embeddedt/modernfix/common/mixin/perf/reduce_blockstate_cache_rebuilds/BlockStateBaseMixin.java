package org.embeddedt.modernfix.common.mixin.perf.reduce_blockstate_cache_rebuilds;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.embeddedt.modernfix.duck.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ BlockBehaviour.BlockStateBase.class })
public abstract class BlockStateBaseMixin extends StateHolder<Block, BlockState> implements IBlockState {

    private static final FluidState MFIX$VANILLA_DEFAULT_FLUID = Fluids.EMPTY.defaultFluidState();

    @Shadow
    private BlockBehaviour.BlockStateBase.Cache cache;

    @Shadow
    private FluidState fluidState;

    @Shadow
    private boolean isRandomlyTicking;

    @Shadow
    @Deprecated
    private boolean legacySolid;

    private volatile boolean cacheInvalid = false;

    private static boolean buildingCache = false;

    protected BlockStateBaseMixin(Block object, ImmutableMap<Property<?>, Comparable<?>> immutableMap, MapCodec<BlockState> mapCodec) {
        super(object, immutableMap, mapCodec);
    }

    @Shadow
    public abstract void initCache();

    @Shadow
    protected abstract BlockState asState();

    @Override
    public void clearCache() {
        this.cacheInvalid = true;
    }

    @Override
    public boolean isCacheInvalid() {
        return this.cacheInvalid;
    }

    private void mfix$generateCache() {
        if (this.cacheInvalid) {
            synchronized (BlockBehaviour.BlockStateBase.class) {
                if (this.cacheInvalid && !buildingCache) {
                    buildingCache = true;
                    try {
                        this.initCache();
                        this.cacheInvalid = false;
                    } finally {
                        buildingCache = false;
                    }
                }
            }
        }
    }

    @Redirect(method = { "*" }, at = @At(value = "FIELD", opcode = 180, target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$BlockStateBase;cache:Lnet/minecraft/world/level/block/state/BlockBehaviour$BlockStateBase$Cache;", ordinal = 0))
    private BlockBehaviour.BlockStateBase.Cache dynamicCacheGen(BlockBehaviour.BlockStateBase base) {
        this.mfix$generateCache();
        return this.cache;
    }

    @Redirect(method = { "*" }, at = @At(value = "FIELD", opcode = 180, target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$BlockStateBase;fluidState:Lnet/minecraft/world/level/material/FluidState;", ordinal = 0), require = 0)
    private FluidState genCacheBeforeGettingFluid(BlockBehaviour.BlockStateBase base) {
        if (this.cacheInvalid && this.fluidState == MFIX$VANILLA_DEFAULT_FLUID) {
            synchronized (BlockBehaviour.BlockStateBase.class) {
                if (!buildingCache) {
                    buildingCache = true;
                    try {
                        this.fluidState = ((Block) this.f_61112_).m_5888_(this.asState());
                    } finally {
                        buildingCache = false;
                    }
                }
            }
        }
        return this.fluidState;
    }

    @Redirect(method = { "*" }, at = @At(value = "FIELD", opcode = 180, target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$BlockStateBase;isRandomlyTicking:Z", ordinal = 0))
    private boolean genCacheBeforeGettingTicking(BlockBehaviour.BlockStateBase base) {
        return this.cacheInvalid ? ((Block) this.f_61112_).isRandomlyTicking(this.asState()) : this.isRandomlyTicking;
    }

    @Redirect(method = { "*" }, at = @At(value = "FIELD", opcode = 180, target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$BlockStateBase;legacySolid:Z", ordinal = 0))
    private boolean genCacheBeforeCheckingSolid(BlockBehaviour.BlockStateBase base) {
        this.mfix$generateCache();
        return this.legacySolid;
    }
}