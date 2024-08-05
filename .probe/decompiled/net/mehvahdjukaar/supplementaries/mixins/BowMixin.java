package net.mehvahdjukaar.supplementaries.mixins;

import java.util.function.Predicate;
import net.mehvahdjukaar.supplementaries.common.items.RopeArrowItem;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ BowItem.class })
public abstract class BowMixin {

    @Inject(method = { "getAllSupportedProjectiles" }, at = { @At("RETURN") }, cancellable = true)
    public void getAllSupportedProjectiles(CallbackInfoReturnable<Predicate<ItemStack>> cir) {
        if ((Boolean) CommonConfigs.Tools.ROPE_ARROW_CROSSBOW.get()) {
            Predicate<ItemStack> v = (Predicate<ItemStack>) cir.getReturnValue();
            cir.setReturnValue((Predicate) s -> s.getItem() instanceof RopeArrowItem ? false : v.test(s));
        }
    }
}