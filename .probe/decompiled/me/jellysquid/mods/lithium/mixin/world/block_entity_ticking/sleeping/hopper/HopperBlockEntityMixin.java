package me.jellysquid.mods.lithium.mixin.world.block_entity_ticking.sleeping.hopper;

import java.util.function.BooleanSupplier;
import me.jellysquid.mods.lithium.common.block.entity.SleepingBlockEntity;
import me.jellysquid.mods.lithium.mixin.world.block_entity_ticking.sleeping.WrappedBlockEntityTickInvokerAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ HopperBlockEntity.class })
public class HopperBlockEntityMixin extends BlockEntity implements SleepingBlockEntity {

    @Shadow
    private long tickedGameTime;

    private WrappedBlockEntityTickInvokerAccessor tickWrapper = null;

    private TickingBlockEntity sleepingTicker = null;

    @Shadow
    private native boolean m_59407_();

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

    public HopperBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = { "insertAndExtract" }, at = { @At(value = "RETURN", ordinal = 2) })
    private static void sleepIfNoCooldownAndLocked(Level world, BlockPos pos, BlockState state, HopperBlockEntity blockEntity, BooleanSupplier booleanSupplier, CallbackInfoReturnable<Boolean> cir) {
        if (!((HopperBlockEntityMixin) blockEntity).m_59407_() && !((HopperBlockEntityMixin) blockEntity).isSleeping() && !(Boolean) state.m_61143_(HopperBlock.ENABLED)) {
            ((HopperBlockEntityMixin) blockEntity).startSleeping();
        }
    }

    @Override
    public boolean startSleeping() {
        if (this.isSleeping()) {
            return false;
        } else {
            WrappedBlockEntityTickInvokerAccessor tickWrapper = this.getTickWrapper();
            if (tickWrapper != null) {
                this.setSleepingTicker(tickWrapper.getWrapped());
                tickWrapper.callSetWrapped(SleepingBlockEntity.SLEEPING_BLOCK_ENTITY_TICKER);
                this.tickedGameTime = Long.MAX_VALUE;
                return true;
            } else {
                return false;
            }
        }
    }

    @Inject(method = { "setTransferCooldown" }, at = { @At("HEAD") })
    private void wakeUpOnCooldownSet(int transferCooldown, CallbackInfo ci) {
        if (transferCooldown == 7) {
            if (this.tickedGameTime == Long.MAX_VALUE) {
                this.sleepOnlyCurrentTick();
            } else {
                this.wakeUpNow();
            }
        } else if (transferCooldown > 0 && this.sleepingTicker != null) {
            this.wakeUpNow();
        }
    }
}