package me.jellysquid.mods.lithium.mixin.entity.collisions.fluid;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMaps;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap.Entry;
import java.util.function.BiPredicate;
import javax.annotation.Nullable;
import me.jellysquid.mods.lithium.common.block.BlockCountingSection;
import me.jellysquid.mods.lithium.common.block.BlockStateFlags;
import me.jellysquid.mods.lithium.common.entity.FluidCachingEntity;
import me.jellysquid.mods.lithium.common.util.Pos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.extensions.IForgeEntity;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = { Entity.class }, priority = 900)
public abstract class EntityMixin implements FluidCachingEntity, IForgeEntity {

    @Shadow
    public Level level;

    @Shadow
    protected Object2DoubleMap<FluidType> forgeFluidTypeHeight;

    @Shadow
    @Deprecated
    protected Object2DoubleMap<TagKey<Fluid>> fluidHeight;

    @Shadow
    public float fallDistance;

    private boolean radium$isInModdedFluid;

    @Shadow
    public abstract AABB getBoundingBox();

    @Shadow
    abstract void updateInWaterStateAndDoWaterCurrentPushing();

    @Shadow
    @Override
    public abstract boolean isInFluidType();

    @Shadow
    @Nullable
    public abstract Entity getVehicle();

    @Shadow
    public abstract void clearFire();

    @Inject(method = { "updateFluidHeightAndDoFluidPushing" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isPushedByFluids()Z", shift = Shift.BEFORE) }, cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD, require = 0)
    public void tryShortcutFluidPushing(CallbackInfo ci, AABB box, int x1, int x2, int y1, int y2, int z1, int z2, double zero) {
        int chunkX1 = x1 >> 4;
        int chunkZ1 = z1 >> 4;
        int chunkX2 = x2 - 1 >> 4;
        int chunkZ2 = z2 - 1 >> 4;
        int chunkYIndex1 = Math.max(Pos.SectionYIndex.fromBlockCoord(this.level, y1), Pos.SectionYIndex.getMinYSectionIndex(this.level));
        int chunkYIndex2 = Math.min(Pos.SectionYIndex.fromBlockCoord(this.level, y2 - 1), Pos.SectionYIndex.getMaxYSectionIndexInclusive(this.level));
        for (int chunkX = chunkX1; chunkX <= chunkX2; chunkX++) {
            for (int chunkZ = chunkZ1; chunkZ <= chunkZ2; chunkZ++) {
                ChunkAccess chunk = this.level.getChunk(chunkX, chunkZ);
                for (int chunkYIndex = chunkYIndex1; chunkYIndex <= chunkYIndex2; chunkYIndex++) {
                    LevelChunkSection section = chunk.getSections()[chunkYIndex];
                    if (((BlockCountingSection) section).mayContainAny(BlockStateFlags.ANY_FLUID)) {
                        return;
                    }
                }
            }
        }
        if (!this.forgeFluidTypeHeight.isEmpty()) {
            this.forgeFluidTypeHeight.clear();
        }
        ci.cancel();
    }

    @Inject(method = { "setFluidTypeHeight" }, at = { @At("RETURN") })
    private void markInModdedFluid(FluidType type, double height, CallbackInfo ci) {
        if (!type.isAir() && !type.isVanilla()) {
            this.radium$isInModdedFluid = true;
        }
    }

    @Overwrite
    protected boolean updateInWaterStateAndDoFluidPushing() {
        this.fluidHeight.clear();
        this.forgeFluidTypeHeight.clear();
        this.radium$isInModdedFluid = false;
        this.updateInWaterStateAndDoWaterCurrentPushing();
        if (this.radium$isInModdedFluid && !(this.getVehicle() instanceof Boat)) {
            float fallDistanceModifier = Float.MAX_VALUE;
            boolean canExtinguish = false;
            ObjectIterator var3 = this.forgeFluidTypeHeight.keySet().iterator();
            while (var3.hasNext()) {
                FluidType type = (FluidType) var3.next();
                if (!type.isAir() && !type.isVanilla()) {
                    fallDistanceModifier = Math.min(this.getFluidFallDistanceModifier(type), fallDistanceModifier);
                    canExtinguish |= this.canFluidExtinguish(type);
                }
            }
            if (fallDistanceModifier != Float.MAX_VALUE) {
                this.fallDistance *= fallDistanceModifier;
            }
            if (canExtinguish) {
                this.clearFire();
            }
        }
        return this.isInFluidType();
    }

    @Overwrite
    @Override
    public final boolean isInFluidType(BiPredicate<FluidType, Double> predicate, boolean forAllTypes) {
        if (this.forgeFluidTypeHeight.isEmpty()) {
            return false;
        } else {
            ObjectIterator<Entry<FluidType>> it = Object2DoubleMaps.fastIterator(this.forgeFluidTypeHeight);
            if (forAllTypes) {
                while (it.hasNext()) {
                    Entry<FluidType> entry = (Entry<FluidType>) it.next();
                    if (!predicate.test((FluidType) entry.getKey(), entry.getDoubleValue())) {
                        return false;
                    }
                }
                return true;
            } else {
                while (it.hasNext()) {
                    Entry<FluidType> entry = (Entry<FluidType>) it.next();
                    if (predicate.test((FluidType) entry.getKey(), entry.getDoubleValue())) {
                        return true;
                    }
                }
                return false;
            }
        }
    }
}