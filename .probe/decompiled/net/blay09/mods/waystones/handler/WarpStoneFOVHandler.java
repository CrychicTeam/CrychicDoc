package net.blay09.mods.waystones.handler;

import net.blay09.mods.balm.api.event.client.FovUpdateEvent;
import net.blay09.mods.waystones.api.IFOVOnUse;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class WarpStoneFOVHandler {

    public static void onFOV(FovUpdateEvent event) {
        LivingEntity entity = event.getEntity();
        ItemStack activeItemStack = entity.getUseItem();
        if (isScrollItem(activeItemStack)) {
            float fov = (float) entity.getUseItemRemainingTicks() / 32.0F * 2.0F;
            event.setFov((float) Mth.lerp(Minecraft.getInstance().options.fovEffectScale().get(), 1.0, (double) fov));
        }
    }

    private static boolean isScrollItem(ItemStack activeItemStack) {
        return !activeItemStack.isEmpty() && activeItemStack.getItem() instanceof IFOVOnUse;
    }
}