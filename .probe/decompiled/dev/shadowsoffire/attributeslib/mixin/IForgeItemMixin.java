package dev.shadowsoffire.attributeslib.mixin;

import dev.shadowsoffire.attributeslib.api.ALObjects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin({ IForgeItem.class })
public interface IForgeItemMixin {

    @Overwrite(remap = false)
    default boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return entity.getAttributeValue(ALObjects.Attributes.ELYTRA_FLIGHT.get()) > 0.0;
    }

    @Overwrite(remap = false)
    default boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        return entity.getAttributeValue(ALObjects.Attributes.ELYTRA_FLIGHT.get()) > 0.0;
    }
}