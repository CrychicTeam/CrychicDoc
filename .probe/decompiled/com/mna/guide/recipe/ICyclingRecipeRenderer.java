package com.mna.guide.recipe;

import com.mna.api.guidebook.RecipeRendererBase;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface ICyclingRecipeRenderer<T extends RecipeRendererBase> {

    void init_cycling(ResourceLocation[] var1);

    int countRecipes();

    default int getIndex() {
        return ((RecipeRendererBase) this).lockIndex == -1 ? (int) (Minecraft.getInstance().level.m_46467_() / 20L) % Math.max(this.countRecipes(), 1) : ((RecipeRendererBase) this).lockIndex;
    }
}