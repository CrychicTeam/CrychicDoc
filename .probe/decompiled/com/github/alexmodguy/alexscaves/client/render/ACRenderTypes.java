package com.github.alexmodguy.alexscaves.client.render;

import com.github.alexmodguy.alexscaves.client.ClientProxy;
import com.github.alexthe666.citadel.client.shader.PostEffectRegistry;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ForgeRenderTypes;

public class ACRenderTypes extends RenderType {

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_FEROUSSLIME_GEL_SHADER = new RenderStateShard.ShaderStateShard(ACInternalShaders::getRenderTypeFerrouslimeGelShader);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_HOLOGRAM_SHADER = new RenderStateShard.ShaderStateShard(ACInternalShaders::getRenderTypeHologramShader);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_IRRADIATED_SHADER = new RenderStateShard.ShaderStateShard(ACInternalShaders::getRenderTypeIrradiatedShader);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_BLUE_IRRADIATED_SHADER = new RenderStateShard.ShaderStateShard(ACInternalShaders::getRenderTypeBlueIrradiatedShader);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_BUBBLED_SHADER = new RenderStateShard.ShaderStateShard(ACInternalShaders::getRenderTypeBubbledShader);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_SEPIA_SHADER = new RenderStateShard.ShaderStateShard(ACInternalShaders::getRenderTypeSepiaShader);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_RED_GHOST_SHADER = new RenderStateShard.ShaderStateShard(ACInternalShaders::getRenderTypeRedGhostShader);

    protected static final RenderStateShard.OutputStateShard IRRADIATED_OUTPUT = new RenderStateShard.OutputStateShard("irradiated_target", () -> {
        RenderTarget target = PostEffectRegistry.getRenderTargetFor(ClientProxy.IRRADIATED_SHADER);
        if (target != null) {
            target.copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
            target.bindWrite(false);
        }
    }, () -> Minecraft.getInstance().getMainRenderTarget().bindWrite(false));

    protected static final RenderStateShard.OutputStateShard HOLOGRAM_OUTPUT = new RenderStateShard.OutputStateShard("hologram_target", () -> {
        RenderTarget target = PostEffectRegistry.getRenderTargetFor(ClientProxy.HOLOGRAM_SHADER);
        if (target != null) {
            target.copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
            target.bindWrite(false);
        }
    }, () -> Minecraft.getInstance().getMainRenderTarget().bindWrite(false));

    protected static final RenderStateShard.TransparencyStateShard EYES_ALPHA_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("eyes_alpha_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    public ACRenderTypes(String s, VertexFormat format, VertexFormat.Mode mode, int i, boolean b1, boolean b2, Runnable runnable1, Runnable runnable2) {
        super(s, format, mode, i, b1, b2, runnable1, runnable2);
    }

