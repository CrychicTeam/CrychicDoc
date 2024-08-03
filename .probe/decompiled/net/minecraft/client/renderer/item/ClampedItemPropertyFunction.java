package net.minecraft.client.renderer.item;

import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface ClampedItemPropertyFunction extends ItemPropertyFunction {

    @Deprecated
    @Override
    default float call(ItemStack itemStack0, @Nullable ClientLevel clientLevel1, @Nullable LivingEntity livingEntity2, int int3) {
        return Mth.clamp(this.unclampedCall(itemStack0, clientLevel1, livingEntity2, int3), 0.0F, 1.0F);
    }

    float unclampedCall(ItemStack var1, @Nullable ClientLevel var2, @Nullable LivingEntity var3, int var4);
}