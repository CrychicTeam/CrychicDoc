package org.violetmoon.quark.mixin.mixins;

import java.util.List;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.violetmoon.quark.content.experimental.module.EnchantmentsBegoneModule;

@Mixin({ VillagerTrades.EnchantBookForEmeralds.class })
public class EnchantBookForEmeraldsMixin {

    @ModifyVariable(method = { "getOffer" }, at = @At("STORE"))
    private List<Enchantment> filterBegoneFromTrades(List<Enchantment> enchantments) {
        return EnchantmentsBegoneModule.begoneEnchantments(enchantments);
    }
}