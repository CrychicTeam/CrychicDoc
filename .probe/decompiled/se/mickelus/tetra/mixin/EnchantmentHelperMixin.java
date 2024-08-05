package se.mickelus.tetra.mixin;

import java.util.Map;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import se.mickelus.tetra.aspect.TetraEnchantmentHelper;
import se.mickelus.tetra.items.modular.IModularItem;

@ParametersAreNonnullByDefault
@Mixin({ EnchantmentHelper.class })
public class EnchantmentHelperMixin {

    @Inject(at = { @At("RETURN") }, method = { "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;setEnchantments(Ljava/util/Map;Lnet/minecraft/world/item/ItemStack;)V" })
    private static void setEnchantments(Map<Enchantment, Integer> enchantments, ItemStack itemStack, CallbackInfo ci) {
        if (itemStack.getItem() instanceof IModularItem item) {
            TetraEnchantmentHelper.mapEnchantments(itemStack);
            item.assemble(itemStack, null, 0.0F);
        }
    }
}