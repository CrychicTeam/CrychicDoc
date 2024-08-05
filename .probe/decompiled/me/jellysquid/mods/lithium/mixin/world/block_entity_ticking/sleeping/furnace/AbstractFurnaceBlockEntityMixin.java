package me.jellysquid.mods.lithium.mixin.world.block_entity_ticking.sleeping.furnace;

import me.jellysquid.mods.lithium.common.block.entity.SleepingBlockEntity;
import me.jellysquid.mods.lithium.mixin.world.block_entity_ticking.sleeping.WrappedBlockEntityTickInvokerAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ AbstractFurnaceBlockEntity.class })
public abstract class AbstractFurnaceBlockEntityMixin extends BlockEntity implements SleepingBlockEntity {

    @Shadow
    int cookingProgress;

    private WrappedBlockEntityTickInvokerAccessor tickWrapper = null;

    private TickingBlockEntity sleepingTicker = null;

    @Shadow
    protected abstract boolean isLit();

    public AbstractFurnaceBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public WrappedBlockEntityTickInvokerAccessor getTickWrapper() {
        return this.tickWrapper;
    }

    @Override
    public void setTickWrapper(WrappedBlockEntityTickInvokerAccessor tickWrapper) {
        this.tickWrapper = tickWrapper;
        this.setSleepingTicker(null);
    }

    @Override
    public TickingBlockEntity getSleepingTicker() {
        return this.sleepingTicker;
    }

    @Override
    public void setSleepingTicker(TickingBlockEntity sleepingTicker) {
        this.sleepingTicker = sleepingTicker;
    }

    @Inject(method = { "tick" }, at = { @At("RETURN") })
    private static void checkSleep(Level world, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {
        ((AbstractFurnaceBlockEntityMixin) blockEntity).checkSleep(state);
    }

    private void checkSleep(BlockState state) {
        if (!this.isLit() && this.cookingProgress == 0 && (state.m_60713_(Blocks.FURNACE) || state.m_60713_(Blocks.BLAST_FURNACE) || state.m_60713_(Blocks.SMOKER)) && this.f_58857_ != null) {
            this.startSleeping();
        }
    }

    @Inject(method = { "readNbt" }, at = { @At("RETURN") })
    private void wakeUpAfterFromTag(CallbackInfo ci) {
        if (this.isSleeping() && this.f_58857_ != null && !this.f_58857_.isClientSide) {
            this.wakeUpNow();
        }
    }

    @Intrinsic
    @Override
    public void setChanged() {
        super.setChanged();
    }

    @Inject(method = { "markDirty()V" }, at = { @At("RETURN") })
    private void wakeOnMarkDirty(CallbackInfo ci) {
        if (this.isSleeping() && this.f_58857_ != null && !this.f_58857_.isClientSide) {
            this.wakeUpNow();
        }
    }
}