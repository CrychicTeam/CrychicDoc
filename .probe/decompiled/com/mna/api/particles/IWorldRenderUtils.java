package com.mna.api.particles;

import com.mna.recipes.manaweaving.ManaweavingPattern;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public interface IWorldRenderUtils {

    void radiant(float var1, PoseStack var2, MultiBufferSource var3, int[] var4, int[] var5, int var6, float var7, boolean var8);

    void radiant(Entity var1, PoseStack var2, MultiBufferSource var3, int[] var4, int[] var5, int var6, float var7);

    void directionalRadiant(Entity var1, PoseStack var2, MultiBufferSource var3, int[] var4, int[] var5, int var6, float var7);

    void beam(Level var1, float var2, PoseStack var3, MultiBufferSource var4, int var5, Vec3 var6, Vec3 var7, float var8, int[] var9, RenderType var10);

    void beam(Level var1, float var2, PoseStack var3, MultiBufferSource var4, int var5, Vec3 var6, Vec3 var7, float var8, int[] var9, float var10, RenderType var11);

    void beam(Level var1, float var2, PoseStack var3, MultiBufferSource var4, int var5, Vec3 var6, Vec3 var7, float var8, int[] var9, int var10, float var11, RenderType var12);

    void manaweavePatternNoTransparent(ManaweavingPattern var1, Quaternionf var2, PoseStack var3, MultiBufferSource var4, boolean var5);

    void manaweavePatternNoTransparent(ManaweavingPattern var1, Quaternionf var2, PoseStack var3, MultiBufferSource var4, boolean var5, int[] var6);

    void manaweavePattern(ManaweavingPattern var1, Quaternionf var2, PoseStack var3, MultiBufferSource var4, boolean var5);

    void manaweavePattern(ManaweavingPattern var1, Quaternionf var2, PoseStack var3, MultiBufferSource var4, boolean var5, int[] var6);
}