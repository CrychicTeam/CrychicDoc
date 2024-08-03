package team.lodestar.lodestone.registry.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import java.io.IOException;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.rendering.shader.ShaderHolder;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "lodestone", bus = Bus.MOD)
public class LodestoneShaderRegistry {

    public static ShaderHolder LODESTONE_TEXTURE = new ShaderHolder(LodestoneLib.lodestonePath("lodestone_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "LumiTransparency");

    public static ShaderHolder LODESTONE_TEXT = new ShaderHolder(LodestoneLib.lodestonePath("lodestone_text"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);

    public static ShaderHolder PARTICLE = new ShaderHolder(LodestoneLib.lodestonePath("particle/lodestone_particle"), DefaultVertexFormat.PARTICLE, "LumiTransparency", "DepthFade");

    public static ShaderHolder SCREEN_PARTICLE = new ShaderHolder(LodestoneLib.lodestonePath("screen/screen_particle"), DefaultVertexFormat.POSITION_TEX_COLOR);

    public static ShaderHolder DISTORTED_TEXTURE = new ShaderHolder(LodestoneLib.lodestonePath("screen/distorted_texture"), DefaultVertexFormat.POSITION_COLOR_TEX, "Speed", "TimeOffset", "Intensity", "XFrequency", "YFrequency", "UVCoordinates");

    public static ShaderHolder SCROLLING_TEXTURE = new ShaderHolder(LodestoneLib.lodestonePath("shapes/scrolling_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "Speed", "LumiTransparency");

    public static ShaderHolder TRIANGLE_TEXTURE = new ShaderHolder(LodestoneLib.lodestonePath("shapes/triangle_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "LumiTransparency");

    public static ShaderHolder SCROLLING_TRIANGLE_TEXTURE = new ShaderHolder(LodestoneLib.lodestonePath("shapes/scrolling_triangle_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "Speed", "LumiTransparency");

    @SubscribeEvent
    public static void shaderRegistry(RegisterShadersEvent event) {
        registerShader(event, LODESTONE_TEXTURE);
        registerShader(event, LODESTONE_TEXT);
        registerShader(event, PARTICLE);
        registerShader(event, SCREEN_PARTICLE);
        registerShader(event, DISTORTED_TEXTURE);
        registerShader(event, SCROLLING_TEXTURE);
        registerShader(event, TRIANGLE_TEXTURE);
        registerShader(event, SCROLLING_TRIANGLE_TEXTURE);
    }

    public static void registerShader(RegisterShadersEvent event, ShaderHolder shaderHolder) {
        try {
            ResourceProvider provider = event.getResourceProvider();
            event.registerShader(shaderHolder.createInstance(provider), shaderHolder::setShaderInstance);
        } catch (IOException var3) {
            LodestoneLib.LOGGER.error("Error registering shader", var3);
            var3.printStackTrace();
        }
    }
}