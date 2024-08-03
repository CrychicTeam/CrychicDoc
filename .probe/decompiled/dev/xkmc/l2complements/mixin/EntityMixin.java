package dev.xkmc.l2complements.mixin;

import dev.xkmc.l2complements.events.SpecialEquipmentEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ Entity.class })
public abstract class EntityMixin {

    @Inject(at = { @At("HEAD") }, method = { "dampensVibrations" }, cancellable = true)
    public void l2complements_dampensVibrations_delegate(CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) this;
        if (entity instanceof LivingEntity self) {
            int count = 0;
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack stack = self.getItemBySlot(slot);
                count += SpecialEquipmentEvents.blockSound(stack);
            }
            if (count > 0) {
                cir.setReturnValue(true);
            }
        }
    }
}