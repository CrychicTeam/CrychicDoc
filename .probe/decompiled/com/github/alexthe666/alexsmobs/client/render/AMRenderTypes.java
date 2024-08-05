package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class AMRenderTypes extends RenderType {

    public static final ResourceLocation STATIC_TEXTURE = new ResourceLocation("alexsmobs:textures/static.png");

    private static boolean encounteredMultiConsumerError = false;

    protected static final RenderStateShard.TexturingStateShard RAINBOW_TEXTURING = new RenderStateShard.TexturingStateShard("entity_glint_texturing", () -> setupRainbowTexturing(1.2F, 4L), () -> RenderSystem.resetTextureMatrix());

    protected static final RenderStateShard.TexturingStateShard COMB_JELLY_TEXTURING = new RenderStateShard.TexturingStateShard("entity_glint_texturing", () -> setupRainbowTexturing(2.0F, 16L), () -> RenderSystem.resetTextureMatrix());

    protected static final RenderStateShard.TexturingStateShard RAINBOW_TEXTURING_LARGE = new RenderStateShard.TexturingStateShard("entity_glint_texturing", () -> setupRainbowTexturing2(5.0F, 14L), () -> RenderSystem.resetTextureMatrix());

    protected static final RenderStateShard.TexturingStateShard WEEZER_TEXTURING = new RenderStateShard.TexturingStateShard("entity_glint_texturing", () -> setupRainbowTexturing2(7.0F, 16L), () -> RenderSystem.resetTextureMatrix());

    protected static final RenderStateShard.TexturingStateShard STATIC_PORTAL_TEXTURING = new RenderStateShard.TexturingStateShard("entity_glint_texturing", () -> setupStaticTexturing(1.1F, 12L), () -> RenderSystem.resetTextureMatrix());

    protected static final RenderStateShard.TexturingStateShard STATIC_PARTICLE_TEXTURING = new RenderStateShard.TexturingStateShard("entity_glint_texturing", () -> setupStaticTexturing(0.1F, 12L), () -> RenderSystem.resetTextureMatrix());

    protected static final RenderStateShard.TexturingStateShard STATIC_ENTITY_TEXTURING = new RenderStateShard.TexturingStateShard("entity_glint_texturing", () -> setupStaticTexturing(3.0F, 12L), () -> RenderSystem.resetTextureMatrix());

    public static final RenderType COMBJELLY_RAINBOW_GLINT = m_173215_("cj_rainbow_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(f_173083_).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation("alexsmobs:textures/entity/rainbow_jelly_overlays/glint_rainbow.png"), true, false)).setWriteMaskState(f_110114_).setCullState(f_110110_).setDepthTestState(f_110112_).setTransparencyState(f_110134_).setTexturingState(COMB_JELLY_TEXTURING).createCompositeState(false));

    public static final RenderType RAINBOW_GLINT = m_173215_("rainbow_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder().setShaderState(f_173083_).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation("alexsmobs:textures/entity/rainbow_jelly_overlays/glint_rainbow.png"), true, false)).setWriteMaskState(f_110114_).setCullState(f_110110_).setDepthTestState(f_110112_).setTransparencyState(f_110137_).setTexturingState(RAINBOW_TEXTURING).setOverlayState(f_110154_).createCompositeState(true));

    public static final RenderType TRANS_GLINT = m_173215_("trans_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(f_173083_).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation("alexsmobs:textures/entity/rainbow_jelly_overlays/glint_trans.png"), true, false)).setWriteMaskState(f_110114_).setCullState(f_110110_).setDepthTestState(f_110112_).setTransparencyState(f_110137_).setTexturingState(RAINBOW_TEXTURING).setOverlayState(f_110154_).createCompositeState(true));

    public static final RenderType NONBI_GLINT = m_173215_("nonbi_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(f_173083_).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation("alexsmobs:textures/entity/rainbow_jelly_overlays/glint_nonbi.png"), true, false)).setWriteMaskState(f_110114_).setCullState(f_110110_).setDepthTestState(f_110112_).setTransparencyState(f_110137_).setTexturingState(RAINBOW_TEXTURING).setOverlayState(f_110154_).createCompositeState(true));

    public static final RenderType BI_GLINT = m_173215_("bi_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(f_173083_).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation("alexsmobs:textures/entity/rainbow_jelly_overlays/glint_bi.png"), true, false)).setWriteMaskState(f_110114_).setCullState(f_110110_).setDepthTestState(f_110112_).setTransparencyState(f_110137_).setTexturingState(RAINBOW_TEXTURING).setOverlayState(f_110154_).createCompositeState(true));

    public static final RenderType ACE_GLINT = m_173215_("ace_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(f_173083_).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation("alexsmobs:textures/entity/rainbow_jelly_overlays/glint_ace.png"), true, false)).setWriteMaskState(f_110114_).setCullState(f_110110_).setDepthTestState(f_110112_).setTransparencyState(f_110137_).setTexturingState(RAINBOW_TEXTURING).setOverlayState(f_110154_).createCompositeState(true));

    public static final RenderType BRAZIL_GLINT = m_173215_("brazil_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(f_173083_).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation("alexsmobs:textures/entity/rainbow_jelly_overlays/glint_brazil.png"), true, false)).setWriteMaskState(f_110114_).setCullState(f_110110_).setDepthTestState(f_110112_).setTransparencyState(f_110137_).setTexturingState(RAINBOW_TEXTURING_LARGE).setOverlayState(f_110154_).createCompositeState(true));

    public static final RenderType WEEZER_GLINT = m_173215_("weezer_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(f_173083_).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation("alexsmobs:textures/entity/rainbow_jelly_overlays/glint_weezer.png"), false, false)).setWriteMaskState(f_110114_).setCullState(f_110110_).setDepthTestState(f_110112_).setTransparencyState(f_110137_).setTexturingState(WEEZER_TEXTURING).setOverlayState(f_110154_).createCompositeState(true));

    public static final RenderType STATIC_PORTAL = m_173215_("static_portal", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(f_173083_).setTextureState(new RenderStateShard.TextureStateShard(STATIC_TEXTURE, false, false)).setWriteMaskState(f_110114_).setCullState(f_110110_).setDepthTestState(f_110112_).setTexturingState(STATIC_PORTAL_TEXTURING).setOverlayState(f_110154_).setTransparencyState(f_110139_).createCompositeState(true));

    public static final RenderType STATIC_PARTICLE = m_173215_("static_particle", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(f_173083_).setTextureState(new RenderStateShard.TextureStateShard(STATIC_TEXTURE, false, false)).setWriteMaskState(f_110114_).setCullState(f_110110_).setDepthTestState(f_110112_).setTexturingState(STATIC_PARTICLE_TEXTURING).setOverlayState(f_110154_).setTransparencyState(f_110139_).createCompositeState(true));

    public static final RenderType STATIC_ENTITY = m_173215_("static_entity", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(f_173083_).setTextureState(new RenderStateShard.TextureStateShard(STATIC_TEXTURE, false, false)).setWriteMaskState(f_110114_).setCullState(f_110110_).setDepthTestState(f_110112_).setTexturingState(STATIC_ENTITY_TEXTURING).setOverlayState(f_110154_).setTransparencyState(f_110139_).createCompositeState(true));

    public static final RenderType VOID_WORM_PORTAL_OVERLAY = m_173215_("void_worm_portal_overlay", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(f_173093_).setDepthTestState(f_110112_).setCullState(f_110110_).setTransparencyState(f_110134_).setTextureState(RenderStateShard.MultiTextureStateShard.builder().add(TheEndPortalRenderer.END_SKY_LOCATION, false, false).add(TheEndPortalRenderer.END_PORTAL_LOCATION, false, false).build()).createCompositeState(false));

    protected static final RenderStateShard.TransparencyStateShard WORM_TRANSPARANCY = new RenderStateShard.TransparencyStateShard("translucent_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    protected static final RenderStateShard.TransparencyStateShard MIMICUBE_TRANSPARANCY = new RenderStateShard.TransparencyStateShard("mimicube_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    protected static final RenderStateShard.TransparencyStateShard GHOST_TRANSPARANCY = new RenderStateShard.TransparencyStateShard("translucent_ghost_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    public AMRenderTypes(String string0, VertexFormat vertexFormat1, VertexFormat.Mode vertexFormatMode2, int int3, boolean boolean4, boolean boolean5, Runnable runnable6, Runnable runnable7) {
        super(string0, vertexFormat1, vertexFormatMode2, int3, boolean4, boolean5, runnable6, runnable7);
    }

    public static RenderType getTransparentMimicube(ResourceLocation texture) {
        RenderType.CompositeState lvt_1_1_ = RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).setShaderState(f_173065_).setTransparencyState(f_110139_).setOverlayState(f_110154_).setOutputState(f_110125_).setCullState(f_110158_).setLightmapState(f_110152_).setOverlayState(f_110154_).setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE).setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST).createCompositeState(true);
        return m_173215_("mimicube", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, lvt_1_1_);
    }

    public static RenderType getEyesFlickering(ResourceLocation p_228652_0_, float lightLevel) {
        RenderStateShard.TextureStateShard lvt_1_1_ = new RenderStateShard.TextureStateShard(p_228652_0_, false, false);
        return m_173215_("eye_flickering", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setTextureState(lvt_1_1_).setShaderState(f_173065_).setTransparencyState(f_110139_).setCullState(f_110110_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(false));
    }

    public static RenderType getFullBright(ResourceLocation p_228652_0_) {
        RenderStateShard.TextureStateShard lvt_1_1_ = new RenderStateShard.TextureStateShard(p_228652_0_, false, false);
        return m_173215_("full_bright", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setTextureState(lvt_1_1_).setShaderState(f_173065_).setTransparencyState(f_110139_).setCullState(f_110110_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(false));
    }

    public static RenderType getFreddy(ResourceLocation p_228652_0_) {
        RenderStateShard.TextureStateShard lvt_1_1_ = new RenderStateShard.TextureStateShard(p_228652_0_, false, false);
        return m_173215_("freddy", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setTextureState(lvt_1_1_).setShaderState(f_173065_).setTransparencyState(f_110139_).setLightmapState(RenderStateShard.NO_LIGHTMAP).setCullState(f_110110_).setOverlayState(f_110154_).createCompositeState(true));
    }

    public static RenderType getFrilledSharkTeeth(ResourceLocation p_228652_0_) {
        RenderStateShard.TextureStateShard lvt_1_1_ = new RenderStateShard.TextureStateShard(p_228652_0_, false, false);
        return m_173215_("sharkteeth", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setTextureState(lvt_1_1_).setShaderState(f_173065_).setTransparencyState(f_110134_).setCullState(f_110110_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(false));
    }

    public static RenderType getEyesNoCull(ResourceLocation p_228652_0_) {
        RenderStateShard.TextureStateShard lvt_1_1_ = new RenderStateShard.TextureStateShard(p_228652_0_, false, false);
        return m_173215_("eyes_no_cull", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setTextureState(lvt_1_1_).setShaderState(f_173065_).setTransparencyState(f_110135_).setWriteMaskState(f_110115_).setCullState(f_110110_).createCompositeState(false));
    }

    public static RenderType getSpectreBones(ResourceLocation p_228652_0_) {
        RenderStateShard.TextureStateShard lvt_1_1_ = new RenderStateShard.TextureStateShard(p_228652_0_, false, false);
        return m_173215_("spectre_bones", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setTextureState(lvt_1_1_).setShaderState(f_173073_).setTransparencyState(GHOST_TRANSPARANCY).setDepthTestState(f_110113_).setWriteMaskState(f_110114_).setCullState(f_110110_).setLightmapState(f_110153_).setOverlayState(f_110154_).createCompositeState(false));
    }

    public static RenderType getGhost(ResourceLocation p_228652_0_) {
        RenderStateShard.TextureStateShard lvt_1_1_ = new RenderStateShard.TextureStateShard(p_228652_0_, false, false);
        return m_173215_("ghost_am", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 262144, false, true, RenderType.CompositeState.builder().setTextureState(lvt_1_1_).setShaderState(f_173073_).setWriteMaskState(f_110114_).setDepthTestState(f_110112_).setLightmapState(f_110153_).setOverlayState(f_110154_).setTransparencyState(GHOST_TRANSPARANCY).setCullState(RenderStateShard.NO_CULL).createCompositeState(true));
    }

    public static RenderType getEyesAlphaEnabled(ResourceLocation locationIn) {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder().setShaderState(f_173073_).setTextureState(new RenderStateShard.TextureStateShard(locationIn, false, false)).setTransparencyState(WORM_TRANSPARANCY).setCullState(f_110110_).setLightmapState(f_110152_).setOverlayState(f_110154_).setDepthTestState(f_110112_).createCompositeState(true);
        return m_173215_("eye_alpha", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, rendertype$compositestate);
    }

    public static RenderType getEyesNoFog(ResourceLocation locationIn) {
        RenderStateShard.TextureStateShard renderstateshard$texturestateshard = new RenderStateShard.TextureStateShard(locationIn, false, false);
        return m_173215_("eyes_nofog", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, true, false, RenderType.CompositeState.builder().setShaderState(f_173077_).setTextureState(renderstateshard$texturestateshard).setTransparencyState(f_110136_).setWriteMaskState(f_110114_).setCullState(f_110110_).setDepthTestState(f_110113_).setOverlayState(f_110154_).createCompositeState(true));
    }

    public static RenderType getSunbirdShine() {
        return m_173215_("sunbird_shine", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder().setShaderState(f_173083_).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation("alexsmobs:textures/entity/sunbird_shine.png"), true, true)).setLightmapState(f_110152_).setCullState(RenderStateShard.NO_CULL).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setOverlayState(f_110154_).setDepthTestState(f_110113_).createCompositeState(true));
    }

    public static RenderType getSkulkBoom() {
        RenderType.CompositeState renderState = RenderType.CompositeState.builder().setShaderState(f_173074_).setCullState(f_110110_).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation("alexsmobs:textures/particle/skulk_boom.png"), true, true)).setTransparencyState(f_110139_).setLightmapState(f_110152_).setOverlayState(f_110154_).setWriteMaskState(f_110115_).setDepthTestState(f_110113_).setLayeringState(f_110119_).createCompositeState(false);
        return m_173215_("skulk_boom", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, renderState);
    }

    public static RenderType getUnderminer(ResourceLocation texture) {
        RenderType.CompositeState renderState = RenderType.CompositeState.builder().setShaderState(f_173074_).setCullState(f_110110_).setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).setTransparencyState(f_110139_).setLightmapState(f_110152_).setOverlayState(f_110154_).setWriteMaskState(f_110114_).setDepthTestState(f_110113_).setLayeringState(f_110117_).createCompositeState(false);
        return m_173215_("underminer", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, renderState);
    }

    public static RenderType getGhostPickaxe(ResourceLocation texture) {
        RenderType.CompositeState renderState = RenderType.CompositeState.builder().setShaderState(f_173064_).setCullState(f_110110_).setOutputState(f_110129_).setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY).setLightmapState(f_110152_).setOverlayState(f_110154_).setWriteMaskState(f_110114_).setDepthTestState(f_110113_).setLayeringState(f_110117_).createCompositeState(false);
        return m_173215_("ghost_pickaxe", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, renderState);
    }

    public static RenderType getGhostCrumbling(ResourceLocation texture) {
        RenderStateShard.TextureStateShard lvt_1_1_ = new RenderStateShard.TextureStateShard(texture, false, false);
        return m_173215_("ghost_crumbling_am", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 262144, false, true, RenderType.CompositeState.builder().setTextureState(lvt_1_1_).setShaderState(RenderStateShard.RENDERTYPE_ENERGY_SWIRL_SHADER).setTransparencyState(f_110136_).setCullState(f_110110_).setLightmapState(f_110152_).setOverlayState(f_110154_).setLayeringState(f_110119_).setDepthTestState(f_110113_).setCullState(RenderStateShard.NO_CULL).createCompositeState(true));
    }

    private static void setupRainbowTexturing(float in, long time) {
        long i = Util.getMillis() * time;
        float f = (float) (i % 110000L) / 110000.0F;
        float f1 = (float) (i % 30000L) / 30000.0F;
        Matrix4f matrix4f = new Matrix4f().translation(0.0F, f1, 0.0F);
        matrix4f.scale(in);
        RenderSystem.setTextureMatrix(matrix4f);
    }

    private static void setupRainbowTexturing2(float in, long time) {
        long i = Util.getMillis() * time;
        float f = (float) (i % 110000L) / 110000.0F;
        float f1 = (float) (i % 30000L) / 30000.0F;
        float f2 = (float) Math.sin((double) ((float) i / 30000.0F));
        Matrix4f matrix4f = new Matrix4f().translation(f1, f2, 0.0F);
        matrix4f.scale(in);
        RenderSystem.setTextureMatrix(matrix4f);
    }

    private static void setupStaticTexturing(float in, long time) {
        long i = Util.getMillis() * time;
        float f = (float) (i % 110000L) / 110000.0F;
        float f1 = (float) (i % 30000L) / 30000.0F;
        float f2 = (float) Math.floor((double) ((float) (i % 3000L) / 3000.0F * 4.0F));
        float f3 = (float) Math.sin((double) ((float) i / 30000.0F)) * 0.05F;
        Matrix4f matrix4f = new Matrix4f().translation(f1, f2 * 0.25F + f3, 0.0F);
        matrix4f.scale(in * 1.5F, in * 0.25F, in);
        RenderSystem.setTextureMatrix(matrix4f);
    }

    public static RenderType getFarseerBeam() {
        RenderType.CompositeState renderState = RenderType.CompositeState.builder().setShaderState(f_173074_).setCullState(f_110158_).setTextureState(new RenderStateShard.TextureStateShard(STATIC_TEXTURE, false, false)).setTransparencyState(f_110139_).setLightmapState(f_110152_).setOverlayState(f_110154_).setWriteMaskState(f_110115_).setDepthTestState(f_110113_).setLayeringState(f_110119_).createCompositeState(false);
        return m_173215_("farseer_beam", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, renderState);
    }

    public static VertexConsumer createMergedVertexConsumer(VertexConsumer consumer1, VertexConsumer consumer2) {
        VertexConsumer vertexConsumer = consumer2;
        if (!encounteredMultiConsumerError) {
            try {
                vertexConsumer = VertexMultiConsumer.create(consumer1, consumer2);
            } catch (Exception var4) {
                AlexsMobs.LOGGER.warn("Encountered issue mixing two render types together. Likely an issue with Optifine or other rendering mod. This warning will only display once.");
                encounteredMultiConsumerError = true;
            }
        }
        return vertexConsumer;
    }
}