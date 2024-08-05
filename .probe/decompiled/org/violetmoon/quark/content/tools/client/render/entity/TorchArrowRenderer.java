package org.violetmoon.quark.content.tools.client.render.entity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.violetmoon.quark.content.tools.entity.TorchArrow;

public class TorchArrowRenderer extends ArrowRenderer<TorchArrow> {

    public static final ResourceLocation TORCH_ARROW_LOCATION = new ResourceLocation("quark", "textures/model/entity/torch_arrow.png");

    public TorchArrowRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0);
    }

    public ResourceLocation getTextureLocation(TorchArrow torchArrow0) {
        return new ResourceLocation("quark", "textures/model/entity/torch_arrow.png");
    }
}