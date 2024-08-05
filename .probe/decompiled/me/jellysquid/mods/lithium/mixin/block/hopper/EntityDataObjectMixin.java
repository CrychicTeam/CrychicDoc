package me.jellysquid.mods.lithium.mixin.block.hopper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.commands.data.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ EntityDataAccessor.class })
public class EntityDataObjectMixin {

    @Shadow
    @Final
    private Entity entity;

    @Inject(method = { "setNbt(Lnet/minecraft/nbt/NbtCompound;)V" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setUuid(Ljava/util/UUID;)V", shift = Shift.AFTER) })
    private void updateEntityTrackerEngine(CompoundTag nbt, CallbackInfo ci) {
        Entity entity = this.entity;
        if (entity instanceof ItemEntity) {
            ((EntityAccessor) entity).getChangeListener().onMove();
        }
    }
}