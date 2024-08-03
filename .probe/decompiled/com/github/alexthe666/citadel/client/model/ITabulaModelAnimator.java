package com.github.alexthe666.citadel.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@FunctionalInterface
public interface ITabulaModelAnimator<T extends Entity> {

    void setRotationAngles(TabulaModel var1, T var2, float var3, float var4, float var5, float var6, float var7, float var8);
}