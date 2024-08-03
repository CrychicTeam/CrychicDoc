package com.rekindled.embers.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.OptionalDouble;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class EmbersRenderTypes extends RenderType {

    public static ShaderInstance additiveShader;

    public static final RenderStateShard.ShaderStateShard ADDITIVE_SHADER = new RenderStateShard.ShaderStateShard(() -> additiveShader);

    public static ShaderInstance mithrilShader;

    public static final RenderStateShard.ShaderStateShard MITHRIL_SHADER = new RenderStateShard.ShaderStateShard(() -> mithrilShader);

    public static ParticleRenderType PARTICLE_SHEET_ADDITIVE = new ParticleRenderType() {

        @Override
        public void begin(BufferBuilder p_107455_, TextureManager p_107456_) {
            RenderSystem.enableDepthTest();
            Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
            RenderSystem.depthMask(false);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            p_107455_.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator p_107458_) {
            p_107458_.end();
        }

        public String toString() {
            return "PARTICLE_SHEET_ADDITIVE";
        }
    };

    public static ParticleRenderType PARTICLE_SHEET_ADDITIVE_XRAY = new ParticleRenderType() {

        @Override
        public void begin(BufferBuilder p_107455_, TextureManager p_107456_) {
            RenderSystem.enableDepthTest();
            Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
            RenderSystem.depthMask(false);
            RenderSystem.disableDepthTest();
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            p_107455_.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator p_107458_) {
            p_107458_.end();
            RenderSystem.enableDepthTest();
        }

        public String toString() {
            return "PARTICLE_SHEET_ADDITIVE_XRAY";
        }
    };

    public static ParticleRenderType PARTICLE_SHEET_TRANSLUCENT_NODEPTH = new ParticleRenderType() {

        @Override
        public void begin(BufferBuilder p_107455_, TextureManager p_107456_) {
            RenderSystem.enableDepthTest();
            Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
            RenderSystem.depthMask(false);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            p_107455_.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator p_107458_) {
            p_107458_.end();
        }

        public String toString() {
            return "PARTICLE_SHEET_TRANSLUCENT_NODEPTH";
        }
    };

    public static final RenderType FLUID = m_173215_("embers:fluid_render_type", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder().setLightmapState(f_110152_).setShaderState(f_173065_).setTextureState(f_110145_).setTransparencyState(f_110139_).setCullState(f_110158_).setOverlayState(f_110154_).createCompositeState(true));

    public static final RenderStateShard.ShaderStateShard PTLC_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172637_);

    public static final RenderStateShard.ShaderStateShard PTCN_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172838_);

    public static final RenderStateShard.ShaderStateShard PTC_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172820_);

    public static final RenderType CRYSTAL = m_173215_("embers:crystal_render_type", DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS, 256, true, false, RenderType.CompositeState.builder().setShaderState(ADDITIVE_SHADER).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation("embers:textures/block/crystal_material.png"), false, false)).setTransparencyState(f_110136_).setCullState(f_110110_).setLightmapState(f_110153_).setOverlayState(f_110155_).createCompositeState(false));

    public static final RenderType CRYSTAL_FALLBACK = m_173215_("embers:crystal_render_type", DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS, 256, true, false, RenderType.CompositeState.builder().setShaderState(PTC_SHADER).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation("embers:textures/block/crystal_material.png"), false, false)).setTransparencyState(f_110136_).setCullState(f_110110_).setLightmapState(f_110153_).setOverlayState(f_110155_).createCompositeState(false));

    public static final RenderType FIELD_CHART = m_173215_("embers:field_chart_render_type", DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(ADDITIVE_SHADER).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation("embers:textures/block/field_square.png"), false, false)).setTransparencyState(f_110136_).setCullState(f_110110_).createCompositeState(false));

    public static final RenderType FIELD_CHART_FALLBACK = m_173215_("embers:field_chart_render_type", DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(PTC_SHADER).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation("embers:textures/block/field_square.png"), false, false)).setTransparencyState(f_110136_).setCullState(f_110110_).createCompositeState(false));

    public static Function<ResourceLocation, RenderType> CRYSTAL_SEED = Util.memoize(EmbersRenderTypes::getSeed);

    public static final RenderType BEAM = m_173215_("embers:beam_render_type", DefaultVertexFormat.POSITION_TEX_LIGHTMAP_COLOR, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(PTLC_SHADER).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation("embers:textures/entity/alchemy_circle.png"), false, false)).setTransparencyState(f_110136_).setCullState(f_110110_).setLayeringState(f_110119_).setWriteMaskState(f_110114_).setOutputState(f_110125_).createCompositeState(false));

    public static final RenderType GLOW_LINES = m_173215_("embers:glow_lines", DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.LINES, 256, false, false, RenderType.CompositeState.builder().setShaderState(f_173095_).setLightmapState(f_110153_).setLineState(new RenderStateShard.LineStateShard(OptionalDouble.of(6.0))).setLayeringState(f_110119_).setTransparencyState(f_110139_).setOutputState(f_110129_).setWriteMaskState(f_110114_).setCullState(f_110110_).createCompositeState(false));

    public static final RenderType GLOW_GUI = m_173215_("embers:glow_gui", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(f_285573_).setTransparencyState(f_110136_).setDepthTestState(f_110113_).createCompositeState(false));

    public static final RenderType HEAT_BAR_ENDS = m_173215_("embers:heat_bar_ends", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(f_173102_).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation("embers", "textures/gui/heat_bar.png"), false, false)).setTransparencyState(f_110134_).setDepthTestState(f_110113_).createCompositeState(false));

    public static Function<RenderStateShard.EmptyTextureStateShard, RenderType> GLOW_TEXT = Util.memoize(EmbersRenderTypes::getText);

    public static final RenderType NOTE_BACKGROUND = getNote(new ResourceLocation("embers", "textures/gui/alchemical_note.png"));

    public static final RenderType NOTE_PEDESTAL = getNote(new ResourceLocation("embers", "textures/gui/alchemical_note_pedestal.png"));

    public static ResourceLocation MITHRIL_REFLECTION = new ResourceLocation("embers:textures/misc/mithril_reflection.png");

    public static RenderType MITHRIL = m_173215_("embers:mithril_render_type", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, RenderType.CompositeState.builder().setShaderState(MITHRIL_SHADER).setTextureState(new RenderStateShard.EmptyTextureStateShard(() -> {
        TextureManager texturemanager = Minecraft.getInstance().getTextureManager();
        texturemanager.getTexture(InventoryMenu.BLOCK_ATLAS).setFilter(false, false);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        texturemanager.getTexture(MITHRIL_REFLECTION).setFilter(true, false);
        RenderSystem.setShaderTexture(3, MITHRIL_REFLECTION);
    }, () -> {
    })).setOverlayState(f_110154_).setLightmapState(f_110152_).createCompositeState(true));

    public EmbersRenderTypes(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) {
        super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
    }

    private static RenderType getSeed(ResourceLocation texture) {
        return m_173215_("embers:crystal_seed_render_type", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES, 256, true, false, RenderType.CompositeState.builder().setShaderState(f_173112_).setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).setOverlayState(f_110154_).setLightmapState(f_110152_).createCompositeState(true));
    }

    private static RenderType getText(RenderStateShard.EmptyTextureStateShard state) {
        RenderType.CompositeState rendertype$state = RenderType.CompositeState.builder().setShaderState(f_173086_).setTextureState(state).setTransparencyState(f_110136_).setLightmapState(f_110152_).createCompositeState(false);
        return m_173215_("embers:glow_text", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true, rendertype$state);
    }

    public static RenderType getNote(ResourceLocation location) {
        RenderType.CompositeState rendertype$state = RenderType.CompositeState.builder().setShaderState(f_173065_).setTextureState(new RenderStateShard.TextureStateShard(location, false, false)).setTransparencyState(f_110139_).setLightmapState(f_110152_).setCullState(f_110110_).setOverlayState(f_110154_).createCompositeState(false);
        return m_173215_("alchemical_note", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, rendertype$state);
    }
}