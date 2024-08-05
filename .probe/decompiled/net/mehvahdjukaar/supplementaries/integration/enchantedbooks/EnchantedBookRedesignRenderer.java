package net.mehvahdjukaar.supplementaries.integration.enchantedbooks;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class EnchantedBookRedesignRenderer extends RenderStateShard {

    public EnchantedBookRedesignRenderer(String p_i225973_1_, Runnable p_i225973_2_, Runnable p_i225973_3_) {
        super(p_i225973_1_, p_i225973_2_, p_i225973_3_);
    }

    @Nullable
    public static VertexConsumer getColoredFoil(ItemStack stack, MultiBufferSource buffer) {
        return null;
    }
}