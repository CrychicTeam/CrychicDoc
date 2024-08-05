package team.lodestar.lodestone.mixin.common;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import java.util.Map.Entry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import team.lodestar.lodestone.registry.common.LodestoneAttributeRegistry;

@Mixin({ ItemStack.class })
public class ItemStackMixin {

    @Unique
    private AttributeModifier lodestone$attributeModifier;

    @ModifyVariable(method = { "getTooltipLines" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;getId()Ljava/util/UUID;", ordinal = 0), index = 13)
    private AttributeModifier lodestone$getTooltip(AttributeModifier value) {
        this.lodestone$attributeModifier = value;
        return value;
    }

    @ModifyVariable(method = { "getTooltipLines" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;getOperation()Lnet/minecraft/world/entity/ai/attributes/AttributeModifier$Operation;", ordinal = 0), index = 16)
    private boolean lodestone$getTooltip(boolean value, @Nullable Player player, TooltipFlag flag) {
        return player != null && this.lodestone$attributeModifier.getId().equals(LodestoneAttributeRegistry.UUIDS.get(LodestoneAttributeRegistry.MAGIC_DAMAGE)) ? true : value;
    }

    @ModifyVariable(method = { "getTooltipLines" }, at = @At("STORE"))
    private Multimap<Attribute, AttributeModifier> lodestone$getTooltip(Multimap<Attribute, AttributeModifier> map, @Nullable Player player, TooltipFlag flag) {
        if (player != null) {
            Multimap<Attribute, AttributeModifier> copied = LinkedHashMultimap.create();
            for (Entry<Attribute, AttributeModifier> entry : map.entries()) {
                Attribute key = (Attribute) entry.getKey();
                AttributeModifier modifier = (AttributeModifier) entry.getValue();
                double amount = modifier.getAmount();
                if (modifier.getId().equals(LodestoneAttributeRegistry.UUIDS.get(LodestoneAttributeRegistry.MAGIC_DAMAGE))) {
                    AttributeInstance instance = player.m_21051_(LodestoneAttributeRegistry.MAGIC_PROFICIENCY.get());
                    if (instance != null && instance.getValue() > 0.0) {
                        amount *= 1.0 + instance.getValue() * 0.1F;
                    }
                    copied.put(key, new AttributeModifier(modifier.getId(), modifier.getName(), amount, modifier.getOperation()));
                } else {
                    copied.put(key, modifier);
                }
            }
            return copied;
        } else {
            return map;
        }
    }
}