    public static RenderType getParticleTrail(ResourceLocation resourceLocation) {
        return m_173215_("particle_trail", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_ENERGY_SWIRL_SHADER).setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, true, true)).setLightmapState(f_110152_).setCullState(RenderStateShard.NO_CULL).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setOverlayState(f_110154_).setDepthTestState(f_110113_).createCompositeState(true));
    }

    public static RenderType getVoidBeingCloud(ResourceLocation resourceLocation) {
        return m_173215_("void_being", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_ENERGY_SWIRL_SHADER).setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, true)).setLightmapState(f_110152_).setCullState(RenderStateShard.NO_CULL).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setOverlayState(f_110154_).setDepthTestState(f_110113_).createCompositeState(true));
    }

    public static RenderType getEyesAlphaEnabled(ResourceLocation locationIn) {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder().setShaderState(f_173073_).setTextureState(new RenderStateShard.TextureStateShard(locationIn, false, false)).setTransparencyState(EYES_ALPHA_TRANSPARENCY).setCullState(f_110110_).setLightmapState(f_110152_).setOverlayState(f_110154_).setDepthTestState(f_110112_).createCompositeState(true);
        return m_173215_("eye_alpha", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, rendertype$compositestate);
    }

    public static RenderType getAmbersolShine() {
        return m_173215_("ambersol_shine", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder().setShaderState(RenderType.f_173091_).setTransparencyState(EYES_ALPHA_TRANSPARENCY).setCullState(f_110158_).setLightmapState(f_110153_).setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING).setOutputState(RenderStateShard.PARTICLES_TARGET).createCompositeState(true));
    }

    public static RenderType getNucleeperLights() {
        return m_173215_("nucleeper_lights", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder().setShaderState(RenderType.f_173091_).setTransparencyState(EYES_ALPHA_TRANSPARENCY).setCullState(f_110158_).setLightmapState(f_110153_).setOutputState(RenderStateShard.ITEM_ENTITY_TARGET).createCompositeState(true));
    }

    public static RenderType getHologramLights() {
        return m_173215_("hologram_lights", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_HOLOGRAM_SHADER).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setCullState(f_110158_).setDepthTestState(f_110113_).setLightmapState(f_110153_).setOutputState(HOLOGRAM_OUTPUT).createCompositeState(false));
    }

    public static RenderType getSubmarineLights() {
        return m_173215_("submarine_lights", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder().setShaderState(f_173091_).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setCullState(f_110158_).setDepthTestState(f_110113_).setLightmapState(f_110153_).setOutputState(RenderStateShard.ITEM_ENTITY_TARGET).createCompositeState(false));
    }

    public static RenderType getGel(ResourceLocation locationIn) {
        return m_173215_("ferrouslime_gel", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(locationIn, false, false)).setCullState(f_110110_).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setShaderState(RENDERTYPE_FEROUSSLIME_GEL_SHADER).setLightmapState(f_110152_).createCompositeState(true));
    }

    public static RenderType getRadiationGlow(ResourceLocation locationIn) {
        return m_173215_("radiation_glow", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_IRRADIATED_SHADER).setCullState(f_110110_).setTextureState(new RenderStateShard.TextureStateShard(locationIn, false, false)).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setDepthTestState(f_110113_).setOutputState(IRRADIATED_OUTPUT).createCompositeState(false));
    }

    public static RenderType getBlueRadiationGlow(ResourceLocation locationIn) {
        return m_173215_("blue_radiation_glow", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_BLUE_IRRADIATED_SHADER).setCullState(f_110110_).setTextureState(new RenderStateShard.TextureStateShard(locationIn, false, false)).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setDepthTestState(f_110113_).setOutputState(IRRADIATED_OUTPUT).createCompositeState(false));
    }

    public static RenderType getGelTriangles(ResourceLocation locationIn) {
        return m_173215_("ferrouslime_gel_triangles", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES, 256, true, true, RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(locationIn, false, false)).setCullState(f_110110_).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setShaderState(RENDERTYPE_FEROUSSLIME_GEL_SHADER).setLightmapState(f_110152_).createCompositeState(false));
    }

    public static RenderType getSubmarineMask() {
        return m_173215_("submarine_mask", DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder().setShaderState(f_173076_).setTextureState(f_110147_).setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST).setWriteMaskState(f_110116_).setCullState(f_110110_).createCompositeState(false));
    }

    public static RenderType getGhostly(ResourceLocation texture) {
        RenderType.CompositeState renderState = RenderType.CompositeState.builder().setShaderState(f_234323_).setCullState(f_110110_).setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).setTransparencyState(f_110139_).setLightmapState(f_110152_).setOverlayState(f_110154_).setWriteMaskState(f_110114_).setDepthTestState(f_110113_).createCompositeState(true);
        return m_173215_("ghostly", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, renderState);
    }

    public static RenderType getTeslaBulb(ResourceLocation resourceLocation) {
        return m_173215_("tesla_bulb", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_ENERGY_SWIRL_SHADER).setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, true)).setLightmapState(f_110152_).setCullState(RenderStateShard.NO_CULL).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setDepthTestState(f_110113_).createCompositeState(true));
    }

    public static RenderType getHologram(ResourceLocation locationIn) {
        return m_173215_("hologram", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173068_).setCullState(f_110110_).setTextureState(new RenderStateShard.TextureStateShard(locationIn, false, false)).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setWriteMaskState(f_110114_).setDepthTestState(f_110113_).setOutputState(HOLOGRAM_OUTPUT).createCompositeState(false));
    }

    public static RenderType getRedGhost(ResourceLocation locationIn) {
        return m_173215_("red_ghost", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_RED_GHOST_SHADER).setCullState(f_110110_).setTextureState(new RenderStateShard.TextureStateShard(locationIn, false, false)).setTransparencyState(EYES_ALPHA_TRANSPARENCY).setWriteMaskState(f_110114_).setDepthTestState(f_110113_).setOverlayState(f_110154_).createCompositeState(true));
    }

    public static RenderType getCaveMapBackground(ResourceLocation locationIn, boolean showBackground) {
        RenderType.CompositeState rendertype$state = RenderType.CompositeState.builder().setShaderState(f_173086_).setTextureState(new RenderStateShard.TextureStateShard(locationIn, false, false)).setTransparencyState(f_110139_).setLightmapState(f_110152_).setCullState(showBackground ? f_110110_ : f_110158_).createCompositeState(false);
        return m_173215_("cave_map_background", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true, rendertype$state);
    }

    public static RenderType getBookWidget(ResourceLocation locationIn, boolean sepia) {
        return (RenderType) (sepia ? m_173215_("book_widget", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_SEPIA_SHADER).setCullState(f_110110_).setTextureState(new RenderStateShard.TextureStateShard(locationIn, false, false)).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setOverlayState(f_110154_).createCompositeState(true)) : ForgeRenderTypes.getUnlitTranslucent(locationIn));
    }

    public static RenderType getBubbledCull(ResourceLocation locationIn) {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_BUBBLED_SHADER).setTextureState(new RenderStateShard.TextureStateShard(locationIn, false, false)).setTransparencyState(f_110139_).setLightmapState(f_110152_).setOutputState(RenderStateShard.ITEM_ENTITY_TARGET).setOverlayState(f_110154_).setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE).createCompositeState(true);
        return m_173215_("bubbled_cull", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, rendertype$compositestate);
    }

    public static RenderType getBubbledNoCull(ResourceLocation locationIn) {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_BUBBLED_SHADER).setTextureState(new RenderStateShard.TextureStateShard(locationIn, false, false)).setTransparencyState(f_110139_).setCullState(f_110110_).setLightmapState(f_110152_).setOutputState(RenderStateShard.ITEM_ENTITY_TARGET).setOverlayState(f_110154_).setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE).createCompositeState(true);
        return m_173215_("bubbled_no_cull", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, false, rendertype$compositestate);
    }

    public static RenderType getRaygunRay(ResourceLocation locationIn, boolean irradiated) {
        return m_173215_("raygun_ray", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES, 256, true, true, RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(locationIn, false, false)).setShaderState(RenderType.f_173074_).setTransparencyState(f_110139_).setCullState(f_110110_).setLightmapState(f_110152_).setOutputState(irradiated ? IRRADIATED_OUTPUT : f_110129_).createCompositeState(false));
    }

    public static RenderType getTremorzillaBeam(ResourceLocation locationIn, boolean irradiated) {
        return m_173215_("tremorzilla_beam", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(locationIn, false, false)).setShaderState(RenderType.f_173074_).setTransparencyState(f_110139_).setCullState(f_110110_).setLightmapState(f_110152_).setOutputState(irradiated ? IRRADIATED_OUTPUT : f_110129_).createCompositeState(false));
    }
}