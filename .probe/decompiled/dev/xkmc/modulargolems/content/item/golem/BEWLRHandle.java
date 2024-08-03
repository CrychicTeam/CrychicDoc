package dev.xkmc.modulargolems.content.item.golem;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public record BEWLRHandle(ItemStack stack, ItemDisplayContext type, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
}