package net.mehvahdjukaar.supplementaries.forge;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.function.Function;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.events.ClientEvents;
import net.mehvahdjukaar.supplementaries.common.utils.VibeChecker;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = "supplementaries", value = { Dist.CLIENT }, bus = Bus.MOD)
public class SupplementariesForgeClient {

    private static boolean hasOptifine;

    private static boolean firstScreenShown;

    private static ShaderInstance staticNoiseShader;

    @SubscribeEvent
    public static void setup(FMLClientSetupEvent event) {
        VibeChecker.checkVibe();
        MinecraftForge.EVENT_BUS.addListener(SupplementariesForgeClient::handleDrawScreenEventPost);
    }

    public static void handleDrawScreenEventPost(ScreenEvent.Init.Post event) {
        if (!firstScreenShown && event.getScreen() instanceof TitleScreen) {
            ClientEvents.onFirstScreen(event.getScreen());
            firstScreenShown = true;
        }
    }

    public static ShaderInstance getStaticNoiseShader() {
        return staticNoiseShader;
    }

    public static RenderType staticNoise(ResourceLocation location) {
        return (RenderType) SupplementariesForgeClient.RenderTypeAccessor.STATIC_NOISE.apply(location);
    }

    @SubscribeEvent
    public static void registerShader(RegisterShadersEvent event) {
        try {
            ShaderInstance translucentParticleShader = new ShaderInstance(event.getResourceProvider(), Supplementaries.res("static_noise"), DefaultVertexFormat.NEW_ENTITY);
            event.registerShader(translucentParticleShader, s -> staticNoiseShader = s);
        } catch (Exception var2) {
            Supplementaries.LOGGER.error("Failed to parse shader: " + var2);
        }
    }

    private abstract static class RenderTypeAccessor extends RenderType {

        protected static final RenderStateShard.ShaderStateShard STATIC_NOISE_SHARD = new RenderStateShard.ShaderStateShard(SupplementariesForgeClient::getStaticNoiseShader);

        static final Function<ResourceLocation, RenderType> STATIC_NOISE = Util.memoize((Function<ResourceLocation, RenderType>) (resourceLocation -> {
            RenderType.CompositeState compositeState = RenderType.CompositeState.builder().setShaderState(STATIC_NOISE_SHARD).setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false)).setTransparencyState(f_110134_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(true);
            return m_173215_("static_noise", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, compositeState);
        }));

        public RenderTypeAccessor(String string, VertexFormat arg, VertexFormat.Mode arg2, int i, boolean bl, boolean bl2, Runnable runnable, Runnable runnable2) {
            super(string, arg, arg2, i, bl, bl2, runnable, runnable2);
        }
    }
}