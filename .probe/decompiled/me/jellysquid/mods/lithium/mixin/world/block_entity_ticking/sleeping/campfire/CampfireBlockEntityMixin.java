package me.jellysquid.mods.lithium.mixin.world.block_entity_ticking.sleeping.campfire;

import me.jellysquid.mods.lithium.common.block.entity.SleepingBlockEntity;
import me.jellysquid.mods.lithium.mixin.world.block_entity_ticking.sleeping.WrappedBlockEntityTickInvokerAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ CampfireBlockEntity.class })
public class CampfireBlockEntityMixin extends BlockEntity implements SleepingBlockEntity {

    private WrappedBlockEntityTickInvokerAccessor tickWrapper = null;

    private TickingBlockEntity sleepingTicker = null;

    public CampfireBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
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

    @Inject(method = { "addItem" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;set(ILjava/lang/Object;)Ljava/lang/Object;") })
    private void wakeUpOnAddItem(Entity user, ItemStack stack, int cookTime, CallbackInfoReturnable<Boolean> cir) {
        this.wakeUpNow();
    }

    @Inject(method = { "readNbt" }, at = { @At("RETURN") })
    private void wakeUpOnReadNbt(CompoundTag nbt, CallbackInfo ci) {
        this.wakeUpNow();
    }
}