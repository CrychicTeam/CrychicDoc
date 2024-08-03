package net.minecraftforge.client.model.renderable;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface ITextureRenderTypeLookup {

    RenderType get(ResourceLocation var1);
}