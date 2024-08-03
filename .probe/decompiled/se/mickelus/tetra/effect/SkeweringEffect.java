package se.mickelus.tetra.effect;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

@ParametersAreNonnullByDefault
public class SkeweringEffect {

    public static void onLivingDamage(LivingDamageEvent event, int skeweringLevel, ItemStack itemStack) {
        if ((float) event.getEntity().getArmorValue() <= EffectHelper.getEffectEfficiency(itemStack, ItemEffect.skewering)) {
            event.setAmount(event.getAmount() + (float) skeweringLevel);
        }
    }
}