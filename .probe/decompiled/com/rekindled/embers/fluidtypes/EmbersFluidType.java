package com.rekindled.embers.fluidtypes;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.function.Consumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import org.joml.Vector3f;

public class EmbersFluidType extends FluidType {

    public final ResourceLocation RENDER_OVERLAY;

    public final ResourceLocation TEXTURE_STILL;

    public final ResourceLocation TEXTURE_FLOW;

    public final ResourceLocation TEXTURE_OVERLAY;

    public final Vector3f FOG_COLOR;

    public final float fogStart;

    public final float fogEnd;

    public EmbersFluidType(FluidType.Properties properties, EmbersFluidType.FluidInfo info) {
        super(properties);
        this.RENDER_OVERLAY = new ResourceLocation("embers", "textures/overlay/" + info.name + ".png");
        this.TEXTURE_STILL = new ResourceLocation("embers", "block/fluid/" + info.name + "_still");
        this.TEXTURE_FLOW = new ResourceLocation("embers", "block/fluid/" + info.name + "_flow");
        this.TEXTURE_OVERLAY = new ResourceLocation("embers", "block/fluid/" + info.name + "_overlay");
        Color colorObject = new Color(info.color);
        this.FOG_COLOR = new Vector3f((float) colorObject.getRed() / 255.0F, (float) colorObject.getGreen() / 255.0F, (float) colorObject.getBlue() / 255.0F);
        this.fogStart = info.fogStart;
        this.fogEnd = info.fogEnd;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {

            @Override
            public ResourceLocation getStillTexture() {
                return EmbersFluidType.this.TEXTURE_STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return EmbersFluidType.this.TEXTURE_FLOW;
            }

            @Override
            public ResourceLocation getOverlayTexture() {
                return EmbersFluidType.this.TEXTURE_OVERLAY;
            }

            @Override
            public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                return EmbersFluidType.this.RENDER_OVERLAY;
            }

            @Override
            public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                return EmbersFluidType.this.FOG_COLOR;
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
                RenderSystem.setShaderFogStart(EmbersFluidType.this.fogStart);
                RenderSystem.setShaderFogEnd(EmbersFluidType.this.fogEnd);
            }
        });
    }

    public static class FluidInfo {

        public String name;

        public int color;

        public float fogStart;

        public float fogEnd;

        public FluidInfo(String name, int color, float fogStart, float fogEnd) {
            this.name = name;
            this.color = color;
            this.fogStart = fogStart;
            this.fogEnd = fogEnd;
        }
    }
}