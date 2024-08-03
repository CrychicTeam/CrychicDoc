package dev.xkmc.modulargolems.content.entity.common;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;

public record RenderHandle<T>(T entity, float f1, float f2, PoseStack stack, MultiBufferSource source, int i) {
}