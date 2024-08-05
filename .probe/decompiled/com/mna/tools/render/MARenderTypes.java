package com.mna.tools.render;

import com.mna.api.tools.RLoc;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.io.IOException;
import java.util.OptionalDouble;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class MARenderTypes extends RenderType {

    private static final ResourceLocation SPARKLE_TEXTURE = RLoc.create("textures/particle/sparkle.png");

    public static final ResourceLocation TEXTURE_BEACON_BEAM = RLoc.create("textures/particle/beam_b.png");

    public static final ResourceLocation TEXTURE_VINE = RLoc.create("textures/entity/boss/vine.png");

    public static final ResourceLocation PORTAL_TEXTURE = RLoc.create("textures/entity/vortex.png");

    public static final ResourceLocation RTP_PORTAL_TEXTURE = RLoc.create("textures/entity/vortex_rainbow.png");

    public static final ResourceLocation BRIGHT_LIGHT_TEXTURE = RLoc.create("textures/entity/fx/bright_light.png");

    public static final ResourceLocation MAGELIGHT_TEXTURE = RLoc.create("textures/block/magelight_ring.png");

    public static final ResourceLocation PORTAL_TEXTURE_RAID = RLoc.create("textures/entity/vortex_raid.png");

    public static final ResourceLocation TEXTURE_ORB = RLoc.create("textures/block/lightorb.png");

    public static final RenderType MARKING_RUNE_MARK = m_173215_("marking_rune_loc", DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.LINES, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173095_).setLineState(new RenderStateShard.LineStateShard(OptionalDouble.empty())).setLayeringState(f_110119_).setTransparencyState(f_110139_).setOutputState(f_110124_).setWriteMaskState(f_110115_).setDepthTestState(f_110111_).setCullState(f_110110_).createCompositeState(false));

    public static final RenderType RENDER_TYPE_MANAWEAVE = m_173215_("manaweave_dots", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173068_).setTextureState(new RenderStateShard.TextureStateShard(SPARKLE_TEXTURE, false, false)).setTransparencyState(f_110136_).setOutputState(f_110125_).setLightmapState(f_110152_).setCullState(f_110110_).setWriteMaskState(f_110115_).createCompositeState(true));

    public static final RenderType RENDER_TYPE_MANAWEAVE_SOLID = m_173215_("manaweave_dots_solid", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder().setShaderState(f_173113_).setTextureState(new RenderStateShard.TextureStateShard(SPARKLE_TEXTURE, false, false)).setTransparencyState(f_110134_).setWriteMaskState(f_110114_).createCompositeState(true));

    public static final RenderType RADIANT_RENDER_TYPE = m_173215_("radiant", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173091_).setWriteMaskState(f_110115_).setTransparencyState(f_110139_).setOutputState(f_110126_).setCullState(f_110110_).setDepthTestState(f_110113_).createCompositeState(false));

    public static final RenderType SOLID_RENDER_TYPE = m_173215_("mna_solid", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173091_).setWriteMaskState(f_110115_).setTransparencyState(f_110134_).setOutputState(f_110126_).setCullState(f_110110_).setDepthTestState(f_110113_).createCompositeState(false));

    public static final RenderType RITUAL_BEAM_RENDER_TYPE = m_173215_("beam", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173068_).setTextureState(new RenderStateShard.TextureStateShard(TEXTURE_BEACON_BEAM, false, false)).setOutputState(f_110126_).setTransparencyState(f_110139_).setLightmapState(f_110152_).setDepthTestState(f_110113_).setWriteMaskState(f_110115_).setCullState(f_110110_).createCompositeState(false));

    public static final RenderType BOSS_VINE = m_173215_("beam", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173114_).setLightmapState(f_110152_).setTextureState(new RenderStateShard.TextureStateShard(TEXTURE_VINE, false, false)).setWriteMaskState(f_110115_).setCullState(f_110110_).createCompositeState(false));

    public static final RenderType PORTAL_RENDER = m_173215_("portal_render_type", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173066_).setTextureState(new RenderStateShard.TextureStateShard(PORTAL_TEXTURE, false, false)).setTransparencyState(f_110139_).setOutputState(f_110125_).setLightmapState(f_110152_).setCullState(f_110110_).setDepthTestState(f_110113_).setWriteMaskState(f_110115_).setOverlayState(f_110155_).createCompositeState(false));

    public static final RenderType RTP_PORTAL_RENDER = m_173215_("portal_render_type", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173066_).setTextureState(new RenderStateShard.TextureStateShard(RTP_PORTAL_TEXTURE, false, false)).setTransparencyState(f_110139_).setOutputState(f_110125_).setLightmapState(f_110152_).setCullState(f_110110_).setDepthTestState(f_110113_).setWriteMaskState(f_110115_).setOverlayState(f_110155_).createCompositeState(false));

    public static final RenderType BRIGHT_LIGHT_RENDER = m_173215_("portal_render_type", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173066_).setTextureState(new RenderStateShard.TextureStateShard(BRIGHT_LIGHT_TEXTURE, false, false)).setTransparencyState(f_110139_).setOutputState(f_110125_).setLightmapState(f_110152_).setCullState(f_110110_).setDepthTestState(f_110113_).setWriteMaskState(f_110115_).setOverlayState(f_110155_).createCompositeState(false));

    public static final RenderType ORB_RENDER = m_173215_("orb_render_type", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173066_).setTextureState(new RenderStateShard.TextureStateShard(TEXTURE_ORB, false, false)).setTransparencyState(f_110139_).setOutputState(f_110125_).setLightmapState(f_110152_).setCullState(f_110110_).setDepthTestState(f_110113_).setWriteMaskState(f_110115_).setOverlayState(f_110155_).createCompositeState(false));

    public static final RenderType RAID_PORTAL_RENDER = m_173215_("portal_render_type", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173066_).setTextureState(new RenderStateShard.TextureStateShard(PORTAL_TEXTURE_RAID, false, false)).setTransparencyState(f_110139_).setCullState(f_110110_).setLightmapState(f_110152_).setWriteMaskState(f_110115_).setOverlayState(f_110154_).createCompositeState(true));

    public MARenderTypes(String name, VertexFormat format, VertexFormat.Mode p_i225992_3_, int p_i225992_4_, boolean p_i225992_5_, boolean p_i225992_6_, Runnable runnablePre, Runnable runnablePost) {
        super(name, format, p_i225992_3_, p_i225992_4_, p_i225992_5_, p_i225992_6_, runnablePre, runnablePost);
    }

    public static RenderType PORTAL_UVROTATE(ResourceLocation texture) {
        return (RenderType) MARenderTypes.CustomRenderTypes.PORTAL_UVROTATE.apply(texture);
    }

    private static class CustomRenderTypes {

        private static ShaderInstance portalUVRotate;

        private static final RenderStateShard.ShaderStateShard RENDERTYPE_PORTAL_UVROTATE = new RenderStateShard.ShaderStateShard(() -> portalUVRotate);

        public static Function<ResourceLocation, RenderType> PORTAL_UVROTATE = Util.memoize(MARenderTypes.CustomRenderTypes::uvRotate);

        private static RenderType uvRotate(ResourceLocation rLoc) {
            RenderType.CompositeState rendertype$state = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_PORTAL_UVROTATE).setTextureState(new RenderStateShard.TextureStateShard(rLoc, false, false)).setTransparencyState(MARenderTypes.f_110139_).setLightmapState(MARenderTypes.f_110153_).setOverlayState(MARenderTypes.f_110155_).createCompositeState(false);
            return RenderType.create("mna_portal_uvrotate", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, false, rendertype$state);
        }
    }

    @EventBusSubscriber(value = { Dist.CLIENT }, modid = "mna", bus = Bus.MOD)
    public static class MARenderClientEvents {

        @SubscribeEvent
        public static void registerCustomShaders(RegisterShadersEvent event) throws IOException {
            event.registerShader(new ShaderInstance(event.getResourceProvider(), RLoc.create("portal_uv_rotate"), DefaultVertexFormat.NEW_ENTITY), shaderInstance -> MARenderTypes.CustomRenderTypes.portalUVRotate = shaderInstance);
        }
    }
}