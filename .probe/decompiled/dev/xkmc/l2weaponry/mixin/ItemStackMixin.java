package dev.xkmc.l2weaponry.mixin;

import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.l2weaponry.content.item.base.LWTieredItem;
import dev.xkmc.l2weaponry.content.item.base.TooltipProcessor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ ItemStack.class })
public class ItemStackMixin {

    @WrapOperation(at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getAttributeModifiers(Lnet/minecraft/world/entity/EquipmentSlot;)Lcom/google/common/collect/Multimap;") }, method = { "getTooltipLines" })
    public Multimap<Attribute, AttributeModifier> l2weaponry$simplifyTooltips(ItemStack stack, EquipmentSlot slot, Operation<Multimap<Attribute, AttributeModifier>> prev) {
        Multimap<Attribute, AttributeModifier> ans = (Multimap<Attribute, AttributeModifier>) prev.call(new Object[] { stack, slot });
        if (stack.getItem() instanceof LWTieredItem) {
            ans = TooltipProcessor.processTooltip(ans);
        }
        return ans;
    }
}