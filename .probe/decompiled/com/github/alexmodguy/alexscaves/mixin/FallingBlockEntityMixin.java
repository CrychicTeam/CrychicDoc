package com.github.alexmodguy.alexscaves.mixin;

import com.github.alexmodguy.alexscaves.server.entity.util.FallingBlockEntityAccessor;
import com.github.alexmodguy.alexscaves.server.entity.util.MagnetUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ FallingBlockEntity.class })
public abstract class FallingBlockEntityMixin extends Entity implements FallingBlockEntityAccessor {

    @Shadow
    public int time;

    @Shadow
    private BlockState blockState;

    private static final EntityDataAccessor<Integer> FALL_BLOCK_TIME = SynchedEntityData.defineId(FallingBlockEntity.class, EntityDataSerializers.INT);

    public FallingBlockEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = { @At("TAIL") }, remap = true, method = { "Lnet/minecraft/world/entity/item/FallingBlockEntity;defineSynchedData()V" })
    private void citadel_registerData(CallbackInfo ci) {
        this.f_19804_.define(FALL_BLOCK_TIME, 0);
    }

    @Inject(method = { "Lnet/minecraft/world/entity/item/FallingBlockEntity;tick()V" }, remap = true, at = { @At("TAIL") })
    public void ac_tick(CallbackInfo ci) {
        if (!this.m_20068_() && this.hasFallBlocking()) {
            this.time = 10;
            this.m_20256_(this.m_20184_().add(0.0, 0.04, 0.0));
        }
        int fallBlockTime = this.f_19804_.get(FALL_BLOCK_TIME);
        if (fallBlockTime > 0) {
            this.f_19804_.set(FALL_BLOCK_TIME, fallBlockTime - 1);
        }
        if (MagnetUtil.isPulledByMagnets(this)) {
            MagnetUtil.tickMagnetism(this);
            if (MagnetUtil.getEntityMagneticDelta(this) != Vec3.ZERO) {
                this.setFallBlockingTime();
            }
        }
    }

    @Override
    public boolean hasFallBlocking() {
        return this.f_19804_.get(FALL_BLOCK_TIME) > 0;
    }

    @Override
    public void setFallBlockingTime() {
        this.f_19804_.set(FALL_BLOCK_TIME, 10);
    }

    @Override
    public void setBlockState(BlockState blockStateIn) {
        this.blockState = blockStateIn;
    }
}