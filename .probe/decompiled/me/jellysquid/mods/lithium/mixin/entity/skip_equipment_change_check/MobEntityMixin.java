package me.jellysquid.mods.lithium.mixin.entity.skip_equipment_change_check;

import me.jellysquid.mods.lithium.common.entity.EquipmentEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Mob.class })
public class MobEntityMixin implements EquipmentEntity.EquipmentTrackingEntity, EquipmentEntity {

    @Inject(method = { "readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V" }, at = { @At("RETURN") })
    private void trackEquipChange(CompoundTag nbt, CallbackInfo ci) {
        this.lithiumOnEquipmentChanged();
    }

    @Inject(method = { "equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V" }, at = { @At("RETURN") })
    private void trackEquipChange(EquipmentSlot slot, ItemStack stack, CallbackInfo ci) {
        this.lithiumOnEquipmentChanged();
    }
}