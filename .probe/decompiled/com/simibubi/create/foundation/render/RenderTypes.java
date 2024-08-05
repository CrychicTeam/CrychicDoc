package com.simibubi.create.foundation.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.simibubi.create.AllSpecialTextures;
import com.simibubi.create.Create;
import java.io.IOException;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class RenderTypes extends RenderStateShard {

    public static final RenderStateShard.ShaderStateShard GLOWING_SHADER = new RenderStateShard.ShaderStateShard(() -> RenderTypes.Shaders.glowingShader);

    private static final RenderType OUTLINE_SOLID = RenderType.create(createLayerName("outline_solid"), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(f_173112_).setTextureState(new RenderStateShard.TextureStateShard(AllSpecialTextures.BLANK.getLocation(), false, false)).setCullState(f_110158_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(false));

    private static final RenderType GLOWING_SOLID_DEFAULT = getGlowingSolid(InventoryMenu.BLOCK_ATLAS);

    private static final RenderType ADDITIVE = RenderType.create(createLayerName("additive"), DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder().setShaderState(f_173105_).setTextureState(new RenderStateShard.TextureStateShard(InventoryMenu.BLOCK_ATLAS, false, false)).setTransparencyState(f_110135_).setCullState(f_110110_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(true));

    private static final RenderType GLOWING_TRANSLUCENT_DEFAULT = getGlowingTranslucent(InventoryMenu.BLOCK_ATLAS);

    private static final RenderType ITEM_PARTIAL_SOLID = RenderType.create(createLayerName("item_partial_solid"), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, RenderType.CompositeState.builder().setShaderState(f_173112_).setTextureState(f_110146_).setCullState(f_110158_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(true));

    private static final RenderType ITEM_PARTIAL_TRANSLUCENT = RenderType.create(createLayerName("item_partial_translucent"), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder().setShaderState(f_173065_).setTextureState(f_110146_).setTransparencyState(f_110139_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(true));

    private static final RenderType FLUID = RenderType.create(createLayerName("fluid"), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173065_).setTextureState(f_110145_).setTransparencyState(f_110139_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(true));

    public static RenderType getOutlineSolid() {
        return OUTLINE_SOLID;
    }

    public static RenderType getOutlineTranslucent(ResourceLocation texture, boolean cull) {
        return RenderType.create(createLayerName("outline_translucent" + (cull ? "_cull" : "")), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(cull ? f_173065_ : f_173066_).setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).setTransparencyState(f_110139_).setCullState(cull ? f_110158_ : f_110110_).setLightmapState(f_110152_).setOverlayState(f_110154_).setWriteMaskState(f_110115_).createCompositeState(false));
    }

    public static RenderType getGlowingSolid(ResourceLocation texture) {
        return RenderType.create(createLayerName("glowing_solid"), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, RenderType.CompositeState.builder().setShaderState(GLOWING_SHADER).setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).setCullState(f_110158_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(true));
    }

    public static RenderType getGlowingSolid() {
        return GLOWING_SOLID_DEFAULT;
    }

    public static RenderType getGlowingTranslucent(ResourceLocation texture) {
        return RenderType.create(createLayerName("glowing_translucent"), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder().setShaderState(GLOWING_SHADER).setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).setTransparencyState(f_110139_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(true));
    }

    public static RenderType getAdditive() {
        return ADDITIVE;
    }

    public static RenderType getGlowingTranslucent() {
        return GLOWING_TRANSLUCENT_DEFAULT;
    }

    public static RenderType getItemPartialSolid() {
        return ITEM_PARTIAL_SOLID;
    }

    public static RenderType getItemPartialTranslucent() {
        return ITEM_PARTIAL_TRANSLUCENT;
    }

    public static RenderType getFluid() {
        return FLUID;
    }

    private static String createLayerName(String name) {
        return "create:" + name;
    }

    private RenderTypes() {
        super(null, null, null);
    }

    @EventBusSubscriber(value = { Dist.CLIENT }, bus = Bus.MOD)
    private static class Shaders {

        private static ShaderInstance glowingShader;

        @SubscribeEvent
        public static void onRegisterShaders(RegisterShadersEvent event) throws IOException {
            ResourceProvider resourceProvider = event.getResourceProvider();
            event.registerShader(new ShaderInstance(resourceProvider, Create.asResource("glowing_shader"), DefaultVertexFormat.NEW_ENTITY), shader -> glowingShader = shader);
        }
    }
}