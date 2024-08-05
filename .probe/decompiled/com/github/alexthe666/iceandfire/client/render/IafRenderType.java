package com.github.alexthe666.iceandfire.client.render;

import com.github.alexthe666.iceandfire.client.IafClientSetup;
import com.github.alexthe666.iceandfire.client.render.tile.RenderDreadPortal;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class IafRenderType extends RenderType {

    private static final ResourceLocation STONE_TEXTURE = new ResourceLocation("textures/block/stone.png");

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_DREAD_PORTAL_SHADER = new RenderStateShard.ShaderStateShard(IafClientSetup::getRendertypeDreadPortalShader);

    private static final RenderType DREADLANDS_PORTAL = m_173215_("dreadlands_portal", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_DREAD_PORTAL_SHADER).setTextureState(RenderStateShard.MultiTextureStateShard.builder().add(RenderDreadPortal.DREAD_PORTAL_BACKGROUND, false, false).add(RenderDreadPortal.DREAD_PORTAL, false, false).build()).createCompositeState(false));

    protected static final RenderStateShard.TransparencyStateShard GHOST_TRANSPARANCY = new RenderStateShard.TransparencyStateShard("translucent_ghost_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    public IafRenderType(String nameIn, VertexFormat formatIn, VertexFormat.Mode drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
        super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }

    public static RenderType getGhost(ResourceLocation locationIn) {
        RenderStateShard.TextureStateShard lvt_1_1_ = new RenderStateShard.TextureStateShard(locationIn, false, false);
        return m_173215_("ghost_iaf", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173114_).setTextureState(lvt_1_1_).setTransparencyState(GHOST_TRANSPARANCY).setCullState(f_110110_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(true));
    }

    public static RenderType getGhostDaytime(ResourceLocation locationIn) {
        RenderStateShard.TextureStateShard lvt_1_1_ = new RenderStateShard.TextureStateShard(locationIn, false, false);
        return m_173215_("ghost_iaf_day", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173114_).setTextureState(lvt_1_1_).setTransparencyState(f_110139_).setCullState(f_110110_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(true));
    }

    public static RenderType getDreadlandsPortal() {
        return DREADLANDS_PORTAL;
    }

    public static RenderType getStoneMobRenderType(float x, float y) {
        RenderStateShard.TextureStateShard textureState = new RenderStateShard.TextureStateShard(STONE_TEXTURE, false, false);
        RenderType.CompositeState rendertype = RenderType.CompositeState.builder().setShaderState(RenderType.f_173113_).setTextureState(textureState).setTransparencyState(f_110134_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(true);
        return m_173215_("stone_entity_type", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, rendertype);
    }

    public static RenderType getIce(ResourceLocation locationIn) {
        RenderStateShard.TextureStateShard lvt_1_1_ = new RenderStateShard.TextureStateShard(locationIn, false, false);
        return m_173215_("ice_texture", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(RenderType.f_173068_).setTextureState(lvt_1_1_).setTransparencyState(f_110139_).setCullState(f_110158_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(true));
    }

    public static RenderType getStoneCrackRenderType(ResourceLocation crackTex) {
        RenderStateShard.TextureStateShard textureState = new RenderStateShard.TextureStateShard(crackTex, false, false);
        RenderType.CompositeState rendertype$state = RenderType.CompositeState.builder().setTextureState(textureState).setShaderState(RenderType.f_173113_).setTransparencyState(f_110139_).setDepthTestState(f_110112_).setCullState(f_110110_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(false);
        return m_173215_("stone_entity_type_crack", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, rendertype$state);
    }
}