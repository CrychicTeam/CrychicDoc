package fuzs.puzzleslib.api.event.v1.entity.living;

import com.google.common.collect.Multimap;
import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface ItemAttributeModifiersCallback {

    EventInvoker<ItemAttributeModifiersCallback> EVENT = EventInvoker.lookup(ItemAttributeModifiersCallback.class);

    void onItemAttributeModifiers(ItemStack var1, EquipmentSlot var2, Multimap<Attribute, AttributeModifier> var3);
}