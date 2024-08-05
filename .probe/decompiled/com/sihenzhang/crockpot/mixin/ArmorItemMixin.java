package com.sihenzhang.crockpot.mixin;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ ArmorItem.class })
public abstract class ArmorItemMixin {

    @Inject(method = { "dispenseArmor(Lnet/minecraft/core/BlockSource;Lnet/minecraft/world/item/ItemStack;)Z" }, at = { @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/Mob;getEquipmentSlotForItem(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/EquipmentSlot;", ordinal = 0) }, cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void dispenseArmorHandler(BlockSource blockSource, ItemStack stack, CallbackInfoReturnable<Boolean> cir, BlockPos pos, List<LivingEntity> livingEntities, LivingEntity livingEntity, EquipmentSlot equipmentSlot) {
        if (!stack.canEquip(equipmentSlot, livingEntity)) {
            cir.setReturnValue(false);
        }
    }
}