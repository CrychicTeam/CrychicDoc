package com.mna.api.guidebook;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IGuideBookRegistry {

    @OnlyIn(Dist.CLIENT)
    void addGuidebookPath(ResourceLocation var1);

    @OnlyIn(Dist.CLIENT)
    void registerRecipeRenderer(String var1, Class<? extends RecipeRendererBase> var2);

    @OnlyIn(Dist.CLIENT)
    void registerGuidebookCategory(String var1, ResourceLocation var2);
}