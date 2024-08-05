package net.mehvahdjukaar.supplementaries.integration;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.mehvahdjukaar.supplementaries.integration.forge.EnchantRedesignCompatImpl;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;

public class EnchantRedesignCompat {

    @ExpectPlatform
    @Transformed
    public static VertexConsumer getBookColoredFoil(ItemStack stack, MultiBufferSource buffer) {
        return EnchantRedesignCompatImpl.getBookColoredFoil(stack, buffer);
    }
}