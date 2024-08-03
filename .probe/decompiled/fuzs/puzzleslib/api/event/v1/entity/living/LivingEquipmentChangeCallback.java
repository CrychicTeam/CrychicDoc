package fuzs.puzzleslib.api.event.v1.entity.living;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface LivingEquipmentChangeCallback {

    EventInvoker<LivingEquipmentChangeCallback> EVENT = EventInvoker.lookup(LivingEquipmentChangeCallback.class);

    void onLivingEquipmentChange(LivingEntity var1, EquipmentSlot var2, ItemStack var3, ItemStack var4);
}