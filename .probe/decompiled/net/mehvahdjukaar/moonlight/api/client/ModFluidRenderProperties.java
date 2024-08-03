package net.mehvahdjukaar.moonlight.api.client;

import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class ModFluidRenderProperties {

    private final ResourceLocation flowing;

    private final ResourceLocation still;

    private final int tint;

    public ModFluidRenderProperties(ResourceLocation still, ResourceLocation flowing, int tint) {
        this.still = still;
        this.flowing = flowing;
        this.tint = tint;
        this.afterInit();
    }

    private void afterInit() {
    }

    public ModFluidRenderProperties(ResourceLocation still, ResourceLocation flowing) {
        this(still, flowing, -1);
    }

    public int getTintColor() {
        return this.tint;
    }

    @NotNull
    public ResourceLocation getStillTexture() {
        return this.still;
    }

    @NotNull
    public ResourceLocation getFlowingTexture() {
        return this.flowing;
    }

    @Nullable
    public ResourceLocation getOverlayTexture() {
        return null;
    }

    public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
    }

    @NotNull
    public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
        return fluidFogColor;
    }

    @Nullable
    public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
        return null;
    }

    public ResourceLocation getStillTexture(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return this.getStillTexture();
    }

    public ResourceLocation getFlowingTexture(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return this.getFlowingTexture();
    }

    public ResourceLocation getOverlayTexture(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return this.getOverlayTexture();
    }

    public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return this.getTintColor();
    }
}