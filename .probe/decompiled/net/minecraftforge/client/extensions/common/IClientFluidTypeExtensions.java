package net.minecraftforge.client.extensions.common;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public interface IClientFluidTypeExtensions {

    IClientFluidTypeExtensions DEFAULT = new IClientFluidTypeExtensions() {
    };

    static IClientFluidTypeExtensions of(FluidState state) {
        return of(state.getFluidType());
    }

    static IClientFluidTypeExtensions of(Fluid fluid) {
        return of(fluid.getFluidType());
    }

    static IClientFluidTypeExtensions of(FluidType type) {
        return type.getRenderPropertiesInternal() instanceof IClientFluidTypeExtensions props ? props : DEFAULT;
    }

    default int getTintColor() {
        return -1;
    }

    default ResourceLocation getStillTexture() {
        return null;
    }

    default ResourceLocation getFlowingTexture() {
        return null;
    }

    @Nullable
    default ResourceLocation getOverlayTexture() {
        return null;
    }

    @Nullable
    default ResourceLocation getRenderOverlayTexture(Minecraft mc) {
        return null;
    }

    default void renderOverlay(Minecraft mc, PoseStack poseStack) {
        ResourceLocation texture = this.getRenderOverlayTexture(mc);
        if (texture != null) {
            ScreenEffectRenderer.renderFluid(mc, poseStack, texture);
        }
    }

    @NotNull
    default Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
        return fluidFogColor;
    }

    default void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
    }

    default ResourceLocation getStillTexture(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return this.getStillTexture();
    }

    default ResourceLocation getFlowingTexture(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return this.getFlowingTexture();
    }

    default ResourceLocation getOverlayTexture(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return this.getOverlayTexture();
    }

    default int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return this.getTintColor();
    }

    default int getTintColor(FluidStack stack) {
        return this.getTintColor();
    }

    default ResourceLocation getStillTexture(FluidStack stack) {
        return this.getStillTexture();
    }

    default ResourceLocation getFlowingTexture(FluidStack stack) {
        return this.getFlowingTexture();
    }

    default ResourceLocation getOverlayTexture(FluidStack stack) {
        return this.getOverlayTexture();
    }
}